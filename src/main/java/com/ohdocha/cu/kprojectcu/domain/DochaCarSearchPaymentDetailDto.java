package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

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
	private String daily_maxRate        ;
	private String monthly_maxRate      ;
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
	private String branchName        ;

	
	private String rentFee				 ; //대여요금
	private String disRentFee		     ; //할인 후 대여요금
	
	private String mmRentFee			 ; //월결제금액
	private String mmLastRentFee		 ; //장기마지막월결제금액
	
	public String getCrIdx() {
		return crIdx;
	}
	public void setCrIdx(String crIdx) {
		this.crIdx = crIdx;
	}
	public String getMdIdx() {
		return mdIdx;
	}
	public void setMdIdx(String mdIdx) {
		this.mdIdx = mdIdx;
	}
	public String getRtIdx() {
		return rtIdx;
	}
	public void setRtIdx(String rtIdx) {
		this.rtIdx = rtIdx;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getModelDetailName() {
		return modelDetailName;
	}
	public void setModelDetailName(String modelDetailName) {
		this.modelDetailName = modelDetailName;
	}
	public String getFuelCode() {
		return fuelCode;
	}
	public void setFuelCode(String fuelCode) {
		this.fuelCode = fuelCode;
	}
	public String getTransmissionCode() {
		return transmissionCode;
	}
	public void setTransmissionCode(String transmissionCode) {
		this.transmissionCode = transmissionCode;
	}
	public String getDrive_typeCode() {
		return drive_typeCode;
	}
	public void setDrive_typeCode(String drive_typeCode) {
		this.drive_typeCode = drive_typeCode;
	}
	public String getCartypeCode() {
		return cartypeCode;
	}
	public void setCartypeCode(String cartypeCode) {
		this.cartypeCode = cartypeCode;
	}
	public String getDriveLicenseCode() {
		return driveLicenseCode;
	}
	public void setDriveLicenseCode(String driveLicenseCode) {
		this.driveLicenseCode = driveLicenseCode;
	}
	public String getManufacturerCode() {
		return manufacturerCode;
	}
	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getDisplacement() {
		return displacement;
	}
	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getMaximumPassenger() {
		return maximumPassenger;
	}
	public void setMaximumPassenger(String maximumPassenger) {
		this.maximumPassenger = maximumPassenger;
	}
	public String getPyIdx() {
		return pyIdx;
	}
	public void setPyIdx(String pyIdx) {
		this.pyIdx = pyIdx;
	}
	public String getDailyStandardPay() {
		return dailyStandardPay;
	}
	public void setDailyStandardPay(String dailyStandardPay) {
		this.dailyStandardPay = dailyStandardPay;
	}
	public String getMonthlyStandardPay() {
		return monthlyStandardPay;
	}
	public void setMonthlyStandardPay(String monthlyStandardPay) {
		this.monthlyStandardPay = monthlyStandardPay;
	}
	public String getCarStatusCode() {
		return carStatusCode;
	}
	public void setCarStatusCode(String carStatusCode) {
		this.carStatusCode = carStatusCode;
	}
	public String getReserveAbleYn() {
		return reserveAbleYn;
	}
	public void setReserveAbleYn(String reserveAbleYn) {
		this.reserveAbleYn = reserveAbleYn;
	}
	public String getCarRegDt() {
		return carRegDt;
	}
	public void setCarRegDt(String carRegDt) {
		this.carRegDt = carRegDt;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public String getCarChassisNumber() {
		return carChassisNumber;
	}
	public void setCarChassisNumber(String carChassisNumber) {
		this.carChassisNumber = carChassisNumber;
	}
	public String getImgIdx() {
		return imgIdx;
	}
	public void setImgIdx(String imgIdx) {
		this.imgIdx = imgIdx;
	}
	public String getCarDriveLimit() {
		return carDriveLimit;
	}
	public void setCarDriveLimit(String carDriveLimit) {
		this.carDriveLimit = carDriveLimit;
	}
	public String getGarageAddr() {
		return garageAddr;
	}
	public void setGarageAddr(String garageAddr) {
		this.garageAddr = garageAddr;
	}
	public String getCar_etc() {
		return car_etc;
	}
	public void setCar_etc(String car_etc) {
		this.car_etc = car_etc;
	}
	public String getDaily_maxRate() {
		return daily_maxRate;
	}
	public void setDaily_maxRate(String daily_maxRate) {
		this.daily_maxRate = daily_maxRate;
	}
	public String getMonthly_maxRate() {
		return monthly_maxRate;
	}
	public void setMonthly_maxRate(String monthly_maxRate) {
		this.monthly_maxRate = monthly_maxRate;
	}
	public String getMonth3Deposit() {
		return month3Deposit;
	}
	public void setMonth3Deposit(String month3Deposit) {
		this.month3Deposit = month3Deposit;
	}
	public String getMonth6Deposit() {
		return month6Deposit;
	}
	public void setMonth6Deposit(String month6Deposit) {
		this.month6Deposit = month6Deposit;
	}
	public String getMonth9Deposit() {
		return month9Deposit;
	}
	public void setMonth9Deposit(String month9Deposit) {
		this.month9Deposit = month9Deposit;
	}
	public String getMonth12Deposit() {
		return month12Deposit;
	}
	public void setMonth12Deposit(String month12Deposit) {
		this.month12Deposit = month12Deposit;
	}
	public String getDeliveryStandardPay() {
		return deliveryStandardPay;
	}
	public void setDeliveryStandardPay(String deliveryStandardPay) {
		this.deliveryStandardPay = deliveryStandardPay;
	}
	public String getDeliveryAddPay() {
		return deliveryAddPay;
	}
	public void setDeliveryAddPay(String deliveryAddPay) {
		this.deliveryAddPay = deliveryAddPay;
	}
	public String getDelivery_maxRate() {
		return delivery_maxRate;
	}
	public void setDelivery_maxRate(String delivery_maxRate) {
		this.delivery_maxRate = delivery_maxRate;
	}
	public String getDailyYn() {
		return dailyYn;
	}
	public void setDailyYn(String dailyYn) {
		this.dailyYn = dailyYn;
	}
	public String getMonthlyYn() {
		return monthlyYn;
	}
	public void setMonthlyYn(String monthlyYn) {
		this.monthlyYn = monthlyYn;
	}
	public String getClosedvehicleYn() {
		return closedvehicleYn;
	}
	public void setClosedvehicleYn(String closedvehicleYn) {
		this.closedvehicleYn = closedvehicleYn;
	}
	public String getCiIdx() {
		return ciIdx;
	}
	public void setCiIdx(String ciIdx) {
		this.ciIdx = ciIdx;
	}
	public String getPerIdx() {
		return perIdx;
	}
	public void setPerIdx(String perIdx) {
		this.perIdx = perIdx;
	}
	public String getInsuranceFee() {
		return insuranceFee;
	}
	public void setInsuranceFee(String insuranceFee) {
		this.insuranceFee = insuranceFee;
	}
	public String getCarDamageCover() {
		return carDamageCover;
	}
	public void setCarDamageCover(String carDamageCover) {
		this.carDamageCover = carDamageCover;
	}
	public String getOnselfDamageCover() {
		return onselfDamageCover;
	}
	public void setOnselfDamageCover(String onselfDamageCover) {
		this.onselfDamageCover = onselfDamageCover;
	}
	public String getPersonalCover() {
		return personalCover;
	}
	public void setPersonalCover(String personalCover) {
		this.personalCover = personalCover;
	}
	public String getPropertyDamageCover() {
		return propertyDamageCover;
	}
	public void setPropertyDamageCover(String propertyDamageCover) {
		this.propertyDamageCover = propertyDamageCover;
	}
	public String getInsuranceCopayment() {
		return insuranceCopayment;
	}
	public void setInsuranceCopayment(String insuranceCopayment) {
		this.insuranceCopayment = insuranceCopayment;
	}
	public String getDriveCareerLimit() {
		return driveCareerLimit;
	}
	public void setDriveCareerLimit(String driveCareerLimit) {
		this.driveCareerLimit = driveCareerLimit;
	}
	public String getCarDamageCover2() {
		return carDamageCover2;
	}
	public void setCarDamageCover2(String carDamageCover2) {
		this.carDamageCover2 = carDamageCover2;
	}
	public String getInsuranceCopayment2() {
		return insuranceCopayment2;
	}
	public void setInsuranceCopayment2(String insuranceCopayment2) {
		this.insuranceCopayment2 = insuranceCopayment2;
	}
	public String getCarDamageCover3() {
		return carDamageCover3;
	}
	public void setCarDamageCover3(String carDamageCover3) {
		this.carDamageCover3 = carDamageCover3;
	}
	public String getInsuranceCopayment3() {
		return insuranceCopayment3;
	}
	public void setInsuranceCopayment3(String insuranceCopayment3) {
		this.insuranceCopayment3 = insuranceCopayment3;
	}
	public String getCarDamageCover4() {
		return carDamageCover4;
	}
	public void setCarDamageCover4(String carDamageCover4) {
		this.carDamageCover4 = carDamageCover4;
	}
	public String getInsuranceCopayment4() {
		return insuranceCopayment4;
	}
	public void setInsuranceCopayment4(String insuranceCopayment4) {
		this.insuranceCopayment4 = insuranceCopayment4;
	}
	public String getCarDamage1Yn() {
		return carDamage1Yn;
	}
	public void setCarDamage1Yn(String carDamage1Yn) {
		this.carDamage1Yn = carDamage1Yn;
	}
	public String getCarDamage2Yn() {
		return carDamage2Yn;
	}
	public void setCarDamage2Yn(String carDamage2Yn) {
		this.carDamage2Yn = carDamage2Yn;
	}
	public String getCarDamage3Yn() {
		return carDamage3Yn;
	}
	public void setCarDamage3Yn(String carDamage3Yn) {
		this.carDamage3Yn = carDamage3Yn;
	}
	public String getCarDamage4Yn() {
		return carDamage4Yn;
	}
	public void setCarDamage4Yn(String carDamage4Yn) {
		this.carDamage4Yn = carDamage4Yn;
	}
	public String getCiEtc() {
		return ciEtc;
	}
	public void setCiEtc(String ciEtc) {
		this.ciEtc = ciEtc;
	}
	public String getFuelName() {
		return fuelName;
	}
	public void setFuelName(String fuelName) {
		this.fuelName = fuelName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyContact1() {
		return companyContact1;
	}
	public void setCompanyContact1(String companyContact1) {
		this.companyContact1 = companyContact1;
	}
	public String getCompanyZipcode() {
		return companyZipcode;
	}
	public void setCompanyZipcode(String companyZipcode) {
		this.companyZipcode = companyZipcode;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getRentFee() {
		return rentFee;
	}
	public void setRentFee(String rentFee) {
		this.rentFee = rentFee;
	}
	public String getDisRentFee() {
		return disRentFee;
	}
	public void setDisRentFee(String disRentFee) {
		this.disRentFee = disRentFee;
	}
	public String getMmRentFee() {
		return mmRentFee;
	}
	public void setMmRentFee(String mmRentFee) {
		this.mmRentFee = mmRentFee;
	}
	public String getMmLastRentFee() {
		return mmLastRentFee;
	}
	public void setMmLastRentFee(String mmLastRentFee) {
		this.mmLastRentFee = mmLastRentFee;
	}
	public String getInsuranceFee2() {
		return insuranceFee2;
	}
	public void setInsuranceFee2(String insuranceFee2) {
		this.insuranceFee2 = insuranceFee2;
	}
	public String getInsuranceFee3() {
		return insuranceFee3;
	}
	public void setInsuranceFee3(String insuranceFee3) {
		this.insuranceFee3 = insuranceFee3;
	}
	public String getInsuranceFee4() {
		return insuranceFee4;
	}
	public void setInsuranceFee4(String insuranceFee4) {
		this.insuranceFee4 = insuranceFee4;
	}
	public String getLongTermDeposit() {
		return longTermDeposit;
	}
	public void setLongTermDeposit(String longTermDeposit) {
		this.longTermDeposit = longTermDeposit;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}


}


