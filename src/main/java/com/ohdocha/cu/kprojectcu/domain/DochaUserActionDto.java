package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

@Alias("DochaUserActionDto")
public class DochaUserActionDto {

	private String urIdx;
	private String pageUrl;
	private long lati;
	private long longti;
	
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

	public long getLati() {
		return lati;
	}

	public void setLati(long lati) {
		this.lati = lati;
	}

	public long getLongti() {
		return longti;
	}

	public void setLongti(long longti) {
		this.longti = longti;
	}
}
