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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        String rentStartDt = param.getString("rentStartDt");
        String rentEndDt = param.getString("rentEndDt");
        String addr1 = setLocationAddr(param.getString("addr1"));
        String delAddr1 =  setDeliveryAddr(param.getString("addr1"));
        long calHour = 0;

        String startDate = rentStartDt.substring(0, 4) + "-" + rentStartDt.substring(4, 6) + "-" + rentStartDt.substring(6, 8);     // yyyy-MM-dd
        String startTime = rentStartDt.substring(8, 10) + rentStartDt.substring(10, 12);          // HHmm
        String startTimestamp = startDate + " " + startTime.substring(0, 2) + ":" + startTime.substring(2, 4);          // yyyy-MM-dd HH:mm

        String endDate = rentEndDt.substring(0, 4) + "-" + rentEndDt.substring(4, 6) + "-" + rentEndDt.substring(6, 8);     // yyyy-MM-dd
        String endTime = rentEndDt.substring(8, 10) + rentEndDt.substring(10, 12);          // HHmm
        String endTimestamp = endDate + " " + endTime.substring(0, 2) + ":" + endTime.substring(2, 4);          // yyyy-MM-dd HH:mm

        param.put("startDate", startDate);
        param.put("startTime", startTime);
        param.put("startTimestamp", startTimestamp);
        param.put("endDate", endDate);
        param.put("endTime", endTime);
        param.put("endTimestamp", endTimestamp);
        param.put("delAddr1", delAddr1);
        param.put("addr1", addr1);

        try {
            // 연장 결제에서 요금 검색 일 경우
            if (param.get("mode") != null) {
                resData = dao.selectTargetCarForExtension(param);
                List<DochaMap> tmpList = new ArrayList<DochaMap>();

                long calDateDays = 0;

                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
                    Date FirstDate = format.parse(rentStartDt);
                    Date SecondDate = format.parse(rentEndDt);

                    long calDate = FirstDate.getTime() - SecondDate.getTime();

                    calDateDays = calDate / (24 * 60 * 60 * 1000);

                    calDateDays = Math.abs(calDateDays);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (resData.size() > 0) {
                    for (int i = 0; i < resData.size(); i++) {
                        DochaMap tmpParam = new DochaMap();
                        String crIdx = resData.get(i).getCrIdx();
                        String rtIdx = resData.get(i).getRtIdx();
                        tmpParam.put("crIdx", crIdx);
                        tmpParam.put("rentStartDt", param.getString("rentStartDt"));
                        tmpParam.put("rentEndDt", param.getString("rentEndDt"));

                        if (calDateDays >= 30) {
                            dochaCalcRentFeeDtoList.add(calculationPay.getMonthlyTotalFee(crIdx, rentStartDt, rentEndDt));
                        } else {
                            dochaCalcRentFeeDtoList.add(calculationPay.getDailyTotalFee(crIdx, rtIdx, rentStartDt, rentEndDt));
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
                        String rentFee = dochaCalcRentFeeDtoList.get(i).getRentFee();
                        for (int idx = 0; idx < resData.size(); idx++) {
                            if (resData.get(idx).getCrIdx().equals(crIdx)) {
                                resData.get(idx).setCalcDisRentFee(disRentFee);
                                resData.get(idx).setMmRentFee(mmRentFee);
                                resData.get(idx).setMmLastRentFee(mmLastRentFee);
                                resData.get(idx).setInsuranceFee(insuranceFee);
                                resData.get(idx).setInsuranceFee2(insuranceFee2);
                                resData.get(idx).setInsuranceFee3(insuranceFee3);
                                resData.get(idx).setInsuranceFee4(insuranceFee4);
                                resData.get(idx).setCalcRentFee(rentFee);
                                ;
                            }
                        }
                    }
                }

            }
            // 메인 -> 리스트 검색 일 경우
            else {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
                    Date FirstDate = format.parse(rentStartDt);
                    Date SecondDate = format.parse(rentEndDt);

                    long calDate = FirstDate.getTime() - SecondDate.getTime();

                    calHour = calDate / (60 * 60 * 1000);

                    calHour = Math.abs(calHour);
                    param.put("calHour", calHour);
                    if (calHour < 720)
                        param.put("longTermYn", "ST");
                    else
                        param.put("longTermYn", "LT");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (param.get("carOptionCodeList").equals("") || param.get("carOptionCodeList").equals("%")) {
                    resData = dao.selectTargetCarList(param);
                } else {
                    resData = dao.selectTargetCarListSearchCarOption(param);
                }

                List<DochaMap> tmpList = new ArrayList<DochaMap>();

                long calDateDays = 0;

                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
                    Date FirstDate = format.parse(rentStartDt);
                    Date SecondDate = format.parse(rentEndDt);

                    long calDate = FirstDate.getTime() - SecondDate.getTime();

                    calDateDays = calDate / (24 * 60 * 60 * 1000);

                    calDateDays = Math.abs(calDateDays);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (resData.size() > 0) {
                    for (int i = 0; i < resData.size(); i++) {
                        DochaMap tmpParam = new DochaMap();
                        String crIdx = resData.get(i).getCrIdx();
                        String rtIdx = resData.get(i).getRtIdx();
                        tmpParam.put("crIdx", crIdx);
                        tmpParam.put("rtIdx", rtIdx);
                        tmpParam.put("rentStartDt", param.getString("rentStartDt"));
                        tmpParam.put("rentEndDt", param.getString("rentEndDt"));

                        if (calDateDays >= 30) {
                            dochaCalcRentFeeDtoList.add(calculationPay.getMonthlyTotalFee(crIdx, rentStartDt, rentEndDt));
                        } else {
                            dochaCalcRentFeeDtoList.add(calculationPay.getDailyTotalFee(crIdx, rtIdx, rentStartDt, rentEndDt));
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
                        String rentFee = dochaCalcRentFeeDtoList.get(i).getRentFee();
                        for (int idx = 0; idx < resData.size(); idx++) {
                            if (resData.get(idx).getCrIdx().equals(crIdx)) {
                                resData.get(idx).setCalcDisRentFee(disRentFee);
                                resData.get(idx).setMmRentFee(mmRentFee);
                                resData.get(idx).setMmLastRentFee(mmLastRentFee);
                                resData.get(idx).setInsuranceFee(insuranceFee);
                                resData.get(idx).setInsuranceFee2(insuranceFee2);
                                resData.get(idx).setInsuranceFee3(insuranceFee3);
                                resData.get(idx).setInsuranceFee4(insuranceFee4);
                                resData.get(idx).setCalcRentFee(rentFee);
                                ;
                            }
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

    private String setDeliveryAddr (String addr) {
        String deliveryAddr = addr;

        switch (deliveryAddr) {
            case "경남":
                deliveryAddr = "경상남도";
                break;
            case "경북":
                deliveryAddr = "경상북도";
                break;
            case "전남":
                deliveryAddr = "전라남도";
                break;
            case "전북":
                deliveryAddr = "전라북도";
                break;
            case "충남":
                deliveryAddr = "충청남도";
                break;
            case "충북":
                deliveryAddr = "충청북도";
                break;
            default:
                break;
        }

        return deliveryAddr;
    }

    private String setLocationAddr (String addr) {
        String locationAddr = addr;

        switch (locationAddr) {
            case "강원도":
                locationAddr = "강원";
                break;
            case "경기도":
                locationAddr = "경기";
                break;
            case "경상남도":
                locationAddr = "경남";
                break;
            case "경상북도":
                locationAddr = "경북";
                break;
            case "광주광역시":
                locationAddr = "광주";
                break;
            case "대구광역시":
                locationAddr = "대구";
                break;
            case "대전광역시":
                locationAddr = "대전";
                break;
            case "부산광역시":
                locationAddr = "부산";
                break;
            case "서울특별시":
                locationAddr = "서울";
                break;
            case "울산광역시":
                locationAddr = "울산";
                break;
            case "인천광역시":
                locationAddr = "인천";
                break;
            case "전라남도":
                locationAddr = "전남";
                break;
            case "전라북도":
                locationAddr = "전북";
                break;
            case "충청남도":
                locationAddr = "충남";
                break;
            case "충청북도":
                locationAddr = "충북";
                break;
            default:
                break;
        }

        return locationAddr;
    }

}
