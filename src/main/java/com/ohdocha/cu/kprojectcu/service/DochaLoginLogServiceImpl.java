package com.ohdocha.cu.kprojectcu.service;


import com.ohdocha.cu.kprojectcu.domain.DochaLoginLogDto;
import com.ohdocha.cu.kprojectcu.mapper.DochaLoginLogDao;
import com.ohdocha.cu.kprojectcu.security.DochaLoginSuccessHandler;
import com.ohdocha.cu.kprojectcu.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service("LoginLog")
@Transactional
public class DochaLoginLogServiceImpl implements DochaLoginLogService {

	@Autowired
	private DochaLoginLogDao dao;
	
	private final static Logger logger = LoggerFactory.getLogger(DochaLoginSuccessHandler.class);

	@Override
	public int insertLoginLog(DochaLoginLogDto LoginlogDto) {
		// TODO Auto-generated method stub
		return dao.insertLoginLog(LoginlogDto);
	}
	
	@Override
	public int saveLoginLog(String loginType, String LoginYn, String userId, HttpServletRequest request)
	{
		
		System.out.println("saveLoginLog START =====================================");
		
		System.out.println("userId " + userId);
		
		String ip = "";
		try { 
			ip = Util.getIpAddr(request);
		} catch (Exception e1) {
			logger.error("IPADDR READ ERROR", e1);
		}
	

		DochaLoginLogDto LoginlogParam  = new DochaLoginLogDto();
		
		LoginlogParam.setUserId(userId);
		LoginlogParam.setLoginIp(ip);
		LoginlogParam.setLoginSuccess(LoginYn);
		LoginlogParam.setLoginChannel("web");
		
		
		System.out.println("로그등록 성공");

		int nRes = 1;
		nRes = insertLoginLog(LoginlogParam);

		//로그등록 실패시..?
		//user_id varchar2 pk없앰
		//로그니까 pk있으면 안될듯..
		if(nRes < 1) {
			nRes = 2;
		}
		
		System.out.println("saveLoginLog END =====================================");
		
		return nRes;
	}


}
