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
	private String paymentDate; //결제일
	private String amount; //결제금액
	private String userContact1; //연락처
	
}
