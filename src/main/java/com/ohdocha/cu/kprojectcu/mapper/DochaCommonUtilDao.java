package com.ohdocha.cu.kprojectcu.mapper;


import com.ohdocha.cu.kprojectcu.domain.DochaCommonUtilDto;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DochaCommonUtilDao {
//	public List<DochaSystemDto> selectUserList();
	public List<DochaCommonUtilDto> selectCommonCodeList(DochaMap param);
	
	//공통코드 리스트 출력
	public List<DochaCommonUtilDto> selectCodeList(DochaCommonUtilDto dto);
	
}
