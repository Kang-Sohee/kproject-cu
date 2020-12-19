package com.ohdocha.cu.kprojectcu.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Alias("DochaPaymentPeriodDto")
public class DochaPaymentPeriodDto {

	private int perIdx;
	private String rtIdx;
	private String crIdx;
	private String periodEtc;
	private Date periodStartDt;
	private Date periodEndDt;
	private String discountExtrachargeCode;
	private String periodPay;
}
