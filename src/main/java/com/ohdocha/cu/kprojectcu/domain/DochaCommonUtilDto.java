package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

@Alias("DochaCommonUtilDto")
public class DochaCommonUtilDto {
	
	private String codeIdx;
	private String rtCode;
	private String pCode;
	private String code;
	private String codeValue ;
	private String descript;
	private String sort;
	private String rootYn;
	
	public String getCodeIdx() {
		return codeIdx;
	}
	public void setCodeIdx(String codeIdx) {
		this.codeIdx = codeIdx;
	}
	public String getRtCode() {
		return rtCode;
	}
	public void setRtCode(String rtCode) {
		this.rtCode = rtCode;
	}
	public String getpCode() {
		return pCode;
	}
	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeValue() {
		return codeValue;
	}
	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getRootYn() {
		return rootYn;
	}
	public void setRootYn(String rootYn) {
		this.rootYn = rootYn;
	}
	
}
