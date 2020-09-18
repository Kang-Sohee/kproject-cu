package com.ohdocha.cu.kprojectcu.mapper;

import com.ohdocha.cu.kprojectcu.domain.DochaLoginLogDto;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface DochaLoginLogDao {
	
	//로그인 로그 등록
	public int insertLoginLog(DochaLoginLogDto LoginlogDto);
}
