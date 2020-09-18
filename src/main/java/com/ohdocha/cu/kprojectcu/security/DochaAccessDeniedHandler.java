package com.ohdocha.cu.kprojectcu.security;

import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import com.ohdocha.cu.kprojectcu.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class DochaAccessDeniedHandler extends AccessDeniedHandlerImpl {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        /*
    	this.setErrorPage("/access_denied");
        super.handle(request, response, accessDeniedException);
        */
    	logger.debug("=========================Access Denied==============================");
    	String returnUrl = "/user/login.do";
    	
		//response.sendRedirect("/admin/login.do");
    	
    	if(!StringUtil.isEmpty(SecurityContextHolder.getContext().getAuthentication().getPrincipal())){
    		
    		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		if(principal instanceof DochaUserInfoDto) {
    			DochaUserInfoDto userDto = (DochaUserInfoDto) principal;
    			
    			if(!StringUtil.isEmpty(userDto.getUserRole())){
    				if("RU".equals(userDto.getUserRole())) {
    					returnUrl = "/user/main.do";
    				}else if("CA".equals(userDto.getUserRole())) {
    					returnUrl = "/rentcar/estimatList.do";
    				}
    			}
    		}
    		
    	}
    	
		response.sendRedirect(returnUrl);
    }
}
