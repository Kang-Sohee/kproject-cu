package com.ohdocha.cu.kprojectcu.mapper;

import com.ohdocha.cu.kprojectcu.domain.DochaKakaoAlramLogDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DochaKakaoAlramLogDao {
	
	public int insertkakaoAlramLog(DochaKakaoAlramLogDto dto);

}
