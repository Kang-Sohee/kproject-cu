package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.*;
import com.ohdocha.cu.kprojectcu.mapper.DochaRentcarDao;
import com.ohdocha.cu.kprojectcu.mapper.DochaUserInfoDao;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import com.ohdocha.cu.kprojectcu.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("dochaRentcarService")
public class DochaRentcarServiceImpl implements DochaRentcarService {
	
	@Autowired
	DochaRentcarDao dao;
	
	@Autowired
	DochaUserInfoDao userDao;
	
	private final static Logger logger = LoggerFactory.getLogger(DochaRentcarServiceImpl.class);
	
	@Override
	public List<DochaQuoteUserInfoDto> selectQuoteUserList(DochaMap param) {
		return dao.selectQuoteUserList(param);
	}

	@Override
	public List<DochaQuoteCompanyDto> selectQuoteCompanyList(DochaMap param) {
		// TODO Auto-generated method stub
		return dao.selectQuoteCompanyList(param);
	}

	@Override
	public DochaQuoteUserInfoDto selectQuoteUserInfo(DochaMap param) {
		return dao.selectQuoteUserInfo(param);
	}

	@Override
	public List<DochaRentCompanyCarDto> selectCompanyCarList(DochaMap param) {
		return dao.selectCompanyCarList(param);
	}

	@Override
	public List<DochaRentCompanyCarDto> selectCarModelList(DochaMap param) {
		return dao.selectCarModelList(param);
	}

	@Override
	public DochaRentCompanyCarDto selectCompanyCarInfo(DochaMap param) {
		return dao.selectCompanyCarInfo(param);
	}

	@Override
	public int insertQuoteRentCompanyInfo(DochaMap param) {
		int resultCnt = 0;
		String makeSequence = param.getString("quIdx") + param.getString("quRandom");
		DochaQuoteUserInfoDto quInfo = dao.selectQuoteUserInfo(param);
		DochaRentCompanyCarDto carInfo = dao.selectCompanyCarInfo(param);
		DochaCarInsuranceDto carInsuranceInfo = dao.selectCarInsuranceInfo(param);
		
		DochaUserInfoDto userInfoTmp = new DochaUserInfoDto();
		userInfoTmp.setUrIdx(quInfo.getUrIdx());
		DochaUserInfoDto userInfo = userDao.selectUserInfo(userInfoTmp);
		DochaMap tmp = new DochaMap();
		tmp.set("qrIdx", makeSequence);
		tmp.set("quIdx", quInfo.getQuIdx());
		tmp.set("crIdx", carInfo.getCrIdx());
		
		int setRentFee = Integer.parseInt(param.getString("rentFee"));
		int setInsuranceRate = Integer.parseInt(carInsuranceInfo.getInsuranceFee() == null ? "0" : carInsuranceInfo.getInsuranceFee());
		
		
		tmp.set("rentFee", param.getString("rentFee"));
		tmp.set("insuranceFee", param.getString("insuranceFee"));
		tmp.set("paymentAmount", param.getString("paymentAmount"));
		tmp.set("urIdx", quInfo.getUrIdx());
		tmp.set("userName", userInfo.getUserName() );
		tmp.set("rtIdx", param.getString("rtIdx"));    
		tmp.set("quoteStatus" , "QO");
		tmp.set("delYn", "N");
		tmp.set("carDeposit", param.getString("carDeposit"));
		tmp.set("monthlyFee", param.getString("monthlyFee"));
		tmp.set("dailyFee", param.getString("dailyFee"));
		resultCnt += dao.insertQuoteRentCompanyInfo(tmp);
		
		return resultCnt;
	}

	@Override
	public int updateQuoteStatus(DochaMap param) {
		return dao.updateQuoteStatus(param);
	}

	@Override
	public List<DochaCarOptionDto> selectCarOptionList(DochaMap param) {
		return dao.selectCarOptionList(param);
	}

	@Override
	public int cancelQuote(DochaMap param) {
		int resultCnt = 0;
		resultCnt += dao.updateQuoteUser(param);
		
		/*
		 *    상태코드 
		 *    P_CODE : QTC
		 *    QO     : 견적요청
		 *    QP     : 결제요청
		 *    QC     : 결제완료
		 *    QB     : 견적취소
		 *    
		 *    parameter : status
		 * */
		
		param.set("quoteStatus", "QB");  
		resultCnt += dao.updateQuoteRentCompany(param);
		return resultCnt;
	}

