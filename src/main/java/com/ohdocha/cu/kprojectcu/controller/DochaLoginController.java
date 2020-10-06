package com.ohdocha.cu.kprojectcu.controller;

import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import com.ohdocha.cu.kprojectcu.security.DochaAuthenticationProvider;
import com.ohdocha.cu.kprojectcu.security.DochaLoginSuccessHandler;
import com.ohdocha.cu.kprojectcu.service.MailService;
import com.ohdocha.cu.kprojectcu.service.UserService;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import com.ohdocha.cu.kprojectcu.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class DochaLoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DochaLoginSuccessHandler DochaLoginSuccessHandler;

    @Autowired
    private DochaAuthenticationProvider provider;

    @Resource(name = "mailService")
    private MailService mailService;

    @Resource(name = "userInfo")
    UserService userInfoService;

    private final static Logger logger = LoggerFactory.getLogger(DochaLoginController.class);

    /*
     * 일반 member Login
     * */
    @RequestMapping(value = "/user/login.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView userPageLogin(@RequestParam Map<String, Object> reqParam, HttpServletRequest request, ModelAndView mv, Authentication authentication) {

        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        mv.addObject("preParam", param);
        AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
        HttpSession session = request.getSession();
        mv.addObject("kakaoUrl", "/user/social/getKakaoToken.json");
        String alreadyUserId = (String) session.getAttribute("alreadyUserId");

        if (StringUtil.isEmpty(alreadyUserId)) {
            System.out.println("alreadyUserId1 : " + alreadyUserId);

        } else {
            System.out.println("alreadyUserId2 : " + alreadyUserId);
            mv.addObject("alreadyUserId", alreadyUserId);
        }

        List<String> roleNames = new ArrayList<>();

        if (authentication != null) {
            authentication.getAuthorities().forEach(authority -> {
                roleNames.add(authority.getAuthority());
            });
            if (roleNames.size() != 0) {
                mv.setViewName("redirect:/index.html");
            } else {
                mv.setViewName("redirect:/user/login.html");
            }
        }
        return mv;
    }//end login

    /*
     * 회원사  member Login
     * */
    @RequestMapping(value = "member/login.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView memberPageLogin(@RequestParam Map<String, Object> reqParam, HttpServletRequest request, ModelAndView mv, Authentication authentication) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);

        List<String> roleNames = new ArrayList<>();

        AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
        if (authentication != null) {
            authentication.getAuthorities().forEach(authority -> {
                roleNames.add(authority.getAuthority());
            });

            if (roleNames != null) {
                mv.setViewName("redirect:/rentcar/estimatList.do");
            } else {
                HttpSession session = request.getSession();

                String alreadyUserId = (String) session.getAttribute("alreadyUserId");

                System.out.println("session test alreadyUserId : " + alreadyUserId);

                mv.setViewName("redirect:/user/login.do");
            }

        } else {
            HttpSession session = request.getSession();
            System.out.println(session.getAttribute("kakao_userId"));

            if (session.getAttribute("kakao_userId") != null) {
                mv.setViewName("redirect:/rentcar/estimatList.do");
            } else {
                mv.setViewName("rentcar/login");
            }
        }

        mv.addObject("preParam", param);
        return mv;

    }//end login

    @RequestMapping(value = "/access_denied_page")
    public String accessDeniedPage() throws Exception {
        return "/access_denied_page";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            if (auth != null) {
                DochaUserInfoDto user = (DochaUserInfoDto) auth.getPrincipal();
                new SecurityContextLogoutHandler().logout(request, response, auth);

                if ("CA".equals(user.getUserRole())) {
                    return "redirect:/member/login.do";
                }
            }
            return "redirect:/user/index.do";
        } catch (Exception e) {
            logger.error("==================== LOGOUT ERROR", e);
        } finally {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/user/index.do";

    }// logout

    /*
     * 이메일 중복체크
     */
    @RequestMapping(value = "/user/login/chkUser.do", method = {RequestMethod.GET})
    public @ResponseBody
    DochaMap join(HttpServletRequest request,
                  HttpServletResponse response,
                  @RequestParam("username") String userId) {

        DochaUserInfoDto paramDto = new DochaUserInfoDto();

        DochaMap resData = new DochaMap();

        paramDto.setUserId(userId);

        if (userInfoService.selectUserInfoCnt(paramDto) > 0) {
            //아이디 없음
            resData.put("res", true);
            resData.put("errCd", 1);
            resData.put("errMsg", "success");
        } else {
            resData.put("res", false);
            resData.put("errCd", 3);
            resData.put("errMsg", "fail");
        }
        

        return resData;
    }// end signup/duplicatecheck.do

}
