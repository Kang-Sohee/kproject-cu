package com.ohdocha.cu.kprojectcu.mapper;


import com.ohdocha.cu.kprojectcu.domain.*;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DochaPaymentDao {
	
	public int insertReserveMaster(DochaPaymentDto paramMap);
	
	public int insertReserve(DochaPaymentReserveDto paramMap);
	
	public int insertPaymentDetail(DochaPaymentDetailDto paramMap);
	
	public int insertPaymentLog(DochaPaymentLogDto paramMap);
	
	public int updatePaymentLog(DochaPaymentLogDto paramMap);
	
	public int updateReserveMaster(DochaMap paramMap);
	
	public int updateReserve(DochaMap paramMap);
	
	public List<DochaQuoteUserInfoDto> selectQuotePaymentSuccessList(DochaMap paramMap);
		
	public DochaQuoteUserInfoDto selectQuotePaymentSuccessDetail(DochaMap paramMap);
	
	public int updateCompliteQuoteRentCompany(DochaMap paramMap);
	
	public int updateComplitQuoteRentCompany(DochaMap paramMap);
	
	public int updateNotChoiseQuoteRentCompany(DochaMap paramMap);
	
	
	
	

}
