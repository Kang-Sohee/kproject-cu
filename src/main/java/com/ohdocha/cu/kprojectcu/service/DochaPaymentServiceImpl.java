package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.*;
import com.ohdocha.cu.kprojectcu.mapper.DochaPaymentDao;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("payment")
@Slf4j
@AllArgsConstructor
@Transactional
public class DochaPaymentServiceImpl implements DochaPaymentService{

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
	
}
