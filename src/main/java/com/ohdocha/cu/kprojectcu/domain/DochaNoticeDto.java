package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Alias("DochaNoticeDto")
public class DochaNoticeDto {
	
	private String ntIdx;
	private String ntTitle;
	private String ntContent;
	private String regId;
	private String regDt;
	private String modId;
	private String modDt;

  
}