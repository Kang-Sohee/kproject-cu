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
public class DochaSignUpController extends ControllerExtension{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name="userInfo")
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

    @Resource(name="impLogService")
    DochaImpLogService impLogService;


    // todo 임시 회원가입 페이지. 추후 로직에 따라 페이지 작성
    @RequestMapping(value = "/user/signup.do", method = RequestMethod.GET)
    public ModelAndView eventPage(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        mv.setViewName("user/estimation/signup/register");
        return mv;
    }

    //회원가입 step1 본인인증 약관 동의
    @RequestMapping(value="/user/signup/step1.do")
    public ModelAndView step1(ModelAndView mv)
    {
        mv.addObject("type", "signup");
        mv.setViewName("user/signup/step1");
        return mv;
    }

    //회원가입 step1 에서 실제 본인인증 페이지로 이동
    @RequestMapping(value="/user/signup/term.do")
    public ModelAndView terms(ModelAndView mv, HttpServletRequest request, HttpServletResponse response)
    {
        mv.setViewName("user/signup/step2");
        return mv;
    }

    /*
     * 회원가입 → 본인인증
     * 이미 있으면 로그인페이지(login.do)
     * 없으면 정보입력페이지(step3.do)
     *
     * */
    @RequestMapping(value="/user/certifications/confirm.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView certificationsConfirm(@RequestParam("imp_uid") String imp_uid,
                                              @RequestParam("type") String type,
                                              ModelAndView mv,HttpServletRequest request,HttpServletResponse response, HttpSession session) {

        /*
         * 1. 회원가입→ 본인인증 → 회원이 있으면 로그인페이지로
         * */
        DochaUserInfoDto paramDto = new DochaUserInfoDto();
        SmsAuthUtil smsAuthUtil = new SmsAuthUtil();
        paramDto = smsAuthUtil.setCertifications(request, response, imp_uid, imp_key, imp_secret, imp_getTokenUrl, type);

        if("signup".equals(type)) {
            if(userInfoService.selectUserInfoCnt(paramDto)  == 0) {

                DochaUserInfoDto responseDto = new DochaUserInfoDto();
                //imp Log Save
                DochaImpLogDto implogParamDto = new DochaImpLogDto();

                responseDto = userInfoService.selectUserInfo(paramDto);


                mv.addObject("imp_uid", imp_uid);
                mv.setViewName("redirect:/user/signup/step3.do");

            }else {
                paramDto = smsAuthUtil.setCertifications(request, response, imp_uid, imp_key, imp_secret, imp_getTokenUrl, type);

                //imp Log Save
                DochaImpLogDto implogParamDto = new DochaImpLogDto();

                implogParamDto.setImpIdx(KeyMaker.getInsetance().getKeyDeafult("IM"));
                implogParamDto.setImpUid(imp_uid);
                implogParamDto.setImpMsg("success");
                implogParamDto.setUserCi(paramDto.getUserCi());
                implogParamDto.setUserName(paramDto.getUserName());
                implogParamDto.setUserContact1(paramDto.getUserContact1());
                implogParamDto.setImpEtc("");

                impLogService.insertImpLog(implogParamDto);

                mv.addObject("alreadyUserId", "true");
                mv.setViewName("redirect:/user/login.do");
            }
        }

        return mv;
    }

    /*
     * 이메일, 비밀번호 정보 입력(회원가입 완료)
     * iamport에서 본인 인증 후 정보를 입력받아 회원가입절차를 마무리한다.
     * https://docs.iamport.kr/tech/mobile-authentication#send-imp-uid
     *
     * */
    @RequestMapping(value="/user/signup/step3.do")
    public ModelAndView goRegister(ModelAndView mv, @RequestParam("imp_uid") String imp_uid) {
        mv.addObject("imp_uid", imp_uid);
        mv.setViewName("/user/signup/step3");

        return mv;
    }

    /*
     * 이메일 중복체크
     */

    @RequestMapping(value="/user/signup/duplicatecheck.do", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    DochaMap join(HttpServletRequest request,
                  HttpServletResponse response,
                  @RequestParam("imp_uid") String imp_uid) {

        SmsAuthUtil smsAuthUtil = new SmsAuthUtil();
        DochaUserInfoDto paramDto = new DochaUserInfoDto();

        String type = "singup";

        paramDto = smsAuthUtil.setCertifications(request, response, imp_uid, imp_key, imp_secret, imp_getTokenUrl, type);

        //imp Log Save
        DochaImpLogDto implogParamDto = new DochaImpLogDto();

        implogParamDto.setImpIdx(KeyMaker.getInsetance().getKeyDeafult("IM"));
        implogParamDto.setImpUid(imp_uid);
        implogParamDto.setImpMsg("success");
        implogParamDto.setUserCi(paramDto.getUserCi());
        implogParamDto.setUserName(paramDto.getUserName());
        implogParamDto.setUserContact1(paramDto.getUserContact1());
        implogParamDto.setImpEtc("");

        impLogService.insertImpLog(implogParamDto);

        //휴대폰번호로 개인정보를 조회한다.
        //휴대폰번호는 중복일 수 없다.
        DochaMap resData = new DochaMap();

        paramDto.setUserCi(null);

        if(userInfoService.selectUserInfoCnt(paramDto) > 0) {
            resData.put("res", false);
            resData.put("errCd", 3);
            resData.put("errMsg", "fail");
        } else {
            resData.put("res", true);
            resData.put("errCd", 1);
            resData.put("errMsg", "success");
        }



        return resData;
    }// end signup/duplicatecheck.do

    @ResponseBody
    @RequestMapping(value="/user/signup/emailDuplicatecheck.do", method = {RequestMethod.GET, RequestMethod.POST})
    public DochaMap chkDuplicateEmail(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestParam("userId") String userId) {

        //휴대폰번호로 개인정보를 조회한다.
        //휴대폰번호는 중복일 수 없다.
        DochaMap resData = new DochaMap();
        DochaUserInfoDto dto = new DochaUserInfoDto();

        dto.setUserId(userId);

        if(userInfoService.selectUserInfoCnt(dto) > 0) {
            resData.put("res", false);
            resData.put("errCd", 3);
            resData.put("errMsg", "fail");
        } else {
            resData.put("res", true);
            resData.put("errCd", 1);
            resData.put("errMsg", "success");
        }



        return resData;
    }// end signup/duplicatecheck.do

    /*
     * 실제 사용자정보 저장부분을 담당한다.
     * 처리로직
     * 로그인화면 → 회원가입버튼 클릭 → 본인인증 → 완료하면 → 이메일, 비밀번호 입력 → 성별, 이메일, 이름, 휴대폰번호, 생년월일 저장
     */
    @ResponseBody
    @RequestMapping(value="/user/signup/register.do", method = {RequestMethod.GET, RequestMethod.POST})
    public  DochaMap goregiste(HttpServletRequest request,
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
