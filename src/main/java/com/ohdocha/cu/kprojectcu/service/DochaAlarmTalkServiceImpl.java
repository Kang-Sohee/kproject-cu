package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.controller.DochaLoginController;
import com.ohdocha.cu.kprojectcu.domain.DochaAdminUserResponse;
import com.ohdocha.cu.kprojectcu.domain.DochaAlarmTalkDto;
import com.ohdocha.cu.kprojectcu.domain.DochaKakaoAlramLogDto;
import com.ohdocha.cu.kprojectcu.domain.DochaRentCompanyDto;
import com.ohdocha.cu.kprojectcu.mapper.DochaUserInfoDao;
import com.ohdocha.cu.kprojectcu.util.DochaAlarmTalkMsgUtil;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import com.ohdocha.cu.kprojectcu.util.Util;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
@EnableAsync
@Service("dochaAlarmTalkService")
public class DochaAlarmTalkServiceImpl implements DochaAlarmTalkService {
	
	private final static Logger logger = LoggerFactory.getLogger(DochaLoginController.class);
	
	@Autowired
	DochaAlarmTalkMsgUtil util;
	
	@Resource(name="dochaRentcarService")
	DochaRentcarService rentCarService;

	@Autowired
	DochaUserInfoDao userDao;
	
	@Resource(name="dochaKakaoAlramLogService")
	DochaKakaoAlramLogServiceImpl kakaoAlramLogService;
	
	@Resource(name="AdminUserService")
	DochaAdminUserService adminUserService;
	
	@Async
	@Override
	public void sendKakaoAlram(DochaAlarmTalkDto dto) {
		
		// TODO Auto-generated method stub

		dto.setCallBack("16613355"); //공통 callback 주소


		switch (dto.getTemplateCode()) {
		case "A000011": 
			insPersonalAlramTalk(dto);
		break;
		case "A000012": 
			insCompanyAlramTalk(dto);
			break;		
		case "A000003": 
			insPersonalAlramTalk(dto);
		break;
		case "A000004": 
			insPersonalAlramTalk(dto);
		break;
		//회원사
		case "A000005":  
			insCompanyAlramTalk(dto);
		break;
		case "A000006":  
			insCompanyAlramTalk(dto);
		break;

		case "A000013":  
			insPersonalAlramTalk(dto);
		break;

		
		case "A000014":
			HttpResponse<JsonNode> response = null;
			insOPStaffAlramTalk(dto, response);
		break;
		
		case "A000015":
			insCompanyAlramTalk(dto);
		break;
			
		}
	}
	
	/**
	  * @FileName : insCompanyAlramTalk
	  * @Project : casdeal
	  * @Date : 2020.01.13 
	  * @작성자 : woosung.lee
	  * @변경이력 :
	  * @프로그램 설명 : 회원사 알림톡 보내기
	  * 
	  * 1. 전체 회원사 대표번호로 보낸다.
	  * 2. 전체 직원번호로 견적요청알림톡을 보낸다.
	  * 
	  */
	@SuppressWarnings("null")
	@Async
	public void insCompanyAlramTalk(DochaAlarmTalkDto dto) {

		List<DochaRentCompanyDto> rentCmpAll = null;
		List<DochaRentCompanyDto> rent = null;
		//List<DochaUserInfoDto> opList = null;  사용안함
		
		
		String stDt = dto.getRentDate(); 	// 대여시작일
		String endDt = dto.getReturnDate(); // 대여종료일

		int dayDiffValue = dayDiff(endDt , stDt);
		String term = "S";
		DochaMap setParam = new DochaMap();
		
		if( dayDiffValue >= 30 ) {
			term = "L";
		}
		
		setParam.set("dayDiffValue" , dayDiffValue);
		setParam.set("term" , term);
		setParam.set("userAge" , dto.getUserAge().substring(0,2));

		
		//전체 회원사
		if(Util.isEmpty(dto.getRtIdx()) == true) {
			rentCmpAll = rentCarService.selectRentCompanyListAll(setParam); // 제휴사
			for(int i=0; i<rentCmpAll.size(); i++ ) {
				DochaMap localParam = new DochaMap();
				localParam.set("rtIdx" , rentCmpAll.get(i).getRtIdx());
				
				List<DochaRentCompanyDto> tmp = rentCarService.selectRentStaffAndCompanyList(localParam);
				HttpResponse<JsonNode> response = null;
				if(!Util.isEmpty(tmp)) {
					getStaffList(dto, tmp,  response);
				}

			}

		} else { //결제요청한 회원사만 보냄
			DochaMap localParam = new DochaMap();
			localParam.set("rtIdx", dto.getRtIdx());
			rentCmpAll = rentCarService.selectRentCompanyListAllForPaymentComP(localParam);
			rent = rentCarService.selectRentStaffAndCompanyList(localParam);
		}
		
		/**
		 * 		2020-01-22
		 * 
		 * 		쿼리 불필요하게 많이탐
		 * 		장단기 구분 나이제한을 쿼리 한번으로 변경해야함
		 * 		 *
		 * 
		 * **/
		
		HttpResponse<JsonNode> response = null;
		if(!Util.isEmpty(rent)) {
			getStaffList(dto, rent,  response);
		}
		//제휴사 알림톡 발송 시작
		if(!Util.isEmpty(rentCmpAll)) {
			getCompanyList(dto, rentCmpAll,  response);
		}


	}//end insCompanyAlramTalk
	
