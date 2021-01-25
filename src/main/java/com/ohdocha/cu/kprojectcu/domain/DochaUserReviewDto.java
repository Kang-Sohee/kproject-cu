package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

import com.ohdocha.cu.kprojectcu.domain.DochaUserReviewFileDto.DochaUserReviewFileDtoBuilder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@Alias("DochaUserReviewDto")
public class DochaUserReviewDto {

	  private Integer rvIdx;  //'SEQUENCE'
	  private String rmIdx;  //'예약번호'
	  private String urIdx;  //'고객번호'
	  private String comment;//'후기내용'
	  private String rating; //'평가점수,
	  private String regDt;  //'등록일시'
	  private String regId;  //'등록자'
	  private String modDt;  //'수정일시'
	  private String modId;  //'수정자'
	  private String delYn;  //'삭제여부'
	  
}
