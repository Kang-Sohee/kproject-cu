package com.ohdocha.cu.kprojectcu.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@AllArgsConstructor
@Controller
public class DochaMainController extends ControllerExtension {

    @GetMapping(value = "/user/index.do")
    public String home() {

        return "redirect:/index.html";
    }
}