	@Override
	public DochaQuoteUserInfoDto selectQuoteCompanyInfo(DochaMap param) {
		return dao.selectQuoteCompanyInfo(param);
	}

	@Override
	public int updateQuoteRentCompanyByUser(DochaMap param) {
		// TODO Auto-generated method stub
		return dao.updateQuoteRentCompanyByUser(param);
	}
	
	@Override
	public List<DochaQuoteCompanyDto> selectQuoteRentCompany(DochaMap param) {
		// TODO Auto-generated method stub
		return dao.selectQuoteRentCompany(param);
	}

	@Override
	public List<DochaRentCompanyDto> selectRentCompanyList() {
		// TODO Auto-generated method stub
		return dao.selectRentCompanyList();
	}

	@Override
	public List<DochaRentCompanyDto> selectRentStaffAndCompanyList(DochaMap param) {
		// TODO Auto-generated method stub
		return dao.selectRentStaffAndCompanyList(param);
	}

	@Override
	public DochaQuoteUserInfoDto selectRentCompanyInfo(DochaMap param) {
		return dao.selectRentCompanyInfo(param);
	}

	@Override
	public List<DochaRentCompanyDto> selectRentCompanyListAll( DochaMap param) {
		// TODO Auto-generated method stub
		return dao.selectRentCompanyListAll(param);
	}
	
	@Override
	public int updateCancelQuoteRentCompany(DochaMap paramMap) {

		logger.info("==================== CANCEL RENT QUOTE START ====================");
		
		int result = -1;
		
		try {
			
			DochaMap selectParam = new DochaMap();
			selectParam.put("qrIdx", paramMap.getString("qrIdx"));
			
			DochaQuoteCompanyDto rentQuoteInfo = dao.selectQuoteRentCompanyDetail(selectParam);
			
			//견적없음
			if(StringUtil.isEmpty(rentQuoteInfo)) {
				result = 2;
				return result; 
			}
			
			//견적취소 요청자의 회원사IDX와 견적등록 회원사 IDX 일치여부 확인
			if(!paramMap.getString("rtIdx").equals(rentQuoteInfo.getRtIdx())) {
				result = 3;
				return result; 
			}
			
			//기취소
			if("QB".equals(rentQuoteInfo.getQuoteStatus())) {
				result = 4;
				return result;
			}
			
			int updateResult = 0;
			
			updateResult = dao.updateQuoteRentCompany(paramMap);
			
			if(updateResult > 0) {
				result = 1;
			}else{
				result = 5;
			}
			
		}catch(Exception e) {
			logger.info("==================== CANCEL RENT QUOTE ERROR ====================");
			logger.error("==================== ERROR", e);
			
			result = -1;
		}
		
		logger.info("==================== CANCEL RENT QUOTE END ====================");
		
		return result;
	}

	@Override
	public List<DochaRentCompanyCarDto> selectTargetCarType(DochaMap param) {
		// TODO Auto-generated method stub
		return dao.selectTargetCarType(param);
	}

	//결제한 회원사, 직원에게 알림톡 보내는 용도의 쿼리
	@Override
	public List<DochaRentCompanyDto> selectRentStaffAndCompanyListForPaymentComP(DochaMap param) {
		// TODO Auto-generated method stub
		return dao.selectRentStaffAndCompanyListForPaymentComP(param);
	}

	//결제한 회원사, 직원에게 알림톡 보내는 용도의 쿼리
	@Override
	public List<DochaRentCompanyDto> selectRentCompanyListAllForPaymentComP(DochaMap param) {
		// TODO Auto-generated method stub
		return dao.selectRentCompanyListAllForPaymentComP(param);
	}

	@Override
	public DochaQuoteCompanyDto selectQuoteRentCompanyDetail(DochaMap param) {
		// TODO Auto-generated method stub
		return dao.selectQuoteRentCompanyDetail(param);
	}

	@Override
	public DochaCarInsuranceDto selectCarInsuranceInfo(DochaMap param) {
		// TODO Auto-generated method stub
		return dao.selectCarInsuranceInfo(param);
	}

	
}
