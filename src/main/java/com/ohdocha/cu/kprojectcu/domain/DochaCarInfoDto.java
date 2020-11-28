package com.ohdocha.cu.kprojectcu.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;
@Getter
@Setter
@ToString
@NoArgsConstructor
@Alias("DochaCarInfoDto")
public class DochaCarInfoDto {

	private String rowNumber;
	private String totalRowCount;
	private String crIdx;
	private String mdIdx;
	private String rtIdx;
	private String modelName;
	private String modelDetailName;
	private String fuelCode;
	private String fuelName;
	private String transmissionCode;
	private String driveTypeCode;
	private String cartypeCode;
	private String driveLicenseCode;
	private String manufacturerCode;
	private String colorName;
	private String displacement;
	private String year;
	private String mileage;
	private String maximumPassenger;
	private String pyIdx;
	private String shorttermFee;
	private String longtermFee;
	private String longtermDeposit;
	private String carStatusCode;
	private String reserveAbleYn;
	private String carRegDt;
	private String carNumber;
	private String carChassisNumber;
	private String imgIdx;
	private String carDriveLimit;
	private String ageLimit;
	private String garageAddr;
	private String carEtc;
	private String regId;
	private String regDt;
	private String modId;
	private String modDt;
	private String delYn;
	private String dailyStandardPay;
	private String monthlyStandardPay;
	private String dailyMaxRate;
	private String monthlyMaxRate;
	private String month3Deposit;
	private String month6Deposit;
	private String month9Deposit;
	private String month12Deposit;
	private String deliveryStandardPay;
	private String deliveryAddPay;
	private String deliveryMaxRate;
	private String dailyYn;
	private String monthlyYn;
	private String closedvehicleYn;
	private String ciIdx;
	private String perIdx;
	private String companyAddress;
	private String companyName;
	private String companyContact1;
	private String branchName;
	private String optionDetailValue;
	private String optionDetailCode;
	private String calcRentFee;
	private String calcDisRentFee;
	private String mmRentFee;       // 장기 월 대여요금
	private String mmLastRentFee;   // 장기 마지막 월 대여요금
	private String insuranceFee;
	private String insuranceFee2;
	private String insuranceFee3;
	private String insuranceFee4;
	private String carImgList;


}
