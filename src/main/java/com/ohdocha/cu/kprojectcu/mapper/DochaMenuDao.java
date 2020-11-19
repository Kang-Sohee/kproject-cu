package com.ohdocha.cu.kprojectcu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.ohdocha.cu.kprojectcu.domain.DochaNoticeDto;
import com.ohdocha.cu.kprojectcu.domain.DochaQuestionDto;

@Mapper
@Component
public interface DochaMenuDao {

    public List<DochaNoticeDto> getNoticeList();
    
    public List<DochaQuestionDto> getQuestionList();
    
    public void insertQuestion(DochaQuestionDto dto);

}
