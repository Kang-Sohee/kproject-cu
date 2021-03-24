package com.ohdocha.cu.kprojectcu.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Alias("DochaLowcreditDto")
public class DochaLowcreditDto {

	private int lcIdx;
	private String lcImgIdx ;
	private String lcListImgIdx ;
	private String lcBannerImgIdx ;
	private LocalDateTime lcStartDt;
	private LocalDateTime lcEndDt;
	private String lcTitle;
	private String lcContent;
	private String regId;
	private LocalDateTime regDt;
	private String modId;
	private LocalDateTime modDt;

}