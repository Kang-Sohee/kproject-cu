package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

@Alias("DochaRentCompanyDto")
public class DochaRentCompanyDto {

	// 회사정보
	private String rtIdx;                     
	private String rtPIdx;                   
	private String companyName;               
	private String companyZipCode;            
	private String companyAddress;           
	private String companyAddressDetail;     
	private String establishedDate;           
	private String companyRegistrationImg;
	private String companyRegistrationNumber;   
	private String accountBank;               
	private String accountNumber;             
	private String accountHolder;             
	private String accountImgIdx;            
	private String longTermRentYn;           
	private String shortTermRentYn;          
	private String allianceStatus;            
	private String branchAbleYn;             
	private String carCount;                  
	private String etc;                        
	private String regId;
	private String regDt;
	private String modId;
	private String modDt;
	private String ageLimit;

	
	// 회사 시간정보
	private String weekDayOpenStart;
	private String weekDayOpenEnd;
	private String weekEndOpenStart;
	private String weekEndOpenEnd;
	private String weekDayDeliveryStart;
	private String weekDayDeliveryEnd;
	private String weekEndDeliveryStart;
	private String weekEndDeliveryEnd;
	private String returnInspectionTime;
	private String visitAbleTime;
	private String deliveryAbleTime;
	private String alarmYn;

	private String accountNumberUrl;
	private String companyNumberUrl;
	
	
	private String commissionPer;
	private String taxInvoiceCode;
	
	
	private String usercontact1;
	
	//회사직원정보
	//겹치는건 주석처리
    private int rsIdx; 	// 제휴사직원idsx
    //private String rtIdx;    // 제휴사idx 
    private String staffName;    // 직원명 
    private String staffContact1;    // 연락처1 
    private String staffContact2;    // 연락처2 
    private String staffEmail;    // 이메일 
    private String staffTitle;    // 직위 
    private String ownerYn;    // 대표여부 
    private String staffTypeCode;    // 직원분류code 
    //private String regId;    // 등록자 
    //private String regDt;    // 등록일시 
    //private String modId;    // 수정자 
    //private String modDt;    // 수정일시 
    private String delYn;    // 삭제여부
    
    
    private String companyContact1;
    private String companyContact2;
    
    private String al1;
    private String al2;
	
