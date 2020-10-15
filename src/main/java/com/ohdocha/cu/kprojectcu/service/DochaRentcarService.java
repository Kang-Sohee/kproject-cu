package com.carssum.csdeal.rentcar;

import java.util.List;


import com.carssum.csdeal.util.CarssumMap;


public interface CarssumRentcarService {

	public List<CarssumQuoteUserInfoDto> selectQuoteUserList(CarssumMap param);
	public List<CarssumQuoteCompanyDto> selectQuoteCompanyList(CarssumMap param);
	public CarssumQuoteUserInfoDto selectQuoteUserInfo(CarssumMap param);
	public List<CarssumRentCompanyCarDto> selectCompanyCarList(CarssumMap param);
	public List<CarssumRentCompanyCarDto> selectCarModelList(CarssumMap param);
	public CarssumRentCompanyCarDto selectCompanyCarInfo(CarssumMap param);
	public int insertQuoteRentCompanyInfo(CarssumMap param);
	public int updateQuoteStatus(CarssumMap param);
	public List<CarssumCarOptionDto> selectCarOptionList(CarssumMap param);
	public int cancelQuote(CarssumMap param);
	public CarssumQuoteUserInfoDto selectQuoteCompanyInfo (CarssumMap param);
	public int updateQuoteRentCompanyByUser(CarssumMap param);
	public List<CarssumQuoteCompanyDto> selectQuoteRentCompany(CarssumMap param);
	
	public List<CarssumRentCompanyDto> selectRentCompanyList();
	
	public List<CarssumRentCompanyDto> selectRentStaffAndCompanyList(CarssumMap param);
	public CarssumQuoteUserInfoDto selectRentCompanyInfo(CarssumMap param);
	
	public List<CarssumRentCompanyDto> selectRentCompanyListAll(CarssumMap param);
	public List<CarssumRentCompanyCarDto> selectTargetCarType(CarssumMap param);
		
	/**
	 * 
	 * @param paramMap
	 * @return 1 : 성공, 2 : 견적없음, 3 : 견적요청자와 취소요청자 불일치, 4 : 견적 기취소, 5 : 취소 update 실패, -1 : 시스템 에러 
	 */
	public int updateCancelQuoteRentCompany(CarssumMap paramMap);
	
	//결제한 회원사, 직원에게 알림톡 보내는 용도의 쿼리
	public List<CarssumRentCompanyDto> selectRentStaffAndCompanyListForPaymentComP(CarssumMap param);
	public List<CarssumRentCompanyDto> selectRentCompanyListAllForPaymentComP(CarssumMap param);
	
	public CarssumQuoteCompanyDto selectQuoteRentCompanyDetail(CarssumMap param);
	
	public CarssumCarInsuranceDto selectCarInsuranceInfo(CarssumMap param);

}
