package com.carssum.csdeal.payment;

import java.util.List;

import com.carssum.csdeal.rentcar.CarssumQuoteUserInfoDto;
import com.carssum.csdeal.userInfo.CarssumUserInfoDto;
import com.carssum.csdeal.util.CarssumMap;

public interface CarssumPaymentService {

	public int insertReserveMaster(CarssumPaymentDto carssumPaymentReserveMasterDto);
	
	public int insertReserve(CarssumPaymentReserveDto paramMap);
	
	public int insertPaymentDetail(CarssumPaymentDetailDto paramMap);
	
	public int insertPaymentLog(CarssumPaymentLogDto paramMap);
	
	public int updatePaymentLog(CarssumPaymentLogDto paramMap);

	public int updateReserveMaster(CarssumMap paramMap);
	
	public int updateReserve(CarssumMap paramMap);
	
	public List<CarssumQuoteUserInfoDto> selectQuotePaymentSuccessList(CarssumMap paramMap);
	
	public CarssumQuoteUserInfoDto selectQuotePaymentSuccessDetail(CarssumMap paramMap);
	
	public int updateCompliteQuoteRentCompany(CarssumMap paramMap);
	
	public int updateNotChoiseQuoteRentCompany(CarssumMap paramMap);
	
	
}
