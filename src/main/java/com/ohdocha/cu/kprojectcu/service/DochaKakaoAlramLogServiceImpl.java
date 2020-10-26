package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaKakaoAlramLogDto;
import com.ohdocha.cu.kprojectcu.mapper.DochaKakaoAlramLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("dochaKakaoAlramLogService")
@Transactional
public class DochaKakaoAlramLogServiceImpl implements DochaKakaoAlramLogService {
	
	@Autowired
	DochaKakaoAlramLogDao dao;

	@Override
	public int insertkakaoAlramLog(DochaKakaoAlramLogDto dto) {
		// TODO Auto-generated method stub
		return dao.insertkakaoAlramLog(dto);
	}

}
