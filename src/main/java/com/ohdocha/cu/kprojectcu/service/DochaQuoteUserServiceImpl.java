package com.carssum.csdeal.estimate;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carssum.csdeal.login.CarssumLoginController;
import com.carssum.csdeal.payment.CarssumPaymentReserveDto;
import com.carssum.csdeal.util.CarssumMap;
import com.carssum.csdeal.util.StringUtil;


@Service("QuoteUser")
@Transactional
public class CarssumQuoteUserServiceImpl implements CarssumQuoteUserService{

	
	@Autowired
	private CarssumQuoteUserDao dao;
	
	private final static Logger logger = LoggerFactory.getLogger(CarssumQuoteUserServiceImpl.class);
	
	//견적요청(user/insertEstimate.do)
	@Override
	public int insertQuoteUser(CarssumQuoteUserDto paramDto) {
		// TODO Auto-generated method stub
		return dao.insertQuoteUser(paramDto);
	}
	
	//견적요청리스트(user/estimate.do)
	@Override
	public List<CarssumQuoteUserDto> selectUserQuoteList(CarssumMap paramMap) {
		return dao.selectUserQuoteList(paramMap);
	}
	
	//견적요청 상세(user/userEstDetail.json)
	@Override
	public CarssumQuoteUserDto selectUserQuoteInfo(CarssumMap paramMap) {
		return dao.selectUserQuoteInfo(paramMap);
	}
	
	//결제완료리스트(user/userPaymentList.json)
	@Override
	public List<CarssumQuoteUserDto> selectUserQuotePaymentList(CarssumMap paramMap) {
		// TODO Auto-generated method stub
		return dao.selectUserQuotePaymentList(paramMap);
	}

	@Override
	public CarssumQuoteUserDto selectRentCompanyQuoteUser(CarssumMap paramMap) {
		// TODO Auto-generated method stub
		return dao.selectRentCompanyQuoteUser(paramMap);
	}

	@Override
	public int updateQuoteUser(CarssumMap paramMap) {
		// TODO Auto-generated method stub
		return dao.updateQuoteUser(paramMap);
	}

	//견적요청 상세(user/userEstDetail.do)
	@Override
	public CarssumPaymentReserveDto selectUserQuoteInfoUsingPayment(CarssumMap paramMap) {
		// TODO Auto-generated method stub
		return dao.selectUserQuoteInfoUsingPayment(paramMap);
	}

	//user/estimate.do?tab=0 of 견적요청 List(user/getUserEstDetail.json)
	@Override
	public List<CarssumPaymentReserveDto> selectQuoteUserDetail(CarssumMap paramMap) {
		// TODO Auto-generated method stub
		return dao.selectQuoteUserDetail(paramMap);
	}
	
	@Override
	public int updateCancelQuoteUser(CarssumMap paramMap) {

		logger.info("==================== CANCEL USER QUOTE START ====================");
		
		int result = -1;
		
		try {
			
			CarssumMap selectParam = new CarssumMap();
			selectParam.put("quIdx", paramMap.getString("quIdx"));
			
			CarssumQuoteUserDto userQuoteInfo = dao.selectUserQuoteInfo(selectParam);
			
			//견적없음
			if(StringUtil.isEmpty(userQuoteInfo)) {
				result = 2;
				return result; 
			}
			
			//요청자와 견적등록자 일치여부 확인
			if(!paramMap.getString("urIdx").equals(userQuoteInfo.getUrIdx())) {
				result = 3;
				return result; 
			}
			
			//기취소
			if("QB".equals(userQuoteInfo.getQuoteStatus())) {
				result = 4;
				return result;
			}
			
			int updateResult = 0;
			
			updateResult = dao.updateQuoteUser(paramMap);
			
			if(updateResult > 0) {
				result = 1;
			}else{
				result = 5;
			}
			
		}catch(Exception e) {
			logger.info("==================== CANCEL USER QUOTE ERROR ====================");
			logger.error("==================== ERROR", e);
			
			result = -1;
		}
		
		logger.info("==================== CANCEL USER QUOTE END ====================");
		
		return result;
	}
	
}
