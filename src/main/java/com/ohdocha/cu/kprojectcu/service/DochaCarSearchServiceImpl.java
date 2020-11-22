package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaCalcRentFeeDto;
import com.ohdocha.cu.kprojectcu.domain.DochaCarInfoDto;
import com.ohdocha.cu.kprojectcu.domain.DochaCarSearchPaymentDetailDto;
import com.ohdocha.cu.kprojectcu.domain.DochaPaymentResultDto;
import com.ohdocha.cu.kprojectcu.mapper.DochaCarSearchDao;
import com.ohdocha.cu.kprojectcu.util.CalculationPay;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("carSearch")
@Slf4j
@AllArgsConstructor
@Transactional
public class DochaCarSearchServiceImpl implements DochaCarSearchService {

    @Autowired
    private final DochaCarSearchDao dao;

    private final CalculationPay calculationPay;

    private final static Logger logger = LoggerFactory.getLogger(DochaCarSearchServiceImpl.class);

    /*
     * 차량목록에서 차량 선택 후 차량조회 상세 부분
     * */
    @Override
    public List<DochaCarSearchPaymentDetailDto> selectCarSearchDetail(DochaMap paramMap) {
        // TODO Auto-generated method stub
        return dao.selectCarSearchDetail(paramMap);
    }

    @Override
    public List<DochaCarInfoDto> selectTargetCarList(DochaMap param) {
        List<DochaCarInfoDto> resData = new ArrayList<DochaCarInfoDto>();
        List<DochaCalcRentFeeDto> dochaCalcRentFeeDtoList = new ArrayList<DochaCalcRentFeeDto>();

        try {
            resData = dao.selectTargetCarList(param);
            List<DochaMap> tmpList = new ArrayList<DochaMap>();
            String rentStartDt = param.getString("rentStartDt");
            String rentEndDt = param.getString("rentEndDt");

            long calDateDays = 0;

            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
                Date FirstDate = format.parse(rentStartDt);
                Date SecondDate = format.parse(rentEndDt);

                long calDate = FirstDate.getTime() - SecondDate.getTime();

                calDateDays = calDate / (24 * 60 * 60 * 1000);

                calDateDays = Math.abs(calDateDays);

                System.out.println("두 날짜의 날짜 차이: " + calDateDays);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (resData.size() > 0) {
                for (int i = 0; i < resData.size(); i++) {
                    DochaMap tmpParam = new DochaMap();
                    String crIdx = resData.get(i).getCrIdx();
                    tmpParam.put("crIdx", crIdx);
                    tmpParam.put("rentStartDt", param.getString("rentStartDt"));
                    tmpParam.put("rentEndDt", param.getString("rentEndDt"));

                    if (calDateDays >= 30) {
                        dochaCalcRentFeeDtoList.add(calculationPay.getMonthlyTotalFee(crIdx, rentStartDt, rentEndDt));
                    }
                    else {
                        dochaCalcRentFeeDtoList.add(calculationPay.getDailyTotalFee(crIdx, rentStartDt, rentEndDt));
                    }

                    tmpList.add(tmpParam);
                }

                for (int i = 0; i < dochaCalcRentFeeDtoList.size(); i++) {
                    String crIdx = dochaCalcRentFeeDtoList.get(i).getCrIdx();
                    String disRentFee = dochaCalcRentFeeDtoList.get(i).getDisRentFee();
                    String mmRentFee = dochaCalcRentFeeDtoList.get(i).getMmRentFee();
                    String mmLastRentFee = dochaCalcRentFeeDtoList.get(i).getMmLastRentFee();
                    String insuranceFee = dochaCalcRentFeeDtoList.get(i).getInsuranceFee();
                    String insuranceFee2 = dochaCalcRentFeeDtoList.get(i).getInsuranceFee2();
                    String insuranceFee3 = dochaCalcRentFeeDtoList.get(i).getInsuranceFee3();
                    String insuranceFee4 = dochaCalcRentFeeDtoList.get(i).getInsuranceFee4();
                    for (int idx = 0; idx < resData.size(); idx++) {
                        if (resData.get(idx).getCrIdx().equals(crIdx)) {
                            resData.get(idx).setCalcDisRentFee(disRentFee);
                            resData.get(idx).setMmRentFee(mmRentFee);
                            resData.get(idx).setMmLastRentFee(mmLastRentFee);
                            resData.get(idx).setInsuranceFee(insuranceFee);
                            resData.get(idx).setInsuranceFee2(insuranceFee2);
                            resData.get(idx).setInsuranceFee3(insuranceFee3);
                            resData.get(idx).setInsuranceFee4(insuranceFee4);
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return resData;
    }

    /*
     * 결제상세
     * */
    @Override
    public DochaPaymentResultDto selectPaymentSuccessDetail(DochaMap param) {
        // TODO Auto-generated method stub
        return dao.selectPaymentSuccessDetail(param);
    }

    @Override
    public int updateCdtCarInfo(DochaMap param) {
        // TODO Auto-generated method stub
        return dao.updateDcCarInfo(param);
    }

    public DochaCarInfoDto selectTargetCar(DochaMap param) {
        return dao.selectTargetCar(param);
    }

}
