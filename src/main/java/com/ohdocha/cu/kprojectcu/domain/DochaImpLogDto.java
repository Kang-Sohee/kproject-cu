package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;


@Alias("DochaImpLogDto")
public class DochaImpLogDto {

	private String impIdx;
	private String impUid;
	private String impMsg;
	private String userName;
	private String userContact1;
	private String userCi;
	private String impEtc;
	private String regDt;
	public String getImpIdx() {
		return impIdx;
	}
	public void setImpIdx(String impIdx) {
		this.impIdx = impIdx;
	}
	public String getImpUid() {
		return impUid;
	}
	public void setImpUid(String impUid) {
		this.impUid = impUid;
	}
	public String getImpMsg() {
		return impMsg;
	}
	public void setImpMsg(String impMsg) {
		this.impMsg = impMsg;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserContact1() {
		return userContact1;
	}
	public void setUserContact1(String userContact1) {
		this.userContact1 = userContact1;
	}
	public String getUserCi() {
		return userCi;
	}
	public void setUserCi(String userCi) {
		this.userCi = userCi;
	}
	public String getImpEtc() {
		return impEtc;
	}
	public void setImpEtc(String impEtc) {
		this.impEtc = impEtc;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	
	
	
    
}
