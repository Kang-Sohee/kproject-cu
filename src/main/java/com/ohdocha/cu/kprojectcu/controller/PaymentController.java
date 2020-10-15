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
public class PaymentController extends ControllerExtension{

    @RequestMapping(value = "/user/payment.do", method = RequestMethod.GET)
    public ModelAndView paymentDo(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {


        mv.setViewName("user/estimation/payment.html");
        return mv;
    }

    @RequestMapping(value = "/user/payment/complete.do", method = RequestMethod.GET)
    public ModelAndView paymentCompleteDo(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {


        mv.setViewName("user/estimation/payment_complete.html");
        return mv;
    }

    @RequestMapping(value = "/user/payment/completeDetail.do", method = RequestMethod.GET)
    public ModelAndView paymentCompleteDetailDo(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {


        mv.setViewName("user/estimation/payment_complete_detail_day.html");
        return mv;
    }

    @RequestMapping(value = "/user/payment/extension.do", method = RequestMethod.GET)
    public ModelAndView paymentExtensionDo(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {


        mv.setViewName("extension_payment.html");
        return mv;
    }

    @RequestMapping(value = "/user/payment/extensionDay.do", method = RequestMethod.GET)
    public ModelAndView paymentExtensionDayDo(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {


        mv.setViewName("extension_payment_day.html");
        return mv;
    }

    @RequestMapping(value = "/user/payment/review.do", method = RequestMethod.GET)
    public ModelAndView reviewDo(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {


        mv.setViewName("review_register.html");
        return mv;
    }

    @RequestMapping(value = "/user/payment/review/photo.do", method = RequestMethod.GET)
    public ModelAndView reviewsPhotoRegisterDo(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {


        mv.setViewName("reviews_photo.html");
        return mv;
    }

    @RequestMapping(value = "/user/payment/review/register.do", method = RequestMethod.GET)
    public ModelAndView reviewRegisterDo(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {


        mv.setViewName("reviews_register2.html");
        return mv;
    }
}