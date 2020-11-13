package com.ohdocha.cu.kprojectcu.controller;


import com.ohdocha.cu.kprojectcu.domain.DochaImpLogDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import com.ohdocha.cu.kprojectcu.service.DochaImpLogService;
import com.ohdocha.cu.kprojectcu.service.DochaUserInfoService;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import com.ohdocha.cu.kprojectcu.util.KeyMaker;
import com.ohdocha.cu.kprojectcu.util.SmsAuthUtil;
import com.ohdocha.cu.kprojectcu.util.StringUtil;
import io.swagger.models.Model;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.janino.Mod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
class DochaUserController extends ControllerExtension {

    @Value("0283834654731140")
    private String imp_key;

    @Value("WIcseIQj0IHS2cxxCeWJnkNAaq8AD8hzQt25cTodB55EhCcUR8EetsPDSXew6v6XZ82c2rpDxviP7uKZ")
    private String imp_secret;

    @Value("https://api.iamport.kr/users/getToken")
    private String imp_getTokenUrl;

    @Resource(name = "userInfo")
    DochaUserInfoService userInfoService;

    @Resource(name = "impLogService")
    DochaImpLogService impLogService;

    private SmsAuthUtil smsAuthUtil;

    //mypage
    @RequestMapping(value = "/user/mypage.do", method = RequestMethod.GET)
    public ModelAndView mypage(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        authentication = SecurityContextHolder.getContext().getAuthentication();

        DochaUserInfoDto dto = (DochaUserInfoDto) authentication.getPrincipal();

        mv.addObject("socialType", dto.getUserPassword());
        mv.setViewName("user/estimation/mypage/mypage.html");
        return mv;
    }

    //회원정보
    @ResponseBody
    @RequestMapping(value = "/user/mypage/userInfo.do", method = RequestMethod.POST)
    public DochaMap userInfo(ModelAndView mv, HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication ,@RequestBody Map<String, Object> params) {

        DochaUserInfoDto dochaUserInfoDto = (DochaUserInfoDto) authentication.getPrincipal();

        DochaMap resData = new DochaMap();
        resData.put("userInfo", dochaUserInfoDto);

        return resData;
    }

