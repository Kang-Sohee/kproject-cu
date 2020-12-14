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
@Alias("DochaCarSearchPaymentDetailDto")
public class DochaCarSearchPaymentDetailDto {

	//DC_CAR_INFO
	private String crIdx                ;
	private String mdIdx                ;
	private String rtIdx                ;
	private String modelName            ;
	private String modelDetailName      ;
	private String fuelCode             ;
	private String fuelName             ;
	private String transmissionCode     ;
	private String drive_typeCode       ;
	private String cartypeCode          ;
	private String driveLicenseCode     ;
	private String manufacturerCode     ;
	private String colorName            ;
	private String displacement         ;
	private String year                 ;
	private String mileage              ;
	private String maximumPassenger     ;
	private String pyIdx                ;
	private String dailyStandardPay     ;
	private String monthlyStandardPay   ;
	private String longTermDeposit      ;
	private String carStatusCode        ;
	private String reserveAbleYn        ;
	private String carRegDt             ;
	private String carNumber            ;
	private String carChassisNumber     ;
	private String imgIdx               ;
	private String carDriveLimit        ;
	private String ageLimit             ;
	private String garageAddr           ;
	private String car_etc              ;
	//private String regId                ;
	//private String regDt                ;
	//private String modId                ;
	//private String modDt                ;
	//private String delYn                ;
	private String dailyMaxRate        ;
	private String monthlyMaxRate      ;
	private String month3Deposit        ;
	private String month6Deposit        ;
	private String month9Deposit        ;
	private String month12Deposit       ;
	private String deliveryStandardPay  ;
	private String deliveryAddPay       ;
	private String delivery_maxRate     ;
	private String dailyYn              ;
	private String monthlyYn            ;
	private String closedvehicleYn      ;
	private String ciIdx                ;
	private String perIdx               ;
	private String optionDetailCode;
	//private String onselfDamageCover    ;
	//private String personalCover        ;
	//private String propertyDamageCover  ;
	//private String insuranceCopayment   ;
	//private String carDamageCover       ;
	//private String insuranceCopayment2  ;
	//private String carDamageCover2      ;
	//private String insuranceCopayment3  ;
	//private String carDamageCover3      ;
	//private String insuranceCopayment4  ;
	//private String carDamageCover4      ;
	//private String carDamage1Yn         ;
	//private String carDamage2Yn         ;
	//private String carDamage3Yn         ;
	//private String carDamage4Yn         ;
	
	//DC_CAR_INFO_INSURANCD
	
	//private String ciIdx                 ;
	//private String crIdx                 ;
	private String insuranceFee          ;
	private String insuranceFee2          ;
	private String insuranceFee3          ;
	private String insuranceFee4          ;

	private String carDamageCover        ;
	private String onselfDamageCover     ;
	private String personalCover         ;
	private String propertyDamageCover   ;
	private String insuranceCopayment    ;
	//private String ageLimit              ;
	private String driveCareerLimit      ;
	//private String regId                 ;
	//private String regDt                 ;
	//private String modId                 ;
	//private String modDt                 ;
	//private String delYn                 ;
	//private String rtIdx                 ;
	private String carDamageCover2       ;
	private String insuranceCopayment2   ;
	private String carDamageCover3       ;
	private String insuranceCopayment3   ;
	private String carDamageCover4       ;
	private String insuranceCopayment4   ;
	private String carDamage1Yn          ;
	private String carDamage2Yn          ;
	private String carDamage3Yn          ;
	private String carDamage4Yn          ;
	private String ciEtc                 ;
	
	//DC_RENT_COMPANY
	private String companyName           ;
	private String companyContact1       ;
	private String companyZipcode        ;
	private String companyAddress        ;
	private String companyAddressDetail        ;
	private String branchName        ;

	
	private String rentFee				 ; //대여요금
	private String disRentFee		     ; //할인 후 대여요금
	
	private String mmRentFee			 ; //월결제금액
	private String mmLastRentFee		 ; //장기마지막월결제금액



}


