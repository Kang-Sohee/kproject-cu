package com.ohdocha.cu.kprojectcu.controller;



import com.ohdocha.cu.kprojectcu.domain.*;
import com.ohdocha.cu.kprojectcu.service.DochaAlarmTalkService;
import com.ohdocha.cu.kprojectcu.service.DochaCommonUtilService;
import com.ohdocha.cu.kprojectcu.service.DochaQuoteUserService;
import com.ohdocha.cu.kprojectcu.service.DochaRentcarService;
import com.ohdocha.cu.kprojectcu.util.DochaAlarmTalkMsgUtil;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import com.ohdocha.cu.kprojectcu.util.DochaTemplateCodeProvider;
import com.ohdocha.cu.kprojectcu.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;


@Controller
public class DochaRentcarController {
	
	
	@Resource(name="dochaRentcarService")
	DochaRentcarService service;
	
	@Resource(name="commonutil")
	DochaCommonUtilService commonService;
	
	private StringUtil _stringUtil;
	
	@Resource(name="dochaAlarmTalkService")
	DochaAlarmTalkService alramtalkService;
	
	@Value("${pg.mid.00}")
	private String LGD_MID;
	
	
	@Value("${pg.mertkey.00}")
	private String MertKey;
	
	@Value("${lg.receipt_link}")
	private String receipt_link;
	

	
	@Resource(name="QuoteUser")
	DochaQuoteUserService quoteUserService;
	
	private final static Logger logger = LoggerFactory.getLogger(DochaRentcarController.class);
	
	
	//예약리스트
	@RequestMapping(value = "/rentcar/estimatList.do")
	public ModelAndView estimatelist(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
		DochaMap param = new DochaMap();
		param.putAll(reqParam);
		if(!StringUtil.isEmpty(reqParam.get("listValue"))){
			mv.addObject("listValue", reqParam.get("listValue"));
		}else {
			mv.addObject("listValue", "");
		}
		
		DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
		String rtIdx = loginSessionInfo.getRtIdx();
		param.set("rtIdx", rtIdx);
		mv.addObject("rentCompanyInfo",service.selectRentCompanyInfo(param));
		mv.addObject("preParam",param);
		mv.setViewName("rentcar/estimatelist");
		return mv;
	}	
	
	
	//결제대기상세
	@RequestMapping(value = "/rentcar/quoteWaitDetail.do")
	public ModelAndView quoteWaitDetailDo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
		DochaMap param = new DochaMap();
		param.putAll(reqParam);

		DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
		param.set("rtIdx" , loginSessionInfo.getRtIdx());
		
		
		
		DochaQuoteUserInfoDto selectQuoteUserInfo = service.selectQuoteCompanyInfo(param);
		mv.addObject("userQuoteInfo",selectQuoteUserInfo);
		param.set("crIdx" , selectQuoteUserInfo.getCrIdx());
		//mv.addObject("carInfo",service.selectCompanyCarInfo(param));
		mv.addObject("carOptionList",service.selectCarOptionList(param));
		mv.addObject("preParam",param);
		
