package com.ohdocha.cu.kprojectcu.controller;

import com.ohdocha.cu.kprojectcu.domain.DochaCommonUtilDto;
import com.ohdocha.cu.kprojectcu.domain.DochaMainDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import com.ohdocha.cu.kprojectcu.mapper.DochaCommonUtilDao;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import com.ohdocha.cu.kprojectcu.util.ServiceMessage;
import com.ohdocha.cu.kprojectcu.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Controller
@NoArgsConstructor
public class DochaMainController extends ControllerExtension {

//    @Value("${kakao.alert.talk.key}")
//    private String apiKey;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    DochaCommonUtilDao commonUtilDao;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //유저 메인페이지
    @RequestMapping(value = "/")
    public ModelAndView defaultPage(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request,
                                    Authentication authentication, HttpSession httpSession
    ) {

        DochaMap param = new DochaMap();
        param.putAll(reqParam);

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date now = new Date();
        String serverTime = format1.format(now);
        param.put("nowServerTime", serverTime);

        List<DochaMainDto> imgList = commonUtilDao.getMainImg(param);

        mv.addObject("imgList", imgList);

        mv.addObject("preParam", param);
        mv.setViewName("index");
        return mv;
    }

    //유저 메인페이지
    @RequestMapping(value = "/user/main.do")
    public ModelAndView loginSuccess(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpSession session, HttpServletRequest request,
                                     Authentication authentication) {

        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        List<DochaMainDto> imgList = commonUtilDao.getMainImg(param);

        mv.addObject("imgList", imgList);
        mv.addObject("preParam", param);
        mv.setViewName("index");
        return mv;
    }

    //유저 메인페이지 이미지 조회
    @RequestMapping(value = "/getMainImg.json")
    @ResponseBody
    public Object getMainImg(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);

        return commonUtilDao.getMainImg(param);
    }


    @RequestMapping(value = "/main/findAddess.do", method = RequestMethod.GET)
    public ModelAndView findAddess(ModelAndView mv, HttpSession session, HttpServletRequest request) {
        mv.setViewName("main/findAddress");
        return mv;
    }


    @RequestMapping(value = "/error.do", method = RequestMethod.GET)
    public void errorRedirect(ModelAndView mv, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {

        logger.debug("=========================ERROR PAGE==============================");
        String returnUrl = "/user/login.do";

        if (!StringUtil.isEmpty(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof DochaUserInfoDto) {
                DochaUserInfoDto userDto = (DochaUserInfoDto) principal;

                if (!StringUtil.isEmpty(userDto.getUserRole())) {
                    if ("RU".equals(userDto.getUserRole())) {
                        returnUrl = "/user/main.do";
                    } else if ("CA".equals(userDto.getUserRole())) {
                        returnUrl = "/rentcar/estimatList.do";
                    }
                }
            }

        }

        response.sendRedirect(returnUrl);

    }

    //유저 메인페이지
    @RequestMapping(value = "/robots.txt")
    //@ResponseBody
    public void roboot(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request,
                       HttpServletResponse response, Authentication authentication, HttpSession httpSession
    ) throws IOException {

        Resource resource = resourceLoader.getResource("classpath:robots.txt");
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8"));

            String line;

            while ((line = rd.readLine()) != null) {
                buffer.append(line);
                buffer.append("\r\n");
            }

            rd.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(buffer.toString());


        writer.flush();
        writer.close();

        //return buffer.toString();

    }


    // 차량 크기에 해당하는 차량 조회
    @RequestMapping(value = "/user/getServerTime.json")
    @ResponseBody
    public Object getServerTimeJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date nowServerTime = new Date();
        String settingTime = format1.format(nowServerTime);
        param.put("serverTime", settingTime);

        return param;
    }

    /* 공통 코드 리스트 */
    @PostMapping(value = "/api/v1.0/commonCodeInfo.json")
    @ResponseBody
    public Object userInfoDetail(@RequestBody DochaCommonUtilDto commonUtilDto, HttpServletRequest request) {
        ServiceMessage serviceMessage = createServiceMessage(request);

        List<DochaCommonUtilDto> commonUtilDtoList = commonUtilDao.selectCodeList(commonUtilDto);

        serviceMessage.addData("result", commonUtilDtoList);

        return serviceMessage.get("result");
    }

}