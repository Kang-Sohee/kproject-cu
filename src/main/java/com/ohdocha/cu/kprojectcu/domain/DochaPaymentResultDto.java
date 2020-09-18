package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

@Alias("DochaPaymentResultDto")
public class DochaPaymentResultDto {
	private String quIdx;
	private String qrIdx;
	private String rmIdx;
	private String quoteCode;
	private String quoteStatus;
	private String carTypeCodeList;
	private String crIdx;
	private String quLat;
	private String quLng;
	private String cartypeCode;
	private String rentStartDay;
	private String rentStartTime;
	private String rentEndDay;
	private String rentEndTime;
	private String deliveryTypeCode;
	private String deliveryAddr;
	private String deliveryLat;
	private String deliveryLng;
	private String returnTypeCode;
	private String returnAddr;
	private String returnLng;
	private String returnLat;
	private String rtIdx;
	private String companyName;
	private String rentFee;
	private String paymentAmount;
	private String firstDriverName;
	private String urIdx1;
	private String secondDriverName;
	private String urIdx2;
	private String urIdx;
	private String userName;
	private String regId;
	private String regDt;
	private String modId;
	private String delYn;
	
	private String userBirthday;
	private String userGender;
	private String userContact1;
	private String userContact2;

	private String insuranceRate;			  
	private String mdIdx;             
	private String modelName;         
	private String modelDetailName; 
	private String fuelCode; 
	private String fuelName;
	private String driveLicenseCode; 
	private String manufacturerCode; 
	private String manufacturerName;
	private String colorCode;   
	private String colorName;
	private String year;               
	private String maximumPassenger;  
	private String reserveStatusCode;
	private String reserveAbleYn;    
	private String carNumber;
	
	
	private String insuranceFee;         
	private String carDamageCover;      
	private String onselfDamageCover;   
	private String personalCover;   
	               
	private String propertyDamageCover; 
	private String driveCareerLimit;    
	private String insuranceCopayment; 
	
	private String approvalNumber;
	private String userId;
	private String longtermYn;
	private String mileage;
	private String pyIdx;
	private String shortTermFee;
	private String longTermDeposit;
	private String carStatusCode;
	private String carRegDt;
	private String carChassisNumber;
	private String imgIdx;
	private String carDriveLimit;
	private String ageLimit;
	private String garageAddr;
	private String carEtc;
	
	private String companyAddress;
	private String companyContact1;
	private String companyContact2;
	

	private String qrtCount;
	private String monthlyFee;
	private String dailyFee;

	private String reserveDate;
	
	private String driverLicenseName;
	private String companyRegDt;

	private String carDeposit; // DC_QUOTE_RENT_COMPANY 의 보증금
	
