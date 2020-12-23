package com.ohdocha.cu.kprojectcu.service;

import java.security.Principal;
import java.util.List;

import com.ohdocha.cu.kprojectcu.domain.DochaEventDto;
import com.ohdocha.cu.kprojectcu.domain.DochaMainDto;
import com.ohdocha.cu.kprojectcu.util.ServiceMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ohdocha.cu.kprojectcu.domain.DochaQuestionDto;
import com.ohdocha.cu.kprojectcu.mapper.DochaMenuDao;
import com.ohdocha.cu.kprojectcu.util.DochaMap;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("menuInfo")
@Slf4j
@AllArgsConstructor
@Transactional
public class DochaMenuServiceImpl implements DochaMenuService {

    @Autowired
    private final DochaMenuDao dao;

    public List<?> getNoticeList(){
    	return dao.getNoticeList();
    }
    
    public List<?> getQuestionList(DochaQuestionDto dochaQuestionDto){

    	return dao.getQuestionList(dochaQuestionDto);
    }

	@Override
	public int insertQuestion(DochaMap req) {
		int returnInt = 0;
		
		DochaQuestionDto dto = new DochaQuestionDto();
		dto.setQuTitle(req.getString("title"));
		dto.setQuContents(req.getString("contents"));
		dto.setQuestionId(req.getString("questionId"));
		dto.setQuestionerPhone(req.getString("questionerPhone"));
		dao.insertQuestion(dto);
		
		return returnInt;
	}

}
