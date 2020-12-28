package com.ohdocha.cu.kprojectcu.mapper;

import com.ohdocha.cu.kprojectcu.domain.DochaEventDto;
import com.ohdocha.cu.kprojectcu.domain.DochaNoticeDto;
import com.ohdocha.cu.kprojectcu.domain.DochaQuestionDto;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface DochaMenuDao {

    public List<DochaNoticeDto> getNoticeList();

    public List<DochaQuestionDto> getQuestionList(DochaQuestionDto dochaQuestionDto);

    public void insertQuestion(DochaQuestionDto dto);

    public List<DochaEventDto> getPresentEventList(DochaMap dochaMap);

    public List<DochaEventDto> getPastEventList(DochaMap dochaMap);

    public DochaEventDto getEventDetail(int evIdx);

}
