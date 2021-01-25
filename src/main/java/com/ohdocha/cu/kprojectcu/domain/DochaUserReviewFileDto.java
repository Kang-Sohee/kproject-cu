package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@Alias("DochaUserReviewFileDto")
public class DochaUserReviewFileDto {

	private Integer idx;
	private Integer rvIdx;
	private String filePath;
	private String fileRealPath;
	private String fileNm; 
	private String orgFileNm;
	private String regDt;
	private String modDt;
	private String delYn;
	
}
