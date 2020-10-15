package com.carssum.csdeal.payment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.carssum.csdeal.rentcar.CarssumQuoteUserInfoDto;
import com.carssum.csdeal.userInfo.CarssumUserInfoDto;
import com.carssum.csdeal.util.CarssumMap;

@Service("payment")
@Transactional
public class CarssumPaymentServiceImpl implements CarssumPaymentService{

	@Autowired
	private CarssumPaymentDao dao;

	@Override
	public int insertReserveMaster(CarssumPaymentDto paramMap) {
		// TODO Auto-generated method stub
		return dao.insertReserveMaster(paramMap);
	}

	@Override
	public int insertReserve(CarssumPaymentReserveDto paramMap) {
		// TODO Auto-generated method stub
		return dao.insertReserve(paramMap);
	}

	@Override
	public int insertPaymentDetail(CarssumPaymentDetailDto paramMap) {
		// TODO Auto-generated method stub
		return dao.insertPaymentDetail(paramMap);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public int insertPaymentLog(CarssumPaymentLogDto paramMap) {
		// TODO Auto-generated method stub
		return dao.insertPaymentLog(paramMap);
	}

	@Override
	public int updatePaymentLog(CarssumPaymentLogDto paramMap) {
		// TODO Auto-generated method stub
		return dao.updatePaymentLog(paramMap);
	}

	@Override
	public int updateReserveMaster(CarssumMap paramMap) {
		// TODO Auto-generated method stub
		return dao.updateReserveMaster(paramMap);
	}

	@Override
	public int updateReserve(CarssumMap paramMap) {
		// TODO Auto-generated method stub
		return dao.updateReserve(paramMap);
	}

	@Override
	public List<CarssumQuoteUserInfoDto> selectQuotePaymentSuccessList(CarssumMap paramMap) {
		// TODO Auto-generated method stub
		return dao.selectQuotePaymentSuccessList(paramMap);
	}
	
	@Override
	public CarssumQuoteUserInfoDto selectQuotePaymentSuccessDetail(CarssumMap paramMap) {
		// TODO Auto-generated method stub
		return dao.selectQuotePaymentSuccessDetail(paramMap);
	}

	@Override
	public int updateCompliteQuoteRentCompany(CarssumMap paramMap) {
		// TODO Auto-generated method stub
		return dao.updateCompliteQuoteRentCompany(paramMap);
	}

	@Override
	public int updateNotChoiseQuoteRentCompany(CarssumMap paramMap) {
		// TODO Auto-generated method stub
		return dao.updateNotChoiseQuoteRentCompany(paramMap);
	}
	
}
