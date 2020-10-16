package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.*;
import com.ohdocha.cu.kprojectcu.util.DochaMap;

import java.util.List;

public interface DochaRentcarService {

	public List<DochaQuoteUserInfoDto> selectQuoteUserList(DochaMap param);
	public List<DochaQuoteCompanyDto> selectQuoteCompanyList(DochaMap param);
	public DochaQuoteUserInfoDto selectQuoteUserInfo(DochaMap param);
	public List<DochaRentCompanyCarDto> selectCompanyCarList(DochaMap param);
	public List<DochaRentCompanyCarDto> selectCarModelList(DochaMap param);
	public DochaRentCompanyCarDto selectCompanyCarInfo(DochaMap param);
	public int insertQuoteRentCompanyInfo(DochaMap param);
	public int updateQuoteStatus(DochaMap param);
	public List<DochaCarOptionDto> selectCarOptionList(DochaMap param);
	public int cancelQuote(DochaMap param);
	public DochaQuoteUserInfoDto selectQuoteCompanyInfo (DochaMap param);
	public int updateQuoteRentCompanyByUser(DochaMap param);
	public List<DochaQuoteCompanyDto> selectQuoteRentCompany(DochaMap param);
	
	public List<DochaRentCompanyDto> selectRentCompanyList();
	
	public List<DochaRentCompanyDto> selectRentStaffAndCompanyList(DochaMap param);
	public DochaQuoteUserInfoDto selectRentCompanyInfo(DochaMap param);
	
	public List<DochaRentCompanyDto> selectRentCompanyListAll(DochaMap param);
	public List<DochaRentCompanyCarDto> selectTargetCarType(DochaMap param);
		
	/**
	 * 
	 * @param paramMap
	 * @return 1 : 성공, 2 : 견적없음, 3 : 견적요청자와 취소요청자 불일치, 4 : 견적 기취소, 5 : 취소 update 실패, -1 : 시스템 에러 
	 */
	public int updateCancelQuoteRentCompany(DochaMap paramMap);
	
	//결제한 회원사, 직원에게 알림톡 보내는 용도의 쿼리
	public List<DochaRentCompanyDto> selectRentStaffAndCompanyListForPaymentComP(DochaMap param);
	public List<DochaRentCompanyDto> selectRentCompanyListAllForPaymentComP(DochaMap param);
	
	public DochaQuoteCompanyDto selectQuoteRentCompanyDetail(DochaMap param);
	
	public DochaCarInsuranceDto selectCarInsuranceInfo(DochaMap param);

}
