package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaQuestionDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserActionDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import com.ohdocha.cu.kprojectcu.util.DochaMap;

import java.util.List;

/**
 * <pre>
 * Statements
 * </pre>
 *
 * @version 1.0
 * @ClassName : DochaServiceNameService.java
 * @Description : 서비스 인터페이스.
 * @Modification Information
 * @see
 * @since 2019. 11. 13.
 */


public interface DochaMenuService {

	 public List<?> getNoticeList();
	 
	 public List<?> getQuestionList(DochaQuestionDto dochaQuestionDto);
	 
	 public int insertQuestion(DochaMap req);
}
