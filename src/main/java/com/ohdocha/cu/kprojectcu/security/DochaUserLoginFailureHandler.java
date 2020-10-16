package com.ohdocha.cu.kprojectcu.security;

import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/*
 * 실패핸들러에서
 *
 * */
@Component
public class DochaUserLoginFailureHandler implements AuthenticationFailureHandler {
    private String username;
    private String userPassword;
    private String defaultFailureUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        System.out.println("******************** onAuthenticationFailure ********************");

        String referer = request.getHeader("referer");

        String userId = request.getParameter(username);
        String password = request.getParameter(userPassword);
        String errormsg = null;

        request.setAttribute("userId", userId);
        request.setAttribute("userPassword", password);

        HttpSession session = request.getSession();

        if (exception instanceof BadCredentialsException) {
            //request.setAttribute("ERRORMSG_ID", "아이디나 비밀번호가 맞지 않습니다. 다시 확인해주세요");

            if (referer.contains("user")) {
                defaultFailureUrl = "/user/login.do?err_code=1";
            } else if (referer.contains("member")) {
                defaultFailureUrl = "/member/login.do?err_code=1";
            }

        } else if (exception instanceof UsernameNotFoundException) {
            //request.setAttribute("ERRORMSG_ID", "접속자 정보를 찾을 수 없습니다");
            if (referer.contains("user")) {
                defaultFailureUrl = "/user/login.do?err_code=2";
            } else if (referer.contains("member")) {
                defaultFailureUrl = "/user/login.do?err_code=2";
            }
            //↓ todo. 에러처리 추가해야할 리스트들
        } else if (exception instanceof InternalAuthenticationServiceException) {
            //errormsg = MessageUtils.getMessage("error.BadCredentials");
        } else if (exception instanceof DisabledException) {
            //errormsg = MessageUtils.getMessage("error.Disaled");
        } else if (exception instanceof CredentialsExpiredException) {
            //errormsg = MessageUtils.getMessage("error.CredentialsExpired");
        }
        response.sendRedirect(defaultFailureUrl);
    }


    public String getDefaultFailureUrl() {
        return defaultFailureUrl;
    }

    public void setDefaultFailureUrl(String defaultFailureUrl) {
        this.defaultFailureUrl = defaultFailureUrl;
    }

}