	public String getQuIdx() {
		return quIdx;
	}
	public void setQuIdx(String quIdx) {
		this.quIdx = quIdx;
	}
	public String getQrIdx() {
		return qrIdx;
	}
	public void setQrIdx(String qrIdx) {
		this.qrIdx = qrIdx;
	}
	public String getQuoteCode() {
		return quoteCode;
	}
	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}
	public String getQuoteStatus() {
		return quoteStatus;
	}
	public void setQuoteStatus(String quoteStatus) {
		this.quoteStatus = quoteStatus;
	}

	public String getCrIdx() {
		return crIdx;
	}
	public void setCrIdx(String crIdx) {
		this.crIdx = crIdx;
	}
	public String getQuLat() {
		return quLat;
	}
	public void setQuLat(String quLat) {
		this.quLat = quLat;
	}
	public String getQuLng() {
		return quLng;
	}
	public void setQuLng(String quLng) {
		this.quLng = quLng;
	}
	public String getCartypeCode() {
		return cartypeCode;
	}
	public void setCartypeCode(String cartypeCode) {
		this.cartypeCode = cartypeCode;
	}
	public String getRentStartDay() {
		return rentStartDay;
	}
	public void setRentStartDay(String rentStartDay) {
		this.rentStartDay = rentStartDay;
	}
	public String getRentStartTime() {
		return rentStartTime;
	}
	public void setRentStartTime(String rentStartTime) {
		this.rentStartTime = rentStartTime;
	}
	public String getRentEndDay() {
		return rentEndDay;
	}
	public void setRentEndDay(String rentEndDay) {
		this.rentEndDay = rentEndDay;
	}
	public String getRentEndTime() {
		return rentEndTime;
	}
	public void setRentEndTime(String rentEndTime) {
		this.rentEndTime = rentEndTime;
	}
	public String getDeliveryTypeCode() {
		return deliveryTypeCode;
	}
	public void setDeliveryTypeCode(String deliveryTypeCode) {
		this.deliveryTypeCode = deliveryTypeCode;
	}
	public String getDeliveryAddr() {
		return deliveryAddr;
	}
	public void setDeliveryAddr(String deliveryAddr) {
		this.deliveryAddr = deliveryAddr;
	}
	public String getDeliveryLat() {
		return deliveryLat;
	}
	public void setDeliveryLat(String deliveryLat) {
		this.deliveryLat = deliveryLat;
	}
	public String getDeliveryLng() {
		return deliveryLng;
	}
	public void setDeliveryLng(String deliveryLng) {
		this.deliveryLng = deliveryLng;
	}
	public String getReturnTypeCode() {
		return returnTypeCode;
	}
	public void setReturnTypeCode(String returnTypeCode) {
		this.returnTypeCode = returnTypeCode;
	}
	public String getReturnAddr() {
		return returnAddr;
	}
	public void setReturnAddr(String returnAddr) {
		this.returnAddr = returnAddr;
	}
	public String getReturnLng() {
		return returnLng;
	}
	public void setReturnLng(String returnLng) {
		this.returnLng = returnLng;
	}
	public String getReturnLat() {
		return returnLat;
	}
	public void setReturnLat(String returnLat) {
		this.returnLat = returnLat;
	}
	public String getRtIdx() {
		return rtIdx;
	}
	public void setRtIdx(String rtIdx) {
		this.rtIdx = rtIdx;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getRentFee() {
		return rentFee;
	}
	public void setRentFee(String rentFee) {
		this.rentFee = rentFee;
	}
	public String getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getFirstDriverName() {
		return firstDriverName;
	}
	public void setFirstDriverName(String firstDriverName) {
		this.firstDriverName = firstDriverName;
	}
	public String getUrIdx1() {
		return urIdx1;
	}
	public void setUrIdx1(String urIdx1) {
		this.urIdx1 = urIdx1;
	}
	public String getSecondDriverName() {
		return secondDriverName;
	}
	public void setSecondDriverName(String secondDriverName) {
		this.secondDriverName = secondDriverName;
	}
	public String getUrIdx2() {
		return urIdx2;
	}
	public void setUrIdx2(String urIdx2) {
		this.urIdx2 = urIdx2;
	}
	public String getUrIdx() {
		return urIdx;
	}
	public void setUrIdx(String urIdx) {
		this.urIdx = urIdx;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getModId() {
		return modId;
	}
	public void setModId(String modId) {
		this.modId = modId;
	}
	public String getDelYn() {
		return delYn;
	}
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}
	public String getUserBirthday() {
		return userBirthday;
	}
	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}
	public String getUserGender() {
		return userGender;
	}
	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}
	public String getUserContact1() {
		return userContact1;
	}
	public void setUserContact1(String userContact1) {
		this.userContact1 = userContact1;
	}
	public String getUserContact2() {
		return userContact2;
	}
	public void setUserContact2(String userContact2) {
		this.userContact2 = userContact2;
	}
	public String getInsuranceRate() {
		return insuranceRate;
	}
	public void setInsuranceRate(String insuranceRate) {
		this.insuranceRate = insuranceRate;
	}
	public String getMdIdx() {
		return mdIdx;
	}
	public void setMdIdx(String mdIdx) {
		this.mdIdx = mdIdx;
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
	public String getFuelName() {
		return fuelName;
	}
	public void setFuelName(String fuelName) {
		this.fuelName = fuelName;
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
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMaximumPassenger() {
		return maximumPassenger;
	}
	public void setMaximumPassenger(String maximumPassenger) {
		this.maximumPassenger = maximumPassenger;
	}
	public String getReserveStatusCode() {
		return reserveStatusCode;
	}
	public void setReserveStatusCode(String reserveStatusCode) {
		this.reserveStatusCode = reserveStatusCode;
	}
	public String getReserveAbleYn() {
		return reserveAbleYn;
	}
	public void setReserveAbleYn(String reserveAbleYn) {
		this.reserveAbleYn = reserveAbleYn;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
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
	public String getDriveCareerLimit() {
		return driveCareerLimit;
	}
	public void setDriveCareerLimit(String driveCareerLimit) {
		this.driveCareerLimit = driveCareerLimit;
	}
	public String getInsuranceCopayment() {
		return insuranceCopayment;
	}
	public void setInsuranceCopayment(String insuranceCopayment) {
		this.insuranceCopayment = insuranceCopayment;
	}
	public String getApprovalNumber() {
		return approvalNumber;
	}
	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLongtermYn() {
		return longtermYn;
	}
	public void setLongtermYn(String longtermYn) {
		this.longtermYn = longtermYn;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getPyIdx() {
		return pyIdx;
	}
	public void setPyIdx(String pyIdx) {
		this.pyIdx = pyIdx;
	}
	public String getShortTermFee() {
		return shortTermFee;
	}
	public void setShortTermFee(String shortTermFee) {
		this.shortTermFee = shortTermFee;
	}
	public String getLongTermDeposit() {
		return longTermDeposit;
	}
	public void setLongTermDeposit(String longTermDeposit) {
		this.longTermDeposit = longTermDeposit;
	}
	public String getCarStatusCode() {
		return carStatusCode;
	}
	public void setCarStatusCode(String carStatusCode) {
		this.carStatusCode = carStatusCode;
	}
	public String getCarRegDt() {
		return carRegDt;
	}
	public void setCarRegDt(String carRegDt) {
		this.carRegDt = carRegDt;
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
	public String getAgeLimit() {
		return ageLimit;
	}
	public void setAgeLimit(String ageLimit) {
		this.ageLimit = ageLimit;
	}
	public String getGarageAddr() {
		return garageAddr;
	}
	public void setGarageAddr(String garageAddr) {
		this.garageAddr = garageAddr;
	}
	public String getCarEtc() {
		return carEtc;
	}
	public void setCarEtc(String carEtc) {
		this.carEtc = carEtc;
	}
	public String getCarTypeCodeList() {
		return carTypeCodeList;
	}
	public void setCarTypeCodeList(String carTypeCodeList) {
		this.carTypeCodeList = carTypeCodeList;
	}
	public String getCompanyContact1() {
		return companyContact1;
	}
	public void setCompanyContact1(String companyContact1) {
		this.companyContact1 = companyContact1;
	}
	public String getCompanyContact2() {
		return companyContact2;
	}
	public void setCompanyContact2(String companyContact2) {
		this.companyContact2 = companyContact2;
	}

	public String getQrtCount() {
		return qrtCount;
	}
	public void setQrtCount(String qrtCount) {
		this.qrtCount = qrtCount;
	}
	public String getDriverLicenseName() {
		return driverLicenseName;
	}
	public void setDriverLicenseName(String driverLicenseName) {
		this.driverLicenseName = driverLicenseName;
	}
	public String getReserveDate() {
		return reserveDate;
	}
	public void setReserveDate(String reserveDate) {
		this.reserveDate = reserveDate;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;

	}
	public String getCarDeposit() {
		return carDeposit;
	}
	public void setCarDeposit(String carDeposit) {
		this.carDeposit = carDeposit;
	}
	public String getRmIdx() {
		return rmIdx;
	}
	public void setRmIdx(String rmIdx) {
		this.rmIdx = rmIdx;
	}
	public String getMonthlyFee() {
		return monthlyFee;
	}
	public void setMonthlyFee(String monthlyFee) {
		this.monthlyFee = monthlyFee;
	}
	public String getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(String dailyFee) {
		this.dailyFee = dailyFee;
	}
	public String getCompanyRegDt() {
		return companyRegDt;
	}
	public void setCompanyRegDt(String companyRegDt) {
		this.companyRegDt = companyRegDt;
	}


}
