package com.ohdocha.cu.kprojectcu.controller;

import NiceID.Check.CPClient;
import com.ohdocha.cu.kprojectcu.config.Properties;
import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import com.ohdocha.cu.kprojectcu.service.DochaImpLogService;
import com.ohdocha.cu.kprojectcu.service.DochaUserInfoService;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import com.ohdocha.cu.kprojectcu.util.KeyMaker;
import com.ohdocha.cu.kprojectcu.util.PasswordEncoding;
import com.ohdocha.cu.kprojectcu.util.SHAPasswordEncoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Slf4j
@AllArgsConstructor
@Controller
public class DochaSignUpController extends ControllerExtension {

    //    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Properties properties;
//    private final MailServiceImpl mailService;

    @Resource(name = "userInfo")
    DochaUserInfoService userInfoService;

//    @Value("${iamport.imp_key}")
//    private String imp_key;
//
//    @Value("${iamport.imp_secret}")
//    private String imp_secret;
//
//    @Value("${iamport.getToken_url}")
//    private String imp_getTokenUrl;

    @Resource(name = "impLogService")
    DochaImpLogService impLogService;


    @RequestMapping(value = "/user/signup.do", method = RequestMethod.GET)
    public ModelAndView signUp(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        CPClient niceCheck = new CPClient();

        String sSiteCode = "BT018";            // NICE로부터 부여받은 사이트 코드
        String sSitePassword = "6mnNC6qsed8Z";        // NICE로부터 부여받은 사이트 패스워드

        String sRequestNumber = niceCheck.getRequestNO(sSiteCode);        // 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로
        // 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
        request.getSession().setAttribute("REQ_SEQ", sRequestNumber);    // 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.

        String sAuthType = "";        // 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서

        String popgubun = "N";        //Y : 취소버튼 있음 / N : 취소버튼 없음
        String customize = "";        // 없으면 기본 웹페이지 / Mobile : 모바일페이지
        String sGender = "";            // 없으면 기본 선택 값, 0 : 여자, 1 : 남자

        // CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
        // 리턴 url은 인증 전 인증페이지를 호출하기 전 url과 동일해야 합니다. ex) 인증 전 url : http://www.~ 리턴 url : http://www.~

        String sReturnUrl = properties.isDebug() ? // 성공시 이동될 URL
                "http://localhost:8080/user/signup/check/success.do" :
                "production url /user/signup/check/success.do";
        String sErrorUrl = properties.isDebug() ?
                "http://localhost:8080/user/signup/check/fail.do" :
                "http://192.168.34.103:8080/user/signup/check/fail.do";          // 실패시 이동될 URL

        // 입력될 plain 데이타를 만든다.
        String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
                "8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
                "9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
                "7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
                "7:ERR_URL" + sErrorUrl.getBytes().length + ":" + sErrorUrl +
                "11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
                "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize +
                "6:GENDER" + sGender.getBytes().length + ":" + sGender;

        String sMessage = "";
        String sEncData = "";

        int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);
        if (iReturn == 0) {
            sEncData = niceCheck.getCipherData();
        } else if (iReturn == -1) {
            sMessage = "암호화 시스템 에러입니다.";
        } else if (iReturn == -2) {
            sMessage = "암호화 처리오류입니다.";
        } else if (iReturn == -3) {
            sMessage = "암호화 데이터 오류입니다.";
        } else if (iReturn == -9) {
            sMessage = "입력 데이터 오류입니다.";
        } else {
            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
        }

        mv.addObject("sEncData", sEncData);
        mv.addObject("sMessage", sMessage);

        mv.setViewName("user/estimation/signup/register");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/user/signup/step1.do", method = {RequestMethod.GET, RequestMethod.POST})
    public DochaMap chkDuplicateEmail(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestParam("userId") String userId,
                                      @RequestParam("userPassword") String userPassword) {

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

            dto.setUserPassword(userPassword);
            request.getSession().setAttribute("SIGNUP_SESSION", dto);
            resData.put("res", true);
            resData.put("errCd", 1);
            resData.put("errMsg", "success");
        }

