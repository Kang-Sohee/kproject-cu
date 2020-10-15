package com.carssum.csdeal.rentcar;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carssum.csdeal.estimate.CarssumQuoteUserDto;
import com.carssum.csdeal.estimate.CarssumQuoteUserServiceImpl;
import com.carssum.csdeal.userInfo.CarssumUserInfoDao;
import com.carssum.csdeal.userInfo.CarssumUserInfoDto;
import com.carssum.csdeal.util.CarssumMap;
import com.carssum.csdeal.util.SmsAuthUtil;
import com.carssum.csdeal.util.StringUtil;

@Service("carssumRentcarService")
public class CarssumRentcarServiceImpl implements CarssumRentcarService {
	
	@Autowired
	CarssumRentcarDao dao;
	
	@Autowired
	CarssumUserInfoDao userDao;
	
	private final static Logger logger = LoggerFactory.getLogger(CarssumRentcarServiceImpl.class);
	
	@Override
	public List<CarssumQuoteUserInfoDto> selectQuoteUserList(CarssumMap param) {
		return dao.selectQuoteUserList(param);
	}

	@Override
	public List<CarssumQuoteCompanyDto> selectQuoteCompanyList(CarssumMap param) {
		// TODO Auto-generated method stub
		return dao.selectQuoteCompanyList(param);
	}

	@Override
	public CarssumQuoteUserInfoDto selectQuoteUserInfo(CarssumMap param) {
		return dao.selectQuoteUserInfo(param);
	}

	@Override
	public List<CarssumRentCompanyCarDto> selectCompanyCarList(CarssumMap param) {
		return dao.selectCompanyCarList(param);
	}

	@Override
	public List<CarssumRentCompanyCarDto> selectCarModelList(CarssumMap param) {
		return dao.selectCarModelList(param);
	}

	@Override
	public CarssumRentCompanyCarDto selectCompanyCarInfo(CarssumMap param) {
		return dao.selectCompanyCarInfo(param);
	}

	@Override
	public int insertQuoteRentCompanyInfo(CarssumMap param) {
		int resultCnt = 0;
		String makeSequence = param.getString("quIdx") + param.getString("quRandom");
		CarssumQuoteUserInfoDto quInfo = dao.selectQuoteUserInfo(param);
		CarssumRentCompanyCarDto carInfo = dao.selectCompanyCarInfo(param);
		CarssumCarInsuranceDto carInsuranceInfo = dao.selectCarInsuranceInfo(param);
		
		CarssumUserInfoDto userInfoTmp = new CarssumUserInfoDto();
		userInfoTmp.setUrIdx(quInfo.getUrIdx());
		CarssumUserInfoDto userInfo = userDao.selectUserInfo(userInfoTmp);
		CarssumMap tmp = new CarssumMap();
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
	public int updateQuoteStatus(CarssumMap param) {
		return dao.updateQuoteStatus(param);
	}

	@Override
	public List<CarssumCarOptionDto> selectCarOptionList(CarssumMap param) {
		return dao.selectCarOptionList(param);
	}

	@Override
	public int cancelQuote(CarssumMap param) {
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
	public CarssumQuoteUserInfoDto selectQuoteCompanyInfo(CarssumMap param) {
		return dao.selectQuoteCompanyInfo(param);
	}

	@Override
	public int updateQuoteRentCompanyByUser(CarssumMap param) {
		// TODO Auto-generated method stub
		return dao.updateQuoteRentCompanyByUser(param);
	}
	
	@Override
	public List<CarssumQuoteCompanyDto> selectQuoteRentCompany(CarssumMap param) {
		// TODO Auto-generated method stub
		return dao.selectQuoteRentCompany(param);
	}

	@Override
	public List<CarssumRentCompanyDto> selectRentCompanyList() {
		// TODO Auto-generated method stub
		return dao.selectRentCompanyList();
	}

	@Override
	public List<CarssumRentCompanyDto> selectRentStaffAndCompanyList(CarssumMap param) {
		// TODO Auto-generated method stub
		return dao.selectRentStaffAndCompanyList(param);
	}

	@Override
	public CarssumQuoteUserInfoDto selectRentCompanyInfo(CarssumMap param) {
		return dao.selectRentCompanyInfo(param);
	}

	@Override
	public List<CarssumRentCompanyDto> selectRentCompanyListAll( CarssumMap param) {
		// TODO Auto-generated method stub
		return dao.selectRentCompanyListAll(param);
	}
	
	@Override
	public int updateCancelQuoteRentCompany(CarssumMap paramMap) {

		logger.info("==================== CANCEL RENT QUOTE START ====================");
		
		int result = -1;
		
		try {
			
			CarssumMap selectParam = new CarssumMap();
			selectParam.put("qrIdx", paramMap.getString("qrIdx"));
			
			CarssumQuoteCompanyDto rentQuoteInfo = dao.selectQuoteRentCompanyDetail(selectParam);
			
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
	public List<CarssumRentCompanyCarDto> selectTargetCarType(CarssumMap param) {
		// TODO Auto-generated method stub
		return dao.selectTargetCarType(param);
	}

	//결제한 회원사, 직원에게 알림톡 보내는 용도의 쿼리
	@Override
	public List<CarssumRentCompanyDto> selectRentStaffAndCompanyListForPaymentComP(CarssumMap param) {
		// TODO Auto-generated method stub
		return dao.selectRentStaffAndCompanyListForPaymentComP(param);
	}

	//결제한 회원사, 직원에게 알림톡 보내는 용도의 쿼리
	@Override
	public List<CarssumRentCompanyDto> selectRentCompanyListAllForPaymentComP(CarssumMap param) {
		// TODO Auto-generated method stub
		return dao.selectRentCompanyListAllForPaymentComP(param);
	}

	@Override
	public CarssumQuoteCompanyDto selectQuoteRentCompanyDetail(CarssumMap param) {
		// TODO Auto-generated method stub
		return dao.selectQuoteRentCompanyDetail(param);
	}

	@Override
	public CarssumCarInsuranceDto selectCarInsuranceInfo(CarssumMap param) {
		// TODO Auto-generated method stub
		return dao.selectCarInsuranceInfo(param);
	}

	
}
