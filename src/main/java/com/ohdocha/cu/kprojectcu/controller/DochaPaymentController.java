package com.ohdocha.cu.kprojectcu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ohdocha.cu.kprojectcu.domain.DochaCarInfoDto;
import com.ohdocha.cu.kprojectcu.domain.DochaCarSearchPaymentDetailDto;
import com.ohdocha.cu.kprojectcu.service.DochaCarSearchService;
import com.ohdocha.cu.kprojectcu.service.DochaPaymentService;
import com.ohdocha.cu.kprojectcu.service.DochaRentcarService;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Controller
public class DochaPaymentController extends ControllerExtension{
	
	@Value("${import.url}")
	private String url;
	
	@Value("${import.imp_key}")
	private String impKey;
	
	@Value("${import.imp_secret}")
	private String impSecret;

    @Resource(name="dochaRentcarService")
    DochaRentcarService rentCarService;
    
    @Resource(name="carSearch")
    DochaCarSearchService carSearchService;
    
    @Resource(name="payment")
    DochaPaymentService paymentService;

    /**
     * 
     * 결제전 화면 호출
     * 
     * @param reqParam
     * @param mv
     * @param request
     * @param authentication
     * @param principal
     * @return
     */
    @RequestMapping(value = "/user/payment.do", method =  RequestMethod.POST, produces = "application/x-www-form-urlencoded")
    public ModelAndView paymentDo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);

        mv.addObject("preParam", param);

        mv.setViewName("user/estimation/payment.html");
        return mv;
    }
    
    /**
     * 
     * 결제정보 조회
     * 
     * @param reqParam
     * @param mv
     * @param request
     * @param authentication
     * @param principal
     * @return
     */
    @RequestMapping(value = "/user/paymentInfo.json")
    @ResponseBody
    public Object paymentInfo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
    	DochaMap param = new DochaMap();
        param.putAll(reqParam);
        DochaMap resData = new DochaMap();
        
        //선택한 차량의 가격을 포함한 정보를 가져옴
        DochaCarInfoDto resCarDto = carSearchService.selectTargetCar(param);
        
        //결제처리 전 금액검증을 위해 세션에 결제정보를 담음(결제 후 검증을 위해서 세션사용)
        HttpSession session = request.getSession();
        session.setAttribute("resCarDto", resCarDto);
        
        //조회정보를 return
        resData.put("resCarDto", resCarDto);
        //결제사용자정보를 return
        resData.put("user", authentication.getPrincipal());

        return resData;
    }
    
    /**
     * 
     * payment.html에서 아임포트 결제 완료 후 주문저장 및 검증시 호출되는 컨트롤러(일반결제)
     * 
     * @param reqParam
     * @param mv
     * @param request
     * @param authentication
     * @param principal
     * @return
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws Exception
     */
    @RequestMapping(value = "/user/paymentSave.json")
    @ResponseBody
    public Object paymentSave(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) throws JsonMappingException, JsonProcessingException, Exception {
    	DochaMap param = new DochaMap();
        param.putAll(reqParam);
       
        //세션에서 결제 전 불러왔던 금액정보를 가져와 검증하기 위해 파라미터 셋팅
        param.put("resCarDto", request.getSession().getAttribute("resCarDto"));
        param.put("user", authentication.getPrincipal());
        
        //주문정보 저장
        paymentService.paymentOne(param, url, impKey, impSecret);
        
        return param;
    }
    
    /**
     * 
     * payment.html에서 아임포트 결제 완료 후 주문저장 및 검증시 호출되는 컨트롤러(정기결제)
     * 
     * @param reqParam
     * @param mv
     * @param request
     * @param authentication
     * @param principal
     * @return
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws Exception
     */
    @RequestMapping(value = "/user/paymentSaveSchedule.json")
    @ResponseBody
    public Object paymentSaveSchedule(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) throws JsonMappingException, JsonProcessingException, Exception {
    	DochaMap param = new DochaMap();
        param.putAll(reqParam);
       
        //세션에서 결제 전 불러왔던 금액정보를 가져와 검증하기 위해 파라미터 셋팅
        param.put("resCarDto", request.getSession().getAttribute("resCarDto"));
        param.put("user", authentication.getPrincipal());
        
        //주문정보 저장(정기결제)
        paymentService.paymentSchedule(param, url, impKey, impSecret);
        
        return param;
    }

    @RequestMapping(value = "/user/payment/complete.do", method = RequestMethod.GET)
    public ModelAndView paymentCompleteDo(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {


        mv.setViewName("user/estimation/payment_complete_detail_day.html");
        return mv;
    }

    @RequestMapping(value = "/user/payment/completeDetail.do", method = RequestMethod.GET)
    public ModelAndView paymentCompleteDetailDo(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {


        mv.setViewName("user/estimation/payment_complete.html");
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