	public String getRtIdx() {
		return rtIdx;
	}
	public void setRtIdx(String rtIdx) {
		this.rtIdx = rtIdx;
	}
	public String getRtPIdx() {
		return rtPIdx;
	}
	public void setRtPIdx(String rtPIdx) {
		this.rtPIdx = rtPIdx;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyZipCode() {
		return companyZipCode;
	}
	public void setCompanyZipCode(String companyZipCode) {
		this.companyZipCode = companyZipCode;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getCompanyAddressDetail() {
		return companyAddressDetail;
	}
	public void setCompanyAddressDetail(String companyAddressDetail) {
		this.companyAddressDetail = companyAddressDetail;
	}

	public String getCompanyRegistrationImg() {
		return companyRegistrationImg;
	}
	public void setCompanyRegistrationImg(String companyRegistrationImg) {
		this.companyRegistrationImg = companyRegistrationImg;
	}
	public String getCompanyRegistrationNumber() {
		return companyRegistrationNumber;
	}
	public void setCompanyRegistrationNumber(String companyRegistrationNumber) {
		this.companyRegistrationNumber = companyRegistrationNumber;
	}
	public String getAccountBank() {
		return accountBank;
	}
	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountHolder() {
		return accountHolder;
	}
	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}
	public String getAccountImgIdx() {
		return accountImgIdx;
	}
	public void setAccountImgIdx(String accountImgIdx) {
		this.accountImgIdx = accountImgIdx;
	}
	public String getLongTermRentYn() {
		return longTermRentYn;
	}
	public void setLongTermRentYn(String longTermRentYn) {
		this.longTermRentYn = longTermRentYn;
	}
	public String getShortTermRentYn() {
		return shortTermRentYn;
	}
	public void setShortTermRentYn(String shortTermRentYn) {
		this.shortTermRentYn = shortTermRentYn;
	}
	public String getAllianceStatus() {
		return allianceStatus;
	}
	public void setAllianceStatus(String allianceStatus) {
		this.allianceStatus = allianceStatus;
	}
	public String getBranchAbleYn() {
		return branchAbleYn;
	}
	public void setBranchAbleYn(String branchAbleYn) {
		this.branchAbleYn = branchAbleYn;
	}
	public String getCarCount() {
		return carCount;
	}
	public void setCarCount(String carCount) {
		this.carCount = carCount;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
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
	public String getWeekDayOpenStart() {
		return weekDayOpenStart;
	}
	public void setWeekDayOpenStart(String weekDayOpenStart) {
		this.weekDayOpenStart = weekDayOpenStart;
	}
	public String getWeekDayOpenEnd() {
		return weekDayOpenEnd;
	}
	public void setWeekDayOpenEnd(String weekDayOpenEnd) {
		this.weekDayOpenEnd = weekDayOpenEnd;
	}
	public String getWeekEndOpenStart() {
		return weekEndOpenStart;
	}
	public void setWeekEndOpenStart(String weekEndOpenStart) {
		this.weekEndOpenStart = weekEndOpenStart;
	}
	public String getWeekEndOpenEnd() {
		return weekEndOpenEnd;
	}
	public void setWeekEndOpenEnd(String weekEndOpenEnd) {
		this.weekEndOpenEnd = weekEndOpenEnd;
	}
	public String getWeekDayDeliveryStart() {
		return weekDayDeliveryStart;
	}
	public void setWeekDayDeliveryStart(String weekDayDeliveryStart) {
		this.weekDayDeliveryStart = weekDayDeliveryStart;
	}
	public String getWeekDayDeliveryEnd() {
		return weekDayDeliveryEnd;
	}
	public void setWeekDayDeliveryEnd(String weekDayDeliveryEnd) {
		this.weekDayDeliveryEnd = weekDayDeliveryEnd;
	}
	public String getWeekEndDeliveryStart() {
		return weekEndDeliveryStart;
	}
	public void setWeekEndDeliveryStart(String weekEndDeliveryStart) {
		this.weekEndDeliveryStart = weekEndDeliveryStart;
	}
	public String getWeekEndDeliveryEnd() {
		return weekEndDeliveryEnd;
	}
	public void setWeekEndDeliveryEnd(String weekEndDeliveryEnd) {
		this.weekEndDeliveryEnd = weekEndDeliveryEnd;
	}
	public String getReturnInspectionTime() {
		return returnInspectionTime;
	}
	public void setReturnInspectionTime(String returnInspectionTime) {
		this.returnInspectionTime = returnInspectionTime;
	}
	public String getVisitAbleTime() {
		return visitAbleTime;
	}
	public void setVisitAbleTime(String visitAbleTime) {
		this.visitAbleTime = visitAbleTime;
	}
	public String getDeliveryAbleTime() {
		return deliveryAbleTime;
	}
	public void setDeliveryAbleTime(String deliveryAbleTime) {
		this.deliveryAbleTime = deliveryAbleTime;
	}
	
	public String getCommissionPer() {
		return commissionPer;
	}
	public void setCommissionPer(String commissionPer) {
		this.commissionPer = commissionPer;
	}
	public String getTaxInvoiceCode() {
		return taxInvoiceCode;
	}
	public void setTaxInvoiceCode(String taxInvoiceCode) {
		this.taxInvoiceCode = taxInvoiceCode;
	}
	public String getEstablishedDate() {
		return establishedDate;
	}
	public void setEstablishedDate(String establishedDate) {
		this.establishedDate = establishedDate;
	}
	public String getAccountNumberUrl() {
		return accountNumberUrl;
	}
	public void setAccountNumberUrl(String accountNumberUrl) {
		this.accountNumberUrl = accountNumberUrl;
	}
	public String getCompanyNumberUrl() {
		return companyNumberUrl;
	}
	public void setCompanyNumberUrl(String companyNumberUrl) {
		this.companyNumberUrl = companyNumberUrl;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getStaffContact1() { 
		return staffContact1; 
	}
	public void setStaffContact1(String staffContact1) {
		this.staffContact1 = staffContact1;
	}
	public String getUsercontact1() {
		return usercontact1;
	}
	public void setUsercontact1(String usercontact1) {
		this.usercontact1 = usercontact1;
	}
	public int getRsIdx() {
		return rsIdx;
	}
	public void setRsIdx(int rsIdx) {
		this.rsIdx = rsIdx;
	}
	public String getStaffContact2() {
		return staffContact2;
	}
	public void setStaffContact2(String staffContact2) {
		this.staffContact2 = staffContact2;
	}
	public String getStaffEmail() {
		return staffEmail;
	}
	public void setStaffEmail(String staffEmail) {
		this.staffEmail = staffEmail;
	}
	public String getStaffTitle() {
		return staffTitle;
	}
	public void setStaffTitle(String staffTitle) {
		this.staffTitle = staffTitle;
	}
	public String getOwnerYn() {
		return ownerYn;
	}
	public void setOwnerYn(String ownerYn) {
		this.ownerYn = ownerYn;
	}
	public String getStaffTypeCode() {
		return staffTypeCode;
	}
	public void setStaffTypeCode(String staffTypeCode) {
		this.staffTypeCode = staffTypeCode;
	}
	public String getDelYn() {
		return delYn;
	}
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	public String getAgeLimit() {
		return ageLimit;
	}
	public void setAgeLimit(String ageLimit) {
		this.ageLimit = ageLimit;
	}
	public String getCompanyContact1() {
		return companyContact1;
	}
	public void setCompanyContact1(String companyContact1) {
		this.companyContact1 = companyContact1;
	}
	public String getCompanyContact2() {
		return companyContact2;
	}
	public void setCompanyContact2(String companyContact2) {
		this.companyContact2 = companyContact2;
	}
	public String getAlarmYn() {
		return alarmYn;
	}
	public void setAlarmYn(String alarmYn) {
		this.alarmYn = alarmYn;
	}
	public String getAl1() {
		return al1;
	}
	public void setAl1(String al1) {
		this.al1 = al1;
	}
	public String getAl2() {
		return al2;
	}
	public void setAl2(String al2) {
		this.al2 = al2;
	}
	

    
}
