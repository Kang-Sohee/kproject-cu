package com.ohdocha.cu.kprojectcu.security;

import com.ohdocha.cu.kprojectcu.util.DochaMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Order(-99)
@Configuration
public class RootFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Map<String, String[]> reqParameter = new HashMap<>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse)servletResponse;

		String reqUrl = new String(((HttpServletRequest) httpRequest).getRequestURL());

		// 출입이 resource가 아니라면
		if( !reqUrl.contains("static")) {
			if(logger.isInfoEnabled()) {
				logger.info("### dochadeal common filter log handler request url : [" + reqUrl + "]");
			}

			DochaMap paramData = new DochaMap();
			Enumeration<String> ee = httpRequest.getParameterNames();
			while(ee.hasMoreElements()) {
				String key = ee.nextElement();
				String value = httpRequest.getParameter(key);
				if(value == null) value = "";
				paramData.set(key, value);
			}
			logger.info(paramData.toString());
		}else {

		}

		try {
			filterChain.doFilter(servletRequest, servletResponse);

		} catch(Exception e) {
			logger.error("Servlet Filter init fail : " , e);
		}

	}

	@Override
	public void destroy() {

	}
}
