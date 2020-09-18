package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

import java.util.ArrayList;


@Alias("DochaQuoteUserDto")
public class DochaQuoteUserDto {

	private String quIdx;            //고객견적idx 
	private String qrIdx;            //회원사견적idx
	private String quoteCode;        //견적구분
	private String quoteStatus;      //견적상태
	private String longTermYn;		 //장단기구분
	private String carTypeCodeList;  //견적요청차량분류code
	private String crIdx;            //차량idx
	private String carTypeCode;      //차량code
	private String rentStartDay;     //대여시작일자
	private String rentEndDay;       //대여종료일자
	private String rentStartTime;    //대여시작시
	private String rentEndTime;      //대여종료시
	private String deliveryTypeCode; //배차방법CODE
	private String deliveryTypeValue;//배차방법
	private String deliveryAddr;     //배차주소
	private String returnTypeCode;   //반차방법CODE
	private String returnTypeValue;  //반차방법
	private String returnAddr;       //반차주소
	private String rtIdx;            //제휴사idx
	private String companyName;      //제휴사명
	private String rentFee;          //대여요금
	private String insuranceRate;    //보험요금
	private String paymentAmount;    //결제금액
	private String firstDriverName;  //제1운전자
	private String ulIdx1;           //제1운전자idx
	private String secondDriverName; //제2운전자
	private String ulIdx2;          //제2운전자idx
	private String urIdx;            //회원idx
	private String userName;         //요청자명
	private String regId;            //등록자
	private String regDt;            //등록일시
	private String modId;            //수정자
	private String modDt;            //수정일시
	private String delYn;            //삭제여부
	
	private String userBirthday;
	private String userGender;
	
	
	
	private String rentStartDow;	//대여시작요일
	private String rentEndDow;		//대여종료요일
	private String rentStart12Time; //대여시작시간 12시표기
	private String rentEnd12Time;	//대여종료시간 12시표기
	
	private String approvalNumber; //승인번호
	
	private int totCnt;				

