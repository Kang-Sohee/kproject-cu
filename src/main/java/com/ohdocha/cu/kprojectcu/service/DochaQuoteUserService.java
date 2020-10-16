package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaPaymentReserveDto;
import com.ohdocha.cu.kprojectcu.domain.DochaQuoteUserDto;
import com.ohdocha.cu.kprojectcu.util.DochaMap;

import java.util.List;

public interface DochaQuoteUserService {

	public int insertQuoteUser(DochaQuoteUserDto paramDto);
	
	public List<DochaQuoteUserDto> selectUserQuoteList(DochaMap paramMap);
	
	public DochaQuoteUserDto selectUserQuoteInfo(DochaMap paramMap);
	
	//결제완료리스트(user/userPaymentList.do)
	public List<DochaQuoteUserDto> selectUserQuotePaymentList(DochaMap paramMap);
	
	public DochaQuoteUserDto selectRentCompanyQuoteUser(DochaMap paramMap);
	
	//결제 후 사용자 견적요청 업데이트
	public int updateQuoteUser(DochaMap paramMap);
	
	public DochaPaymentReserveDto selectUserQuoteInfoUsingPayment(DochaMap paramMap);

	//user/estimate.do?tab=0 of 견적요청 List(user/getUserEstDetail.json)
	public List<DochaPaymentReserveDto> selectQuoteUserDetail(DochaMap paramMap);

	/**
	 * 견적취소
	 * @param paramMap
	 * @return 1 : 성공, 2 : 견적없음, 3 : 견적요청자와 취소요청자 불일치, 4 : 견적 기취소, 5 : 취소 update 실패, -1 : 시스템 에러 
	 */
	public int updateCancelQuoteUser(DochaMap paramMap);
}
