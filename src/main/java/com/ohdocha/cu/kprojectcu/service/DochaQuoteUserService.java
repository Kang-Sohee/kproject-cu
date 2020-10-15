package com.carssum.csdeal.estimate;

import java.util.List;

import com.carssum.csdeal.payment.CarssumPaymentReserveDto;
import com.carssum.csdeal.util.CarssumMap;

public interface CarssumQuoteUserService {

	public int insertQuoteUser(CarssumQuoteUserDto paramDto);
	
	public List<CarssumQuoteUserDto> selectUserQuoteList(CarssumMap paramMap);
	
	public CarssumQuoteUserDto selectUserQuoteInfo(CarssumMap paramMap);
	
	//결제완료리스트(user/userPaymentList.do)
	public List<CarssumQuoteUserDto> selectUserQuotePaymentList(CarssumMap paramMap);
	
	public CarssumQuoteUserDto selectRentCompanyQuoteUser(CarssumMap paramMap);
	
	//결제 후 사용자 견적요청 업데이트
	public int updateQuoteUser(CarssumMap paramMap);
	
	public CarssumPaymentReserveDto selectUserQuoteInfoUsingPayment(CarssumMap paramMap);

	//user/estimate.do?tab=0 of 견적요청 List(user/getUserEstDetail.json)
	public List<CarssumPaymentReserveDto> selectQuoteUserDetail(CarssumMap paramMap);

	/**
	 * 견적취소
	 * @param paramMap
	 * @return 1 : 성공, 2 : 견적없음, 3 : 견적요청자와 취소요청자 불일치, 4 : 견적 기취소, 5 : 취소 update 실패, -1 : 시스템 에러 
	 */
	public int updateCancelQuoteUser(CarssumMap paramMap);
}