    // 면허등록 & 변경 페이지
    @RequestMapping(value = "/user/license.do", method = RequestMethod.GET)
    public ModelAndView licenseDo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        mv.addObject("preParam", param);
        mv.setViewName("license_register.html");
        return mv;
    }

    //mypage - 면허정보 등록
    @ResponseBody
    @RequestMapping(value = "/user/mypage/license.do", method = {RequestMethod.GET, RequestMethod.POST})
    public DochaMap registerLicenseInfo(ModelAndView mv, HttpServletRequest request, HttpServletResponse response,
                                        @RequestBody DochaUserInfoDto dochaUserInfoDto) {
        DochaMap resData = new DochaMap();

        int res = userInfoService.insertUserLicense(dochaUserInfoDto);
        resData.put("res", res);

        return resData;
    }

    //mypage - 면허정보 수정
    @ResponseBody
    @RequestMapping(value = "/user/mypage/updateLicense.do", method = {RequestMethod.GET, RequestMethod.POST})
    public DochaMap updateLicenseInfo(ModelAndView mv, HttpServletRequest request, HttpServletResponse response,
                                      @RequestBody DochaUserInfoDto dochaUserInfoDto) {
        DochaMap resData = new DochaMap();

        int res = userInfoService.updateUserLicense(dochaUserInfoDto);
        resData.put("res", res);

        return resData;
    }

    //면허 중복 체크
    @ResponseBody
    @RequestMapping(value = "/user/mypage/chkLicense.do", method = {RequestMethod.GET, RequestMethod.POST})
    public DochaMap chkDuplicateEmail(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestParam("urIdx") String urIdx) {

        DochaMap resData = new DochaMap();
        DochaUserInfoDto dto = new DochaUserInfoDto();

        dto.setUrIdx(urIdx);

        if (userInfoService.selectLicenseCnt(dto) > 0) {
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

    //면허정보
    @ResponseBody
    @RequestMapping(value = "/user/mypage/licenseInfo.do", method = RequestMethod.POST)
    public DochaMap licenseInfo(ModelAndView mv, HttpServletRequest request, HttpServletResponse response,
                             Authentication authentication ,@RequestBody Map<String, Object> params) {

        DochaUserInfoDto dochaLicenseInfoDto = (DochaUserInfoDto) authentication.getPrincipal();

        DochaUserInfoDto licenseInfo = userInfoService.selectLicenseInfo(dochaLicenseInfoDto);

        DochaMap resData = new DochaMap();
        resData.put("licenseInfo", licenseInfo);

        return resData;
    }

    //비밀번호변경
    @RequestMapping(value = "/user/find_pw.do", method = RequestMethod.GET)
    public ModelAndView findpw(ModelAndView mv, HttpServletRequest request, Principal principal) {

        mv.setViewName("user/estimation/mypage/find_pw");
        return mv;
    }

    //아이디찾기 user/mypage/find_id.do
    @RequestMapping(value = "user/find_id.do", method = RequestMethod.GET)
    public ModelAndView findId(ModelAndView mv, HttpServletRequest request, Principal principal) {

        mv.setViewName("user/estimation/mypage/find_id");
        return mv;
    }

    /*
     * 아이디찾기 , 비밀번호찾기, 화면이동(본인인증 성공시)
     * */

    @RequestMapping(value = "/user/signup/certification.do", method = RequestMethod.GET)
    public ModelAndView certification(ModelAndView mv,
                                      @RequestParam String type) {

        mv.addObject("type", type);

        mv.setViewName("user/signup/step2");

        return mv;
    }

    /*
     * 아이디찾기 : 본인인증 후, user_contact1, user_ci로 정보 조회 후 있으면 find_id_result.do 없으면 step3.do로 이동
     * 비밀번호찾기 : 본인인증 후, user_contact1, user_ci로 정보 조회 후 있으면 find_id_result.do 없으면 new_password.do로 이동
     * */
    @RequestMapping(value = "/user/mypage/certificationSuccess.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView certificationSuccess(ModelAndView mv,
                                             HttpServletRequest request,
                                             HttpServletResponse response,
                                             @RequestParam(value = "type") String type,
                                             @RequestParam(value = "imp_uid") String imp_uid
    ) {

//      SmsAuthUtil smsAuthUtil = new SmsAuthUtil();
        String requestURL = "";
        DochaUserInfoDto paramDto = new DochaUserInfoDto();
        DochaUserInfoDto responseDto = new DochaUserInfoDto();

        paramDto = smsAuthUtil.setCertifications(request, response, imp_uid, imp_key, imp_secret, imp_getTokenUrl, type);


        HttpSession session = request.getSession();
        //회원조회결과가 없다면 회원가입 페이지로 이동
        if (userInfoService.selectUserInfoCnt(paramDto) == 0) {

            //imp Log Save
            DochaImpLogDto implogParamDto = new DochaImpLogDto();

            //ImpLogSave
            saveIMPLog(paramDto, imp_uid, "success", "");

            impLogService.insertImpLog(implogParamDto);

            mv.addObject("imp_uid", imp_uid);
            mv.setViewName("redirect:user/signup/step3.do");

        } else {
            DochaImpLogDto implogParamDto = new DochaImpLogDto();
            DochaUserInfoDto findIdParamDto = new DochaUserInfoDto();


            if ("findid".equals(type)) {
                findIdParamDto = smsAuthUtil.setCertifications(request, response, imp_uid, imp_key, imp_secret, imp_getTokenUrl, type);
                //아이디찾기는 where
                //UserCi만 업데이트 한다.

                findIdParamDto.setUrIdx(null);

                findIdParamDto.setUserZipCode(null);
                findIdParamDto.setUserAddress(null);
                findIdParamDto.setUserAddressDetail(null);


                requestURL = "redirect:find_id_result.do";
                responseDto = userInfoService.selectUserInfo(findIdParamDto);

                session.setAttribute("userId", responseDto.getUserId());
                session.setAttribute("userCi", paramDto.getUserCi());
                session.setAttribute("findid", "findid");

                paramDto = new DochaUserInfoDto();

                paramDto.setUserId(responseDto.getUserId());
                paramDto.setUserCi(responseDto.getUserCi());

                //ImpLogSave
                saveIMPLog(findIdParamDto, imp_uid, "success", "");

                //imp로그 저장일시를 구해서 CDT_USER_INFO에 UPDATE
                DochaImpLogDto implogResDto = new DochaImpLogDto();
                implogParamDto = new DochaImpLogDto();

                implogParamDto.setImpUid(imp_uid);

                implogResDto = impLogService.selectImpLogData(implogParamDto);
                paramDto.setUserIdentityAuthDate(implogResDto.getRegDt());

                try {
                    userInfoService.updateUserInfo(paramDto);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mv.setViewName(requestURL);
            } else if ("findpw".equals(type)) {
                DochaUserInfoDto findPwParamDto = new DochaUserInfoDto();
                findPwParamDto = smsAuthUtil.setCertifications(request, response, imp_uid, imp_key, imp_secret, imp_getTokenUrl, type);

                findPwParamDto.setUrIdx(null);

                requestURL = "redirect:new_password.do";
                responseDto = userInfoService.selectUserInfo(findPwParamDto);

                responseDto.setUserContact1(findPwParamDto.getUserContact1());
                responseDto.setUserCi(findPwParamDto.getUserCi());

                //ImpLogSave
                saveIMPLog(findIdParamDto, imp_uid, "success", "");

                paramDto.setUserId(responseDto.getUserId());
                paramDto.setUserCi(responseDto.getUserCi());

                session.setAttribute("userDto", responseDto);
                session.setAttribute("userId", responseDto.getUserId());
                session.setAttribute("userCi", findPwParamDto.getUserCi());
                mv.addObject("findpw", "findpw");

                //imp로그 저장일시를 구해서 CDT_USER_INFO에 UPDATE
                DochaImpLogDto implogResDto = new DochaImpLogDto();
                implogParamDto = new DochaImpLogDto();

                implogParamDto.setImpUid(imp_uid);

                implogResDto = impLogService.selectImpLogData(implogParamDto);
                paramDto.setUserIdentityAuthDate(implogResDto.getRegDt());


                mv.setViewName(requestURL);
            }
        }

        return mv;
    }//아이디, 비밀번호 찾기 종료

    // 아이디찾기 결과페이지
    @RequestMapping(value = "/user/mypage/find_id_result.do", method = RequestMethod.GET)
    public ModelAndView findaccount(ModelAndView mv, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        String userId = (String) session.getAttribute("userId");

        mv.addObject("userId", userId);

        mv.setViewName("user/mypage/find_id_result");

        return mv;
    }

    // 비밀번호입력페이지 이동
    @RequestMapping(value = "/user/mypage/new_password.do", method = RequestMethod.GET)
    public ModelAndView newpw(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();

        DochaUserInfoDto userDto = (DochaUserInfoDto) session.getAttribute("userDto");

        mv.addObject("urIdx", userDto.getUrIdx());
        mv.addObject("userId", userDto.getUserId());
        mv.addObject("userCi", userDto.getUserCi());
        mv.setViewName("user/mypage/new_password");

        return mv;
    }

    //새로운 비밀번호 Update
    @ResponseBody
    @RequestMapping(value = "/user/mypage/newPasswordSave.do", method = {RequestMethod.GET, RequestMethod.POST})
    public DochaMap newPasswordSave(ModelAndView mv, HttpServletRequest request, HttpServletResponse response,
                                    @RequestBody Map<String, Object> params) {
        mv.setViewName("user/login.do");

        HttpSession session = request.getSession();
        DochaMap resData = new DochaMap();

        String urIdx = (String) params.get("urIdx");
        String userId = (String) session.getAttribute("userId");
        String userCi = (String) session.getAttribute("userCi");
        String userPassword = (String) params.get("userPassword");

        System.out.println("userId : " + userId);
        System.out.println("userCi : " + userCi);
        System.out.println("userPassword : " + userPassword);

        DochaUserInfoDto paramDto = new DochaUserInfoDto();

        paramDto.setUserId(userId);
        paramDto.setUserCi(userCi);
        paramDto.setUserPassword(userPassword);
        paramDto.setUrIdx(urIdx);

        System.out.println("userId : " + paramDto.getUrIdx());
        System.out.println("userId : " + paramDto.getUserId());
        System.out.println("userCi : " + paramDto.getUserCi());
        System.out.println("userPassword : " + paramDto.getUserPassword());

        boolean result = false;
        int errCd = 1;

        try {
            errCd = userInfoService.updateUserInfo(paramDto);
        } catch (Exception e) {
            e.printStackTrace();
            errCd = -1;
        } finally {

            String errMsg = null;

            if (errCd == 1) {
                errMsg = "등록성공";
                result = true;
            } else if (errCd == 2) {
                errMsg = "유저정보 업데이트 실패";
            } else if (errCd == 3) {
                errMsg = "중복ID";
            } else if (errCd == 4) {
                errMsg = "유저정보 업데이트 오류";
            } else if (errCd == 5) {
                errMsg = "유저정보 업데이트 오류";
            } else {
                errMsg = "시스템 에러";
            }

            resData.put("res", result);
            resData.put("errCd", errCd);
            resData.put("errMsg", errMsg);
        }

        session.invalidate();
        return resData;
    }

    // 카드 등록
    @RequestMapping(value = "/user/card.do", method = RequestMethod.GET)
    public ModelAndView cardDo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        mv.addObject("preParam", param);
        mv.setViewName("card_register.html");
        return mv;
    }

    //mypage - 카드정보 등록
    @ResponseBody
    @RequestMapping(value = "/user/mypage/card.do", method = {RequestMethod.GET, RequestMethod.POST})
    public DochaMap registerCardInfo(ModelAndView mv, HttpServletRequest request, HttpServletResponse response,
                                     @RequestBody DochaUserInfoDto dochaUserInfoDto) {

        DochaMap resData = new DochaMap();

        int res = userInfoService.insertUserCard(dochaUserInfoDto);
        resData.put("res", res);

        return resData;
    }

    //카드정보
    @ResponseBody
    @RequestMapping(value = "/user/mypage/cardInfo.do", method = RequestMethod.POST)
    public DochaMap cardInfo(ModelAndView mv, HttpServletRequest request, HttpServletResponse response,
                             Authentication authentication ,@RequestBody Map<String, Object> params) {

        DochaUserInfoDto dochaCardInfoDto = (DochaUserInfoDto) authentication.getPrincipal();

        List<DochaUserInfoDto> dochaUserInfoDtoList = userInfoService.selectCardInfo(dochaCardInfoDto);

        DochaMap resData = new DochaMap();
        resData.put("cardInfo", dochaUserInfoDtoList);

        return resData;
    }

/*
    @RequestMapping(value="search_check.do", method=RequestMethod.POST)
    @ResponseBody
    public List<Map> searchCheck(@RequestParam(value = "valueArr[]") List<String> valueArr) throws Exception {

    }
*/

    /*
     * 아임포트 본인인증 로그 저장
     * DochaUserInfoDto paramDto : 회원 정보
     * DochaImpLogDto implogParamDto : 저장하려는 아임포트 로그
     * imp_uid : 본인인증 성공 코드
     * ImpMsg  : 아임포트 메세지(본인인증 성공이기 때문에 sucess)
     * ImpEtc  : 기타 메모
     *
     * */
    private int saveIMPLog(DochaUserInfoDto ParamDto, String imp_uid, String ImpMsg, String ImpEtc) {

        DochaImpLogDto implogParamDto = new DochaImpLogDto();
        implogParamDto.setImpIdx(KeyMaker.getInsetance().getKeyDeafult("IM"));
        implogParamDto.setImpUid(imp_uid);
        implogParamDto.setImpMsg(ImpMsg);
        implogParamDto.setUserCi(ParamDto.getUserCi());
        implogParamDto.setUserName(ParamDto.getUserName());
        implogParamDto.setUserContact1(ParamDto.getUserContact1());
        implogParamDto.setImpEtc(ImpEtc);

        return impLogService.insertImpLog(implogParamDto);
    }
}
