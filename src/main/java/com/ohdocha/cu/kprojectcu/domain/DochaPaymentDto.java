package com.ohdocha.cu.kprojectcu.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

//import gcp.oms.model.base.BaseOmsPaymentif;
@Getter
@Setter
@ToString
@NoArgsConstructor
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
	private String firstDriverContact;
	private String firstDriverBirthday;
	private String ulIdx1;
	private String secondDriverName;
	private String secondDriverContact;
	private String secondDriverBirthday;
	private String ulIdx2;
	private String reserveMEtc;       
	private String regId;    
	private String modId;
	
	private String regDt;
	private String modDt;
	private String delYn;

	//예약테이블
	private String reIdx;
	private String quIdx;
	private String reserveChannel;
	

	
	
}