package com.ohdocha.cu.kprojectcu.controller;


import com.ohdocha.cu.kprojectcu.domain.*;
import com.ohdocha.cu.kprojectcu.mapper.DochaUserInfoDao;
import com.ohdocha.cu.kprojectcu.service.DochaCarSearchService;
import com.ohdocha.cu.kprojectcu.service.DochaRentcarService;
import com.ohdocha.cu.kprojectcu.service.DochaUserInfoService;
import com.ohdocha.cu.kprojectcu.util.CalculationPay;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import com.ohdocha.cu.kprojectcu.util.KeyMaker;
import com.ohdocha.cu.kprojectcu.util.StringUtil;
import lgdacom.XPayClient.XPayClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class DochaCarSearchController extends ControllerExtension {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(DochaCarSearchController.class);

    //상점아이디
    @Value("${pg.mid.00}")
    private String pgMmid00;

    //LG-dacom 설정파일
    //pg.config.path=c:/lgdacom
    @Value("${pg.config.path}")
    private String configPath;

    //pg.platform=test or real
    @Value("${pg.platform}")
    private String CST_PLATFORM;

    @Value("${pg.platform}")
    private String pgPlatform;

    @Value("${pg.mertkey.00}")
    private String MertKey;

    @Value("${lg.receipt_link}")
    private String receipt_link;

    @Autowired
    DochaCarSearchService carSearchService;

    @Autowired
    DochaUserInfoService userInfoService;

    @Autowired
    CalculationPay calculationPay;


    @Autowired
    DochaRentcarService rentCarService;

    @Autowired
    private DochaUserInfoDao userInfoDao;

    /*
     * 결제전 상세
     * todo
     * GET → POST
     * add service
     *
     * ① 결제정보 셋팅 참고 : DochaPaymentController의 user/payment/credit.do를 참고
     * ② U+ 결제 파라메터 셋팅 후 JSP에 보낸다.
     *
     * */
    @RequestMapping(value = "/user/carSearch/paymentDetail.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView beforeCredit(@RequestParam Map<String, Object> reqParam, ModelAndView mv,
                                     HttpServletRequest request, Authentication authentication, HttpSession session) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);


        DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();

        String userName = loginSessionInfo.getUserName();
        String userId = loginSessionInfo.getUserId();

        String crIdx = param.getString("crIdx");
        String rentStartDt = param.getString("rentStartDt");
        String rentEndDt = param.getString("rentEndDt");
        String deliveryTypeCode = param.getString("deliveryTypeCode");//OF,DL
        String deliveryAddr = param.getString("deliveryAddr");
        String returnAddr = param.getString("returnAddr");
        String longterm = param.getString("longterm");

        //rentStartDt    = "202002201000";
        //rentEndDt       = "202002242100";
        //crIdx          = "CR202001101529770757";
        //deliveryAddr    = "서울 특별시 은평구 구산동";
        //returnAddr       = "서울시 강남구 논현로79길 73";


        if (StringUtil.isEmpty(deliveryTypeCode)) {
            deliveryTypeCode = "OF";
            longterm = "ST";
        } else {
            longterm = "LT";
            deliveryTypeCode = "DL";
        }


        int nLGD_AMOUNT = 0;  //총 결제금액

        param.set("crIdx", crIdx);
        param.set("rentStartDate", rentStartDt);
        param.set("rentEndDate", rentEndDt);

        String year = "";
        String modelName = "";

        //결제전 차량정보
        List<DochaCarSearchPaymentDetailDto> carInfoList = carSearchService.selectCarSearchDetail(param);

        if (carInfoList.size() > 0) {
            year = carInfoList.get(0).getYear();
            modelName = carInfoList.get(0).getModelName();
        }

        String rentFee = "";
        String insuranceFee = "";
        String insuranceCopayment = "";

        if (StringUtil.isEmpty(carInfoList.get(0).getRentFee())) {
            rentFee = "0";
        } else {
            rentFee = carInfoList.get(0).getRentFee();
        }

        if (StringUtil.isEmpty(insuranceFee)) {
            insuranceFee = "0";
        } else {
            insuranceFee = carInfoList.get(0).getInsuranceFee();
        }

        if (StringUtil.isEmpty(insuranceCopayment)) {
            insuranceCopayment = "0";
        } else {
            insuranceCopayment = carInfoList.get(0).getInsuranceCopayment();
        }

        System.out.println("차량 리스트");
        System.out.println(rentFee);
        System.out.println(insuranceFee);
        System.out.println(insuranceCopayment);

        //결제금액 정보 셋팅
        nLGD_AMOUNT = Integer.parseInt(rentFee) +
                Integer.parseInt(insuranceFee) +
                Integer.parseInt(insuranceCopayment);

        /*
         *************************************************
         * 0. 차량 선택 후 차량 결제정보 셋팅
         *************************************************/
        //장기 단기 구분
        // 1개월 이하  : 단기
        // 1개월 이상  : 장기
        String LongTermYn = "N";


        /*
         *************************************************
         * 1. 결제 파라메터 셋팅
         *************************************************/
        String LGD_AMOUNT = Integer.toString(nLGD_AMOUNT);
        String LGD_MID = pgMmid00;
        String LGD_OID = KeyMaker.getInsetance().getKeyDeafult("RE");


        /*
         *************************************************
         * 2. MD5 해쉬암호화 (수정하지 마세요) - BEGIN
         *
         * MD5 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
         *************************************************
         *
         * 해쉬 암호화 생성( LGD_MID + LGD_OID + LGD_AMOUNT + LGD_TIMESTAMP )
         * LGD_MID          : 상점아이디
         * LGD_OID          : 주문번호
         * LGD_AMOUNT       : 금액
         * LGD_TIMESTAMP    : 타임스탬프
         *
         * MD5 해쉬데이터 암호화 검증을 위해
         * LG유플러스에서 발급한 상점키(MertKey)를 환경설정 파일(lgdacom/conf/mall.conf)에 반드시 입력하여 주시기 바랍니다.
         */
        String LGD_HASHDATA = "";
        XPayClient xpay = null;


        String LGD_TIMESTAMP = "";
        try {

            xpay = new XPayClient();
            xpay.Init(configPath, CST_PLATFORM);

            if (LGD_TIMESTAMP == null || "".equals(LGD_TIMESTAMP)) {
                LGD_TIMESTAMP = xpay.GetTimeStamp();
            }

            LGD_HASHDATA = xpay.GetHashData(LGD_MID, LGD_OID, LGD_AMOUNT, LGD_TIMESTAMP);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("LG유플러스 제공 API를 사용할 수 없습니다. 환경파일 설정을 확인해 주시기 바랍니다. ");
            System.out.println("" + e.getMessage());

        } finally {
            xpay = null;
        }
        String LGD_CUSTOM_TITLE = "";

        if ("N".equals(LongTermYn)) { //장기
            LGD_CUSTOM_TITLE = "카썸모빌리티 장기 렌트 결제안내입니다.";
        } else if ("Y".equals(LongTermYn)) { //단기
            LGD_CUSTOM_TITLE = "카썸모빌리티 단기 렌트 결제안내입니다.";
        }

        String LGD_BUYER = userName;
        String LGD_BUYEREMAIL = userId;
        String LGD_PRODUCTINFO = year + "년형 " + modelName;

        String LGD_CUSTOM_PROCESSTYPE = "TWOTR";
        String LGD_CASNOTEURL = "/user/carSearch/returnurl.do";
        String LGD_NOTEURL = "/user/carSearch/returnurl.do";
        String LGD_RETURNURL = "/user/carSearch/returnurl.do";
        String LGD_CANCELURL = "/user/carSearch/returnurl.do";
        String CST_PLATFORM = pgPlatform;
        String LGD_RESPCODE = "";
        String LGD_RESPMSG = "";
        String LGD_PAYKEY = "";
        String LGD_BILLKEY = "";
        String LGD_ENCODING = "UTF-8";


        DochaPaymentLgdDto lgdDto = new DochaPaymentLgdDto();
        lgdDto.setLGD_AMOUNT(Integer.toString(nLGD_AMOUNT));
        lgdDto.setLGD_PRODUCTINFO(LGD_PRODUCTINFO);
        lgdDto.setLGD_BUYER(LGD_BUYER);
        lgdDto.setLGD_BUYEREMAIL(userId);
        lgdDto.setLGD_WINDOW_VER("2.5");
        lgdDto.setLGD_ACTIVEXYN("N");
        lgdDto.setLGD_CUSTOM_TITLE("카썸 결제안내");
        lgdDto.setLGD_CUSTOM_PROCESSTYPE("TOWER");
        lgdDto.setLGD_CASNOTEURL(LGD_CASNOTEURL);
        lgdDto.setLGD_NOTEURL(LGD_NOTEURL);
        lgdDto.setLGD_RETURNURL(LGD_RETURNURL);
        lgdDto.setLGD_CANCELURL(LGD_CANCELURL);
        lgdDto.setCST_PLATFORM(CST_PLATFORM);
        lgdDto.setLGD_MTRANSFERWAPURL("");
        lgdDto.setLGD_MTRANSFERCANCELURL("");
        lgdDto.setLGD_MTRANSFERNOTEURL(LGD_RETURNURL);
        lgdDto.setLGD_KVPMISPWAPURL("");
        lgdDto.setLGD_KVPMISPAUTOAPPYN("");
        lgdDto.setLGD_RESPCODE(LGD_RESPCODE);
        lgdDto.setLGD_RESPMSG(LGD_RESPMSG);
        lgdDto.setLGD_PAYKEY(LGD_PAYKEY);
        lgdDto.setLGD_BILLKEY(LGD_BILLKEY);
        lgdDto.setLGD_ENCODING(LGD_ENCODING);
        lgdDto.setDeliveryTypeCode(deliveryTypeCode);
        lgdDto.setLongterm(longterm);

        DochaPaymentLgdDto responseDto = new DochaPaymentLgdDto();

        HashMap payReqMap = new HashMap();
        payReqMap.put("CST_PLATFORM", CST_PLATFORM);                        // 테스트, 서비스 구분
        payReqMap.put("CST_MID", pgMmid00);                            // 상점아이디
        payReqMap.put("CST_WINDOW_TYPE", "submit");                            // 전송방식 구분
        payReqMap.put("LGD_MID", LGD_MID);                            // 상점아이디
        payReqMap.put("LGD_OID", LGD_OID);                            // 주문번호
        payReqMap.put("LGD_BUYER", LGD_BUYER);                            // 구매자
        payReqMap.put("LGD_PRODUCTINFO", LGD_PRODUCTINFO);                    // 상품정보
        payReqMap.put("LGD_AMOUNT", LGD_AMOUNT);                            // 결제금액
        payReqMap.put("LGD_BUYEREMAIL", LGD_BUYEREMAIL);                        // 구매자 이메일
        payReqMap.put("LGD_CUSTOM_SKIN", "SMART_XPAY2");                        // 결제창 SKIN
        payReqMap.put("LGD_CUSTOM_PROCESSTYPE", LGD_CUSTOM_PROCESSTYPE);                // 트랜잭션 처리방식
        payReqMap.put("LGD_TIMESTAMP", LGD_TIMESTAMP);                        // 타임스탬프
        payReqMap.put("LGD_HASHDATA", LGD_HASHDATA);                        // MD5 해쉬암호값
        payReqMap.put("LGD_RETURNURL", LGD_RETURNURL);                        // 응답수신페이지
        payReqMap.put("LGD_VERSION", "JSP_Non-ActiveX_SmartXPay");            // 버전정보 (삭제하지 마세요)
        payReqMap.put("LGD_CUSTOM_FIRSTPAY", "");                                    // 디폴트 결제수단
        payReqMap.put("LGD_PCVIEWYN", "N");                                // 휴대폰번호 입력 화면 사용 여부

        payReqMap.put("LGD_CUSTOM_SWITCHINGTYPE", "SUBMIT");                            // 신용카드 카드사 인증 페이지 연동 방식
        payReqMap.put("LGD_DOMAIN_URL", "xpayvvip");

        //iOS 연동시 필수
        payReqMap.put("LGD_MPILOTTEAPPCARDWAPURL", "");

        /*
         ****************************************************
         * 신용카드 ISP(국민/BC)결제에만 적용 - BEGIN
         ****************************************************
         */
        payReqMap.put("LGD_KVPMISPWAPURL", "");
        payReqMap.put("LGD_KVPMISPCANCELURL", "");
        /*
         ****************************************************
         * 신용카드 ISP(국민/BC)결제에만 적용  - END
         ****************************************************
         */

        /*
         ****************************************************
         * 계좌이체 결제에만 적용 - BEGIN
         ****************************************************
         */
        payReqMap.put("LGD_MTRANSFERWAPURL", "");
        payReqMap.put("LGD_MTRANSFERCANCELURL", "");

        /*
         ****************************************************
         * 계좌이체 결제에만 적용  - END
         ****************************************************
         */


      /*
      ****************************************************
      * 모바일 OS별 ISP(국민/비씨), 계좌이체 결제 구분 값
      ****************************************************
      1) Web to Web
      - 안드로이드: A (디폴트)
      - iOS: N
        ** iOS일 경우, 반드시 N으로 값을 수정
      2) App to Web(반드시 SmartXPay_AppToWeb_연동가이드를 참조합니다.)
      - 안드로이드, iOS: A
      */
        payReqMap.put("LGD_KVPMISPAUTOAPPYN", "A");                    // 신용카드 결제 사용시 필수
        payReqMap.put("LGD_MTRANSFERAUTOAPPYN", "A");                    // 계좌이체 결제 사용시 필수


        // 가상계좌(무통장) 결제연동을 하시는 경우  할당/입금 결과를 통보받기 위해 반드시 LGD_CASNOTEURL 정보를 LG 유플러스에 전송해야 합니다 .
        payReqMap.put("LGD_CASNOTEURL", LGD_CASNOTEURL);            // 가상계좌 NOTEURL

        // Return URL에서 인증 결과 수신 시 셋팅될 파라미터들
        payReqMap.put("LGD_RESPCODE", "");
        payReqMap.put("LGD_PAYKEY", "");
        payReqMap.put("LGD_RESPMSG", "");


        //내부용세션
        payReqMap.put("crIdx", crIdx);
        payReqMap.put("rentFee", rentFee);
        payReqMap.put("insuranceFee", insuranceFee);
        session.setAttribute("PAYREQ_MAP", payReqMap);

        /*
         * LGU+의 모바일 결제는 iframe방식이 아니라 submit 방식이라 url을 지정해주어야한다.
         * 기본 submit url은 여기에서 지정해주고 앞에 origin은 javascript에서 지정한다.
         *
         * */
        mv.addObject("payreq_URL", "/user/carSearch/payreq.do");

        mv.addObject("LGD_AMOUNT", LGD_AMOUNT);
        mv.addObject("LGD_MID", LGD_MID);
        mv.addObject("LGD_OID", LGD_OID);
        mv.addObject("CST_PLATFORM", CST_PLATFORM);
        mv.addObject("userInfo", loginSessionInfo);
        mv.addObject("carInfoDetail", carInfoList.get(0));
        mv.addObject("rentStartDt", rentStartDt);
        mv.addObject("rentEndDt", rentEndDt);
        mv.addObject("deliveryTypeCode", deliveryTypeCode);
        mv.addObject("deliveryAddr", deliveryAddr);
        mv.addObject("returnAddr", returnAddr);
        mv.addObject("longterm", longterm);

        mv.addObject("LGD_CUSTOM_PROCESSTYPE", LGD_CUSTOM_PROCESSTYPE);
        mv.addObject("LGD_CASNOTEURL", LGD_CASNOTEURL);
        mv.addObject("LGD_NOTEURL", LGD_NOTEURL);
        mv.addObject("LGD_RETURNURL", LGD_RETURNURL);
        mv.addObject("LGD_CANCELURL", LGD_CANCELURL);
        mv.addObject("CST_PLATFORM", CST_PLATFORM);
        mv.addObject("LGD_RESPCODE", LGD_RESPCODE);
        mv.addObject("LGD_RESPMSG", LGD_RESPMSG);
        mv.addObject("LGD_PAYKEY", LGD_PAYKEY);
        mv.addObject("LGD_BILLKEY", LGD_BILLKEY);
        mv.addObject("LGD_ENCODING", LGD_ENCODING);
        mv.addObject("CST_PLATFORM", CST_PLATFORM);                            // 테스트, 서비스 구분
        mv.addObject("CST_MID", pgMmid00);                                    // 상점아이디
        mv.addObject("CST_WINDOW_TYPE", "submit");                            // 전송방식 구분
        mv.addObject("LGD_MID", LGD_MID);                                    // 상점아이디
        mv.addObject("LGD_OID", LGD_OID);                                    // 주문번호
        mv.addObject("LGD_BUYER", LGD_BUYER);                                // 구매자
        mv.addObject("LGD_PRODUCTINFO", LGD_PRODUCTINFO);                    // 상품정보
        mv.addObject("LGD_AMOUNT", LGD_AMOUNT);                            // 결제금액
        mv.addObject("LGD_BUYEREMAIL", LGD_BUYEREMAIL);                    // 구매자 이메일
        mv.addObject("LGD_CUSTOM_SKIN", "SMART_XPAY2");                    // 결제창 SKIN
        mv.addObject("LGD_CUSTOM_PROCESSTYPE", LGD_CUSTOM_PROCESSTYPE);    // 트랜잭션 처리방식
        mv.addObject("LGD_TIMESTAMP", LGD_TIMESTAMP);                        // 타임스탬프
        mv.addObject("LGD_HASHDATA", LGD_HASHDATA);                        // MD5 해쉬암호값
        mv.addObject("LGD_RETURNURL", LGD_RETURNURL);                        // 응답수신페이지
        mv.addObject("LGD_VERSION", "JSP_Non-ActiveX_SmartXPay");            // 버전정보 (삭제하지 마세요)
        mv.addObject("LGD_CUSTOM_FIRSTPAY", "");                            // 디폴트 결제수단
        mv.addObject("LGD_PCVIEWYN", "N");                                    // 휴대폰번호 입력 화면 사용 여부
        mv.setViewName("user/carsearch/paymentDetail");
        return mv;
    }


    /*
     * 사용자가 결제페이지에서 실제 결제서비스로 타고 넘어가는 부분 STEP 1
     *
     * */
