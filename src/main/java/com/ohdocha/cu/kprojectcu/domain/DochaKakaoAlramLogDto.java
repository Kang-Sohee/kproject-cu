package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

@Alias("DochaKakaoAlramLogDto")
public class DochaKakaoAlramLogDto {

	
	private String cmid            ;
	private String contact         ;
	private String division       ;
	private String kaIdx           ;
	private String quIdx           ;
	private String regdt           ;
	private String resultCode      ;
	private String resultFull      ;
	private String resultMsg       ;
	private String rmIdx           ;
	private String rtIdx           ;
	private String templateCode    ;
	private String urIdx           ;
	
	
	
	public String getCmid() {
		return cmid;
	}
	public void setCmid(String cmid) {
		this.cmid = cmid;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getKaIdx() {
		return kaIdx;
	}
	public void setKaIdx(String kaIdx) {
		this.kaIdx = kaIdx;
	}
	public String getQuIdx() {
		return quIdx;
	}
	public void setQuIdx(String quIdx) {
		this.quIdx = quIdx;
	}
	public String getRegdt() {
		return regdt;
	}
	public void setRegdt(String regdt) {
		this.regdt = regdt;
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
	public String getRmIdx() {
		return rmIdx;
	}
	public void setRmIdx(String rmIdx) {
		this.rmIdx = rmIdx;
	}
	public String getRtIdx() {
		return rtIdx;
	}
	public void setRtIdx(String rtIdx) {
		this.rtIdx = rtIdx;
	}
	public String getTemplateCode() {
		return templateCode;
	}
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	public String getUrIdx() {
		return urIdx;
	}
	public void setUrIdx(String urIdx) {
		this.urIdx = urIdx;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	
	
}
