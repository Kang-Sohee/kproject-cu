package com.ohdocha.cu.kprojectcu.mapper;


import com.ohdocha.cu.kprojectcu.domain.DochaCommonUtilDto;
import com.ohdocha.cu.kprojectcu.domain.DochaMainDto;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface DochaCommonUtilDao {
//	public List<DochaSystemDto> selectUserList();
	public List<DochaCommonUtilDto> selectCommonCodeList(DochaMap param);
	
	//공통코드 리스트 출력
	public List<DochaCommonUtilDto> selectCodeList(DochaCommonUtilDto dto);

	//메인화면 이미지 리스트 조회
    List<DochaMainDto> getMainImg(DochaMap param);
}
