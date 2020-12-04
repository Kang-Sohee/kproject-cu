package com.ohdocha.cu.kprojectcu.service;


import com.ohdocha.cu.kprojectcu.domain.DochaImpLogDto;
import com.ohdocha.cu.kprojectcu.mapper.DochaImpLogDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("impLogService")
@Transactional
public class DochaImpLogServiceImpl implements DochaImpLogService {

	@Autowired
	private DochaImpLogDao dao;

	@Override
	public int insertImpLog(DochaImpLogDto paramDto) {
		// TODO Auto-generated method stub
		return dao.insertImpLog(paramDto);
	}

	@Override
	public DochaImpLogDto selectImpLogData(DochaImpLogDto paramDto) {
		// TODO Auto-generated method stub
		return dao.selectImpLogData(paramDto);
	}
}
