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
@Alias("DochaEventDto")
public class DochaEventDto {

	private int evIdx;
	private String evImgIdx ;
	private String evListImgIdx ;
	private String evBannerImgIdx ;
	private LocalDateTime evStartDt;
	private LocalDateTime evEndDt;
	private String evTitle;
	private String evContent;
	private String regId;
	private LocalDateTime regDt;
	private String modId;
	private LocalDateTime modDt;

}