//    @RequestMapping(value = "/user/carSearch/payreq.do", method = RequestMethod.POST)
//    //@ResponseBody
//    public ModelAndView payreq(ModelAndView mv, HttpSession session, HttpServletRequest request,
//                               HttpServletResponse response,
//                               Authentication authentication,
//                               @RequestParam Map<String, Object> reqParam) {
//
//        //credit.do 페이지에서 Submit을 할 때 받아온다.
//        DochaUserInfoDto map = (DochaUserInfoDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        DochaMap resData = new DochaMap();
//        boolean result = false;
//        int errCd = 1;
//
//        DochaMap param = new DochaMap();
//        param.putAll(reqParam);
//        DochaPaymentLgdDto lgdDto = new DochaPaymentLgdDto();
//
//
//        String crIdx = param.getString("crIdx");
//        String deliveryTypeCode = param.getString("deliveryTypeCode");
//        String deliveryAddr = param.getString("deliveryAddr");
//        String returnAddr = param.getString("returnAddr");
//        String LGD_TID = param.getString("LGD_TID");
//        String rentFee = param.getString("rentFee");
//        String LGD_BUYERPHONE = param.getString("LGD_BUYERPHONE");
//        String LGD_PAYKEY = param.getString("LGD_PAYKEY");
//        String LGD_BUYEREMAIL = param.getString("LGD_BUYEREMAIL");
//        String LGD_RECEIVER = param.getString("LGD_RECEIVER");
//        String LGD_DELIVERYINFO = param.getString("LGD_DELIVERYINFO");
//        String LGD_PAYTYPE = param.getString("LGD_PAYTYPE");
//        String LGD_BUYERIP = param.getString("LGD_BUYERIP");
//        String LGD_RECEIVERPHONE = param.getString("LGD_RECEIVERPHONE");
//        String LGD_AFFILIATECODE = param.getString("LGD_AFFILIATECODE");
//        String LGD_RETURNURL = param.getString("LGD_RETURNURL");
//        String LGD_RESPMSG = param.getString("LGD_RESPMSG");
//        String KVP_CARDCODE = param.getString("KVP_CARDCODE");
//        String LGD_ESCROWYN = param.getString("LGD_ESCROWYN");
//        String LGD_FINANCECODE = param.getString("LGD_FINANCECODE");
//        String LGD_HASHDATA = param.getString("LGD_HASHDATA");
//        String LGD_BUYERADDRESS = param.getString("LGD_BUYERADDRESS");
//        String LGD_AUTHTYPE = param.getString("LGD_AUTHTYPE");
//        String LGD_FINANCENAME = param.getString("LGD_FINANCENAME");
//        String LGD_DEVICE = param.getString("LGD_DEVICE");
//        String LGD_TIMESTAMP = param.getString("LGD_TIMESTAMP");
//        String LGD_BUYER = param.getString("LGD_BUYER");
//        String LGD_PRODUCTINFO = param.getString("LGD_PRODUCTINFO");
//        String LGD_BUYERSSN = param.getString("LGD_BUYERSSN");
//        String LGD_PRODUCTCODE = param.getString("LGD_PRODUCTCODE");
//        String LGD_BUYERID = param.getString("LGD_BUYERID");
//        String LGD_CLOSEDATE = param.getString("LGD_CLOSEDATE");
//        String LGD_OID = param.getString("LGD_OID");
//        String LGD_RESPCODE = param.getString("LGD_RESPCODE");
//        String LGD_AMOUNT = param.getString("LGD_AMOUNT");
//        String LGD_CARDPREFIX = param.getString("LGD_CARDPREFIX");
//        String LGD_MID = param.getString("LGD_MID");
//
//        String insuranceFee = param.getString("insuranceFee");
//        String longterm = param.getString("longterm");
//
//        String reserveDate = param.getString("reserveDate");
//        String rentStartDay = param.getString("rentStartDay");
//        String rentStartTime = param.getString("rentStartTime");
//        String rentEndDay = param.getString("rentEndDay");
//        String rentEndTime = param.getString("rentEndTime");
//
//
//        lgdDto.setReserveDate(reserveDate);
//        lgdDto.setRentStartDay(rentStartDay);
//        lgdDto.setRentStartTime(rentStartTime);
//        lgdDto.setRentEndDay(rentEndDay);
//        lgdDto.setRentEndTime(rentEndTime);
//
//        lgdDto.setLGD_BUYERPHONE(LGD_BUYERPHONE);
//        lgdDto.setLGD_PAYKEY(LGD_PAYKEY);
//        lgdDto.setLGD_BUYEREMAIL(LGD_BUYEREMAIL);
//        lgdDto.setLGD_RECEIVER(LGD_RECEIVER);
//        lgdDto.setLGD_DELIVERYINFO(LGD_DELIVERYINFO);
//        lgdDto.setLGD_PAYTYPE(LGD_PAYTYPE);
//        lgdDto.setLGD_BUYERIP(LGD_BUYERIP);
//        lgdDto.setLGD_RECEIVERPHONE(LGD_RECEIVERPHONE);
//        lgdDto.setLGD_AFFILIATECODE(LGD_AFFILIATECODE);
//        lgdDto.setLGD_RETURNURL(LGD_RETURNURL);
//        lgdDto.setLGD_RESPMSG(LGD_RESPMSG);
//        lgdDto.setKVP_CARDCODE(KVP_CARDCODE);
//        lgdDto.setLGD_ESCROWYN(LGD_ESCROWYN);
//        lgdDto.setLGD_FINANCECODE(LGD_FINANCECODE);
//        lgdDto.setLGD_HASHDATA(LGD_HASHDATA);
//        lgdDto.setLGD_BUYERADDRESS(LGD_BUYERADDRESS);
//        lgdDto.setLGD_AUTHTYPE(LGD_AUTHTYPE);
//        lgdDto.setLGD_FINANCENAME(LGD_FINANCENAME);
//        lgdDto.setLGD_DEVICE(LGD_DEVICE);
//        lgdDto.setLGD_TIMESTAMP(LGD_TIMESTAMP);
//        lgdDto.setLGD_BUYER(LGD_BUYER);
//        lgdDto.setLGD_PRODUCTINFO(LGD_PRODUCTINFO);
//        lgdDto.setLGD_BUYERSSN(LGD_BUYERSSN);
//        lgdDto.setLGD_PRODUCTCODE(LGD_PRODUCTCODE);
//        lgdDto.setLGD_BUYERID(LGD_BUYERID);
//        lgdDto.setLGD_CLOSEDATE(LGD_CLOSEDATE);
//        lgdDto.setLGD_OID(LGD_OID);
//        lgdDto.setLGD_RESPCODE(LGD_RESPCODE);
//        lgdDto.setLGD_AMOUNT(LGD_AMOUNT);
//        lgdDto.setLGD_CARDPREFIX(LGD_CARDPREFIX);
//        lgdDto.setLGD_MID(LGD_MID);
//
//        lgdDto.setRentFee(rentFee);
//
//        lgdDto.setInsuranceFee(insuranceFee);
//
//        lgdDto.setDeliveryAddr(deliveryAddr);
//        lgdDto.setReturnAddr(returnAddr);
//        lgdDto.setCarSearchYn("Y");
//        lgdDto.setDeliveryTypeCode(deliveryTypeCode);
//        lgdDto.setLongterm(longterm);
//        lgdDto.setCrIdx(crIdx);
//        lgdDto.setUrIdx(map.getUrIdx());
//
//        DochaMap res = new DochaMap();
//
//        try {
//            res = pgService.payres(lgdDto, authentication);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        if (!res.containsKey("result")) {
//            res.set("result", -1);
//        }
//
//        String payResult = res.getString("result");
//        String errMsg = null;
//
//        if ("0".equals(payResult)) {
//            mv.addObject("result", true);
//            errMsg = "success";
//            mv.setViewName("redirect:payment_complete.do");
//        } else if ("1".equals(payResult)) {
//
//            mv.addObject("result", false);
//            errMsg = "결제모듈 초기화 중 오류가 발생했습니다. 잠시 후 다시 시도 해 주시기 바랍니다";
//            mv.setViewName("redirect:paymentFail.do");
//
//        } else if ("2".equals(payResult) || "3".equals(payResult)) {
//
//            mv.addObject("result", false);
//            errMsg = "결제진행 중 오류가 발생했습니다. 잠시 후 다시 시도 해 주시기 바랍니다";
//            mv.setViewName("redirect:paymentFail.do");
//
//        } else if ("4".equals(payResult)) {
//
//            mv.addObject("result", false);
//            errMsg = "예약진행 중 오류가 발생했습니다. 잠시 후 다시 시도 해 주시기 바랍니다";
//            mv.setViewName("redirect:paymentFail.do");
//
//        } else if ("5".equals(payResult) || "6".equals(payResult)) {
//
//            mv.addObject("result", false);
//
//            if (res.containsKey("m_szResCode")) {
//                mv.addObject("m_szResCode", res.get("m_szResCode"));
//            }
//
//            if (res.containsKey("m_szResMsg")) {
//                errMsg = res.getString("m_szResMsg");
//            }
//
//            mv.setViewName("redirect:paymentFail.do");
//
//        } else {
//
//            mv.addObject("result", false);
//            errMsg = "결제진행 중 오류가 발생했습니다. 잠시 후 다시 시도 해 주시기 바랍니다";
//            mv.setViewName("redirect:paymentFail.do");
//        }
//
//        resData.put("res", result);
//        resData.put("errCd", errCd);
//        resData.put("errMsg", errMsg);
//
//        mv.addObject("LGD_TID", LGD_TID);
//
//        mv.addObject("errMsg", errMsg);
//        mv.addObject("payResult", payResult);
//
//        mv.addObject("crIdx", crIdx);
//
//        mv.addObject("rmIdx", lgdDto.getRmIdx());
//
//        return mv;
//    }


    /*
     * 직접견적요청페이지
     *
     *
     *
     * */
    @RequestMapping(value = "/user/carSearch/carList.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView carListDo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);

        String referrer = request.getHeader("Referer");
        if (referrer.contains("login")){
            param = (DochaMap) request.getSession().getAttribute("preParam");
            request.getSession().removeAttribute("preParam");
        }else {
            request.getSession().setAttribute("preParam", param);
        }

        mv.addObject("preParam", param);
        mv.setViewName("user/carsearch/user_car_search_list.html");

        return mv;
    }

    @RequestMapping(value = "/user/carSearch/carList.json")
    @ResponseBody
    public Object carListJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        DochaMap resData = new DochaMap();

        List<DochaCarInfoDto> resCarDto = carSearchService.selectTargetCarList(param);
        resData.put("result", resCarDto);

        return resData;
    }

    // 차량 상세 페이지
    @RequestMapping(value = "/user/carSearch/carDetail.do", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/x-www-form-urlencoded")
    public ModelAndView carDetailDo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);

        String referrer = request.getHeader("Referer");
        if (referrer.contains("login") || referrer.contains("driver") || referrer.contains("license") ) {
            param = (DochaMap) request.getSession().getAttribute("preParam");
        } else {
            request.getSession().setAttribute("preParam", param);
        }

        List<DochaCarSearchPaymentDetailDto> resCarDto = carSearchService.selectCarSearchDetail(param);

        mv.addObject("preParam", param);
        mv.addObject("resCarDto", resCarDto);
        mv.setViewName("user/carsearch/car_detail_day1.html");
        return mv;
    }

    @RequestMapping(value = "/user/carSearch/carDetail.json")
    @ResponseBody
    public Object carDetailJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        DochaMap resData = new DochaMap();

        // 차량 정보
        List<DochaCarSearchPaymentDetailDto> resCarDto = carSearchService.selectCarSearchDetail(param);

        // 유저 정보

        if ( authentication != null ) {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            DochaUserInfoDto userInfo = (DochaUserInfoDto) authentication.getPrincipal();
            resData.put("userInfo", userInfo);

            // 면허 정보
            DochaUserInfoDto dochaLicenseInfoDto = (DochaUserInfoDto) authentication.getPrincipal();
            DochaUserInfoDto licenseInfo = userInfoService.selectLicenseInfo(dochaLicenseInfoDto);

            resData.put("licenseInfo", licenseInfo);
        }

        resData.put("resCarDto", resCarDto);

        return resData;
    }


    @RequestMapping(value = "/user/carSearch/location.do", method = RequestMethod.POST)
    public ModelAndView carLocationDo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        mv.addObject("preParam", param);
        mv.setViewName("user/carsearch/map.html");

        return mv;
    }

    @RequestMapping(value = "/user/myLocation.do", method = RequestMethod.POST)
    public ModelAndView myLocationDo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        mv.addObject("preParam", param);
        mv.setViewName("my_location_map.html");

        return mv;
    }

    // 제2운전자추가 페이지
    @RequestMapping(value = "/user/carSearch/driver.do", method = RequestMethod.GET)
    public ModelAndView secondDriverDo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        mv.addObject("preParam", param);
        mv.setViewName("second_driver_register.html");
        return mv;
    }



    //결제완료페이지
