package com.ohdocha.cu.kprojectcu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdocha.cu.kprojectcu.domain.*;
import com.ohdocha.cu.kprojectcu.mapper.DochaPaymentDao;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import com.ohdocha.cu.kprojectcu.util.KeyMaker;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("payment")
@Slf4j
@AllArgsConstructor
@Transactional
public class DochaPaymentServiceImpl implements DochaPaymentService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private final DochaPaymentDao dao;

	@Override
	public int insertReserveMaster(DochaPaymentDto paramMap) {
		// TODO Auto-generated method stub
		return dao.insertReserveMaster(paramMap);
	}

	@Override
	public int insertReserve(DochaPaymentReserveDto paramMap) {
		// TODO Auto-generated method stub
		return dao.insertReserve(paramMap);
	}

	@Override
	public int insertPaymentDetail(DochaPaymentDetailDto paramMap) {
		// TODO Auto-generated method stub
		return dao.insertPaymentDetail(paramMap);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public int insertPaymentLog(DochaPaymentLogDto paramMap) {
		// TODO Auto-generated method stub
		return dao.insertPaymentLog(paramMap);
	}

	@Override
	public int updatePaymentLog(DochaPaymentLogDto paramMap) {
		// TODO Auto-generated method stub
		return dao.updatePaymentLog(paramMap);
	}

	@Override
	public int updateReserveMaster(DochaMap paramMap) {
		// TODO Auto-generated method stub
		return dao.updateReserveMaster(paramMap);
	}

	@Override
	public int updateReserve(DochaMap paramMap) {
		// TODO Auto-generated method stub
		return dao.updateReserve(paramMap);
	}

	@Override
	public List<DochaQuoteUserInfoDto> selectQuotePaymentSuccessList(DochaMap paramMap) {
		// TODO Auto-generated method stub
		return dao.selectQuotePaymentSuccessList(paramMap);
	}
	
	@Override
	public DochaQuoteUserInfoDto selectQuotePaymentSuccessDetail(DochaMap paramMap) {
		// TODO Auto-generated method stub
		return dao.selectQuotePaymentSuccessDetail(paramMap);
	}

	@Override
	public int updateCompliteQuoteRentCompany(DochaMap paramMap) {
		// TODO Auto-generated method stub
		return dao.updateCompliteQuoteRentCompany(paramMap);
	}

	@Override
	public int updateNotChoiseQuoteRentCompany(DochaMap paramMap) {
		// TODO Auto-generated method stub
		return dao.updateNotChoiseQuoteRentCompany(paramMap);
	}
	
	public void paymentSave(DochaMap paramMap, String url, String impKey, String impSecret) throws JsonMappingException, JsonProcessingException, Exception {
		
		//DB 저장 전 각 테이블의 key값 생성
		String rmIdx = KeyMaker.getInsetance().getKeyDeafult("RM");
		String reIdx = KeyMaker.getInsetance().getKeyDeafult("RE");
		String plIdx = KeyMaker.getInsetance().getKeyDeafult("PL");
		String pdIdx = KeyMaker.getInsetance().getKeyDeafult("PD");
		
		//결제검증 오류시 취소처리를 위해 Exception 저장
		Exception payServiceException = null;
		
		//결제검증전문
		String orgMsg = null;
		//결제검증 결과
		Map<String, Object> result = null;
		//결제 중 paydata
		Map<String, Object> payData = null;
		
		//세션에 저장했던 결제 전 호출된 금액 및 차량정보
		DochaCarInfoDto resCarInfo = (DochaCarInfoDto) paramMap.get("resCarDto");
		//결제유저정보
		DochaUserInfoDto userInfo = (DochaUserInfoDto) paramMap.get("user");
		
		//세션의 일별요금, 보험요금을 불러옴
		String sessionDailyStandardPay = resCarInfo.getDailyStandardPay();
		String sessionInsuranceFee = resCarInfo.getInsuranceFee(); 
		
		sessionDailyStandardPay = sessionDailyStandardPay == null ? "0" : sessionDailyStandardPay;
		sessionInsuranceFee = sessionInsuranceFee == null ? "0" : sessionInsuranceFee;
		
		int dailyStandardPay = Integer.parseInt(sessionDailyStandardPay);
		int insuranceFee = Integer.parseInt(sessionInsuranceFee);
		
		//아임포트 결제 key값을 셋팅
		String impUid = paramMap.getString("impUid");
		//결제검증을 위해 아임포트 AccessToken 발급
		String accessToken = getAccessToken(impKey, impSecret, url);
		//아임포트 결제 검증 호출부분
		try {
			//아임포트 AccessToken, 결제 key값을 전달하여 결제데이터 호출
			result = getPaymentInfo(impUid, accessToken, url);
			
			//결제전문 중 결제관련한 데이터를 가져옴
			payData = (Map<String, Object>) result.get("response");
			
			//결제전문을 JSONString형태로 변환
			ObjectMapper mapper = new ObjectMapper();
			orgMsg = mapper.writeValueAsString(result);
		}catch (Exception e) {
			//에러발생시 로그처리 후 에러 throws
			logger.error("Import Connect Error", e);
			throw e;
		}
		
		//결제검증 데이터 중 결제금액 가져옴
		int payment = (int) payData.get("amount");
		//결제검증 데이터 중 승인번호 가져옴
		String applyNum = (String) payData.getOrDefault("apply_num", null);
		
		//결제금액이 세션금액과 일치하지 않는경우
		if(payment != dailyStandardPay + insuranceFee) {
			
			//paylog 저장 후 Exception throws
			DochaPaymentLogDto payLog = new DochaPaymentLogDto();
			payLog.setRmIdx(rmIdx);
			payLog.setApprovalNumber(applyNum);
			payLog.setPaymentAmount(Integer.toString(payment));
			payLog.setOrgMsg(orgMsg);
			payLog.setApprovalYn(applyNum == null ? "N" : "Y");
			payLog.setPaymentRequestAmount(Integer.toString(dailyStandardPay + insuranceFee));
			payLog.setPlIdx(plIdx);
			payLog.setPdIdx(pdIdx);
			dao.insertPaymentLog(payLog);
			
			payServiceException =  new Exception("Payment Amount Check Error");
			throw payServiceException;
		}
		
		//주문 및 결제데이터 저장
		try {
			
			//ReserveMaster 저장(현재 필수정보만 셋팅, 비지니스 로직에 따라 데이터 추가필요)
			DochaPaymentDto paymentDto = new DochaPaymentDto();
			
			paymentDto.setRmIdx(rmIdx);
			paymentDto.setCrIdx(resCarInfo.getCrIdx());
			paymentDto.setUrIdx(userInfo.getUrIdx());
			paymentDto.setUlIdx1(userInfo.getUlIdx());
			paymentDto.setPaymentAmount(Integer.toString(payment));
			paymentDto.setRentFee(sessionDailyStandardPay);
			paymentDto.setInsuranceFee(sessionInsuranceFee);
			paymentDto.setReserveStatusCode("예약완료");
			paymentDto.setCarTypeCode(resCarInfo.getCartypeCode());
			
			dao.insertReserveMaster(paymentDto);
			
			//ReserveLog 저장 (현재 필수정보만 셋팅, 비지니스 로직에 따라 데이터 추가필요)
			DochaPaymentReserveDto paymentReserveDto = new DochaPaymentReserveDto();
			
			paymentReserveDto.setReIdx(reIdx);
			paymentReserveDto.setRmIdx(rmIdx);
			paymentReserveDto.setRentFee(sessionDailyStandardPay);
			paymentReserveDto.setInsuranceFee(sessionInsuranceFee);
			paymentReserveDto.setReserveStatusCode("예약완료");		
			
			dao.insertReserve(paymentReserveDto);
			
			
			//PaymentDetail저장 (현재 필수정보만 셋팅, 비지니스 로직에 따라 데이터 추가필요)
			DochaPaymentDetailDto paymentDetailDto = new DochaPaymentDetailDto();	
			paymentDetailDto.setApprovalNumber(applyNum);
			paymentDetailDto.setPaymentAmount(Integer.toString(payment));
			paymentDetailDto.setPgCode((String) payData.get("pg_id"));
			paymentDetailDto.setPaymentKindCode((String) payData.get("card_name"));
			paymentDetailDto.setPaymentTypeCode((String) payData.get("pay_method"));
			paymentDetailDto.setRmIdx(rmIdx);
			paymentDetailDto.setReIdx(reIdx);
			paymentDetailDto.setUrIdx(userInfo.getUrIdx());
			paymentDetailDto.setPlIdx(plIdx);
			paymentDetailDto.setPdIdx(pdIdx);
			
			dao.insertPaymentDetail(paymentDetailDto);
			
		}catch (Exception e) {
			//오류발생시 로그처리 후 Exception throws
			logger.error("Payment Save Error", e);
			payServiceException = e;
			throw e;
			
		}finally {
			//결제검증을 마친 이후기 때문에 결제로그 저장
			DochaPaymentLogDto payLog = new DochaPaymentLogDto();
			payLog.setRmIdx(rmIdx);
			payLog.setApprovalNumber(applyNum);
			payLog.setPaymentAmount(Integer.toString(payment));
			payLog.setOrgMsg(orgMsg);
			payLog.setApprovalYn(applyNum == null ? "N" : "Y");
			payLog.setPaymentRequestAmount(Integer.toString(dailyStandardPay + insuranceFee));
			payLog.setPlIdx(plIdx);
			payLog.setPdIdx(pdIdx);
			dao.insertPaymentLog(payLog);
			
			if(payServiceException != null) {
				//결제검증 혹은 주문저장 실패이므로 결제취소처리 로직
			}
		}
	}
	
	/**
	 * 
	 * 아임포트 AccessKey 발급
	 * 
	 * @param impKey 아임포트 key
	 * @param impSecret 아임포트 시크릿 키
	 * @param url url
	 * @return
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	private String getAccessToken(String impKey, String impSecret, String url) throws JsonMappingException, JsonProcessingException, Exception {
		Map<String, String> body = new LinkedHashMap<String, String>();
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		
        //파라미터로 imp_key, imp_secret 설정
		body.put("imp_key", impKey);
		body.put("imp_secret", impSecret);
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> resultMap = mapper.readValue(connectImport(url + "/users/getToken", headers, HttpMethod.POST, body), Map.class);
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("response");
		
		return (String) dataMap.get("access_token");

	}
	
	/**
	 * 
	 * 아임포트 결제정보 조회
	 * 
	 * @param impUid 아임포트 결제 유니크 key
	 * @param token 아임포트 AccessToken
	 * @param url Url
	 * @return
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	private Map<String, Object>  getPaymentInfo(String impUid, String token, String url) throws JsonMappingException, JsonProcessingException, Exception {
		Map<String, String> body = new LinkedHashMap<String, String>();
		
		//헤더에 AccessToken 설정
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> resultMap = mapper.readValue(connectImport(url + "/payments/" + impUid, headers, HttpMethod.GET, null), Map.class);
	
		return resultMap;

	}
	
	private String connectImport(String url, HttpHeaders headers, HttpMethod method, Map body) throws Exception {
		
		RestTemplate connect = new RestTemplate();
		
        HttpEntity<Map> entity = new HttpEntity<Map>(body, headers);
        ResponseEntity<String> payResponse = null;
        try {
        	payResponse = connect.exchange(url, method, entity, String.class);
        }catch (HttpServerErrorException ex) {
        	
        	logger.info("ImportConnect Error");
        	logger.info("Error Request Url : " + url);
        	logger.info("Error Request Body : " + body);
        	logger.info("Error Response : " + ex.getResponseBodyAsString());
        	logger.error("Error Request Url : " + url);
        	logger.error("Error Request Body : " + body);
        	logger.error(ex.getMessage());
        	logger.error(ex.getResponseBodyAsString());
        	logger.error("Error", ex);
        	
        	throw new Exception("Import Connection Error", ex);
        	
        }catch (Exception e) {
        	logger.error(e.getMessage());
        	logger.error("Error", e);
        	
        	throw new Exception("Import Connection Error");
        }
        
        return payResponse.getBody();
	}
	
}
