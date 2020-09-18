package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

@Alias("DochaUserActionDto")
public class DochaUserActionDto {

	private String urIdx;
	private String pageUrl;
	private String lati;
	private String longti;
	
	public String getUrIdx() {
		return urIdx;
	}
	public void setUrIdx(String urIdx) {
		this.urIdx = urIdx;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	public String getLati() {
		return lati;
	}
	public void setLati(String lati) {
		this.lati = lati;
	}
	public String getLongti() {
		return longti;
	}
	public void setLongti(String longti) {
		this.longti = longti;
	}
}
