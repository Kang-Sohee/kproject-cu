package com.ohdocha.cu.kprojectcu.security;

import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class DochaLogoutSuccessHandler implements LogoutSuccessHandler{
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                             Authentication authentication) throws IOException, ServletException {
    	
    	
    	logger.info("DochaLogoutSuccessHandler Start================================================");
    	
    	String referer = request.getHeader("referer");
    	
    	
    	logger.info("referer addresss ===" + referer);
    	
        if (authentication != null && authentication.getDetails() != null) {
            try {
                 request.getSession().invalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } 
        response.setStatus(HttpServletResponse.SC_OK);
        
        if(referer.contains("/user/")) {
        	response.sendRedirect("/user/login.do?logout");	
        } else if(referer.contains("/member/")) {
        	response.sendRedirect("/member/login.do?logout");
        }
    }
}