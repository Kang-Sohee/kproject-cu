package com.ohdocha.cu.kprojectcu.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DochaAuthenticationEntryPoint implements AuthenticationEntryPoint{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

		logger.debug("=========================Anonymous Access==============================");

		String returnUrl = "/user/login.do";
		
		response.sendRedirect(returnUrl);

	}

}
