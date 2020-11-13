package com.ohdocha.cu.kprojectcu.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Slf4j
@AllArgsConstructor
@Controller
public class DochaMenuController extends ControllerExtension{

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

    @RequestMapping(value = "/question.do", method = RequestMethod.GET)
    public ModelAndView questionPage(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        mv.setViewName("menu/question");
        return mv;
    }

    @RequestMapping(value = "/question_confirm.do", method = RequestMethod.GET)
    public ModelAndView questionConfirmPage(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        mv.setViewName("/menu/question_confirm");
        return mv;
    }
}
