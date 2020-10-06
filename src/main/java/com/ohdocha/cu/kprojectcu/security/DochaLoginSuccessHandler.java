package com.ohdocha.cu.kprojectcu.security;

import com.ohdocha.cu.kprojectcu.domain.DochaLoginLogDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserActionDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import com.ohdocha.cu.kprojectcu.mapper.DochaUserInfoDao;
import com.ohdocha.cu.kprojectcu.service.DochaLoginLogService;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import com.ohdocha.cu.kprojectcu.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DochaLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Resource(name = "LoginLog")
    DochaLoginLogService dochaLoginLogService;

    @Autowired
    private DochaUserInfoDao userInfoDao;

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private String targetUrlParameter;

    private String defaultUrl;

    private boolean useReferer;

    public String getDefaultUrl() {
        return defaultUrl;
    }

    public void setDefaultUrl(String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    public String getTargetUrlParameter() {
        return targetUrlParameter;
    }

    public void setTargetUrlParameter(String targetUrlParameter) {
        this.targetUrlParameter = targetUrlParameter;
    }


    public boolean isUseReferer() {
        return useReferer;
    }

    public void setUseReferer(boolean useReferer) {
        this.useReferer = useReferer;
    }

    private final static Logger logger = LoggerFactory.getLogger(DochaLoginSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // TODO Auto-generated method stub


        System.out.println("onAuthenticationSuccess START =====================================");
        defaultUrl = "main.do";

        targetUrlParameter = "";
        defaultUrl = "";
        useReferer = false;


        int intRedirectStrategy = decideRedirectStrategy(request, response);

        System.out.println("redirection : " + intRedirectStrategy);

        List<String> roleNames = new ArrayList<>();
        authentication.getAuthorities().forEach(authority -> {
            roleNames.add(authority.getAuthority());
        });

        switch (intRedirectStrategy) {
            case 0:
                initRoleRedirectUrl(authentication, roleNames, request, response);
                break;
            case 1:
                useTargetUrl(roleNames, request, response);
                break;
            case 2:
                useSessionUrl(roleNames, request, response);
                break;
            case 3:
                useRefererUrl(roleNames, request, response);
                break;
            default:
                useDefaultUrl(roleNames, request, response);
        }

        System.out.println("onAuthenticationSuccess END =====================================");

        return;

    }

    private void initRoleRedirectUrl(Authentication authentication, List<String> roleNames, HttpServletRequest request, HttpServletResponse response) {

        System.out.println("initRoleRedirectUrl START =====================================");

        DochaMap param = new DochaMap();


        String loginType = (String) request.getAttribute("loginType");
        String userId = "";

        DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();

        System.out.println("loginType : " + loginType);

        if (loginType != "" && loginType != null) {
            userId = (String) request.getAttribute("userId");
            loginType = "kakao";
        } else {
            userId = loginSessionInfo.getUserId();
            loginType = "web";
        }

        //LOGIN HIST 기록
        DochaUserActionDto userActionDto = new DochaUserActionDto();
        userActionDto.setUrIdx(loginSessionInfo.getUrIdx());
        userActionDto.setPageUrl("/user/login.do");
        userActionDto.setLati("0");
        userActionDto.setLongti("0");

        try {
            //LOGIN HIST 기록
            userInfoDao.insertUserActionData(userActionDto);
            //로그인은 성공한 것이므로 로그를 저장한다.
            dochaLoginLogService.saveLoginLog(loginType, "Y", userId, request);
        } catch (Exception logError) {
            logger.error("====================LOGIN LOG INSERT FAIL", logError);
        }

        System.out.println("user role 체크 : " + roleNames);
        String targetUrl = "";

        System.out.println("loginType : " + request.getAttribute("loginType"));
        System.out.println("roleNames : " + roleNames);


        if (roleNames.contains("RA")) {
            targetUrl = "/user/main.do";
        } else if (roleNames.contains("CA")) {
            targetUrl = "/rentcar/estimatList.do";
        } else {
            targetUrl = "/user/main.do";
        }

        //todo loginType 별 구분을 위해 사용..
		/* if(loginType.equals("web")){
			if(roleNames.contains("RU")) {
				targetUrl = "/user/main.do";	
			} if(roleNames.contains("CA")) {
				targetUrl = "/rentcar/estimatList.do";				
			}
		} else if(loginType.equals("kakao")){
			
			request.setAttribute("userId", userId);
			
			if(roleNames.contains("RU")) {
				targetUrl = "/user/social/main.do";
			} if(roleNames.contains("CA")) {
				targetUrl = "/rentcar/estimatelist";				
			}
		}
*/

        String reqParameter = null;

        String startDay = request.getParameter("startDay");
        String startAmPm = request.getParameter("startAmPm");
        String startTime = request.getParameter("startTime");
        String endDay = request.getParameter("endDay");
        String endAmPm = request.getParameter("endAmPm");
        String endTime = request.getParameter("endTime");
        String addr1 = request.getParameter("addr1");
        String addr2 = request.getParameter("addr2");
        String addr3 = request.getParameter("addr3");
        String carTypeList = request.getParameter("carTypeList");
        String tabIdx = request.getParameter("tabIdx");

        if (!roleNames.contains("CA")) {
			
			
			
			/*
			request.setAttribute("startDay", startDay);
			request.setAttribute("startAmPm", startAmPm);
			request.setAttribute("startTime", startTime);
			request.setAttribute("endDay", endDay);
			request.setAttribute("endAmPm", endAmPm);
			request.setAttribute("endTime",endTime );
			request.setAttribute("addr1", addr1);
			request.setAttribute("addr2", addr2);
			request.setAttribute("addr3", addr3);
			request.setAttribute("carTypeList", carTypeList);  
			request.setAttribute("tabIdx", tabIdx);  
			*/

            reqParameter = "?startDay=" + startDay + "&" +
                    "startAmPm=" + startAmPm + "&" +
                    "startTime=" + startTime + "&" +
                    "endDay=" + endDay + "&" +
                    "endAmPm=" + endAmPm + "&" +
                    "endTime=" + endTime + "&" +
                    "addr1=" + addr1 + "&" +
                    "addr2=" + addr2 + "&" +
                    "addr3=" + addr3 + "&" +
                    "carTypeList=" + carTypeList + "&" +
                    "tabIdx=" + tabIdx;

            targetUrl = targetUrl + reqParameter;
        }


        try {
            //RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetUrl);
            //requestDispatcher.forward(request, response);
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } catch (IOException e) {
            try {
                redirectStrategy.sendRedirect(request, response, "/user/main.do");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
        }


        System.out.println("initRoleRedirectUrl END =====================================");
    }

    /*
     * 권한 매칭이 되면 로그인 성공 후 Log를 저장하는 부분
     *
     * */
    private void saveLoginLog(Authentication authentication, String loginType, String userId, HttpServletRequest request) {

        System.out.println("saveLoginLog START =====================================");

        System.out.println("userId " + userId);

        String ip = "";
        try {
            ip = Util.getIpAddr(request);
        } catch (Exception e1) {
            logger.error("IPADDR READ ERROR", e1);
        }
		
		/*
		InetAddress local; 
		
		try { 
			local = InetAddress.getLocalHost(); 
			ip = local.getHostAddress(); 
			} catch (UnknownHostException e1) { 
				e1.printStackTrace(); 
		}
		*/

        DochaLoginLogDto LoginlogParam = new DochaLoginLogDto();

        LoginlogParam.setUserId(userId);
        LoginlogParam.setLoginIp(ip);
        LoginlogParam.setLoginSuccess("Y");
        LoginlogParam.setLoginChannel("web");


        System.out.println("로그등록 성공");

        int nRes = 1;
//		nRes = dochaLoginLogService.insertLoginLog(LoginlogParam);

        //로그등록 실패시..?
        //user_id varchar2 pk없앰
        //로그니까 pk있으면 안될듯..
        if (nRes < 1) {
            nRes = 2;
        }

        System.out.println("saveLoginLog END =====================================");
    }


    /*
     * 인증성공 후 어떤 url로 redirect할지 결정한다
     * return 	1 : targetUrlParameter 값을 읽은 url	(1순위)
     * 			2 : Session에 저장되어 있는 url			(2순위)
     * 			3 : referer 헤더에 있는 url				(3순위)
     * 			4 : default url						(4순위)
     */
    private int decideRedirectStrategy(HttpServletRequest request, HttpServletResponse response) {
        int result = 0;

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (!"".equals(targetUrlParameter)) {
            String targetUrl = request.getParameter(targetUrlParameter);
            if (StringUtils.hasText(targetUrl)) {
                result = 1;
            } else {
                if (savedRequest != null) {
                    result = 2;
                } else {
                    String refererUrl = request.getHeader("REFERER");
                    if (useReferer && StringUtils.hasText(refererUrl)) {
                        result = 3;
                    } else {
                        result = 4;
                    }
                }
            }

            return result;
        }

        if (savedRequest != null) {
            result = 2;
            return result;
        }

        String refererUrl = request.getHeader("REFERER");
        if (useReferer && StringUtils.hasText(refererUrl)) {
            result = 3;
        } else {
            result = 0;
        }

        return result;
    }


    // 로그인을 하는 과정에서 실패한 경우에도 세션에 관련 에러를 저장하게 되는데, 로그인에 성공하였을 시 앞에 실패했던 에러를 세션에서 삭제한다.
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    private void useTargetUrl(List<String> userRoles, HttpServletRequest request, HttpServletResponse response) {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            requestCache.removeRequest(request, response);
        }

        String targetUrl = request.getParameter(targetUrlParameter);

        try {
            redirectStrategy.sendRedirect(request, response, targetUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void useSessionUrl(List<String> userRoles, HttpServletRequest request, HttpServletResponse response) {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String targetUrl = savedRequest.getRedirectUrl();

        try {
            redirectStrategy.sendRedirect(request, response, targetUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void useRefererUrl(List<String> userRoles, HttpServletRequest request, HttpServletResponse response) {
        String targetUrl = request.getHeader("REFERER");

        try {
            redirectStrategy.sendRedirect(request, response, targetUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void useDefaultUrl(List<String> userRoles, HttpServletRequest request, HttpServletResponse response) {
        try {
            redirectStrategy.sendRedirect(request, response, defaultUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void resultRedirectStrategy(List<String> userRoles, HttpServletRequest request, HttpServletResponse response,
                                          Authentication authentication) throws IOException, ServletException {

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            redirectStrategy.sendRedirect(request, response, defaultUrl);
        }

    }

}
