package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

@Alias("alarmTalkDto")
public class DochaAlarmTalkDto {
	
	//카카오 기본 parameter
	private String phone;						//알림톡보낼 연락처
	private String callBack;					//발신번호
	private String msg;							//보낼 MSG
	private String templateCode;	//사전등록된 알림톡 템플릿 코드
	private String btnTypes;					//버튼타임 (웹링크, 앱링크, 봇키워드, 메시지전달, 배송조회)
	private String btnTxts;						//버튼명
	private String btnUrls1;					//웹링크1
	private String btnUrls2;					//웹링크2
	
	private String rentDate;					//대여일시
	private String returnDate;					//반납일시
	private String cancelDate;					//취소일시
	private String alarmYn;						//알맅목 수신여부
	private String carType;						//차종
	private String rentAddr;					//대여위치
	private String returnAddr;					//반납위치
	
	private String insurancerate;				//자차보험
	private String insurancecopayment;			//면책금
	
	private String rentAmount;					//대여금액
	private String discountAmount;				//할인금액
	private String payAmount;					//결제금액
	private String cancelAmount;				//취소금액
	
	private String carDeposit;					//보증금
	
	
	private String companyName;					//회원사 이름
	private String companyContact;				//회원사 연락처
	private String companyAddr;					//회원사 주소
	
		
	private String userName;					//예약자
	private String userBirthday;				//예약자 생년월일
	private String userDriverLience;			//면허종류
	
	private String failedType;					//sms전송 타입(알림톡 전송 실패시 sms로 )
	private String failedSubject;				//sms 제목
	private String failedMsg;					//발송 실패 타입

	private String reserveDate;
	private String discountFee;
	private String carName;
	
	private String rtIdx; //회원사 idx
	
	private String deliveryTypeCode; //대여방법
	
	private String tmp_Code;
	
	private String userAge;
	
	private String carNumber;
	
	private String userContact; //예약자연락처
	private String urIdx;
	//KAKAO_LOG_DTO 시작
	private String kaIdx;      
	private String resultCode; 
	private String resultFull;
	private String resultMsg;
	private String cmid;
	
	private String rmIdx;
	private String quIdx;
	private int    rsIdx;
	private String contact;
	private String division;
	
	private String bookDate;					//예약일시
	