//    @RequestMapping(value = "/user/carSearch/payment_complete.do")
//    public ModelAndView pay_complete(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication
//            , HttpSession session) {
//
//        DochaMap param = new DochaMap();
//        param.putAll(reqParam);
//
//        String crIdx = param.getString("crIdx");
//        String rmIdx = param.getString("rmIdx");
//
//        //quIdx로 DC_QUOTE_RENT_COMPANY 테이블에서 QB로 업데이트
//      /*
//      DochaMap rentParam = new DochaMap();
//
//      rentParam.set("urIdx", urIdx);
//      rentParam.set("quIdx", quIdx);
//
//
//      List<DochaQuoteCompanyDto> listNotQuoteInfo = rentCarService.selectQuoteRentCompany(param);
//
//      for (DochaQuoteCompanyDto dto : listNotQuoteInfo) {
//          if(("QO").equals(dto.getQuoteStatus())) {
//             rentParam.set("quoteStatus", "QB"); //취소완료
//             rentParam.set("quIdx", dto.getQuIdx());
//             rentParam.set("urIdx", dto.getUrIdx());
//             rentParam.set("crIdx", dto.getCrIdx());
//             rentCarService.updateQuoteRentCompanyByUser(rentParam);
//          }
//      }
//      */
//
//        String LGD_MID = pgMmid00;
//
//        param.set("rmIdx", rmIdx);
//        param.set("quoteStatus", "QC");
//        DochaPaymentResultDto paymentResultDto = carSearchService.selectPaymentSuccessDetail(param);
//
//        mv.addObject("receipt_link", receipt_link);
//        mv.addObject("paymentResultList", paymentResultDto);
//        mv.addObject("crIdx", paymentResultDto.getCrIdx());
//        mv.addObject("carInfo", rentCarService.selectCompanyCarInfo(param));
//        mv.addObject("carOptionList", rentCarService.selectCarOptionList(param));
//
//        mv.addObject("LGD_MID", LGD_MID);
//        mv.addObject("rmIdx", rmIdx);
//        mv.addObject("quoteStatus", "QC");
//
//        //카드전표===============
//        String LGD_TID = paymentResultDto.getApprovalNumber();
//        String LGD_MERTKEY = MertKey;
//
//        mv.addObject("LGD_MID", LGD_MID);
//
//        StringBuffer sb = new StringBuffer();
//        sb.append(LGD_MID);
//        sb.append(LGD_TID);
//        sb.append(LGD_MERTKEY);
//
//        byte[] bNoti = sb.toString().getBytes();
//        MessageDigest md = null;
//        try {
//            md = MessageDigest.getInstance("MD5");
//        } catch (NoSuchAlgorithmException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        byte[] digest = md.digest(bNoti);
//
//        StringBuffer strBuf = new StringBuffer();
//        for (int i = 0; i < digest.length; i++) {
//            int c = digest[i] & 0xff;
//            if (c <= 15) {
//                strBuf.append("0");
//            }
//            strBuf.append(Integer.toHexString(c));
//        }
//
//        String authdata = strBuf.toString();
//        mv.addObject("authdata", authdata);
//
//        DochaUserInfoDto userinfoDto = (DochaUserInfoDto) authentication.getPrincipal();
//
//        DochaUserActionDto userActionDto = new DochaUserActionDto();
//        userActionDto.setUrIdx(userinfoDto.getUrIdx());
//        userActionDto.setPageUrl("/user/carSearch/payment_complete.do");
//        userActionDto.setLati("");
//        userActionDto.setLongti("");
//        userInfoDao.insertUserActionData(userActionDto);
//
//        //차량 상태 업데이트
//        DochaMap reqParam2 = new DochaMap();
//
//        reqParam2.set("reserveAbleYn", "N");
//        reqParam2.set("crIdx", crIdx);
//        carSearchService.updateCdtCarInfo(reqParam2);
//
//        //알림톡발송
//        setingDtoForKakaoSend(paymentResultDto, "RU");  //회원
//        setingDtoForKakaoSend(paymentResultDto, "CA");  //회원사
//
//        mv.setViewName("user/carsearch/payment_complete");
//
//        return mv;
//    }

    /*
     * 결제 후 return
     * */
//    @RequestMapping(value = "/user/carSearch/returnurl.do")
//    public ModelAndView lgdreturnUrl(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication
//            , HttpSession session) {
//
//        DochaMap param = new DochaMap();
//        param.putAll(reqParam);
//
//        String crIdx = param.getString("crIdx");
//
//        System.out.println("여기========================");
//
//        mv.setViewName("user/carsearch/pg/returnurl");
//        return mv;
//    }
}