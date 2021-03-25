package com.ohdocha.cu.kprojectcu.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

//import gcp.oms.model.base.BaseOmsPaymentif;
@Getter
@Setter
@ToString
@NoArgsConstructor
@Alias("DochaPaymentDto")
public class DochaPaymentDto {
	
	//예약마스터
	private String rmIdx;           
	private String extensionRmIdx;
	private String extensionYn;
	private String reserveTypeCode;
	private String reserveStatusCode;     
	private String longTermYn;
	private String urIdx; 
	private String reserveUserName;
	private String reserveUserContact1;
	private String reserveUserBirthday;
	private String rentStartDay;
	private String rentEndDay;      
	private String rentStartTime;   
	private String rentEndTime;
	private String periodDt;
	private String deliveryTypeCode;
	private String deliveryAddr;    
	private String returnTypeCode;  
	private String returnAddr;
	private String crIdx;
	private String imgIdx;
	private String carTypeCode;
	private String year;
	private String fuelCode;
	private String colorName;
	private String carNumber;
	private String mileage;
	private String modelName;
	private String modelDetailName;
	private String rtIdx;
	private String companyName;
	private String branchName;
	private String companyContact1;
	private String reserveDate;
	private String paymentDate;
	private String carDeposit;
	private String totalFee;
	private String rentFee;
	private String insuranceFee;
	private String deliveryFee;
	private String carDamage1Yn;
	private String carDamage2Yn;
	private String carDamage3Yn;
	private String carDamage4Yn;
	private String personalCover;
	private String propertyDamageCover;
	private String onselfDamageCover;
	private String carDamageCover;
	private String carDamageNumber;
	private String discountFee;
	private String paymentAmount;

	private String paymentTotalAmount;
	private String sumPaymentAmount;
	private String monthlyFee;
	private int balance;
	private String nextPaymentDay;
	private int    payCount;
	private int    totalPayCount;
	private String merchantUid;
	private String impUid;
	private String receiptUrl;


	private String cancelFee;
	private String cancelAmount;
	private String cancelReason;
	private String pdIdx;   
	private String firstDriverName; 
	private String firstDriverContact;
	private String firstDriverBirthday;
	private String ulIdx1;
	private String ulIdx2;
	private String reserveMEtc;       
	private String regId;    
	private String modId;
	
	private String regDt;
	private String modDt;
	private String delYn;

	private String optionDetailCode;

	//예약테이블
	private String reIdx;
	private String quIdx;
	private String reserveChannel;

	// 면허 관련
	private String firstDriverLicenseCode;
	private String firstDriverLicenseExpirationDate;
	private String firstDriverLicenseIsDate;
	private String firstDriverLicenseNumber;
	private String firstDriverLicenseDate;

	private String secondDriverName;
	private String secondDriverContact;
	private String secondDriverBirthday;
	private String secondDriverLicenseCode;
	private String secondDriverLicenseNumber;
	private String secondDriverExpirationDate;
	private String secondDriverLicenseDate;


	
	
}