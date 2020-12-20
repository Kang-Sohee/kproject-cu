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
@Alias("DochaMainDto")
public class DochaMainDto {
	
	private int miIdx;
	private String miImgIdx;
	private String miStartDt;
	private String miEndDt;
	private String miTitle;
	private String regId;
	private String regDt;
	private String modId;
	private String modDt;

  
}