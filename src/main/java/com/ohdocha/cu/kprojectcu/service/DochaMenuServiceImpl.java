package com.ohdocha.cu.kprojectcu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ohdocha.cu.kprojectcu.mapper.DochaMenuDao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("menuInfo")
@Slf4j
@AllArgsConstructor
@Transactional
public class DochaMenuServiceImpl implements DochaMenuService {

    @Autowired
    private final DochaMenuDao dao;

    public List<?> getNoticeList(){
    	return dao.getNoticeList();
    }
  
   
}