	/**
	  * @FileName : sendPersonalAlramTalk
	  * @Project : casdeal
	  * @Date : 2020.01.13 
	  * @작성자 : woosung.lee
	  * @변경이력 :
	  * @프로그램 설명 : 개인회원 알림톡 보내기
	  */
	@Async
	public void insPersonalAlramTalk(DochaAlarmTalkDto dto) {

		//회원 알림톡 전달
		HttpResponse<JsonNode> response = util.sendAlramTalk(dto);
		saveCallBackResult(response, dto);
	}
	
	/**
	  * @FileName : DochaAlarmTalkServiceImpl
	  * @Project : casdeal
	  * @Date : 2020.01.13 
	  * @작성자 : woosung.lee
	  * @변경이력 :
	  * @프로그램 설명 : 알림톡 보낸 후 로그저장용
	  */
	@Async
	public void saveCallBackResult(HttpResponse<JsonNode> response, DochaAlarmTalkDto dto) {
		
		final JSONObject root = response.getBody().getObject();
		
		String result_code = (String)root.get("result_code");

		String cmid = "";
		String resultFull = "";
		
		if(!"400".equals(result_code)) {
			cmid = (String)root.get("cmid");
			resultFull = response.getBody().toString();
			
			DochaKakaoAlramLogDto logDto  = new DochaKakaoAlramLogDto();
			
			logDto.setResultCode(result_code);
			logDto.setCmid(cmid);
			logDto.setResultMsg("success");
			logDto.setResultFull(resultFull);
			logDto.setUrIdx(dto.getUrIdx());
			logDto.setRtIdx(dto.getRtIdx());
			logDto.setTemplateCode(dto.getTemplateCode());
			
			if(dto.getDivision().equals("U")) {
				logDto.setDivision("U");
				logDto.setContact(dto.getUserContact());
			} else if(dto.getDivision().equals("S")) {
				logDto.setDivision("S");
				logDto.setContact(dto.getContact());
			} else if(dto.getDivision().equals("C")) {
				logDto.setDivision("C");
				logDto.setContact(dto.getContact());
			}
			
			logDto.setQuIdx(dto.getQuIdx());
			logDto.setRmIdx(dto.getRmIdx());

			kakaoAlramLogService.insertkakaoAlramLog(logDto);
		} else {
			cmid = "";
			resultFull = "";
		
			DochaKakaoAlramLogDto logDto  = new DochaKakaoAlramLogDto();
			
			//logDto.setKaIdx(kaIdx);
			logDto.setResultCode(result_code);
			logDto.setCmid(cmid);
			logDto.setResultMsg("fail");
			logDto.setResultFull(resultFull);
			logDto.setUrIdx(dto.getUrIdx());
			logDto.setRtIdx(dto.getRtIdx());
			logDto.setTemplateCode(dto.getTemplateCode());
			logDto.setContact(dto.getUserContact());
			logDto.setDivision("U");
			
			kakaoAlramLogService.insertkakaoAlramLog(logDto);
		}

	}
	
	
	
