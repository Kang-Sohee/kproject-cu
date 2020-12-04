package com.ohdocha.cu.kprojectcu.controller;

import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import com.ohdocha.cu.kprojectcu.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("${server.error.path:$*error.path:/error}}")
public class DochaErrorController extends BasicErrorController{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public DochaErrorController(ErrorAttributes errorAttributes,
								ServerProperties serverProperties,
								List<ErrorViewResolver> errorViewResolvers) {
		super(errorAttributes, serverProperties.getError(), errorViewResolvers);
	}

	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status = getStatus(request);
		Map<String, Object> model = Collections
				.unmodifiableMap(getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
		response.setStatus(status.value());
		ModelAndView modelAndView = resolveErrorView(request, response, status, model);
	
		logger.debug("=========================Access Denied==============================");
		String returnUrl = "/user/login.do";

		//response.sendRedirect("/admin/login.do");

		if(!Util.isEmpty(SecurityContextHolder.getContext().getAuthentication().getPrincipal())){

			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(principal instanceof DochaUserInfoDto) {
				DochaUserInfoDto userDto = (DochaUserInfoDto) principal;

				if(!Util.isEmpty(userDto.getUserRole())){
					if("RU".equals(userDto.getUserRole())) {
						returnUrl = "/user/main.do";
					}else if("CA".equals(userDto.getUserRole())) {
						returnUrl = "/rentcar/estimatList.do";
					}
				}
			}

		}

		try {
			response.sendRedirect(returnUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			modelAndView.setViewName("user/index");
		}
		return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);
	}

	@Override
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		return super.error(request);
	}

}



