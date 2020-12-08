package com.ohdocha.cu.kprojectcu.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Slf4j
@Controller
public class DochaTermsController {

    @RequestMapping(value = "/terms1.do", method = RequestMethod.GET)
    public ModelAndView terms1(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        mv.setViewName("user/estimation/signup/terms/text_terms1");
        return mv;
    }

    @RequestMapping(value = "/terms2.do", method = RequestMethod.GET)
    public ModelAndView terms2(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        mv.setViewName("user/estimation/signup/terms/text_terms2");
        return mv;
    }

    @RequestMapping(value = "/terms3.do", method = RequestMethod.GET)
    public ModelAndView terms3(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        mv.setViewName("user/estimation/signup/terms/text_terms3");
        return mv;
    }

    @RequestMapping(value = "/terms4.do", method = RequestMethod.GET)
    public ModelAndView terms4(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        mv.setViewName("user/estimation/signup/terms/text_terms4");
        return mv;
    }

}
