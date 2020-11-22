package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

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
	public String getDriveTypeCode() {
		return driveTypeCode;
	}
	public void setDriveTypeCode(String driveTypeCode) {
		this.driveTypeCode = driveTypeCode;
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
	public String getShorttermFee() {
		return shorttermFee;
	}
	public void setShorttermFee(String shorttermFee) {
		this.shorttermFee = shorttermFee;
	}
	public String getLongtermFee() {
		return longtermFee;
	}
	public void setLongtermFee(String longtermFee) {
		this.longtermFee = longtermFee;
	}
	public String getLongtermDeposit() {
		return longtermDeposit;
	}
	public void setLongtermDeposit(String longtermDeposit) {
		this.longtermDeposit = longtermDeposit;
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
	public String getModDt() {
		return modDt;
	}
	public void setModDt(String modDt) {
		this.modDt = modDt;
	}
	public String getDelYn() {
		return delYn;
	}
	public void setDelYn(String delYn) {
		this.delYn = delYn;
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
	public String getDailyMaxRate() {
		return dailyMaxRate;
	}
	public void setDailyMaxRate(String dailyMaxRate) {
		this.dailyMaxRate = dailyMaxRate;
	}
	public String getMonthlyMaxRate() {
		return monthlyMaxRate;
	}
	public void setMonthlyMaxRate(String monthlyMaxRate) {
		this.monthlyMaxRate = monthlyMaxRate;
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
	public String getDeliveryMaxRate() {
		return deliveryMaxRate;
	}
	public void setDeliveryMaxRate(String deliveryMaxRate) {
		this.deliveryMaxRate = deliveryMaxRate;
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
	public String getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}
	public String getTotalRowCount() {
		return totalRowCount;
	}
	public void setTotalRowCount(String totalRowCount) {
		this.totalRowCount = totalRowCount;
	}
	public String getFuelName() {
		return fuelName;
	}
	public void setFuelName(String fuelName) {
		this.fuelName = fuelName;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getOptionDetailValue() {
		return optionDetailValue;
	}
	public void setOptionDetailValue(String optionDetailValue) {
		this.optionDetailValue = optionDetailValue;
	}
	public String getOptionDetailCode() {
		return optionDetailCode;
	}
	public void setOptionDetailCode(String optionDetailCode) {
		this.optionDetailCode = optionDetailCode;
	}
	public String getCalcRentFee() {
		return calcRentFee;
	}
	public void setCalcRentFee(String calcRentFee) {
		this.calcRentFee = calcRentFee;
	}
	public String getCalcDisRentFee() {
		return calcDisRentFee;
	}
	public void setCalcDisRentFee(String calcDisRentFee) {
		this.calcDisRentFee = calcDisRentFee;
	}
	public String getInsuranceFee() {
		return insuranceFee;
	}
	public void setInsuranceFee(String insuranceFee) {
		this.insuranceFee = insuranceFee;
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
	public String getCarImgList() {
		return carImgList;
	}
	public void setCarImgList(String carImgList) {
		this.carImgList = carImgList;
	}

}