        return resData;
    }

    @RequestMapping(value = "/user/signup/check/success.do", method = RequestMethod.GET)
    public ModelAndView identityVerificationSuccess(ModelAndView mv, HttpServletRequest request) {

        CPClient niceCheck = new  CPClient();

        String sEncodeData = requestReplace(request.getParameter("EncodeData"), "encodeData");

        String sSiteCode = "BT018";            // NICE로부터 부여받은 사이트 코드
        String sSitePassword = "6mnNC6qsed8Z";        // NICE로부터 부여받은 사이트 패스워드

        String sCipherTime = "";			// 복호화한 시간
        String sRequestNumber = "";			// 요청 번호
        String sResponseNumber = "";		// 인증 고유번호
        String sAuthType = "";				// 인증 수단
        String sName = "";					// 성명
        String sDupInfo = "";				// 중복가입 확인값 (DI_64 byte)
        String sConnInfo = "";				// 연계정보 확인값 (CI_88 byte)
        String sBirthDate = "";				// 생년월일(YYYYMMDD)
        String sGender = "";				// 성별
        String sNationalInfo = "";			// 내/외국인정보 (개발가이드 참조)
        String sMobileNo = "";				// 휴대폰번호
        String sMobileCo = "";				// 통신사
        String sMessage = "";
        String sPlainData = "";

        int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);

        if( iReturn == 0 )
        {
            sPlainData = niceCheck.getPlainData();
            sCipherTime = niceCheck.getCipherDateTime();

            // 데이타를 추출합니다.
            java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);

            sRequestNumber  = (String)mapresult.get("REQ_SEQ");
            sResponseNumber = (String)mapresult.get("RES_SEQ");
            sAuthType		= (String)mapresult.get("AUTH_TYPE");
            sName			= (String)mapresult.get("NAME");
            //sName			= (String)mapresult.get("UTF8_NAME"); //charset utf8 사용시 주석 해제 후 사용
            sBirthDate		= (String)mapresult.get("BIRTHDATE");
            sGender			= (String)mapresult.get("GENDER");
            sNationalInfo  	= (String)mapresult.get("NATIONALINFO");
            sDupInfo		= (String)mapresult.get("DI");
            sConnInfo		= (String)mapresult.get("CI");
            sMobileNo		= (String)mapresult.get("MOBILE_NO");
            sMobileCo		= (String)mapresult.get("MOBILE_CO");

            String session_sRequestNumber = (String)request.getSession().getAttribute("REQ_SEQ");
            if(!sRequestNumber.equals(session_sRequestNumber))
            {
                sMessage = "세션값 불일치 오류입니다.";
                sResponseNumber = "";
                sAuthType = "";
            }
        }
        else if( iReturn == -1)
        {
            sMessage = "복호화 시스템 오류입니다.";
        }
        else if( iReturn == -4)
        {
            sMessage = "복호화 처리 오류입니다.";
        }
        else if( iReturn == -5)
        {
            sMessage = "복호화 해쉬 오류입니다.";
        }
        else if( iReturn == -6)
        {
            sMessage = "복호화 데이터 오류입니다.";
        }
        else if( iReturn == -9)
        {
            sMessage = "입력 데이터 오류입니다.";
        }
        else if( iReturn == -12)
        {
            sMessage = "사이트 패스워드 오류입니다.";
        }
        else
        {
            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
        }

        mv.addObject("sMessage",sMessage);
        mv.addObject("sCipherTime",sCipherTime);
        mv.addObject("sRequestNumber",sRequestNumber);
        mv.addObject("sResponseNumber",sResponseNumber);
        mv.addObject("sAuthType",sAuthType);
        mv.addObject("sName",sName);
        mv.addObject("sDupInfo",sDupInfo);
        mv.addObject("sConnInfo",sConnInfo);
        mv.addObject("sBirthDate",sBirthDate);
        mv.addObject("sGender",sGender);
        mv.addObject("sNationalInfo",sNationalInfo);
        mv.addObject("sMobileNo",sMobileNo);
        mv.addObject("sMobileCo",sMobileCo);

        mv.setViewName("user/estimation/signup/checkplus_success.html");
        return mv;
    }

    @RequestMapping(value = "/user/signup/check/fail.do", method = RequestMethod.GET)
    public ModelAndView identityVerificationFail(ModelAndView mv, HttpServletRequest request) {

        CPClient niceCheck = new CPClient();

        String sEncodeData = requestReplace(request.getParameter("EncodeData"), "encodeData");

        String sSiteCode = "BT018";            // NICE로부터 부여받은 사이트 코드
        String sSitePassword = "6mnNC6qsed8Z";        // NICE로부터 부여받은 사이트 패스워드

        String sCipherTime = "";			// 복호화한 시간
        String sRequestNumber = "";			// 요청 번호
        String sErrorCode = "";				// 인증 결과코드
        String sAuthType = "";				// 인증 수단
        String sMessage = "";
        String sPlainData = "";

        int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);

        if( iReturn == 0 )
        {
            sPlainData = niceCheck.getPlainData();
            sCipherTime = niceCheck.getCipherDateTime();

            // 데이타를 추출합니다.
            java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);

            sRequestNumber 	= (String)mapresult.get("REQ_SEQ");
            sErrorCode 		= (String)mapresult.get("ERR_CODE");
            sAuthType 		= (String)mapresult.get("AUTH_TYPE");
        }
        else if( iReturn == -1)
        {
            sMessage = "복호화 시스템 에러입니다.";
        }
        else if( iReturn == -4)
        {
            sMessage = "복호화 처리오류입니다.";
        }
        else if( iReturn == -5)
        {
            sMessage = "복호화 해쉬 오류입니다.";
        }
        else if( iReturn == -6)
        {
            sMessage = "복호화 데이터 오류입니다.";
        }
        else if( iReturn == -9)
        {
            sMessage = "입력 데이터 오류입니다.";
        }
        else if( iReturn == -12)
        {
            sMessage = "사이트 패스워드 오류입니다.";
        }
        else {
            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
        }

        mv.addObject("");
        mv.setViewName("user/estimation/signup/checkplus_fail.html");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/user/signup/register.do", method = {RequestMethod.GET, RequestMethod.POST})
    public DochaMap goregister(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestBody DochaMap dochaMap) {

        DochaMap resData = new DochaMap();
        boolean result = false;
        int errCd = 1;
        String errMsg = null;

        DochaUserInfoDto userDto = new DochaUserInfoDto();

        DochaUserInfoDto signupSession = (DochaUserInfoDto) request.getSession().getAttribute("SIGNUP_SESSION");
        //아이디
        userDto.setUserId(signupSession.getUserId());
        userDto.setSocialLoginEmail(signupSession.getUserId());

        //비밀번호 암호화 시작---------------------------------------------------------
//        SHAPasswordEncoder shaPasswordEncoder = new SHAPasswordEncoder(512);
//        shaPasswordEncoder.setEncodeHashAsBase64(true);
//        PasswordEncoding passwordEncoding = new PasswordEncoding(shaPasswordEncoder);
//        userDto.setUserPassword(passwordEncoding.encode(signupSession.getUserPassword()));

        userDto.setUserPassword(signupSession.getUserPassword());
        request.getSession().removeAttribute("SIGNUP_SESSION");

        userDto.setUrIdx(KeyMaker.getInsetance().getKeyDeafult("UR"));
        userDto.setUserRole("RU");
        userDto.setUserName(dochaMap.getString("sName"));
        userDto.setUserBirthday(dochaMap.getString("sBirthDate"));
        userDto.setUserContact1(dochaMap.getString("sMobileNo"));
        String gender = dochaMap.getString("sGender").equals("0") ? "여성" : "남성";
        userDto.setUserGender(gender);
        userDto.setUserIdentityAuthYn("Y");
        userDto.setUserNationalCode(dochaMap.getString("sNationalInfo"));

        errCd = userInfoService.insertUserInfo(userDto);

        if (errCd == 1){
            errMsg = "등록성공";
        }else {
            errMsg = "등록실패";
        }

        resData.put("errCd", errCd);
        resData.put("errMsg", errMsg);

        return resData;
    }//end register.do

    private String requestReplace(String paramValue, String gubun) {

        String result = "";

        if (paramValue != null) {

            paramValue = paramValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

            paramValue = paramValue.replaceAll("\\*", "");
            paramValue = paramValue.replaceAll("\\?", "");
            paramValue = paramValue.replaceAll("\\[", "");
            paramValue = paramValue.replaceAll("\\{", "");
            paramValue = paramValue.replaceAll("\\(", "");
            paramValue = paramValue.replaceAll("\\)", "");
            paramValue = paramValue.replaceAll("\\^", "");
            paramValue = paramValue.replaceAll("\\$", "");
            paramValue = paramValue.replaceAll("'", "");
            paramValue = paramValue.replaceAll("@", "");
            paramValue = paramValue.replaceAll("%", "");
            paramValue = paramValue.replaceAll(";", "");
            paramValue = paramValue.replaceAll(":", "");
            paramValue = paramValue.replaceAll("-", "");
            paramValue = paramValue.replaceAll("#", "");
            paramValue = paramValue.replaceAll("--", "");
            paramValue = paramValue.replaceAll("-", "");
            paramValue = paramValue.replaceAll(",", "");

            if(gubun != "encodeData"){
                paramValue = paramValue.replaceAll("\\+", "");
                paramValue = paramValue.replaceAll("/", "");
                paramValue = paramValue.replaceAll("=", "");
            }

            result = paramValue;

        }
        return result;
    }

}
