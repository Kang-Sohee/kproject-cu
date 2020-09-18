package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

@Alias("DochaQuoteCompanyDto")
public class DochaQuoteCompanyDto {

	private String qrIdx;
	private String quIdx;
	private String crIdx;
	private String quoteStatus;
	private String rentFee;
	private String insuranceRate;
	private String paymentAmount;
	private String urIdx;
	private String userName;
	private String rtIdx;
	private String regId;
	private String regDt;
	private String modId;
	private String modDt;
	private String delYn;
	private String monthlyFee;
	private String dailyFee;
	
	
	public String getQrIdx() {
		return qrIdx;
	}
	public void setQrIdx(String qrIdx) {
		this.qrIdx = qrIdx;
	}
	public String getQuIdx() {
		return quIdx;
	}
	public void setQuIdx(String quIdx) {
		this.quIdx = quIdx;
	}
	public String getCrIdx() {
		return crIdx;
	}
	public void setCrIdx(String crIdx) {
		this.crIdx = crIdx;
	}
	public String getQuoteStatus() {
		return quoteStatus;
	}
	public void setQuoteStatus(String quoteStatus) {
		this.quoteStatus = quoteStatus;
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
	public String getRtIdx() {
		return rtIdx;
	}
	public void setRtIdx(String rtIdx) {
		this.rtIdx = rtIdx;
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
	
	
}