	private String paymentDate;
	private String amount;
	private String userContact1;
	
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCallBack() {
		return callBack;
	}
	public void setCallBack(String callBack) {
		this.callBack = callBack;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getTemplateCode() {
		return templateCode;
	}
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	public String getBtnTypes() {
		return btnTypes;
	}
	public void setBtnTypes(String btnTypes) {
		this.btnTypes = btnTypes;
	}
	public String getBtnTxts() {
		return btnTxts;
	}
	public void setBtnTxts(String btnTxts) {
		this.btnTxts = btnTxts;
	}
	public String getBtnUrls1() {
		return btnUrls1;
	}
	public void setBtnUrls1(String btnUrls1) {
		this.btnUrls1 = btnUrls1;
	}
	public String getBtnUrls2() {
		return btnUrls2;
	}
	public void setBtnUrls2(String btnUrls2) {
		this.btnUrls2 = btnUrls2;
	}
	public String getRentDate() {
		return rentDate;
	}
	public void setRentDate(String rentDate) {
		this.rentDate = rentDate;
	}
	public String getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
	public String getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public String getRentAddr() {
		return rentAddr;
	}
	public void setRentAddr(String rentAddr) {
		this.rentAddr = rentAddr;
	}
	public String getReturnAddr() {
		return returnAddr;
	}
	public void setReturnAddr(String returnAddr) {
		this.returnAddr = returnAddr;
	}
	public String getInsurancerate() {
		return insurancerate;
	}
	public void setInsurancerate(String insurancerate) {
		this.insurancerate = insurancerate;
	}
	public String getInsurancecopayment() {
		return insurancecopayment;
	}
	public void setInsurancecopayment(String insurancecopayment) {
		this.insurancecopayment = insurancecopayment;
	}
	public String getRentAmount() {
		return rentAmount;
	}
	public void setRentAmount(String rentAmount) {
		this.rentAmount = rentAmount;
	}
	public String getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}
	public String getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}
	public String getCancelAmount() {
		return cancelAmount;
	}
	public void setCancelAmount(String cancelAmount) {
		this.cancelAmount = cancelAmount;
	}
	public String getCarDeposit() {
		return carDeposit;
	}
	public void setCarDeposit(String carDeposit) {
		this.carDeposit = carDeposit;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyContact() {
		return companyContact;
	}
	public void setCompanyContact(String companyContact) {
		this.companyContact = companyContact;
	}
	public String getCompanyAddr() {
		return companyAddr;
	}
	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserBirthday() {
		return userBirthday;
	}
	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}
	public String getUserDriverLience() {
		return userDriverLience;
	}
	public void setUserDriverLience(String userDriverLience) {
		this.userDriverLience = userDriverLience;
	}
	public String getFailedType() {
		return failedType;
	}
	public void setFailedType(String failedType) {
		this.failedType = failedType;
	}
	public String getFailedSubject() {
		return failedSubject;
	}
	public void setFailedSubject(String failedSubject) {
		this.failedSubject = failedSubject;
	}
	public String getFailedMsg() {
		return failedMsg;
	}
	public void setFailedMsg(String failedMsg) {
		this.failedMsg = failedMsg;
	}
	public String getReserveDate() {
		return reserveDate;
	}
	public void setReserveDate(String reserveDate) {
		this.reserveDate = reserveDate;
	}
	public String getDiscountFee() {
		return discountFee;
	}
	public void setDiscountFee(String discountFee) {
		this.discountFee = discountFee;
	}
	public String getCarName() {
		return carName;
	}
	public void setCarName(String carName) {
		this.carName = carName;
	}
	public String getRtIdx() {
		return rtIdx;
	}
	public void setRtIdx(String rtIdx) {
		this.rtIdx = rtIdx;
	}

	public String getAlarmYn() {
		return alarmYn;
	}
	public void setAlarmYn(String alarmYn) {
		this.alarmYn = alarmYn;
	}
	public String getDeliveryTypeCode() {
		return deliveryTypeCode;
	}
	public void setDeliveryTypeCode(String deliveryTypeCode) {
		this.deliveryTypeCode = deliveryTypeCode;
	}
	public String getTmp_Code() {
		return tmp_Code;
	}
	public void setTmp_Code(String string) {
		this.tmp_Code = string;
	}
	public String getUserAge() {
		return userAge;
	}
	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public String getUserContact() {
		return userContact;
	}
	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}
	public String getUrIdx() {
		return urIdx;
	}
	public void setUrIdx(String urIdx) {
		this.urIdx = urIdx;
	}
	public String getKaIdx() {
		return kaIdx;
	}
	public void setKaIdx(String kaIdx) {
		this.kaIdx = kaIdx;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultFull() {
		return resultFull;
	}
	public void setResultFull(String resultFull) {
		this.resultFull = resultFull;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public String getCmid() {
		return cmid;
	}
	public void setCmid(String cmid) {
		this.cmid = cmid;
	}
	public String getRmIdx() {
		return rmIdx;
	}
	public void setRmIdx(String rmIdx) {
		this.rmIdx = rmIdx;
	}
	public String getQuIdx() {
		return quIdx;
	}
	public void setQuIdx(String quIdx) {
		this.quIdx = quIdx;
	}
	public int getRsIdx() {
		return rsIdx;
	}
	public void setRsIdx(int rsIdx) {
		this.rsIdx = rsIdx;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getBookDate() {
		return bookDate;
	}
	public void setBookDate(String bookDate) {
		this.bookDate = bookDate;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getUserContact1() {
		return userContact1;
	}
	public void setUserContact1(String userContact1) {
		this.userContact1 = userContact1;
	}

	
	
}
