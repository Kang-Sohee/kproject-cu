package com.ohdocha.cu.kprojectcu.mapper;


import com.ohdocha.cu.kprojectcu.domain.*;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DochaRentcarDao {

	public List<DochaQuoteUserInfoDto> selectQuoteUserList(DochaMap param);
	public List<DochaQuoteCompanyDto> selectQuoteCompanyList(DochaMap param);
	public DochaQuoteUserInfoDto selectQuoteUserInfo(DochaMap param);
	public List<DochaRentCompanyCarDto> selectCompanyCarList(DochaMap param);

	public default List<DochaRentCompanyCarDto> selectCarModelList(DochaMap param) {
		return null;
	}

	public DochaRentCompanyCarDto selectCompanyCarInfo(DochaMap param);
	public int insertQuoteRentCompanyInfo(DochaMap param);
	public DochaCarInsuranceDto selectCarInsuranceInfo(DochaMap param);
	public int updateQuoteStatus(DochaMap param);
	public List<DochaCarOptionDto> selectCarOptionList(DochaMap param);
	int updateQuoteUser(DochaMap param);
	int updateQuoteRentCompany(DochaMap param);
	public DochaQuoteUserInfoDto selectQuoteCompanyInfo (DochaMap param);
	public int updateQuoteRentCompanyByUser(DochaMap param);


	//견적요청 중 회원사가 견적을 넣은 항목은 목록가져오기
	public List<DochaQuoteCompanyDto> selectQuoteRentCompany(DochaMap param);


	//전체 회원사 목록
	public List<DochaRentCompanyDto> selectRentCompanyList();

	public List<DochaRentCompanyDto> selectRentStaffAndCompanyList(DochaMap param);
	public DochaQuoteUserInfoDto selectRentCompanyInfo(DochaMap param);

	public List<DochaRentCompanyDto> selectRentCompanyListAll(DochaMap param);

	public List<DochaRentCompanyCarDto> selectTargetCarType(DochaMap param);

	public DochaQuoteCompanyDto selectQuoteRentCompanyDetail(DochaMap param);

	//결제한 회원사, 직원에게 알림톡 보내는 용도의 쿼리
	public List<DochaRentCompanyDto> selectRentStaffAndCompanyListForPaymentComP(DochaMap param);
	public List<DochaRentCompanyDto> selectRentCompanyListAllForPaymentComP(DochaMap param);

}
