package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

@Alias("DochaPaymentLogDto")
public class DochaPaymentLogDto {

	
	private String plIdx               ;
	private String pdIdx               ;
	private String rmIdx               ;
	private String paymentRequestAmount;
	private String paymentAmount       ;
	private String approvalYn          ;
	private String approvalNumber      ;
	private String failMsg             ;
	private String errCode             ;
	private String orgMsg	           ;
	private String payLogEtc		   ;	
	private String paymentDate         ;
	
	public String getPlIdx() {
		return plIdx;
	}
	public void setPlIdx(String plIdx) {
		this.plIdx = plIdx;
	}
	public String getPdIdx() {
		return pdIdx;
	}
	public void setPdIdx(String pdIdx) {
		this.pdIdx = pdIdx;
	}
	public String getRmIdx() {
		return rmIdx;
	}
	public void setRmIdx(String rmIdx) {
		this.rmIdx = rmIdx;
	}
	public String getPaymentRequestAmount() {
		return paymentRequestAmount;
	}
	public void setPaymentRequestAmount(String paymentRequestAmount) {
		this.paymentRequestAmount = paymentRequestAmount;
	}
	public String getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getApprovalYn() {
		return approvalYn;
	}
	public void setApprovalYn(String approvalYn) {
		this.approvalYn = approvalYn;
	}
	public String getApprovalNumber() {
		return approvalNumber;
	}
	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}
	public String getFailMsg() {
		return failMsg;
	}
	public void setFailMsg(String failMsg) {
		this.failMsg = failMsg;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getOrgMsg() {
		return orgMsg;
	}
	public void setOrgMsg(String orgMsg) {
		this.orgMsg = orgMsg;
	}
	public String getPayLogEtc() {
		return payLogEtc;
	}
	public void setPayLogEtc(String payLogEtc) {
		this.payLogEtc = payLogEtc;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	
	
}
