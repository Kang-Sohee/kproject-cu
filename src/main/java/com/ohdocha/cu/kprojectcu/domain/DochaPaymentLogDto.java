package com.ohdocha.cu.kprojectcu.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@NoArgsConstructor
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
	private String merchantUid         ;
	private String impUid         	   ;
	private String receiptUrl          ;

	
}
