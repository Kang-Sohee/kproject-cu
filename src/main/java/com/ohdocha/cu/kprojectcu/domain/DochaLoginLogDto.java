package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

//로그인 로그
@Alias("DochaLoginLogDto")
public class DochaLoginLogDto {

	
	private String userId;
	private String loginIp;
	private String loginChannel;
	private String loginSuccess;
	private String loginDate;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getLoginChannel() {
		return loginChannel;
	}
	public void setLoginChannel(String loginChannel) {
		this.loginChannel = loginChannel;
	}
	public String getLoginSuccess() {
		return loginSuccess;
	}
	public void setLoginSuccess(String loginSuccess) {
		this.loginSuccess = loginSuccess;
	}
	public String getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}
	
	
	
}