		mv.setViewName("rentcar/quoteWaitDetail");
		return mv;
	}	
	
	//결제완료
	@RequestMapping(value = "/rentcar/quoteSuccDetail.do")
	public ModelAndView quoteSuccDetailDo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
		DochaMap param = new DochaMap();
		param.putAll(reqParam);
		
		DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
		param.set("rtIdx" , loginSessionInfo.getRtIdx());
		
		DochaQuoteUserInfoDto selectQuoteUserInfo = service.selectQuoteCompanyInfo(param);
		
		mv.addObject("userQuoteInfo",selectQuoteUserInfo);
		param.set("crIdx" , selectQuoteUserInfo.getCrIdx());
		//mv.addObject("carInfo",service.selectCompanyCarInfo(param));
		mv.addObject("carOptionList",service.selectCarOptionList(param));
		mv.addObject("preParam",param);

		
		
		String LGD_TID = selectQuoteUserInfo.getApprovalNumber();
	    String LGD_MERTKEY = MertKey;
	    
		StringBuffer sb = new StringBuffer();
	    sb.append(LGD_MID);
	    sb.append(LGD_TID);
	    sb.append(LGD_MERTKEY);
 

	    byte[] bNoti = sb.toString().getBytes();
	    MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    byte[] digest = md.digest(bNoti);

	    StringBuffer strBuf = new StringBuffer();
	    for (int i=0 ; i < digest.length ; i++) {
	        int c = digest[i] & 0xff;
	        if (c <= 15){
	            strBuf.append("0");
	        }
	        strBuf.append(Integer.toHexString(c));
	    }
	    
	    String  authdata = strBuf.toString();
	    
		mv.addObject("authdata",authdata);
		mv.addObject("receipt_link", receipt_link);
		mv.addObject("LGD_MID",LGD_MID);
		
		mv.setViewName("rentcar/quoteSuccDetail");
		return mv;
	}	
	

	//예약상세
	@RequestMapping(value = "/rentcar/estimatedetail.do")
	public ModelAndView estimatDetail(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
		DochaMap param = new DochaMap();
		param.putAll(reqParam);
		DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
		
		mv.addObject("userQuoteInfo",service.selectQuoteUserInfo(param));
		mv.addObject("preParam",param);
		param.set("rtIdx",loginSessionInfo.getRtIdx());
		mv.addObject("rentCompanyCarTypeList" , service.selectTargetCarType(param));
		mv.setViewName("rentcar/estimatedetail");
		
		return mv;

	}	

	//금액입력
	@RequestMapping(value = "/rentcar/inputPayment.do")
	public ModelAndView inputPayment(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
		DochaMap param = new DochaMap();
		param.putAll(reqParam);
		

		
		
		DochaQuoteUserInfoDto quoteUserlist =  service.selectQuoteUserInfo(param);
		
		DochaRentCompanyCarDto carInfo = service.selectCompanyCarInfo(param);
		
		

		
		mv.addObject("userQuoteInfo",quoteUserlist);
		mv.addObject("carInfo",carInfo); 
		mv.addObject("preParam",param);
		mv.setViewName("rentcar/inputpayment");
		return mv;
	}	
	
	//예약확정 - 고객이결제완료 후
	@RequestMapping(value = "/rentcar/reservationConfirm.do")
	public ModelAndView reservationConfirm(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
		DochaMap param = new DochaMap();
		param.putAll(reqParam);
		mv.setViewName("rentcar/reservationconfirm");
		return mv;
	}		
	
	
	//예약확정상세
	@RequestMapping(value = "/rentcar/reservationConfirmDetail.do")
	public ModelAndView reservationConfirmDetail(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
		DochaMap param = new DochaMap();
		param.putAll(reqParam);
		mv.setViewName("rentcar/reservationconfirmdetail");
		return mv;
	}		
	
	
	// 예약리스트 유저요청 polling
	@RequestMapping(value="/rentcar/quoteUserInfo.json")
	@ResponseBody
	public Object quoteUserInfoJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request , Authentication authentication ) {
		
		DochaMap param = new DochaMap();
		param.putAll(reqParam);
		DochaMap resData = new DochaMap();
		DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
		param.set("rtIdx" , loginSessionInfo.getRtIdx());
		
		resData.put("result",service.selectQuoteUserList(param));
		
		return resData;
	}
	
	// 제휴사가 보낸 견적리스트
	@RequestMapping(value="/rentcar/quoteCompanyInfo.json")
	@ResponseBody
	public Object quoteCompanyInfoJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request , Authentication authentication ) {
		DochaMap param = new DochaMap();
		param.putAll(reqParam);
		DochaMap resData = new DochaMap();
		
		DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
		param.set("rtIdx" , loginSessionInfo.getRtIdx());
		/*
		 *    상태코드 
		 *    P_CODE : QTC
		 *    QO     : 견적요청
		 *    QP     : 결제요청
		 *    QC     : 결제완료
		 *    QB     : 견적취소
		 *    
		 *    parameter : status
		 * */
		
		resData.put("result",service.selectQuoteCompanyList(param));
		
		return resData;
	}
	
	// 차량 크기 목록 조회
	@RequestMapping(value="/rentcar/selectTargetCarTypeList.json")
	@ResponseBody
	public Object selectTargetCarTypeListJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request , Authentication authentication ) {
		DochaMap param = new DochaMap();
		param.putAll(reqParam);
		DochaMap resData = new DochaMap();
		
		/*
		 *    차량크기 코드 
		 *    P_CODE : CTY
		 *    
		 *    	CP	경차
				SM	소형
				SMD	준중형
				MD	중형
				SLG	준대형
				LG	대형
				SSV	소형SUV
				SV	SUV
				VN	승합차
				SP	스포츠카
				IP	수입차
		 *    
		 *      parameter : rtIdx
		 * */
		try {
			resData.put("carTypeCodeList",commonService.selectCommonCodeList(param));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resData;
	}
	
	// 차량 크기에 해당하는 차량 조회
	@RequestMapping(value="/rentcar/selectCarModelList.json")
	@ResponseBody
	public Object selectCarModelListJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request , Authentication authentication ) {
		DochaMap param = new DochaMap();
		param.putAll(reqParam);
		DochaMap resData = new DochaMap();
		
		DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
		param.set("rtIdx",loginSessionInfo.getRtIdx());
		/*
		 *    차량크기 코드 
		 *    P_CODE : CTY
		 *    
		 *    	CP	경차
				SM	소형
				SMD	준중형
				MD	중형
				SLG	준대형
				LG	대형
				SSV	소형SUV
				SV	SUV
				VN	승합차
				SP	스포츠카
				IP	수입차
		 *    
		 *      parameter : rtIdx
		 * */
		resData.put("rentCompanyCarModelList",service.selectCarModelList(param));
		
		return resData;
	}
	
	// 차량 크기에 해당하는 차량 조회
	@RequestMapping(value="/rentcar/selectTargetCarList.json")
	@ResponseBody
	public Object selectTargetCarListJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request , Authentication authentication ) {
		DochaMap param = new DochaMap();
		param.putAll(reqParam);
		DochaMap resData = new DochaMap();
		
		DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
		param.set("rtIdx",loginSessionInfo.getRtIdx());
		/*
		 *    차량크기 코드 
		 *    P_CODE : CTY
		 *    
		 *    	CP	경차
				SM	소형
				SMD	준중형
				MD	중형
				SLG	준대형
				LG	대형
				SSV	소형SUV
				SV	SUV
				VN	승합차
				SP	스포츠카
				IP	수입차
		 *    
		 *      parameter : rtIdx
		 * */
		resData.put("targetCarList",service.selectCompanyCarList(param) );
		
		return resData;
	}

	// 회원사 고객사에게 결제요청
	@RequestMapping(value="/rentcar/insertQuoteRentCompanyInfo.json")
	@ResponseBody
	public Object insertQuoteRentCompanyInfoJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request , Authentication authentication ) {
		DochaMap param = new DochaMap();
		param.putAll(reqParam);
		DochaMap resData = new DochaMap();
		int resultCnt = 0;
		
		DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
		param.set("rtIdx",loginSessionInfo.getRtIdx());
		
		resultCnt += service.insertQuoteRentCompanyInfo(param);
		

		DochaQuoteUserInfoDto quoteUserlist =  service.selectQuoteUserInfo(param);
 
		//차량정보
		DochaRentCompanyCarDto carInfo = service.selectCompanyCarInfo(param);
		
		//보험료정보
		DochaCarInsuranceDto carInsuranceInfo = service.selectCarInsuranceInfo(param);

		//START 고객 알림톡 발송============================================================
		DochaAlarmTalkDto ATDto = new DochaAlarmTalkDto();
		DochaAlarmTalkMsgUtil atMsgUtil = new DochaAlarmTalkMsgUtil();
		
		ATDto.setUrIdx(quoteUserlist.getUrIdx());
		ATDto.setRtIdx(loginSessionInfo.getRtIdx());
		
		ATDto.setCallBack("16613355");						//발신자번호
		ATDto.setPhone(quoteUserlist.getUserContact1());
		ATDto.setContact(quoteUserlist.getUserContact1());
		ATDto.setDivision("U");
		ATDto.setQuIdx(quoteUserlist.getQuIdx());
		ATDto.setRmIdx(loginSessionInfo.getRtIdx());
		
		ATDto.setTemplateCode(DochaTemplateCodeProvider.A000013.name());
		String msg = atMsgUtil.makekakoAlramTalkTemplate(ATDto);
		ATDto.setMsg(msg);
		
		
		ATDto.setBtnTypes("웹링크");
		ATDto.setBtnTxts("확인하기");
		ATDto.setBtnUrls1(",https://www.docha.com/rentcar/estimatList.do?tab=0");//PC
		ATDto.setBtnUrls2(",https://www.docha.com/rentcar/estimatList.do?tab=0");//모바일
		
		alramtalkService.sendKakaoAlram(ATDto);
		//END 고객 알림톡 발송============================================================
		
		//START 운영직원 알림톡 발송=======================================================
		
		_stringUtil = new StringUtil();
		//회원사 정보조회
		List<DochaRentCompanyDto>  companyInfoListDto = service.selectRentCompanyListAllForPaymentComP(param);
		
		//회원사 정보 셋팅
		for(DochaRentCompanyDto companyDto : companyInfoListDto){
			if(companyDto.getCompanyContact1() != null) {
			
				ATDto.setCompanyName(companyDto.getCompanyName()); //회원사
				ATDto.setCompanyContact(_stringUtil.phoneRegx(companyDto.getCompanyContact1())); //회원사연락처
				
				ATDto.setContact(companyDto.getStaffContact1());
				ATDto.setDivision("s");
				ATDto.setQuIdx(quoteUserlist.getQuIdx());
				ATDto.setRmIdx(loginSessionInfo.getRtIdx());
				
				//위치보기(회원사 주소)
				ATDto.setCompanyAddr(companyDto.getCompanyAddress()); //회원사 주소

			}				
		}
		
		//유저정보, 대여정보, 차량정보 셋팅
		ATDto.setUserName(quoteUserlist.getUserName()); //고객면
		ATDto.setUserContact(_stringUtil.phoneRegx(quoteUserlist.getUserContact1())); //고객연락처

		ATDto.setUrIdx(quoteUserlist.getUrIdx());
		ATDto.setRtIdx(loginSessionInfo.getRtIdx());
	
		
		//2020-01-17 (금) 08:16 형식으로 반환한다.
		String rentDate = _stringUtil.getFormatDate(quoteUserlist.getRentStartDay(),
				   		  _stringUtil.changeTimeFormat(quoteUserlist.getRentStartTime()) + ":00")
							  .replace(".", "-").replace(":00", "") +
						  _stringUtil.changeTimeFormat(quoteUserlist.getRentStartTime());

		
		
		ATDto.setRentDate(rentDate); //대여일시
		
		String returnDate = _stringUtil.getFormatDate(quoteUserlist.getRentEndDay(),
		   		  _stringUtil.changeTimeFormat(quoteUserlist.getRentEndTime()) + ":00")
					  .replace(".", "-").replace(":00", "") +
				  _stringUtil.changeTimeFormat(quoteUserlist.getRentEndTime());
		

		ATDto.setRentDate(rentDate); //대여일시
		ATDto.setReturnDate(returnDate); //반납일시
		
		
		//String carType = carInfo.getCartypeCode().replace("CP", "경차").replace("SMD", "준중형").replace("MD", "중형").replace("LG", "대형").replace("VN", "승합").replace("SV", "SUV");
		ATDto.setCarType(carInfo.getYear() + " " + carInfo.getModelName());//년도 차량모델
		
		ATDto.setCarNumber(carInfo.getCarNumber()); //차량번호
		
		//OF	지점방문
		//DL	배달대여
		String delivertType = "";
		if("OF".equals(quoteUserlist.getDeliveryTypeCode())) {
			delivertType = "지점방문";
		} else if("DL".equals(quoteUserlist.getDeliveryTypeCode())){
			delivertType = "배달대여";
		}
		
		ATDto.setDeliveryTypeCode(delivertType);
		
		//대여위치 설정
		ATDto.setRentAddr(quoteUserlist.getDeliveryAddr());
		
		
		param = new DochaMap();
		param.set("urIdx",quoteUserlist.getUrIdx());
		param.set("quIdx",quoteUserlist.getQuIdx());
		param.set("quoteStatus","QO");
		
		//자차보험
		String strInsuranceFee = "0";
		strInsuranceFee =  (String)reqParam.get("insuranceFee");

		if(_stringUtil.isEmpty(strInsuranceFee)) {
			strInsuranceFee = "0";
		}		

		//대여료
		String strRentFee = "0";
		strRentFee =  (String)reqParam.get("rentFee");

		if(_stringUtil.isEmpty(strRentFee)) {
			strRentFee = "0";
		}
		
		//보증금
		String strCarDeposit = "0";
		strCarDeposit =  (String)reqParam.get("carDeposit");
		
		if(_stringUtil.isEmpty(strCarDeposit)) {
			strCarDeposit = "0";
		}
		
		
		System.out.println("cardeposit test");
		System.out.println();
		
		//자차보험.
		ATDto.setInsurancerate(_stringUtil.changeNumberFormat(strInsuranceFee));
		
		//면책금
		int insuranceCopayment = Integer.parseInt(carInsuranceInfo.getPropertyDamageCover())/10000;
		String strinsuranceCopayment = _stringUtil.changeNumberFormat(Integer.toString(insuranceCopayment));
		ATDto.setInsurancecopayment(strinsuranceCopayment);
		
		
		int rentFee = Integer.parseInt(strRentFee);
		int insuranceFee = Integer.parseInt(strInsuranceFee);
		
		int rentAmount = rentFee + insuranceFee;
		
		String strRentAmount = _stringUtil.changeNumberFormat(Integer.toString(rentAmount)); 
		
		//대여금액
		ATDto.setRentAmount(strRentAmount);
		
		//발신자번호
		ATDto.setCallBack("16613355");
		ATDto.setTemplateCode(DochaTemplateCodeProvider.A000014.name());
		
		ATDto.setCarDeposit(_stringUtil.changeNumberFormat(strCarDeposit));
		
		msg = "";
		msg = atMsgUtil.makekakoAlramTalkTemplate(ATDto);
		ATDto.setMsg(msg);
		
		
		ATDto.setFailedType("lms");							//실패시 전송타입 - LMS
		ATDto.setFailedSubject("견적도착");					//LMS보낼 때 제목
		
		ATDto.setBtnTypes("웹링크");
		ATDto.setBtnTxts("확인하기");
		ATDto.setBtnUrls1(",https://www.docha.com/rentcar/estimatList.do?tab=0");//PC
		ATDto.setBtnUrls2(",https://www.docha.com/rentcar/estimatList.do?tab=0");//모바일
		
		alramtalkService.sendKakaoAlram(ATDto);
		
		
		//END 운영직원 알림톡 발송=======================================================
		
		
		resData.put("resultCnt",resultCnt );
		
		return resData;
	}
	
	// 결제요청 취소
	@RequestMapping(value="/rentcar/cancelQuote.json")
	@ResponseBody
	public Object cancelQuoteJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request , Authentication authentication ) {
		DochaMap param = new DochaMap();
		param.putAll(reqParam);
		DochaMap resData = new DochaMap();
		int resultCnt = 0;
		
		DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
		param.set("rtIdx",loginSessionInfo.getRtIdx());
		resultCnt += service.cancelQuote(param);
		resData.put("resultCnt",resultCnt );
		
		return resData;
	}
	
	//멤버사 id, pw 찾기
	@RequestMapping(value = "/rentcar/rentMemberMypage.do")
	public ModelAndView rentMemberMypage(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {

		if(!StringUtil.isEmpty(reqParam.get("myPageView"))){
			mv.addObject("myPageView", reqParam.get("myPageView"));
		}else {
			mv.addObject("myPageView", "");
		}

		mv.setViewName("rentcar/memberMyPage");
		return mv;
	}	
	
	//멤버사 id, pw 찾기
	@RequestMapping(value = "/rentcar/cancelRentQuote.json")
	@ResponseBody
	public DochaMap cancelRentQuote(@RequestParam Map<String, Object> reqParam, Authentication authentication) {

		int resultInt = -1;
		boolean result = false;
		String errMsg = "";
		
		try {
			
			DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
			
			DochaMap paramMap = new DochaMap();			
			
			paramMap.set("quoteStatus", "QB"); //견적취소상태
			paramMap.set("qrIdx", reqParam.get("qrIdx"));
			paramMap.set("rtIdx", loginSessionInfo.getRtIdx());
			paramMap.set("urIdx", loginSessionInfo.getUrIdx());
			
			resultInt = service.updateCancelQuoteRentCompany(paramMap);
			
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
