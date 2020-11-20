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
@Alias("DochaQuestionDto")
public class DochaQuestionDto {
	
	private String quIdx;
	private String quTitle;
	private String quContents;
	private String questionId;
	private String questionDt;
	private String quAnswer;
	private int quAnswerYn;
	private String answererId;
	private String answerDt;

}