package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

//import gcp.oms.model.base.BaseOmsPaymentif;

@Alias("DochaPaymentDto")
public class DochaPaymentDto {
	
	//예약마스터
	private String rmIdx;           
	private String reserveTypeCode;       
	private String reserveStatusCode;     
	private String longTermYn;
	private String urIdx; 
	private String reserveUserName;
	private String rentStartDay;    
	private String rentEndDay;      
	private String rentStartTime;   
	private String rentEndTime;     
	private String deliveryTypeCode;
	private String deliveryAddr;    
	private String returnTypeCode;  
	private String returnAddr;
	private String crIdx;
	private String carTypeCode;        
	private String rtIdx;           
	private String companyName;
	private String reserveDate;
	private String paymentDate;
	private String carDeposit;     
	private String rentFee;     
	private String insuranceFee;
	private String discountFee;
	private String paymentAmount;
	private String cancelFee;
	private String cancelAmount;
	private String cancelReason;
	private String pdIdx;   
	private String firstDriverName; 
	private String ulIdx1;         
	private String secondDriverName;
	private String ulIdx2;
	private String reserveMEtc;       
	private String regId;    
	private String modId;
	
	private String regDt;
	private String modDt;
	private String delYn;
	
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
	public String getReserveStatusCode() {
		return reserveStatusCode;
	}
	public void setReserveStatusCode(String reserveStatusCode) {
		this.reserveStatusCode = reserveStatusCode;
	}
	public String getLongTermYn() {
		return longTermYn;
	}
	public void setLongTermYn(String longTermYn) {
		this.longTermYn = longTermYn;
	}
	public String getUrIdx() {
		return urIdx;
	}
	public void setUrIdx(String urIdx) {
		this.urIdx = urIdx;
	}
	public String getReserveUserName() {
		return reserveUserName;
	}
	public void setReserveUserName(String reserveUserName) {
		this.reserveUserName = reserveUserName;
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
	public String getReturnAddr() {
		return returnAddr;
	}
	public void setReturnAddr(String returnAddr) {
		this.returnAddr = returnAddr;
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
	public String getRentFee() {
		return rentFee;
	}
	public void setRentFee(String rentFee) {
		this.rentFee = rentFee;
	}
	public String getInsuranceFee() {
		return insuranceFee;
	}
	public void setInsuranceFee(String insuranceFee) {
		this.insuranceFee = insuranceFee;
	}
	public String getDiscountFee() {
		return discountFee;
	}
	public void setDiscountFee(String discountFee) {
		this.discountFee = discountFee;
	}
	public String getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
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
	public String getUlIdx2() {
		return ulIdx2;
	}
	public void setUlIdx2(String ulIdx2) {
		this.ulIdx2 = ulIdx2;
	}
	public String getReserveMEtc() {
		return reserveMEtc;
	}
	public void setReserveMEtc(String reserveMEtc) {
		this.reserveMEtc = reserveMEtc;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getModId() {
		return modId;
	}
	public void setModId(String modId) {
		this.modId = modId;
	}           
 	       
	//예약테이블
	private String reIdx;  
	private String quIdx;  
	private String reserveChannel;

	public String getReIdx() {
		return reIdx;
	}
	public void setReIdx(String reIdx) {
		this.reIdx = reIdx;
	}
	public String getQuIdx() {
		return quIdx;
	}
	public void setQuIdx(String quIdx) {
		this.quIdx = quIdx;
	}
	public String getReserveChannel() {
		return reserveChannel;
	}
	public void setReserveChannel(String reserveChannel) {
		this.reserveChannel = reserveChannel;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
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

	
	
	
}