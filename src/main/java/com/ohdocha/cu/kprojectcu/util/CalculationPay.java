package com.ohdocha.cu.kprojectcu.util;


import com.ohdocha.cu.kprojectcu.domain.DochaCalcRentFeeDto;
import com.ohdocha.cu.kprojectcu.domain.DochaCarSearchPaymentDetailDto;
import com.ohdocha.cu.kprojectcu.domain.DochaHolidayDto;
import com.ohdocha.cu.kprojectcu.mapper.DochaCarSearchDao;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class CalculationPay {

    @Autowired
    private final DochaCarSearchDao carSearchDao;

    public DochaCalcRentFeeDto getMonthlyTotalFee(String crIdx, String rentStartDay, String rentEndDay) {
        DochaMap reqParam = new DochaMap();
        reqParam.set("crIdx", crIdx);

        long calDateDays = 0;

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
            Date firstDate = format.parse(rentStartDay);
            Date secondDate = format.parse(rentEndDay);

            long calDate = firstDate.getTime() - secondDate.getTime();

            calDateDays = calDate / (24 * 60 * 60 * 1000);

            calDateDays = Math.abs(calDateDays);

            // 10분이라도 초과 되면 하루가 증가한다. ( 소수점이 있는 경우 )
            double decimalcalDate = Math.abs((double) calDate / (24 * 60 * 60 * 1000));
            if (decimalcalDate > calDateDays) {
                calDateDays++;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String rentFee;
        String disRentFee;
        String mmRentFee;
        String mmLastRentFee;
        String insuranceFee;
        String insuranceFee2;
        String insuranceFee3;
        String insuranceFee4;

        List<DochaCarSearchPaymentDetailDto> paymentDetailDto = carSearchDao.selectCarSearchDetail(reqParam);

        int totalDay = (int) Math.ceil(calDateDays);        // 총 일 수
        String dailyStandardPay = paymentDetailDto.get(0).getDailyStandardPay();    // 해당 차량의 일요금
        String monthlyStandardPay = paymentDetailDto.get(0).getMonthlyStandardPay();    // 해당 차량의 일요금


        if (dailyStandardPay.isEmpty()) {
            dailyStandardPay = "0";
        }
        if (monthlyStandardPay.isEmpty()) {
            monthlyStandardPay = "0";
        }

        double calculateDay = Integer.parseInt(dailyStandardPay);   // 계산용 일요금
        double calculateMonth = Integer.parseInt(monthlyStandardPay);    // 계산용 월요금
        double calculRentFee;    // 계산용 총요금
        double calculTotal;    // 계산용 총요금

        double insuranceCopayment = Integer.parseInt(paymentDetailDto.get(0).getInsuranceCopayment().equals("") ? "0" : paymentDetailDto.get(0).getInsuranceCopayment());
        double insuranceCopayment2 = Integer.parseInt(paymentDetailDto.get(0).getInsuranceCopayment2().equals("") ? "0" : paymentDetailDto.get(0).getInsuranceCopayment2());
        double insuranceCopayment3 = Integer.parseInt(paymentDetailDto.get(0).getInsuranceCopayment3().equals("") ? "0" : paymentDetailDto.get(0).getInsuranceCopayment3());
        double insuranceCopayment4 = Integer.parseInt(paymentDetailDto.get(0).getInsuranceCopayment4().equals("") ? "0" : paymentDetailDto.get(0).getInsuranceCopayment4());
        double monthlyMaxRate = Integer.parseInt(paymentDetailDto.get(0).getMonthlyMaxRate().equals("") ? "10.0" : paymentDetailDto.get(0).getMonthlyMaxRate());
        int monthly = totalDay / 30;
        int days = totalDay % 30;
        double rate = 0.009090909091 * monthly;

        calculateMonth = calculateMonth * monthly;
        calculateMonth = Math.floor(((calculateMonth / 100) * 100) / monthly);
        calculateMonth = Math.round(calculateMonth / 100) * 100.0;
        calculateDay = Math.round(calculateDay / 100) * 100.0;
        calculTotal = calculateDay + (calculateMonth * monthly);

        calculRentFee = calculateMonth * monthly + calculateDay * days;
        if (calculRentFee > calculateMonth * monthly + 1) {
            calculRentFee = calculateMonth * (monthly + 1);
        }


        // 2달 미만은 할인 없이 월 + 일
        if (monthly < 2) {
            // 일대여 요금이 작성되지 않았을 경우
            if (dailyStandardPay.isEmpty()) {
                calculateDay = calculateMonth * 0.1 * days;
            }
            calculateDay = calculateDay * days;

            // 일요금이 월요금을 넘을 경우
            if (calculateDay > calculateMonth) {
                calculateDay = calculateMonth;
            }

        } else {
            if (rate < monthlyMaxRate / 100)
                calculateMonth = calculateMonth - (0.009090909091 * calculateMonth * (monthly - 1));
            else if (rate >= monthlyMaxRate / 100 && monthlyMaxRate != 0)
                calculateMonth = calculateMonth - (monthlyMaxRate / 100 * calculateMonth);

            calculateMonth = Math.round(calculateMonth * 100) / 100.0;

            if (monthly < 6) {
                if (dailyStandardPay.isEmpty()) {
                    calculateDay = calculateMonth * 0.1 * days;
                } else {
                    calculateDay = calculateDay * days;
                }
                if (calculateDay > calculateMonth) {
                    calculateDay = calculateMonth;
                }
            } else {
                calculateDay = calculateMonth * 1 / 30 * days;
            }
        }

        rentFee = Integer.toString((int) calculTotal);

        calculateMonth = calculateMonth * monthly;
        calculateMonth = Math.floor(((calculateMonth / 100) * 100) / monthly);
        calculateMonth = Math.round(calculateMonth / 100) * 100.0;
        calculateDay = Math.round(calculateDay / 100) * 100.0;
        calculTotal = calculateDay + (calculateMonth * monthly);


        insuranceCopayment = 0;
        insuranceCopayment2 = 0;
        insuranceCopayment3 = 0;
        insuranceCopayment4 = 0;

        rentFee = Integer.toString((int) calculRentFee);
        mmRentFee = Integer.toString((int) calculateMonth);
        mmLastRentFee = Integer.toString((int) calculateDay);
        disRentFee = Integer.toString((int) calculTotal);
        insuranceFee = Integer.toString((int) insuranceCopayment);
        insuranceFee2 = Integer.toString((int) insuranceCopayment2);
        insuranceFee3 = Integer.toString((int) insuranceCopayment3);
        insuranceFee4 = Integer.toString((int) insuranceCopayment4);

        DochaCalcRentFeeDto dochaCalcRentFeeDto = new DochaCalcRentFeeDto();
        dochaCalcRentFeeDto.setCrIdx(crIdx);
        dochaCalcRentFeeDto.setRentFee(rentFee);
        dochaCalcRentFeeDto.setMmRentFee(mmRentFee);
        dochaCalcRentFeeDto.setMmLastRentFee(mmLastRentFee);
        dochaCalcRentFeeDto.setDisRentFee(disRentFee);
        dochaCalcRentFeeDto.setInsuranceFee(insuranceFee);
        dochaCalcRentFeeDto.setInsuranceFee2(insuranceFee2);
        dochaCalcRentFeeDto.setInsuranceFee3(insuranceFee3);
        dochaCalcRentFeeDto.setInsuranceFee4(insuranceFee4);

        return dochaCalcRentFeeDto;
    }

    public DochaCalcRentFeeDto getDailyTotalFee(String crIdx, String rentStartDay, String rentEndDay) {
        DochaMap reqParam = new DochaMap();
        reqParam.set("crIdx", crIdx);

        long calMinute = 0;
        long calDays = 0;
        long roundDays = 0;
        long remainMinute = 0;
        String rentFee;
        String disRentFee;
        String mmRentFee;
        String mmLastRentFee;
        String insuranceFee;
        String insuranceFee2;
        String insuranceFee3;
        String insuranceFee4;

        Date startDate = null;
        Date endDate = null;

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
            startDate = format.parse(rentStartDay);
            endDate = format.parse(rentEndDay);

            long calDate = startDate.getTime() - endDate.getTime();

            calMinute = calDate / (60 * 1000);
            calMinute = Math.abs(calMinute);            // 총 분 수로 변경
            calDays = calMinute / 1440;                 // 시, 분을 뺀 일 수
            remainMinute = calMinute % 1440;            // 일을 빼고 난 후 남은 분  [ 총 시간 : calDays + remainMinute ]

            System.out.println("일 수 " + calDays);
            System.out.println("남은 분" + remainMinute);
            roundDays = Math.abs(calDate / (24 * 60 * 60 * 1000));        // 보험 계산에 사용할 1분이라도 초과되면 하루가 증가하는 roundDays

            roundDays = Math.abs(roundDays);
            // 10분이라도 초과 되면 하루가 증가한다. ( 소수점이 있는 경우 )
            double decimalcalDate = Math.abs((double) calDate / (24 * 60 * 60 * 1000));
            if (decimalcalDate > roundDays) {
                roundDays++;
            }
            System.out.println("round 일 수 : " + roundDays);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<DochaCarSearchPaymentDetailDto> paymentDetailDto = carSearchDao.selectCarSearchDetail(reqParam);
        double dailyMaxRate = Integer.parseInt(paymentDetailDto.get(0).getDailyMaxRate().isEmpty() || paymentDetailDto.get(0).getDailyMaxRate() == null
                ? "10.0" : paymentDetailDto.get(0).getDailyMaxRate());

        int totalDay = (int) calDays;                           // 일 수
        double disPer = (calMinute - 1440) / 30;                // 할인율. 1일 이후부터 시작.
        disPer = disPer * 0.02976;
        disPer = Math.round(disPer * 100) / 100.0;

        // 총 대여일이 8일(192시간) 이상일 경우 할인율은 10%
        if (totalDay >= 8 || (calMinute / 60) >= 192 || disPer >= dailyMaxRate) {
            disPer = dailyMaxRate;
        }

        String dailyStandardPay = paymentDetailDto.get(0).getDailyStandardPay();    // 해당 차량의 일요금
        String monthlyStandardPay = paymentDetailDto.get(0).getMonthlyStandardPay();    // 해당 차량의 일요금

        if (dailyStandardPay.isEmpty()) {
            dailyStandardPay = "0";
        }
        if (monthlyStandardPay.isEmpty()) {
            monthlyStandardPay = "0";
        }

        double calculateMinute;   // 계산용 일요금
        double calculateDay = Integer.parseInt(dailyStandardPay);   // 계산용 일요금
        double calculateMonth = Integer.parseInt(monthlyStandardPay);    // 계산용 월요금
        double calculRentFee;    // 계산용 총요금
        double calculTotal;    // 계산용 총요금

        double insuranceCopayment = Integer.parseInt(paymentDetailDto.get(0).getInsuranceCopayment().equals("") ? "0" : paymentDetailDto.get(0).getInsuranceCopayment());
        double insuranceCopayment2 = Integer.parseInt(paymentDetailDto.get(0).getInsuranceCopayment2().equals("") ? "0" : paymentDetailDto.get(0).getInsuranceCopayment2());
        double insuranceCopayment3 = Integer.parseInt(paymentDetailDto.get(0).getInsuranceCopayment3().equals("") ? "0" : paymentDetailDto.get(0).getInsuranceCopayment3());
        double insuranceCopayment4 = Integer.parseInt(paymentDetailDto.get(0).getInsuranceCopayment4().equals("") ? "0" : paymentDetailDto.get(0).getInsuranceCopayment4());

        // 중간 분 요금 ( 30분 당 하루 요금의 1/10 요금을 적용 )
        calculateMinute = remainMinute / 30 * (calculateDay / 20);

        // 1~29분 사이의 잔여 분이 있으면 무조건 30분의 요금을 한번 추가.
        if (remainMinute % 30 > 0) {
            calculateMinute = calculateMinute + (calculateDay / 20);
        }

        // 남은 분 요금이 일 요금을 넘으면 일 요금으로.
        if (calculateMinute >= calculateDay) {
            calculateMinute = calculateDay;
        }

        // 중간 일 요금
        calculateDay = calculateDay * totalDay;
        calculateDay = Math.ceil(calculateDay / 100) * 100;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        LocalDateTime startTime = LocalDateTime.parse(rentStartDay, dateTimeFormatter);
        LocalDateTime endTime = LocalDateTime.parse(rentEndDay, dateTimeFormatter);

        // TODO : 주말, 공휴일 할증 추가 필요.
        double addPay = 0.0;    // 계산용 총요금
        long cycleCount = 0;    // 할증 사이클 ( 30분 당 +1 )
        int addCheck = 0;       // 공휴일, 주말 중복 체크


        //List<DochaHolidayDto> holydayList = carSearchDao.selectHolidayList();

        while (endTime.isAfter(startTime)) {
            DayOfWeek dayOfWeek = startTime.getDayOfWeek();

            if ((dayOfWeek == DayOfWeek.FRIDAY && startTime.getHour() >= 12) || dayOfWeek == DayOfWeek.SATURDAY || (dayOfWeek == DayOfWeek.SUNDAY && startTime.getHour() <= 12)) {
                cycleCount++;
            }
            startTime =  startTime.plusMinutes(30);
            addCheck = 0;
        }

        // 기간 요금제에 따른 할증 / 할인 계산


        // 할증 사이클이 최대 치를 넘지 않도록 고정
        if ( cycleCount > calDays * 20 + remainMinute / 30 ) {
            cycleCount = calDays * 20 + remainMinute / 30;
        }

        // 할증 요금 = addPay
        addPay = cycleCount * Integer.parseInt(dailyStandardPay) * 0.15 * 0.05 ;

        // TODO : 기간 요금제에 따른 할인 / 할증 필요. ( 성수기 )

        // 총 요금 = 일요금 * 할인율 + 분요금  + 할증 요금
        calculTotal = calculateDay * (100 - disPer) / 100 + calculateMinute + addPay;
        calculRentFee = calculateDay + calculateMinute + addPay;
        calculRentFee = Math.ceil(calculRentFee / 100) * 100.0;
        if (calculRentFee >= calculateMonth) {
            calculRentFee = calculateMonth;
        }


        // TOTAL 요금이 월요금을 넘으면 월요금으로.
        if (calculTotal >= calculateMonth) {
            calculTotal = calculateMonth;
        }

        calculTotal = Math.ceil(calculTotal / 100) * 100.0;

        insuranceCopayment = Math.ceil(insuranceCopayment * roundDays / 100) * 100.0;
        insuranceCopayment2 = Math.ceil(insuranceCopayment2 * roundDays / 100) * 100.0;
        insuranceCopayment3 = Math.ceil(insuranceCopayment3 * roundDays / 100) * 100.0;
        insuranceCopayment4 = Math.ceil(insuranceCopayment4 * roundDays / 100) * 100.0;

        rentFee = Integer.toString((int) calculRentFee);
        mmRentFee = Integer.toString(0);
        mmLastRentFee = Integer.toString(0);
        disRentFee = Integer.toString((int) calculTotal);
        insuranceFee = Integer.toString((int) insuranceCopayment);
        insuranceFee2 = Integer.toString((int) insuranceCopayment2);
        insuranceFee3 = Integer.toString((int) insuranceCopayment3);
        insuranceFee4 = Integer.toString((int) insuranceCopayment4);

        DochaCalcRentFeeDto dochaCalcRentFeeDto = new DochaCalcRentFeeDto();
        dochaCalcRentFeeDto.setCrIdx(crIdx);
        dochaCalcRentFeeDto.setRentFee(rentFee);
        dochaCalcRentFeeDto.setMmRentFee(mmRentFee);
        dochaCalcRentFeeDto.setMmLastRentFee(mmLastRentFee);
        dochaCalcRentFeeDto.setDisRentFee(disRentFee);
        dochaCalcRentFeeDto.setInsuranceFee(insuranceFee);
        dochaCalcRentFeeDto.setInsuranceFee2(insuranceFee2);
        dochaCalcRentFeeDto.setInsuranceFee3(insuranceFee3);
        dochaCalcRentFeeDto.setInsuranceFee4(insuranceFee4);

        return dochaCalcRentFeeDto;

    }


}
