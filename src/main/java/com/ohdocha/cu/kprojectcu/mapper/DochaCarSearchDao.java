package com.ohdocha.cu.kprojectcu.mapper;

import com.ohdocha.cu.kprojectcu.domain.DochaCalcRentFeeDto;
import com.ohdocha.cu.kprojectcu.domain.DochaCarInfoDto;
import com.ohdocha.cu.kprojectcu.domain.DochaCarSearchPaymentDetailDto;
import com.ohdocha.cu.kprojectcu.domain.DochaPaymentResultDto;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DochaCarSearchDao {

	//결제전 차량정보 상세
	public List<DochaCarSearchPaymentDetailDto> selectCarSearchDetail(DochaMap paramMap);
	public List<DochaCarInfoDto> selectTargetCarList(DochaMap param);

	//결제상세
	public DochaPaymentResultDto selectPaymentSuccessDetail(DochaMap param);
	
	//예약가능여부 업데이트
	public int updateDcCarInfo(DochaMap param);

	//요금계산
	public List<DochaCalcRentFeeDto> getRentFee(List<DochaMap> param);

}


