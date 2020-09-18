package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

@Alias("DochaCarInsuranceDto")
public class DochaCarInsuranceDto {

	private String ciIdx;
	private String crIdx;
	private String insuranceFee;
	private String car_damageCover;
	private String onselfDamageCover;
	private String personalCover;
	private String propertyDamageCover;
	private String ageLimit;
	private String driveCareerLimit;
	private String insuranceCopayment;
	private String regId;
	private String regDt;
	private String modId;
	private String modDt;
	private String delYn;
	
	public String getCiIdx() {
		return ciIdx;
	}
	public void setCiIdx(String ciIdx) {
		this.ciIdx = ciIdx;
	}
	public String getCrIdx() {
		return crIdx;
	}
	public void setCrIdx(String crIdx) {
		this.crIdx = crIdx;
	}
	public String getInsuranceFee() {
		return insuranceFee;
	}
	public void setInsuranceFee(String insuranceFee) {
		this.insuranceFee = insuranceFee;
	}
	public String getCar_damageCover() {
		return car_damageCover;
	}
	public void setCar_damageCover(String car_damageCover) {
		this.car_damageCover = car_damageCover;
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
	public String getAgeLimit() {
		return ageLimit;
	}
	public void setAgeLimit(String ageLimit) {
		this.ageLimit = ageLimit;
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

	
	
}
