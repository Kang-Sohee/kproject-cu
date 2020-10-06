package com.ohdocha.cu.kprojectcu.security;


import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import com.ohdocha.cu.kprojectcu.service.DochaLoginLogService;
import com.ohdocha.cu.kprojectcu.service.UserService;
import com.ohdocha.cu.kprojectcu.util.PasswordEncoding;
import com.ohdocha.cu.kprojectcu.util.SHAPasswordEncoder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Component("authProvider")
public class DochaAuthenticationProvider implements AuthenticationProvider {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "userInfo")
    UserService dochaUserInfoService;

    @Autowired
    private CustomUserDetailsService userDeSer;

    @Autowired
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Resource(name = "LoginLog")
    private DochaLoginLogService DochaLoginLogService;

    private String defaultFailureUrl;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        System.out.println("authenticate start============================");
        System.out.println(authentication.getPrincipal());

        // todo 권환관련 처리 필요
        // 권한조회 시작 -------------------------------------------------------------
//        String userId = (String) authentication.getPrincipal();
//        String userPassword = (String) authentication.getCredentials();
        String userId = request.getParameter("username");
        String userPassword = request.getParameter("password");

        UserDetails user = userDeSer.loadUserByUsername(userId);

        // 권한조회 종료 -------------------------------------------------------------

        // 비밀번호검증파라메터셋팅------------------------------------------------------
        System.out.println("start install DochaUserInfoDto");
        DochaUserInfoDto paramDto = new DochaUserInfoDto();
        paramDto.setUserId(userId);

        DochaUserInfoDto responseDto = new DochaUserInfoDto();

        // 입력값으로 비밀번호 조회------------------------------------------------------


        //try {
        // todo 비밀번호 암호화
        SHAPasswordEncoder shaPasswordEncoder = new SHAPasswordEncoder(512);
        shaPasswordEncoder.setEncodeHashAsBase64(true);
        PasswordEncoding passwordEncoding = new PasswordEncoding(shaPasswordEncoder);
//        paramDto.setUserPassword(passwordEncoding.encode(userPassword));
        paramDto.setUserPassword(userPassword);

        int nCnt = dochaUserInfoService.selectUserInfoCnt(paramDto);

        if (nCnt > 0) {


            responseDto = dochaUserInfoService.selectUserInfo(paramDto);
        } else {

            //로그인 실패시 LOG INSERT후 Exception throw
            try {
//                dochaLoginLogService.saveLoginLog("web", "N", userId, request);
            } catch (Exception logError) {
                logger.error("====================LOGIN LOG INSERT FAIL", logError);
            }

            throw new BadCredentialsException(userId);
        }
			
	/*	} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BadCredentialsException(userId);
		}*/

        // 비밀번호가 일치하지 않을 경우 DochaLoginFailureHandler로 넘어가서 redirect 처리됨
        if (responseDto == null) {
            logger.debug("matchPassword :::::::: false!");

        }

        // 권한 없어도 BadCredentialsException처리,
/*		if (!user.isEnabled()) {
			logger.debug("isEnabled :::::::: false!");
			throw new BadCredentialsException(userId);
		}
*/
        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();

        if (responseDto != null) {
            String Role = responseDto.getUserRole();

            if (Role.equals("RA")) {
                roles.add(new SimpleGrantedAuthority("RA"));
            } else if (Role.equals("CA")) // COMPANY_ADMIN
            {
                roles.add(new SimpleGrantedAuthority("CA"));
            }
        }

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(responseDto, userPassword,
                roles);

        result.setDetails(user);


        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
        session.setMaxInactiveInterval(0);

        return new UsernamePasswordAuthenticationToken(responseDto, userPassword, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

}
