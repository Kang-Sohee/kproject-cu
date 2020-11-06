package com.ohdocha.cu.kprojectcu.controller;

import com.ohdocha.cu.kprojectcu.domain.DochaImpLogDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import com.ohdocha.cu.kprojectcu.service.DochaImpLogService;
import com.ohdocha.cu.kprojectcu.service.DochaUserInfoService;
import com.ohdocha.cu.kprojectcu.service.MailServiceImpl;
import com.ohdocha.cu.kprojectcu.util.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Controller
public class DochaSignUpController extends ControllerExtension {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "userInfo")
    DochaUserInfoService userInfoService;

    @Autowired
    MailServiceImpl mailService;

    PasswordEncoder passwordEncoder;

    @Value("${iamport.imp_key}")
    private String imp_key;

    @Value("${iamport.imp_secret}")
    private String imp_secret;

    @Value("${iamport.getToken_url}")
    private String imp_getTokenUrl;

    @Resource(name = "impLogService")
    DochaImpLogService impLogService;


    @RequestMapping(value = "/user/signup.do", method = RequestMethod.GET)
    public ModelAndView eventPage(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        mv.setViewName("user/estimation/signup/register");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/user/signup/emailDuplicatecheck.do", method = {RequestMethod.GET, RequestMethod.POST})
    public DochaMap chkDuplicateEmail(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestParam("userId") String userId) {

        //휴대폰번호로 개인정보를 조회한다.
        //휴대폰번호는 중복일 수 없다.
        DochaMap resData = new DochaMap();
        DochaUserInfoDto dto = new DochaUserInfoDto();

        dto.setUserId(userId);

        if (userInfoService.selectUserInfoCnt(dto) > 0) {
            resData.put("res", false);
            resData.put("errCd", 3);
            resData.put("errMsg", "fail");
        } else {
            resData.put("res", true);
            resData.put("errCd", 1);
            resData.put("errMsg", "success");
        }

        return resData;
    }

    @ResponseBody
    @RequestMapping(value="/user/signup/register.do", method = {RequestMethod.GET, RequestMethod.POST})
    public  DochaMap goregister(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestBody DochaUserInfoDto dto) {

        DochaMap resData = new DochaMap();
        boolean result = false;
        int errCd = 1;
        String errMsg = null;

        if(!dto.getImp_uid().equals("") && !dto.getUserId().equals("")) //imp_uid 가 null이면 약관동의 화면으로 이동
        {

            String userId = dto.getUserId();
            String userPassword = dto.getUserPassword();

            //imp_uid는 step2에서 세션에서 받아온다.

            DochaUserInfoDto userDto = new DochaUserInfoDto();

            //imp_uid가 없으면 step1로 이동한다.
            if(!dto.getImp_uid().equals("")) {

                //String imp_uid = (String)session.getAttribute("imp_uid");

                ModelAndView mv = new ModelAndView();

                SmsAuthUtil smsAuthUtil = new SmsAuthUtil();
                String type = "singup";

                userDto = smsAuthUtil.setCertifications(request, response, dto.getImp_uid(), imp_key, imp_secret, imp_getTokenUrl, type);

                //아이디
                userDto.setUserId(dto.getUserId());
                userDto.setSocialLoginEmail(dto.getUserId());

                //비밀번호 암호화 시작---------------------------------------------------------
                SHAPasswordEncoder shaPasswordEncoder = new SHAPasswordEncoder(512);
                shaPasswordEncoder.setEncodeHashAsBase64(true);
                PasswordEncoding passwordEncoding = new PasswordEncoding(shaPasswordEncoder);
                userDto.setUserPassword(passwordEncoding.encode(userPassword));

                //user_ci컬럼으로 아이디 중복 체크
                DochaUserInfoDto paramDto = new DochaUserInfoDto();
                paramDto.setUserCi(userDto.getUserCi());

                int res = userInfoService.selectUserInfoCnt(paramDto);

                if(res > 0){
                    errCd = 3;
                    errMsg = "이름, 생년월일, 휴대폰은 중복일 수 없습니다. 로그인페이지로 이동합니다.";

                    resData.put("res", result);
                    resData.put("errCd", errCd);
                    resData.put("errMsg", errMsg);

                    return resData;
                } else {

                    DochaImpLogDto ImpParamDto = new DochaImpLogDto();

                    ImpParamDto.setImpUid(dto.getImp_uid());

                    //본인인증 성공일시 등록
                    DochaImpLogDto impdto = impLogService.selectImpLogData(ImpParamDto);
                    userDto.setUserIdentityAuthDate(impdto.getRegDt());

                    paramDto = smsAuthUtil.setCertifications(request, response, dto.getImp_uid(), imp_key, imp_secret, imp_getTokenUrl, type);


                    try {
                        userDto.setUrIdx(KeyMaker.getInsetance().getKeyDeafult("UR"));
                        errCd = userInfoService.insertUserInfo(userDto);
                    } catch (Exception e) {
                        e.printStackTrace();
                        errCd = -1;
                    }finally {

                        if(errCd == 1) {
                            errMsg = "등록성공";
                            result = true;
                        }else if(errCd == 2) {
                            errMsg = "면허정보 등록 실패";
                        }else if(errCd == 3) {
                            errMsg = "중복ID";
                        }else if(errCd == 4) {
                            errMsg = "유저등록에 실패했습니다";
                        }else if(errCd == 5) {
                            errMsg = "본인인증 미확인";
                        }else {
                            errMsg = "시스템 에러";
                        }

                        resData.put("res", result);
                        resData.put("errCd", errCd);
                        resData.put("errMsg", errMsg);

                        return resData;
                    }
                }
            } else {
                errCd = 4;
                errMsg = "유저등록에 실패했습니다.";

                resData.put("res", result);
                resData.put("errCd", errCd);
                resData.put("errMsg", errMsg);
                System.out.println("else 탔다");
                return resData;
            }

        } else {
            errCd = 5;
            errMsg = "본인인증 미확인";

            resData.put("res", result);
            resData.put("errCd", errCd);
            resData.put("errMsg", errMsg);
            System.out.println("else 탔다");
            return resData;
        }
    }//end register.do
}
