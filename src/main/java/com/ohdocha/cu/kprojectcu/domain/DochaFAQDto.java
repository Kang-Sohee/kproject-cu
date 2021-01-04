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
@Alias("DochaFAQDto")
public class DochaFAQDto {
	
	private String faIdx;
	private String faTitle;
	private String faContent;
	private String imgIdx;
	private String regId;
	private String regDt;
	private String modId;
	private String modDt;

  
}