package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Alias("scheduledDto")
public class DochaScheduledDto {
	
	private String psIdx; //스케쥴 idx
	private String rmIdx; //예약 idx
	private String paymentDate; //결제일
	private String merchantUid; //결제id
	private String paymentAmount; //결제금액
	private String impUid; //결제정보
	private String payCount; //회차
	private String totalPayCount; //총 회차 정보
	private String paymentStatusCode; //상태 정보
	private String userContact1; //연락처
	
}
