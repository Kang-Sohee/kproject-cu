package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

@Alias("DochaPaymentDetailDto")
public class DochaPaymentDetailDto {

	
	private String pdIdx;
	private String reIdx;
	private String rmIdx;  
	private String plIdx;
	private String urIdx;
	private String pgCode;  
 	private String paymentAmount;
 	private String paymentTypeCode;
 	private String paymentKindCode;
 	private String approvalNumber;
 	private String paymentDate;
 	private String etc;
 	
	public String getPdIdx() {
		return pdIdx;
	}
	public void setPdIdx(String pdIdx) {
		this.pdIdx = pdIdx;
	}
	public String getReIdx() {
		return reIdx;
	}
	public void setReIdx(String reIdx) {
		this.reIdx = reIdx;
	}
	public String getRmIdx() {
		return rmIdx;
	}
	public void setRmIdx(String rmIdx) {
		this.rmIdx = rmIdx;
	}
	public String getPlIdx() {
		return plIdx;
	}
	public void setPlIdx(String plIdx) {
		this.plIdx = plIdx;
	}
	public String getUrIdx() {
		return urIdx;
	}
	public void setUrIdx(String urIdx) {
		this.urIdx = urIdx;
	}
	
	public String getPgCode() {
		return pgCode;
	}
	public void setPgCode(String pgCode) {
		this.pgCode = pgCode;
	}
	public String getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getPaymentTypeCode() {
		return paymentTypeCode;
	}
	public void setPaymentTypeCode(String paymentTypeCode) {
		this.paymentTypeCode = paymentTypeCode;
	}
	public String getPaymentKindCode() {
		return paymentKindCode;
	}
	public void setPaymentKindCode(String paymentKindCode) {
		this.paymentKindCode = paymentKindCode;
	}
	public String getApprovalNumber() {
		return approvalNumber;
	}
	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
	
 	
 	
 	
}
