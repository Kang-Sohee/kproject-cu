package com.ohdocha.cu.kprojectcu.controller;

import com.ohdocha.cu.kprojectcu.domain.*;
import com.ohdocha.cu.kprojectcu.mapper.DochaCommonUtilDao;
import com.ohdocha.cu.kprojectcu.mapper.DochaMenuDao;
import com.ohdocha.cu.kprojectcu.service.DochaMenuService;
import com.ohdocha.cu.kprojectcu.util.DochaAlarmTalkMsgUtil;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import com.ohdocha.cu.kprojectcu.util.DochaTemplateCodeProvider;
import com.ohdocha.cu.kprojectcu.util.ServiceMessage;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Controller
public class DochaMenuController extends ControllerExtension {

    @Resource(name = "menuInfo")
    private DochaMenuService service;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final DochaAlarmTalkMsgUtil alarmTalk;
    private final DochaMenuService menuService;
    @Autowired
    DochaMenuDao menuDao;

    @RequestMapping(value = "/event.do", method = RequestMethod.GET)
    public ModelAndView eventPage(ModelAndView mv, HttpServletRequest request, ModelMap modelMap, Authentication authentication, Principal principal) {
        ServiceMessage serviceMessage = createServiceMessage(request);
        DochaMap param = new DochaMap();

        List<DochaEventDto> bannerImgList = menuDao.getPresentBannerEventList();
        List<DochaEventDto> presentImgList = menuDao.getPresentEventList(param);
        List<DochaEventDto> pastImgList = menuDao.getPastEventList(param);


        mv.addObject("bannerImgList", bannerImgList);
        mv.addObject("presentImgList", presentImgList);
        mv.addObject("pastImgList", pastImgList);

        mv.setViewName("menu/event");
        return mv;
    }

    @GetMapping(value = "/event/detail/{evIdx}")
    public ModelAndView eventDetail(@PathVariable int evIdx, ModelAndView mv, HttpServletRequest request, ModelMap modelMap) {
        ServiceMessage serviceMessage = createServiceMessage(request);
        serviceMessage.addData("evIdx", evIdx);

        DochaEventDto eventInfo = menuDao.getEventDetail(evIdx);

        mv.addObject("eventInfo", eventInfo);

        mv.setViewName("menu/event_detail");
        return mv;
    }

    @RequestMapping(value = "/couponBook.do", method = RequestMethod.GET)
    public ModelAndView couponBookPage(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        mv.setViewName("menu/coupon_book");
        return mv;
    }

    @RequestMapping(value = "/faq.do", method = RequestMethod.GET)
    public ModelAndView faqPage(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        mv.setViewName("menu/faq");
        return mv;
    }

    @RequestMapping(value = "/faq.json")
    @ResponseBody
    public Object faqJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        DochaMap resData = new DochaMap();

        resData.put("data", service.getFAQList());

        return resData;
    }

    @RequestMapping(value = "/notice.do", method = RequestMethod.GET)
    public ModelAndView noticePage(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        mv.setViewName("menu/notice");
        return mv;
    }

    @RequestMapping(value = "/notice.json")
    @ResponseBody
    public Object noticeJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        DochaMap resData = new DochaMap();

        resData.put("data", service.getNoticeList());

        return resData;
    }

    @RequestMapping(value = "/questionList.do", method = RequestMethod.GET)
    public ModelAndView questionListPage(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        mv.setViewName("menu/question_list");
        return mv;
    }

    @RequestMapping(value = "/questionList.json")
    @ResponseBody
    public Object questionJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        DochaMap resData = new DochaMap();

        if ( authentication != null ) {
            DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
            DochaQuestionDto dochaQuestionDto = new DochaQuestionDto();
            dochaQuestionDto.setQuIdx(loginSessionInfo.getUrIdx());

            resData.put("data", service.getQuestionList(dochaQuestionDto));
        }


        return resData;
    }

    @RequestMapping(value = "/question.do", method = RequestMethod.GET)
    public ModelAndView questionPage(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        mv.setViewName("menu/question");
        return mv;
    }

    @RequestMapping(value = "/questionCreate.json")
    @ResponseBody
    public Object questionCreateJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        DochaMap resData = new DochaMap();
        String questionId = null;

        DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
        DochaQuestionDto dochaQuestionDto = new DochaQuestionDto();

        resData.put("data", service.getQuestionList(dochaQuestionDto));
        if (principal == null) {
            questionId = "익명문의";
        } else {
            if (!StringUtils.isEmpty(principal.getName())) {
//        		questionerId = principal.getName();
                questionId = loginSessionInfo.getUrIdx();
            } else {
                questionId = "익명문의";
            }
        }
        param.put("questionId", questionId);
        param.put("questionerPhone", loginSessionInfo.getUserContact1());

        resData.put("data", service.insertQuestion(param));

        try {
            // 문의 알림톡발송
            DochaAlarmTalkDto dto = new DochaAlarmTalkDto();
            dto.setPhone(loginSessionInfo.getUserContact1());//알림톡 전송할 번호
            dto.setTemplateCode(DochaTemplateCodeProvider.A000006.getCode());

            HttpResponse<JsonNode> response = alarmTalk.sendAlramTalk(dto);
            if (response.getStatus() == 200) {
                logger.info("AlarmTalk Send Compelite");
                logger.info(response.getBody().toPrettyString());
            } else {
                logger.info("AlarmTalk Send Fail");
                logger.error(response.getBody().toPrettyString());
            }
        } catch (Exception ex) {
            //알림톡 발송을 실패하더라도 오류발생시키지 않고 결제처리 완료를 위해 오류는 catch에서 로깅처리만 함
            logger.error("Error", ex);
        }



        return resData;
    }

    @RequestMapping(value = "/question_confirm.do", method = RequestMethod.GET)
    public ModelAndView questionConfirmPage(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        mv.setViewName("/menu/question_confirm");
        return mv;
    }
}
