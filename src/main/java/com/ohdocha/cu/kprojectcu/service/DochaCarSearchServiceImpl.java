package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaCalcRentFeeDto;
import com.ohdocha.cu.kprojectcu.domain.DochaCarInfoDto;
import com.ohdocha.cu.kprojectcu.domain.DochaCarSearchPaymentDetailDto;
import com.ohdocha.cu.kprojectcu.domain.DochaPaymentResultDto;
import com.ohdocha.cu.kprojectcu.mapper.DochaCarSearchDao;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service("carSearch")
@Slf4j
@AllArgsConstructor
@Transactional
public class DochaCarSearchServiceImpl implements DochaCarSearchService {

    @Autowired
    private final DochaCarSearchDao dao;

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
        try {

            resData = dao.selectTargetCarList(param);
            List<DochaMap> tmpList = new ArrayList<DochaMap>();

            if (resData.size() > 0) {
                for (int i = 0; i < resData.size(); i++) {
                    DochaMap tmpParam = new DochaMap();
                    String crIdx = resData.get(i).getCrIdx();
                    tmpParam.put("crIdx", crIdx);
                    tmpParam.put("rentStartDt", param.getString("rentStartDt"));
                    tmpParam.put("rentEndDt", param.getString("rentEndDt"));

                    tmpList.add(tmpParam);
                }

//                List<DochaCalcRentFeeDto> feeTmpList = dao.getRentFee(tmpList);
//
//                for (int i = 0; i < feeTmpList.size(); i++) {
//                    String crIdx = feeTmpList.get(i).getCrIdx();
//                    String disRentFee = feeTmpList.get(i).getDisRentFee();
//                    for (int idx = 0; idx < resData.size(); idx++) {
//                        if (resData.get(idx).getCrIdx().equals(crIdx)) {
//                            resData.get(idx).setCalcDisRentFee(disRentFee);
//                        }
//                    }
//                }

                for (int idx = 0; idx < resData.size(); idx++) {
                            resData.get(idx).setCalcDisRentFee("1000000");
                }

                //MapComparator comp = new MapComparator("dailyStandardPay");

                Collections.sort(resData, new Comparator<DochaCarInfoDto>() {
                    @Override
                    public int compare(DochaCarInfoDto o1, DochaCarInfoDto o2) {
                        return o1.getCalcDisRentFee().compareTo(o2.getCalcDisRentFee());
                    }
                });
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

}
