package com.ohdocha.cu.kprojectcu.controller;

import com.ohdocha.cu.kprojectcu.domain.DochaQuestionDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ohdocha.cu.kprojectcu.domain.DochaCarSearchPaymentDetailDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import com.ohdocha.cu.kprojectcu.service.DochaMenuService;
import com.ohdocha.cu.kprojectcu.util.DochaMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Controller
public class DochaMenuController extends ControllerExtension{
	
	@Resource(name="menuInfo")
	private DochaMenuService service;

    @RequestMapping(value = "/event.do", method = RequestMethod.GET)
    public ModelAndView eventPage(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {


        mv.setViewName("menu/event");
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

        DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
        DochaQuestionDto dochaQuestionDto = new DochaQuestionDto();
        dochaQuestionDto.setQuIdx(loginSessionInfo.getUrIdx());


        resData.put("data", service.getQuestionList(dochaQuestionDto));

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
        if(principal == null) {
            questionId ="익명문의";
        }else {
        	if(!StringUtils.isEmpty(principal.getName())) {
//        		questionerId = principal.getName();
                questionId = loginSessionInfo.getUrIdx();
        	}else {
                questionId ="익명문의";
        	}
        }
        param.put("questionId", questionId);
        
        resData.put("data", service.insertQuestion(param));

        return resData;
    }

    @RequestMapping(value = "/question_confirm.do", method = RequestMethod.GET)
    public ModelAndView questionConfirmPage(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        mv.setViewName("/menu/question_confirm");
        return mv;
    }
}
