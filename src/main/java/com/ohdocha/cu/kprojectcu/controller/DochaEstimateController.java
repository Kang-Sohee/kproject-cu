package com.ohdocha.cu.kprojectcu.controller;


import com.ohdocha.cu.kprojectcu.domain.*;
import com.ohdocha.cu.kprojectcu.mapper.DochaUserInfoDao;
import com.ohdocha.cu.kprojectcu.service.*;
import com.ohdocha.cu.kprojectcu.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class DochaEstimateController {
	
	@Resource(name="QuoteUser")
	DochaQuoteUserService service;
	
	
	@Resource(name="userInfo")
	DochaUserInfoService userinfoService;
	
	@Resource(name="dochaRentcarService")
	DochaRentcarService rentCarService;

	@Autowired
	private DochaUserInfoDao userInfoDao;
	
//	@Resource(name="payment")
//	DochaPaymentService paymentService;
	
	@Value("${pg.mid.00}")
	private String LGD_MID;
	
	@Value("${pg.config.path}")
	private String configPath;
	
	@Value("${pg.platform}")
	private String CST_PLATFORM; 
	
	@Value("${pg.mertkey.00}")
	private String MertKey;

	@Value("${lg.receipt_link}")
	private String receipt_link;
	
	@Value("${kakao.alert.talk.key}")
	private String kakao_api_key;
	
	@Value("${kakao.alert.callback.number}")
	private String kakao_callback_number;
	
	@Value("${kakao.alert.fail.type}")
	private String kakao_fail_type;

	private SmsAuthUtil _smsAuthUtil;
	
	
	@Resource(name="dochaAlarmTalkService")
	DochaAlarmTalkService alramtalkService;
	
	
	private StringUtil _stringUtil;
	
	private Util _util;
	
	private final static Logger logger = LoggerFactory.getLogger(DochaQuoteUserServiceImpl.class);
	
	//견적요청을 하면 데이터 저장하는 부분
	@ResponseBody
	@RequestMapping(value = "/user/insertEstimate.do", method = RequestMethod.POST)
	public DochaMap oEstimate(ModelAndView mv, HttpSession session, HttpServletRequest request,
							  HttpServletResponse response,
							  Authentication authentication,
							  @RequestParam Map<String, Object> reqParam
							 ) {
		
		
		_stringUtil = new StringUtil();
		
		DochaMap resData = new DochaMap();
		boolean result = false;
		int errCd = 1;
		String errMsg = null;
		
		DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
		String userBirth = loginSessionInfo.getUserBirthday();

		Util dochaUtil = new Util();
		int setAge = dochaUtil.getAgeFromBirthDay(userBirth);

		if( setAge <= 21 ) {
			resData.put("res", result);
			resData.put("errCd", -1);
			resData.put("errMsg", "만 21세 미만은 대여요청할 수 없습니다.");
			return resData;
		}
		
		
		DochaMap param = new DochaMap();
		param.putAll(reqParam);
		
		String rentStartDay = param.getString("rentStartDay");
		String rentStartTime = param.getString("rentStartTime");
		String rentEndDay = param.getString("rentEndDay");
		String rentEndTime = param.getString("rentEndTime");
		String carTypeCodeList = param.getString("carTypeCodeList");
		String deliveryAddr = "";
		String returnAddr = "";
		String lati = "";
		String longti = "";
		String longtermYn = param.getString("longTermYn");
		String deliveryTypeCode = param.getString("deliveryTypeCode");
		//OF	지점방문
		//DL	배달대여
		if("OF".equals(deliveryTypeCode)) {
			deliveryAddr = param.getString("deliveryAddr");
		} else if("DL".equals(deliveryTypeCode)){
			deliveryAddr = param.getString("deliveryAddr");
			returnAddr = param.getString("returnAddr");
		}
		
		String CarType= "";
		
		CarType = carTypeCodeList.replace("CP", "경차").replace("SMD", "준중형").replace("MD", "중형").replace("LG", "대형").replace("VN", "승합").replace("SV", "SUV");
	
		DochaUserInfoDto paramDto = new DochaUserInfoDto();
		paramDto.setUserId(authentication.getName());
		DochaUserInfoDto userinfoDto = userinfoService.selectUserInfo(paramDto); 

		int nRes = 0;
		
		//START SETTING DC_QUOTE_USER DTO
		DochaQuoteUserDto quParamDto = new DochaQuoteUserDto();
		
		String quIdx = KeyMaker.getInsetance().getKeyDeafult("QU");
		
		quParamDto.setQuIdx(quIdx);
		quParamDto.setQuoteCode("UR");  

		quParamDto.setCarTypeCodeList(carTypeCodeList);
		quParamDto.setLongTermYn(longtermYn);
		quParamDto.setRentStartDay(rentStartDay);
		quParamDto.setRentEndDay(rentEndDay);
		
		quParamDto.setRentStartTime(rentStartTime);
		quParamDto.setRentEndTime(rentEndTime);
		quParamDto.setDeliveryTypeCode(deliveryTypeCode);  
		quParamDto.setDeliveryAddr(deliveryAddr);
		quParamDto.setReturnAddr(returnAddr);		
		quParamDto.setUrIdx(userinfoDto.getUrIdx());

		quParamDto.setQuoteStatus("QO"); 
		quParamDto.setRentStartDay(rentStartDay);
		quParamDto.setDelYn("N");

		nRes = service.insertQuoteUser(quParamDto);
		
		if(nRes < 1) {
			nRes = 2;
		}
		
		//mv.setViewName("redirect:estimate.do");

		if(errCd == 1) {
			errMsg = "등록성공 ";
			result = true;
		}else if(errCd == 2) {
			errMsg = "면허정보 등록 실패";
		}else if(errCd == 3) {
			errMsg = "중복ID";
		}else if(errCd == 4) {
			errMsg = "유저등록에 실패했습니다";
		}else if(errCd == 5) {
			
			errMsg = "본인인증 미확인";
		}else {
			errMsg = "시스템 에러"; 
		}
		
		DochaUserActionDto userActionDto = new DochaUserActionDto();
		userActionDto.setUrIdx(userinfoDto.getUrIdx());
		userActionDto.setPageUrl("/user/insertEstimate.do");
		userActionDto.setLati(lati);
		userActionDto.setLongti(longti);
		userInfoDao.insertUserActionData(userActionDto);
		
		 
		
		resData.put("res", result);
		resData.put("errCd", errCd);
		resData.put("errMsg", errMsg);

		quParamDto.setRentStartDay(rentStartDay);
		quParamDto.setRentEndDay(rentEndDay);
		
		//END SETTING DC_QUOTE_USER DTO
		
		//알림톡 셋팅 시작
		DochaAlarmTalkDto ATDto = new DochaAlarmTalkDto();
		ATDto.setCallBack("16613355");						//발신자번호
		ATDto.setReserveDate(null);							//예약일자 2019.01.01 11:33:00
		String rentDate = "";
		String returnDate = "";
		
		//대여일시==============================
		//2020-01-17 (금) 08:16 형식으로 반환한다.
		rentDate = _stringUtil.getFormatDate(rentStartDay,
				   _stringUtil.changeTimeFormat(rentStartTime) + ":00")
							  .replace(".", "-").replace(":00", "") +
				   _stringUtil.changeTimeFormat(rentStartTime);

		
		ATDto.setRentDate(rentDate);
			
		//end 대여일자=========================
		
		//반납일시=========================
		//2020-01-17 (금) 08:16 형식으로 반환한다.
		
		returnDate = _stringUtil.getFormatDate(rentEndDay,_stringUtil.changeTimeFormat(rentEndTime) + ":00")
						   		.replace(".", "-").replace(":00", "")
				   + _stringUtil.changeTimeFormat(rentEndTime);
				
		ATDto.setReturnDate(returnDate);		
		//end 반납일시=========================
		
		ATDto.setCarType(CarType);					//차종

		ATDto.setUserName(userinfoDto.getUserName());		//예약자
		ATDto.setPhone(userinfoDto.getUserContact1());		//예약자 연락처
		
		_stringUtil = new StringUtil();
		_util = new Util();
		int birthYear = 0;
		int birthMonth = 0;
		int birthDay = 0;
		int nAge = 0;
		int UserAge = 0;
		
		birthYear = Integer.parseInt( userinfoDto.getUserBirthday().substring(0,4));
		birthMonth = Integer.parseInt(userinfoDto.getUserBirthday().substring(4,6));
		birthDay = Integer.parseInt(userinfoDto.getUserBirthday().substring(6,8));
		
		nAge = _util.getAge(birthYear, birthMonth, birthDay);
		
		if(nAge != 0) {
			
			//예약자 나이 설정
			if(!_util.isEmpty(userinfoDto.getUserBirthday())) {
				
				String birthday = userinfoDto.getUserBirthday();
				SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");

				try {
					Date dateOfBirth = transFormat.parse(birthday);
					
					UserAge = _stringUtil.getAge(dateOfBirth);
					
					if(UserAge != 0) {
						ATDto.setUserAge(Integer.toString(UserAge) + " 세");
						ATDto.setUserBirthday(userinfoDto.getUserBirthday());
					}

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					ATDto.setUserAge(Integer.toString(0) + " 세");
					ATDto.setUserBirthday("0");
				}
			}else {
				ATDto.setUserAge("0");
				ATDto.setUserBirthday("0");
			}
		}
		
		ATDto.setUserBirthday(UserAge + " 세");						//생년월일-사용안함
		ATDto.setFailedType("lms");							//실패시 전송타입 - LMS
		ATDto.setFailedSubject("대여요청완료");					//LMS보낼 때 제목
		
		ATDto.setRentAddr(deliveryAddr);	
		ATDto.setReturnAddr(returnAddr);
		ATDto.setUrIdx(loginSessionInfo.getUrIdx());
		ATDto.setRtIdx(loginSessionInfo.getRtIdx());

		DochaAlarmTalkMsgUtil atMsgUtil = new DochaAlarmTalkMsgUtil(); 
		String msg = "";
		//회원발송 START===================================
		//1. 회원발송
		//OF	지점방문
		//DL	배달대여
		if("OF".equals(deliveryTypeCode)) {
			ATDto.setTemplateCode("A000011");
			ATDto.setDeliveryTypeCode("지점방문");
		} else if("DL".equals(deliveryTypeCode)){
			ATDto.setTemplateCode("A000011");
			ATDto.setDeliveryTypeCode("배달대여");
		}
		
		ATDto.setQuIdx(quParamDto.getQuIdx());
		ATDto.setDivision("U");
		ATDto.setUserContact(loginSessionInfo.getUserContact1());
		ATDto.setContact(loginSessionInfo.getUserContact1());
		
		ATDto.setBtnTypes("웹링크");
		ATDto.setBtnTxts("대여요청 상세보기");
		ATDto.setBtnUrls1(",https://www.docha.com/user/estimate.do?tab=0");//PC
		ATDto.setBtnUrls2(",https://www.docha.com/user/estimate.do?tab=0");//모바일
		
		msg = atMsgUtil.makekakoAlramTalkTemplate(ATDto);
		ATDto.setMsg(msg);

		
		logger.info(msg);

		
		alramtalkService.sendKakaoAlram(ATDto);
		//회원발송 END===================================
		
		//회원사발송 START===================================
		ATDto = new DochaAlarmTalkDto();
		ATDto.setRentDate(rentDate);
		ATDto.setReturnDate(returnDate);
		ATDto.setCarType(CarType);					//차종

		ATDto.setUserName(userinfoDto.getUserName());		//예약자
		ATDto.setPhone(userinfoDto.getUserContact1());		//예약자 연락처
		if(UserAge != 0) {
			ATDto.setUserAge(Integer.toString(UserAge) + " 세");
			ATDto.setUserBirthday(userinfoDto.getUserBirthday());
		}else {
			ATDto.setUserAge(Integer.toString(0) + " 세");
			ATDto.setUserBirthday(userinfoDto.getUserBirthday());
		}
		
		ATDto.setFailedType("lms");							//실패시 전송타입 - LMS
		ATDto.setFailedSubject("대여요청도착");					//LMS보낼 때 제목
	
		ATDto.setRentAddr(deliveryAddr);	
		ATDto.setReturnAddr(returnAddr);
		
		ATDto.setUrIdx(loginSessionInfo.getUrIdx());
		ATDto.setRtIdx(loginSessionInfo.getRtIdx());

		//템플릿설정
		//회원사 대여요청도착 A000012 → A000015 변경
		//2020.02.28 이우성
		if("OF".equals(deliveryTypeCode)) { //OF	지점방문
			ATDto.setTemplateCode("A000015");
			ATDto.setRentAddr(deliveryAddr);
			ATDto.setDeliveryTypeCode("지점방문");
		} else if("DL".equals(deliveryTypeCode)){ //DL	배달대여
			ATDto.setTemplateCode("A000015");
			ATDto.setRentAddr(deliveryAddr);
			ATDto.setDeliveryTypeCode("배달대여");
		}
		
		ATDto.setQuIdx(quParamDto.getQuIdx());
		ATDto.setDivision("S");
		ATDto.setContact(loginSessionInfo.getUserContact1());

		
		ATDto.setBtnTypes("웹링크");
		ATDto.setBtnTxts("대여요청 상세보기");
		ATDto.setBtnUrls1(",https://www.docha.com/rentcar/estimatList.do?tab=0");//PC
		ATDto.setBtnUrls2(",https://www.docha.com/rentcar/estimatList.do?tab=0");//모바일
		
		msg = atMsgUtil.makekakoAlramTalkTemplate(ATDto);
		
		logger.info(msg);
		
		ATDto.setMsg(msg);
		
		alramtalkService.sendKakaoAlram(ATDto);
		//회원사발송 END===================================

		return resData;
	}//go estimate
	

	//견적요청을 하면 데이터 저장하는 부분
	@RequestMapping(value = "/user/estimate.do", method = RequestMethod.GET)
	public ModelAndView viewEstimate(ModelAndView mv, HttpSession session, HttpServletRequest request,
                                     Authentication authentication,
                                     @RequestParam Map<String, Object> reqParam) {
	
		mv.setViewName("user/estimation/userEstimateList");

		return mv;
	}//go estimate
	
	//User 견적요청 List
	@RequestMapping(value = "/user/userEstimateList.json", method = RequestMethod.POST)
	@ResponseBody
	public DochaMap userEstimateList(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request , Authentication authentication ) {

		DochaUserInfoDto map = (DochaUserInfoDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		DochaMap paramMap = new DochaMap();
		DochaMap resData = new DochaMap();
		paramMap.set("quoteStatus", "QO"); //견적요청상태
		paramMap.set("urIdx", map.getUrIdx()); 


		resData.put("result",service.selectUserQuoteList(paramMap));

		return resData;
	}

	//user 견적요청 상세
	@RequestMapping(value = "/user/userEstDetail.do", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView userEstimateDetail(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request , Authentication authentication ) {

		DochaUserInfoDto map = (DochaUserInfoDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		DochaMap paramMap = new DochaMap();
		paramMap.set("quoteStatus", "QO"); //견적요청상태
		paramMap.set("quIdx", reqParam.get("quIdx")); //견적요청번호
		paramMap.set("urIdx", map.getUrIdx());        //유져번호
		DochaQuoteUserDto resDto = service.selectUserQuoteInfo(paramMap);

		mv.addObject("quoteInfo", resDto);
		mv.setViewName("user/estimation/userEstimateDetail");
		return mv;
	}		

	//userEstimateDetail페이지 진입시 데이터를 조회해서 javascript에서 바인딩
	@RequestMapping(value = "/user/getUserEstDetail.json", method = RequestMethod.POST)
	@ResponseBody
	public DochaMap userEstimateDetailJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request , Authentication authentication ) {

		DochaUserInfoDto map = (DochaUserInfoDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 

		DochaMap paramMap = new DochaMap();
		//paramMap.set("quoteStatus", "QO"); //견적요청상태
		paramMap.set("quoteStatus", "QP"); //견적취소상태
		paramMap.set("quIdx", reqParam.get("quIdx")); 
		paramMap.set("sort", reqParam.get("sort")); 
		paramMap.set("urIdx", map.getUrIdx()); 

		DochaMap resData = new DochaMap();
		
		List<DochaPaymentReserveDto> reserveList = service.selectQuoteUserDetail(paramMap);
		
		resData.put("result",reserveList);

		return resData;
	}

	//User 견적요청 List
	@RequestMapping(value = "/user/userPaymentList.json", method = RequestMethod.POST)
	@ResponseBody
	public DochaMap userPaymentList(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request , Authentication authentication ) {

		DochaUserInfoDto map = (DochaUserInfoDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		DochaMap paramMap = new DochaMap();
		DochaMap resData = new DochaMap();
		
		paramMap.set("reserveStatusCode", "QC"); //견적요청상태
		paramMap.set("urIdx", map.getUrIdx());
		
		List<DochaQuoteUserDto> resultList = service.selectUserQuotePaymentList(paramMap); 
		
		resData.put("resCnt", resultList.size());
		resData.put("result", resultList);

		return resData;
	}
	
	//User 견적요청 List
	@RequestMapping(value = "/user/userPaymentDetail.do", method = RequestMethod.POST)
	@ResponseBody
	public DochaMap userPaymentDetail(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request , Authentication authentication ) {

		DochaUserInfoDto map = (DochaUserInfoDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		DochaMap paramMap = new DochaMap();
		DochaMap resData = new DochaMap();
		//paramMap.set("quoteStatus", "QT"); //견적요청상태
		paramMap.set("urIdx", map.getUrIdx());

		resData.put("result", service.selectUserQuotePaymentList(paramMap));

		return resData;
	}
	
	
	//결제완료상세
//	@RequestMapping(value = "/user/estimateSuccDetail.do")
//	public ModelAndView quoteSuccDetailDo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
//		DochaMap param = new DochaMap();
//		param.putAll(reqParam);
//
//		//DochaQuoteUserInfoDto selectQuoteUserInfo = rentCarService.selectQuoteCompanyInfo(param);
//
//		param.set("quoteStatus", "QC");
//		DochaQuoteUserInfoDto selectQuoteuser = paymentService.selectQuotePaymentSuccessDetail(param);
//
//		String LGD_TID = selectQuoteuser.getApprovalNumber();
//		String LGD_MERTKEY = MertKey;
//
//
//		mv.addObject("receipt_link", receipt_link);
//		mv.addObject("LGD_MID",LGD_MID);
//
//	    StringBuffer sb = new StringBuffer();
//	    sb.append(LGD_MID);
//	    sb.append(LGD_TID);
//	    sb.append(LGD_MERTKEY);
//
//	    byte[] bNoti = sb.toString().getBytes();
//	    MessageDigest md = null;
//		try {
//			md = MessageDigest.getInstance("MD5");
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	    byte[] digest = md.digest(bNoti);
//
//	    StringBuffer strBuf = new StringBuffer();
//	    for (int i=0 ; i < digest.length ; i++) {
//	        int c = digest[i] & 0xff;
//	        if (c <= 15){
//	            strBuf.append("0");
//	        }
//	        strBuf.append(Integer.toHexString(c));
//	    }
//
//	    String  authdata = strBuf.toString();
//		mv.addObject("authdata",authdata);
//
//		mv.addObject("userQuoteInfo",selectQuoteuser);
//		param.set("crIdx" , selectQuoteuser.getCrIdx());
//		mv.addObject("carInfo",rentCarService.selectCompanyCarInfo(param));
//		mv.addObject("carOptionList",rentCarService.selectCarOptionList(param));
//
//
//		mv.setViewName("user/estimation/estimateSuccDetail");
//		return mv;
//	}
	
	//User 견적요청 List
	@RequestMapping(value = "/user/cancelQuoteUser.json", method = RequestMethod.POST)
	@ResponseBody
	public DochaMap cancelQuoteUser(@RequestParam Map<String, Object> reqParam) {
		
		int resultInt = -1;
		boolean result = false;
		String errMsg = "";
		
		try {
			
			DochaUserInfoDto userInfo = (DochaUserInfoDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			DochaMap paramMap = new DochaMap();			
			
			paramMap.set("quoteStatus", "QB"); //견적취소상태
			paramMap.set("quIdx", reqParam.get("quIdx"));
			paramMap.set("urIdx", userInfo.getUrIdx());
			
			resultInt = service.updateCancelQuoteUser(paramMap);
			
		}catch(Exception e){
			
			logger.error("===================ERROR", e);
			
			result = false;
			resultInt = -1;
			
		}finally {
			if(resultInt == 1) {
				result = true;
				errMsg = "SUCCESS";
			}else {
				result = false;
				
				if(resultInt == 2 || resultInt == 3) {
					errMsg = "견적이 존재하지 않습니다. 확인 후 다시 시도 해 주시기 바랍니다.";
				}else if(resultInt == 4){
					errMsg = "이미 취소된 견적입니다. 확인 후 다시 시도 해 주시기 바랍니다.";
				}else if(resultInt == 5) {
					errMsg = "취소에 실패했습니다. 잠시 후 다시 시도 해 주시기 바랍니다";
				}else if(resultInt == -1) {
					errMsg = "취소에 실패했습니다. 잠시 후 다시 시도 해 주시기 바랍니다";
				}
			}
		}
		
		DochaMap resData = new DochaMap();
		
		resData.put("result", result);
		resData.put("resultInt", resultInt);
		resData.put("errMsg", errMsg);

		return resData;
	}
	

}
	


