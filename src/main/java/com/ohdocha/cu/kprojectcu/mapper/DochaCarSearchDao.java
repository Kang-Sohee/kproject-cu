package com.ohdocha.cu.kprojectcu.mapper;

import com.ohdocha.cu.kprojectcu.domain.*;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface DochaCarSearchDao {

    //결제전 차량정보 상세
    public List<DochaCarSearchPaymentDetailDto> selectCarSearchDetail(DochaMap paramMap);

    public List<DochaCarInfoDto> selectTargetCarList(DochaMap param);

    public List<DochaCarInfoDto> selectTargetCarListSearchCarOption(DochaMap param);

    public List<DochaCarInfoDto> selectTargetCarForExtension(DochaMap param);

    // 요금 계산에 필요한 휴무일 가져옴
    public List<DochaHolidayDto> selectHolidayList();

    //결제상세
    public DochaPaymentResultDto selectPaymentSuccessDetail(DochaMap param);

    //예약가능여부 업데이트
    public int updateDcCarInfo(DochaMap param);

    //요금계산
    public List<DochaCalcRentFeeDto> getRentFee(List<DochaMap> param);

    //결제페이지 차량 상세정보
    public DochaCarInfoDto selectTargetCar(DochaMap paramMap);


}


