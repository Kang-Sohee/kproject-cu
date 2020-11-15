package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaCarInfoDto;
import com.ohdocha.cu.kprojectcu.domain.DochaCarSearchPaymentDetailDto;
import com.ohdocha.cu.kprojectcu.domain.DochaPaymentResultDto;
import com.ohdocha.cu.kprojectcu.util.DochaMap;

import java.util.List;


public interface DochaCarSearchService {

    //결제전 차량정보 상세
    public List<DochaCarSearchPaymentDetailDto> selectCarSearchDetail(DochaMap paramMap);

    public List<DochaCarInfoDto> selectTargetCarList(DochaMap paramMap);

    public DochaPaymentResultDto selectPaymentSuccessDetail(DochaMap param);

    public int updateCdtCarInfo(DochaMap param);
    
    //결제페이지 차량 상세정보
    public DochaCarInfoDto selectTargetCar(DochaMap param);
}