	public int dayDiff( String date1 , String date2 ) {
		int resValue = 0;
		
		try{ 
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
	        Date FirstDate = format.parse(date1.split(" ")[0] + " " + date1.split(" ")[3]);
	        Date SecondDate = format.parse(date2.split(" ")[0] + " " + date2.split(" ")[3]);
	        
			String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
	        
	        long calDate = FirstDate.getTime() - SecondDate.getTime();
	        long calDateDays = calDate / ( 24*60*60*1000); 
	 
	        calDateDays = Math.abs(calDateDays);
	        resValue += calDateDays;
	    }
	    catch(ParseException e){
	         logger.error("날짜계산중 오류 : " + e);
	    }
		
		return resValue;
	}
	
	/*
	 * 수신동의한 제휴사 == Y
	 * 회원사의 장단기 구분 == N
	 * */
	@Async
	public void getCompanyList(DochaAlarmTalkDto dto, List<DochaRentCompanyDto> rentCmpAll, HttpResponse<JsonNode> response) {
		
		for(DochaRentCompanyDto voCmpDto :rentCmpAll){

			// 수신동의한 제휴사만 발송
			if ( voCmpDto.getAlarmYn() != null &&
				 voCmpDto.getAlarmYn().equals("Y")  ) {
				//회원사 장단기 구분 발신
				if( voCmpDto.getShortTermRentYn().equals("N") ) {
					String stDt = dto.getRentDate(); 	// 대여시작일
					String endDt = dto.getReturnDate(); // 대여종료일
					int dayDiffValue = dayDiff(endDt , stDt);			
					
					if( dayDiffValue >= 30 ) {
						if(voCmpDto.getCompanyContact1() != null ) {	 //DC_RENT_COMPANY_STAFF의 스텝번호
							//스텝번호 셋팅
							dto.setPhone(voCmpDto.getCompanyContact1());
							dto.setRsIdx(voCmpDto.getRsIdx());
							dto.setDivision("S");
							dto.setContact(voCmpDto.getStaffContact1());
							response = util.sendAlramTalk(dto);
							saveCallBackResult(response, dto);
						}
					}
				}else {
					if(voCmpDto.getCompanyContact1() != null) {
						//대표번호 셋팅
						dto.setPhone(voCmpDto.getCompanyContact1());
						//회사 대표번호
						dto.setDivision("C");
						dto.setContact(voCmpDto.getCompanyContact1());
						response = util.sendAlramTalk(dto);
						saveCallBackResult(response, dto);
					}
				}
			}
		}
	}//getCompanyList

	/*
	 * DC_RENT_COMPANY_STAFF 테이블에 있는 모든 직원들에게 발송
	 * */
	@Async
	public void getStaffList(DochaAlarmTalkDto dto, List<DochaRentCompanyDto> rent, HttpResponse<JsonNode> response) {
		
		//회사 직원들한테 보내기
		for(DochaRentCompanyDto vo:rent){
			
			System.out.println("직원 알림용 테스트====================");
			System.out.println(vo.getStaffContact1());
			
			//스텝번호 셋팅
			dto.setPhone(vo.getStaffContact1());
			dto.setDivision("S");
			dto.setContact(vo.getStaffContact1());
			response = util.sendAlramTalk(dto);
			saveCallBackResult(response, dto);
		}
	}//getStaffList
	
	
	/**
	  * @FileName : sendPersonalAlramTalk
	  * @Project : casdeal
	  * @Date : 2020.01.13 
	  * @작성자 : woosung.lee
	  * @변경이력 :
	  * @프로그램 설명 : 운영직원 알림톡 보내기
	  */
	@Async
	public void insOPStaffAlramTalk(DochaAlarmTalkDto dto, HttpResponse<JsonNode> response) {

		List<DochaAdminUserResponse> adminUserDto = adminUserService.selectAdminUserList();
		
		
		for(DochaAdminUserResponse getDto : adminUserDto) {
			dto.setPhone(getDto.getUserContact1());
			dto.setDivision("U");
			dto.setContact(dto.getUserContact());
			response = util.sendAlramTalk(dto);
			saveCallBackResult(response, dto);
		}

	}


}
