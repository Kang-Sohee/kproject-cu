package com.ohdocha.cu.kprojectcu.mapper;


import com.ohdocha.cu.kprojectcu.domain.DochaPaymentReserveDto;
import com.ohdocha.cu.kprojectcu.domain.DochaQuoteUserDto;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface DochaQuoteUserDao {

	//견적요청(user/insertEstimate.do)
	public int insertQuoteUser(DochaQuoteUserDto paramDto);
	
	//견적요청리스트(user/estimate.do)
	public List<DochaQuoteUserDto> selectUserQuoteList(DochaMap paramMap);
	
	//견적요청 상세(user/userEstDetail.do)
	public DochaQuoteUserDto selectUserQuoteInfo(DochaMap paramMap);
	
	//결제완료리스트(user/userPaymentList.do)
	public List<DochaQuoteUserDto> selectUserQuotePaymentList(DochaMap paramMap);
	
	//결제 후 사용자 견적요청 업데이트
	public int updateQuoteUser(DochaMap paramMap);
	

	public DochaPaymentReserveDto selectUserQuoteInfoUsingPayment(DochaMap paramMap);
	
	//user/estimate.do?tab=0 of 견적요청 List(user/getUserEstDetail.json) 
	public List<DochaPaymentReserveDto> selectQuoteUserDetail(DochaMap paramMap);
	
	public DochaQuoteUserDto selectRentCompanyQuoteUser(DochaMap paramMap);
	
}
