package com.ohdocha.cu.kprojectcu.mapper;

import com.ohdocha.cu.kprojectcu.domain.DochaImpLogDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DochaImpLogDao {
	
	public int insertImpLog(DochaImpLogDto paramDto);

	public DochaImpLogDto selectImpLogData(DochaImpLogDto paramDto);
}