	private String branchName;		//지점명
	private String companyContact1;	//회원사 연락처
	

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
	public String getLongTermYn() {
		return longTermYn;
	}
	public void setLongTermYn(String longTermYn) {
		this.longTermYn = longTermYn;
	}
	public String getCarTypeCodeList() {
		return carTypeCodeList;
	}
	public void setCarTypeCodeList(String carTypeCodeList) {
		this.carTypeCodeList = carTypeCodeList;
	}
	public String getCrIdx() {
		return crIdx;
	}
	public void setCrIdx(String crIdx) {
		this.crIdx = crIdx;
	}
	public String getCarTypeCode() {
		return carTypeCode;
	}
	public void setCarTypeCode(String carTypeCode) {
		this.carTypeCode = carTypeCode;
	}
	public String getRentStartDay() {
		return rentStartDay;
	}
	public void setRentStartDay(String rentStartDay) {
		this.rentStartDay = rentStartDay;
	}
	public String getRentEndDay() {
		return rentEndDay;
	}
	public void setRentEndDay(String rentEndDay) {
		this.rentEndDay = rentEndDay;
	}
	public String getRentStartTime() {
		return rentStartTime;
	}
	public void setRentStartTime(String rentStartTime) {
		this.rentStartTime = rentStartTime;
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
	public String getDeliveryTypeValue() {
		return deliveryTypeValue;
	}
	public void setDeliveryTypeValue(String deliveryTypeValue) {
		this.deliveryTypeValue = deliveryTypeValue;
	}
	public String getDeliveryAddr() {
		return deliveryAddr;
	}
	public void setDeliveryAddr(String deliveryAddr) {
		this.deliveryAddr = deliveryAddr;
	}
	public String getReturnTypeCode() {
		return returnTypeCode;
	}
	public void setReturnTypeCode(String returnTypeCode) {
		this.returnTypeCode = returnTypeCode;
	}
	public String getReturnTypeValue() {
		return returnTypeValue;
	}
	public void setReturnTypeValue(String returnTypeValue) {
		this.returnTypeValue = returnTypeValue;
	}
	public String getReturnAddr() {
		return returnAddr;
	}
	public void setReturnAddr(String returnAddr) {
		this.returnAddr = returnAddr;
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
	public String getInsuranceRate() {
		return insuranceRate;
	}
	public void setInsuranceRate(String insuranceRate) {
		this.insuranceRate = insuranceRate;
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
	public String getUlIdx1() {
		return ulIdx1;
	}
	public void setUlIdx1(String ulIdx1) {
		this.ulIdx1 = ulIdx1;
	}
	public String getSecondDriverName() {
		return secondDriverName;
	}
	public void setSecondDriverName(String secondDriverName) {
		this.secondDriverName = secondDriverName;
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
	public String getRentStartDow() {
		return rentStartDow;
	}
	public void setRentStartDow(String rentStartDow) {
		this.rentStartDow = rentStartDow;
	}
	public String getRentEndDow() {
		return rentEndDow;
	}
	public void setRentEndDow(String rentEndDow) {
		this.rentEndDow = rentEndDow;
	}
	public String getRentStart12Time() {
		return rentStart12Time;
	}
	public void setRentStart12Time(String rentStart12Time) {
		this.rentStart12Time = rentStart12Time;
	}
	public String getRentEnd12Time() {
		return rentEnd12Time;
	}
	public void setRentEnd12Time(String rentEnd12Time) {
		this.rentEnd12Time = rentEnd12Time;
	}
	public int getTotCnt() {
		return totCnt;
	}
	public void setTotCnt(int totCnt) {
		this.totCnt = totCnt;
	}
	
	//차량모델
	private int    mlIdx            ;   /* 모델idx     */
	private int    mlPIdx           ;   /* 모델부모idx  */
	private String modelName        ;   /* 모델명      	  */
	private String pModelName        ;   /* 모델명      	  */
	private String manufacturerCode ;   /* 제조사code   */
	private String conturyCode      ;   /* 제조국가code */
	private String importCode       ;   /* 국내/해외구분  */
	private String manufacturerValue ;   /* 제조사code   */
	private String conturyValue      ;   /* 제조국가code */
	private String importValue       ;   /* 국내/해외구분  */
	private String carTypeValue      ;   /* 차종code    */
	
	private String rootYn;
	
	
	public int getMlIdx() {
		return mlIdx;
	}
	public void setMlIdx(int mlIdx) {
		this.mlIdx = mlIdx;
	}
	public int getMlPIdx() {
		return mlPIdx;
	}
	public void setMlPIdx(int mlPIdx) {
		this.mlPIdx = mlPIdx;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}	
	public String getpModelName() {
		return pModelName;
	}
	public void setpModelName(String pModelName) {
		this.pModelName = pModelName;
	}
	public String getManufacturerCode() {
		return manufacturerCode;
	}
	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}
	public String getConturyCode() {
		return conturyCode;
	}
	public void setConturyCode(String conturyCode) {
		this.conturyCode = conturyCode;
	}
	public String getImportCode() {
		return importCode;
	}
	public void setImportCode(String importCode) {
		this.importCode = importCode;
	}
	
	public String getManufacturerValue() {
		return manufacturerValue;
	}
	public void setManufacturerValue(String manufacturerValue) {
		this.manufacturerValue = manufacturerValue;
	}
	public String getConturyValue() {
		return conturyValue;
	}
	public void setConturyValue(String conturyValue) {
		this.conturyValue = conturyValue;
	}
	public String getImportValue() {
		return importValue;
	}
	public void setImportValue(String importValue) {
		this.importValue = importValue;
	}
	public String getCarTypeValue() {
		return carTypeValue;
	}
	public void setCarTypeValue(String carTypeValue) {
		this.carTypeValue = carTypeValue;
	}
	
	
	public String getRootYn() {
		return rootYn;
	}
	public void setRootYn(String rootYn) {
		this.rootYn = rootYn;
	}

	//차량모델상세
	private int    mdIdx            ;   /* 모델idx     	*/
	private String modelDetailName  ;   /* 모델상세명      	*/
	private String fuelCode 		;   /* 연료구분code   	*/
	private String transmissionCode ;   /* 변속기구분code 	*/
	private String driveTypeCode    ;   /* 구동방식code  	*/
	private String driveLicenseCode ;   /* 운전면허코드    	*/
	private String fuelValue 		;   /* 연료구분code   	*/
	private String transmissionValue ;   /* 변속기구분code 	*/
	private String driveTypeValue    ;   /* 구동방식code  	*/
	private String driveLicenseValue ;   /* 운전면허코드    	*/
	private String maximumPassenger ;   /* 승차인원            	*/
	private String displacement     ;   /* 배기량          	*/
	private String year             ;   /* 연식   	        */
	private String mdImg            ;   /* 이미지IDX		*/


	public int getMdIdx() {
		return mdIdx;
	}
	public void setMdIdx(int mdIdx) {
		this.mdIdx = mdIdx;
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
	public String getDriveLicenseCode() {
		return driveLicenseCode;
	}
	public void setDriveLicenseCode(String driveLicenseCode) {
		this.driveLicenseCode = driveLicenseCode;
	}
	
	public String getFuelValue() {
		return fuelValue;
	}
	public void setFuelValue(String fuelValue) {
		this.fuelValue = fuelValue;
	}
	public String getTransmissionValue() {
		return transmissionValue;
	}
	public void setTransmissionValue(String transmissionValue) {
		this.transmissionValue = transmissionValue;
	}
	public String getDriveTypeValue() {
		return driveTypeValue;
	}
	public void setDriveTypeValue(String driveTypeValue) {
		this.driveTypeValue = driveTypeValue;
	}
	public String getDriveLicenseValue() {
		return driveLicenseValue;
	}
	public void setDriveLicenseValue(String driveLicenseValue) {
		this.driveLicenseValue = driveLicenseValue;
	}
	public String getMaximumPassenger() {
		return maximumPassenger;
	}
	public void setMaximumPassenger(String maximumPassenger) {
		this.maximumPassenger = maximumPassenger;
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
	public String getMdImg() {
		return mdImg;
	}
	public void setMdImg(String mdImg) {
		this.mdImg = mdImg;
	}
	
	//차량정보
	private String pyIdx;
	private String mileage;
	private int shortTermFee;
	private int longTermFee;
	private int longTermDeposit;
	private String carStatusCode;
	private String reserveStatusCode;
	private String reserveAbleYn;
	private String carRegDt;
	private String carNumber;
	private String carChasssNumber;
	private String imgIdx;
	private String carDriveLimit;
	private String ageLimit;
	private String garageAddr;
	private String carEtc;
	private String colorCode;
	private String carOption;
	
	public String getPyIdx() {
		return pyIdx;
	}
	public void setPyIdx(String pyIdx) {
		this.pyIdx = pyIdx;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public int getShortTermFee() {
		return shortTermFee;
	}
	public void setShortTermFee(int shortTermFee) {
		this.shortTermFee = shortTermFee;
	}
	public int getLongTermFee() {
		return longTermFee;
	}
	public void setLongTermFee(int longTermFee) {
		this.longTermFee = longTermFee;
	}
	public int getLongTermDeposit() {
		return longTermDeposit;
	}
	public void setLongTermDeposit(int longTermDeposit) {
		this.longTermDeposit = longTermDeposit;
	}
	public String getCarStatusCode() {
		return carStatusCode;
	}
	public void setCarStatusCode(String carStatusCode) {
		this.carStatusCode = carStatusCode;
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
	public String getCarChasssNumber() {
		return carChasssNumber;
	}
	public void setCarChasssNumber(String carChasssNumber) {
		this.carChasssNumber = carChasssNumber;
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
	public String getCarEtc() {
		return carEtc;
	}
	public void setCarEtc(String carEtc) {
		this.carEtc = carEtc;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	public String getCarOption() {
		return carOption;
	}
	public void setCarOption(String carOption) {
		this.carOption = carOption;
	}

	//차량보험
	private String ciIdx;
	private String insuranceFee;
	private String carDamageCover;
	private String onselfDamageCover;
	private String personalCover;
	private String propertyDamageCover;
	private String ciAgeLimit;
	private String driveCareerLimit;
	private String insuranceCopayment;


	public String getCiIdx() {
		return ciIdx;
	}
	public void setCiIdx(String ciIdx) {
		this.ciIdx = ciIdx;
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
	public String getCiAgeLimit() {
		return ciAgeLimit;
	}
	public void setCiAgeLimit(String ciAgeLimit) {
		this.ciAgeLimit = ciAgeLimit;
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


	//차량옵션 리스트
	private String optionCode;
	private ArrayList<String> carOptionList;
	

	public String getOptionCode() {
		return optionCode;
	}
	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}
	public ArrayList<String> getCarOptionList() {
		return carOptionList;
	}
	public void setCarOptionList(ArrayList<String> carOptionList) {
		this.carOptionList = carOptionList;
	}
	public String getUlIdx2() {
		return ulIdx2;
	}
	public void setUlIdx2(String ulIdx2) {
		this.ulIdx2 = ulIdx2;
	}
	
	
	// 예약관련
	private String rmIdx;
	private String reserveTypeCode;
	private String longtermYn;
	private String reserveUserName;
	private String reserveDate;
	private String paymentDate;
	private String carDeposit;
	private String RENT_FEE;
	private String discountFee;
	private String cancelFee;
	private String cancelAmount;
	private String cancelReason;
	private String pdIdx;
	private String reserveMEtc;


	public String getRmIdx() {
		return rmIdx;
	}
	public void setRmIdx(String rmIdx) {
		this.rmIdx = rmIdx;
	}
	public String getReserveTypeCode() {
		return reserveTypeCode;
	}
	public void setReserveTypeCode(String reserveTypeCode) {
		this.reserveTypeCode = reserveTypeCode;
	}
	public String getLongtermYn() {
		return longtermYn;
	}
	public void setLongtermYn(String longtermYn) {
		this.longtermYn = longtermYn;
	}
	public String getReserveUserName() {
		return reserveUserName;
	}
	public void setReserveUserName(String reserveUserName) {
		this.reserveUserName = reserveUserName;
	}
	public String getReserveDate() {
		return reserveDate;
	}
	public void setReserveDate(String reserveDate) {
		this.reserveDate = reserveDate;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getCarDeposit() {
		return carDeposit;
	}
	public void setCarDeposit(String carDeposit) {
		this.carDeposit = carDeposit;
	}
	public String getRENT_FEE() {
		return RENT_FEE;
	}
	public void setRENT_FEE(String rENT_FEE) {
		RENT_FEE = rENT_FEE;
	}
	public String getDiscountFee() {
		return discountFee;
	}
	public void setDiscountFee(String discountFee) {
		this.discountFee = discountFee;
	}
	public String getCancelFee() {
		return cancelFee;
	}
	public void setCancelFee(String cancelFee) {
		this.cancelFee = cancelFee;
	}
	public String getCancelAmount() {
		return cancelAmount;
	}
	public void setCancelAmount(String cancelAmount) {
		this.cancelAmount = cancelAmount;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getPdIdx() {
		return pdIdx;
	}
	public void setPdIdx(String pdIdx) {
		this.pdIdx = pdIdx;
	}
	public String getReserveMEtc() {
		return reserveMEtc;
	}
	public void setReserveMEtc(String reserveMEtc) {
		this.reserveMEtc = reserveMEtc;
	}
	public String getApprovalNumber() {
		return approvalNumber;
	}
	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}
	public String getAgeLimit() {
		return ageLimit;
	}
	public void setAgeLimit(String ageLimit) {
		this.ageLimit = ageLimit;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getCompanyContact1() {
		return companyContact1;
	}
	public void setCompanyContact1(String companyContact1) {
		this.companyContact1 = companyContact1;
	}

	
}
