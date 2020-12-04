package com.ohdocha.cu.kprojectcu.service;



import com.ohdocha.cu.kprojectcu.domain.DochaCommonUtilDto;
import com.ohdocha.cu.kprojectcu.mapper.DochaCommonUtilDao;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service("commonutil")
@Transactional
public class DochaCommonUtilServiceImpl implements DochaCommonUtilService {

	@Autowired
	private DochaCommonUtilDao dao;

	@Override
	public List<DochaCommonUtilDto> selectCommonCodeList(DochaMap param) throws Exception {
		// TODO Auto-generated method stub
		return dao.selectCommonCodeList(param);
	}

	@Override
	public List<DochaCommonUtilDto> selectCodeList(DochaCommonUtilDto dto) throws Exception {
		 
		return  dao.selectCodeList(dto);
	}


}
