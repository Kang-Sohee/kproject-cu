package com.ohdocha.cu.kprojectcu.controller;

import com.ohdocha.cu.kprojectcu.util.DochaMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Controller
public class DochaMainController extends ControllerExtension {

    @GetMapping(value = "/")
    public String home() {

        return "redirect:/index.html";
    }
    @GetMapping(value = "/user/main.do")
    public ModelAndView loginSuccess(@RequestParam Map<String, Object> reqParam , ModelAndView mv, HttpSession session, HttpServletRequest request,
                                     Authentication authentication) {

        DochaMap param = new DochaMap();
        param.putAll(reqParam);

        mv.addObject("preParam",param);
        mv.setViewName("index");
        return mv;
    }
}