package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

@Alias("DochaCalcRentFeeDto")
public class DochaCalcRentFeeDto {

	private String crIdx; 			// 차량순번
	private String carNumber;		// 차량번호
	private String rentFee;         // 대여금
	private String disRentFee;      // 할인 후 대여금
	private String insuranceFee;    // 보험료
	private String insuranceFee2;   // 보혐료2
	private String insuranceFee3;   // 보험료3
	private String insuranceFee4;   // 보험료4
	private String mmRentFee;       // 장기 월 대여요금
	private String mmLastRentFee;   // 장기 마지막 월 대여요금

	public String getCrIdx() {
		return crIdx;
	}
	public void setCrIdx(String crIdx) {
		this.crIdx = crIdx;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public String getRentFee() {
		return rentFee;
	}
	public void setRentFee(String rentFee) {
		this.rentFee = rentFee;
	}
	public String getDisRentFee() {
		return disRentFee;
	}
	public void setDisRentFee(String disRentFee) {
		this.disRentFee = disRentFee;
	}
	public String getInsuranceFee() {
		return insuranceFee;
	}
	public void setInsuranceFee(String insuranceFee) {
		this.insuranceFee = insuranceFee;
	}
	public String getInsuranceFee2() {
		return insuranceFee2;
	}
	public void setInsuranceFee2(String insuranceFee2) {
		this.insuranceFee2 = insuranceFee2;
	}
	public String getInsuranceFee3() {
		return insuranceFee3;
	}
	public void setInsuranceFee3(String insuranceFee3) {
		this.insuranceFee3 = insuranceFee3;
	}
	public String getInsuranceFee4() {
		return insuranceFee4;
	}
	public void setInsuranceFee4(String insuranceFee4) {
		this.insuranceFee4 = insuranceFee4;
	}
	public String getMmRentFee() {
		return mmRentFee;
	}
	public void setMmRentFee(String mmRentFee) {
		this.mmRentFee = mmRentFee;
	}
	public String getMmLastRentFee() {
		return mmLastRentFee;
	}
	public void setMmLastRentFee(String mmLastRentFee) {
		this.mmLastRentFee = mmLastRentFee;
	}

	
}
