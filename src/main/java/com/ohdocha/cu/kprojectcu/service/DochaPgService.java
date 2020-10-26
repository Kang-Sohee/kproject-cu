package com.ohdocha.cu.kprojectcu.service;

import lgdacom.XPayClient.XPayClient;

import com.ohdocha.cu.kprojectcu.domain.*;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import com.ohdocha.cu.kprojectcu.util.KeyMaker;
import com.ohdocha.cu.kprojectcu.util.StringUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@Service("pgService")
public class DochaPgService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${pg.platform}")
    private String pgPlatform;


    @Value("${pg.mid.00}")
    private String pgMmid00;


    @Value("${pg.mertkey.00}")
    private String pgMertkey00;


    @Value("${pg.skin}")
    private String pgSkin;

    @Value("${pg.skin.mobile}")
    private String pgSkinMobile;

    @Value("${pg.window.version}")
    private String pgWindowVersion;

    // FOR MANUAL
    @Value("${pg.return.url}")
    private String pgReturnUrl;

    @Value("${carSearch.pg.return.url}")
    private String carSearchPgReturnUrl;

    //수정불가
    @Value("${pg.window.type}")
    private String pgWidnowType;


    @Value("${pg.custom.switchingtype}")
    private String pgCustomSwithchingType;

    @Value("${pg.custom.switchingtype.mobile}")
    private String pgCustomSwithchingTypeMobile;

    @Value("${pg.config.path}")
    private String pgConfigPath;


    @Resource(name = "payment")
    DochaPaymentService paymentService;

    @Resource(name = "QuoteUser")
    DochaQuoteUserService quoteUserservice;

    @Resource(name = "userInfo")
    DochaUserInfoService userinfoService;

    @Resource(name = "Rentcar")
    DochaRentcarService rentcarService;

    @Resource(name = "carSearch")
    DochaCarSearchService carSearchService;

    /**
     * @param payment
     * @return DochaMap <br/>
     * key : result <Integer> <br/>
     * 0 : 성공,<br/>
     * 1 : LG PAY 초기화 에러,<br/>
     * 2 : DB INSERT 전 PARAM 생성 에러,<br/>
     * 3 : DB INSERT 증 결제저장중 에러,<br/>
     * 4 : DB INSERT 전 예약저장중 에러,<br/>
     * 5 : tx(결제요청) 시도 후 결과 code가 0000 아님(결제실패),<br/>
     * 6 : tx(결제요청) 시도결과 false,<br/>
     * -1 : 시스템에러<br/>
     * key : m_szResCode - 결제코드(result 5,6의 경우)<br/>
     * key : m_szResMsg - 결제메세지(result 5,6의 경우)<br/>
     * @throws Exception
     */
    @Transactional
    public DochaMap payres(DochaPaymentLgdDto payment, Authentication authentication) throws Exception {

        DochaMap param = new DochaMap();

        Map<String, Object> result = new HashMap<String, Object>();

        int res = -1;

        //결과값으로 LGD_PAYKEY를 주기 때문에 LGD_PAYKEY를 null레크해서 진행여부를 판별한다.
        if (payment.getLGD_PAYKEY() == null) {

            //paykey가 없는경우는 view에서 인증이 실패한 경우이므로 시스템에러
            logger.info("========================== Not Have LGD_PAYKEY ==========================");
            res = 1;
            param.set("result", res);

            return param;
        }

        //전문 insert
        //혹시모르니까 전문 전체를 Insert하는 작업도 필요할듯..
        //lgdDto = ((PgService) getThis()).insertPaymentTransferNewTx(lgdDto);
        PrintStream out = System.out;

        /*
         * [최종결제요청 페이지(STEP2-2)]
         *
         * 매뉴얼 "5.1. XPay 결제 요청 페이지 개발"의 "단계 5. 최종 결제 요청 및 요청 결과 처리" 참조
         *
         * LG유플러스으로 부터 내려받은 LGD_PAYKEY(인증Key)를 가지고 최종 결제요청.(파라미터 전달시 POST를 사용하세요)
         */

        /* ※ 중요
         * 환경설정 파일의 경우 반드시 외부에서 접근이 가능한 경로에 두시면 안됩니다.
         * 해당 환경파일이 외부에 노출이 되는 경우 해킹의 위험이 존재하므로 반드시 외부에서 접근이 불가능한 경로에 두시기 바랍니다.
         * 예) [Window 계열] C:\inetpub\wwwroot\lgdacom ==> 절대불가(웹 디렉토리)
         */
        String configPath = pgConfigPath;

        /*
         *************************************************
         * 1.최종결제 요청 - BEGIN
         *  (단, 최종 금액체크를 원하시는 경우 금액체크 부분 주석을 제거 하시면 됩니다.)
         *************************************************
         */
        String CST_MID = payment.getCST_MID();   //CST_MID = mertkey
        String LGD_MID = payment.getLGD_MID();
        String LGD_PAYKEY = payment.getLGD_PAYKEY();
        String LGD_ESCROW_USEYN = "N";
        payment.setLGD_ESCROW_USEYN(LGD_ESCROW_USEYN);

        String LGD_CASHRECEIPTYN = "N";
        payment.setLGD_CASHRECEIPTYN(LGD_CASHRECEIPTYN);
        String CST_PLATFORM = payment.getCST_PLATFORM();

        //해당 API를 사용하기 위해 WEB-INF/lib/XPayClient.jar 를 Classpath 로 등록하셔야 합니다.
        // (1) XpayClient의 사용을 위한 xpay 객체 생성
        XPayClient xpay = new XPayClient();

        // (2) Init: XPayClient 초기화(환경설정 파일 로드)
        // configPath: 설정파일
        // CST_PLATFORM: - test, service 값에 따라 lgdacom.conf의 test_url(test) 또는 url(srvice) 사용
        //				- test, service 값에 따라 테스트용 또는 서비스용 아이디 생성

        //configPath = "C:\\lgdacom";
        CST_PLATFORM = pgPlatform;

        boolean isInitPay = false;

        try {

            boolean isInitOK = xpay.Init(configPath, CST_PLATFORM);

            if (!isInitOK) {
                //API 초기화 실패 화면처리
                logger.info("결제요청을 초기화 하는데 실패하였습니다.<br>");
                logger.info("LG유플러스에서 제공한 환경파일이 정상적으로 설치 되었는지 확인하시기 바랍니다.<br>");
                logger.info("mall.conf에는 Mert ID = Mert Key 가 반드시 등록되어 있어야 합니다.<br><br>");
                logger.info("문의전화 LG유플러스 1544-7772<br>");
                logger.info("========================== API Gonfiguration Fail ==========================");

                res = 1;
                param.set("result", res);
            }


            /*
             *************************************************
             * 1.최종결제 요청(수정하지 마세요) - END
             *************************************************
             */

            //LGD_PAYKEY없을 시 LOG저장 후 리턴
            if (LGD_PAYKEY == null) {

                //paykey가 없는경우는 view에서 인증이 실패한 경우이므로 시스템에러
                logger.info("========================== Not Have LGD_PAYKEY ==========================");

                res = 1;
                param.set("result", res);


            } else {
                xpay.Init_TX(LGD_MID);
                xpay.Set("LGD_TXNAME", "PaymentByKey");
                xpay.Set("LGD_PAYKEY", LGD_PAYKEY);

                isInitPay = true;

                //금액을 체크하시기 원하는 경우 아래 주석을 풀어서 이용하십시요.
                //String DB_AMOUNT = "DB나 세션에서 가져온 금액"; //반드시 위변조가 불가능한 곳(DB나 세션)에서 금액을 가져오십시요.
                //xpay.Set("LGD_AMOUNTCHECKYN", "Y");
                //xpay.Set("LGD_AMOUNT", DB_AMOUNT);
            }

        } catch (Exception e) {
            logger.info("LG유플러스 제공 API를 사용할 수 없습니다. 환경파일 설정을 확인해 주시기 바랍니다. ");
            logger.info("" + e.getMessage());
            result.put("PG 환경설정 오류.", "PG 환경설정 오류.");
        } finally {
            if (!isInitPay) {
                logger.info("========================== API Gonfiguration Fail ==========================");
                return param;
            }
        }

        /*
         *************************************************
         * 1.최종결제 요청(수정하지 마세요) - END
         *************************************************
         */

        /*
         * 2. 최종결제 요청 결과처리
         *
         * 최종 결제요청 결과 리턴 파라미터는 연동메뉴얼을 참고하시기 바랍니다.
         */
        // (4) TX: lgdacom.conf에 설정된 URL로 소켓 통신하여 최종 인증요청, 결과값으로 true, false 리턴
        if (xpay.TX()) {
            //1)결제결과 화면처리(성공,실패 결과 처리를 하시기 바랍니다.)

            /*
             * todo : 아래 에러가 뜨지만 정상작동한다 ㅡ.ㅡ;
             * java.io.FileNotFoundException: C:\\Users\\user\\git\\carssum-deal\\carssum-deal\\src\\main\\resources\\static\\lgdacom\\log\\log_20191219.log (지정된 경로를 찾을 수 없습니다)

             * */
            logger.info("TX 결제요청 Response_code = " + xpay.m_szResCode + "<br>");
            logger.info("TX 결제요청 Response_msg = " + xpay.m_szResMsg + "<p>");

            logger.info("거래번호 : " + xpay.Response("LGD_TID", 0) + "<br>");
            logger.info("상점아이디 : " + xpay.Response("LGD_MID", 0) + "<br>");
            logger.info("상점주문번호 : " + xpay.Response("LGD_OID", 0) + "<br>");
            logger.info("결제금액 : " + xpay.Response("LGD_AMOUNT", 0) + "<br>");
            logger.info("결과코드 : " + xpay.Response("LGD_RESPCODE", 0) + "<br>");
            logger.info("결과메세지 : " + xpay.Response("LGD_RESPMSG", 0) + "<p>");

            for (int i = 0; i < xpay.ResponseNameCount(); i++) {
                logger.info(xpay.ResponseName(i) + " = ");
                for (int j = 0; j < xpay.ResponseCount(); j++) {
                    logger.info("\t" + xpay.Response(xpay.ResponseName(i), j) + "<br>");
                }
            }
            logger.info("<p>");

            if ("0000".equals(xpay.m_szResCode)) {
                //최종결제요청 결과 성공 DB처리
                logger.info("최종결제요청 결과 성공 DB처리하시기 바랍니다.<br>");

                //DB처리전 res를 2(파라미터오류)로 셋팅
                res = 2;

                boolean isDBOK = true; //DB처리 실패시 false로 변경해 주세요.
                String urIdx = payment.getUrIdx();
                String quIdx = payment.getQuIdx();
                String qrIdx = payment.getQrIdx();

                //DATABASE INSERT

                logger.info("database save start");

                //데이터조회
                try {

					/*
						1. insertPaymentLog
						2. insertReserveMaster
						3. insertReserve
						4. insertPaymentDetail
						
						위순서로 INSERT 처리가 된다. LOG는 성공, 실패시 무조건 INSERT되며
						3,4에서 EXCEPTION이 걸릴 경우 TRANSACTION을 통해 ALL ROLLBACK 처리 된다.
					 */

                    DochaPaymentLogDto paymentLogDto = new DochaPaymentLogDto();

                    //RM_IDX(예약번호) 생성
                    String rmIdx = KeyMaker.getInsetance().getKeyDeafult("RM");

                    //PL_IDX 생성
                    //insertPaymentLog 시작 ===============================================================================
                    String PL_IDX = KeyMaker.getInsetance().getKeyAddRandomDigit("PL", 3);

                    String LGD_AMOUNT = xpay.Response("LGD_AMOUNT", 0);

                    String APPROVAL_YN = "";
                    String APPROVAL_NUMBER = xpay.Response("LGD_TID", 0);
                    if ("0000".equals(xpay.m_szResCode)) {
                        APPROVAL_YN = "Y";
                    } else {
                        APPROVAL_YN = "N";
                    }

                    String LGD_TIMESTAMP = xpay.Response("LGD_TIMESTAMP", 0);

                    paymentLogDto.setPlIdx(PL_IDX);
                    paymentLogDto.setRmIdx(rmIdx);
                    paymentLogDto.setPaymentAmount(LGD_AMOUNT);
                    paymentLogDto.setPaymentRequestAmount(LGD_AMOUNT);
                    paymentLogDto.setApprovalYn(APPROVAL_YN);
                    paymentLogDto.setApprovalNumber(APPROVAL_NUMBER);
                    paymentLogDto.setPaymentDate(LGD_TIMESTAMP);

                    try {

                        int errCd = paymentService.insertPaymentLog(paymentLogDto);

                        if (errCd > 0) {
                            logger.info("결제요청 성공 로그 DB저장 완료");
                        } else {
                            logger.info("결제요청 실패 로그 DB저장 실패");
                        }

                    } catch (Exception e) {
                        logger.error("========================== PayLog Insert ERROR ");
                        logger.error("ERROR MSG", e);
                    }
                    logger.info("SaveLog ==========================");

                    //insertPaymentLog 종료 ===============================================================================

                    logger.info("insertReserveMaster ==========================");

                    //selectUserQuoteInfo
                    DochaMap paramMap = new DochaMap();
                    paramMap.set("quoteStatus", "QO"); //견적요청상태
                    paramMap.set("quIdx", quIdx); //견적요청번호
                    paramMap.set("urIdx", urIdx);        //유져번호
                    paramMap.set("qrIdx", qrIdx);        //유져번호


                    String crIdx = "";
                    String carTypeCode = "";
                    String rtIdx = "";
                    String companyName = "";
                    String reserveDate = "";

                    String paymentDate = "";
                    String carDeposit = "";
                    String rentFee = "";
                    String insuranceFee = "";
                    String discountFee = "";

                    String paymentAmount = "";
                    String cancelFee = "";
                    String cancelAmount = "";
                    String cancelReason = "";
                    String firstDriverName = "";
                    String ulIdx1 = "";
                    String secondDriverName = "";
                    String ulIdx2 = "";
                    String reserveMEtc = "";
                    String regId = "";
                    String regDt = "";
                    String modId = "";
                    String modDt = "";
                    String delYn = "";

                    String reserveTypeCode = "";
                    String reserveStatusCode = "";
                    String longTerm = "";
                    String reserveUserName = "";
                    String rentStartDay = "";
                    String rentEndDay = "";
                    String rentStartTime = "";
                    String rentEndTime = "";
                    String deliveryTypeCode = "";
                    String returnTypeCode = "";
                    String deliveryAddr = "";
                    String returnAddr = "";

                    DochaPaymentDto dochaPaymentReserveMasterDto = new DochaPaymentDto();
                    DochaUserInfoDto paramUserInfoDto = new DochaUserInfoDto();
                    DochaUserInfoDto responseUserInfoDto = new DochaUserInfoDto();

                    DochaPaymentReserveDto quoteUserDto = new DochaPaymentReserveDto();

                    if (StringUtil.isEmpty(payment.getCarSearchYn()) || "N".equals(payment.getCarSearchYn())) {

                        quoteUserDto = quoteUserservice.selectUserQuoteInfoUsingPayment(paramMap);

                        DochaMap userParam = new DochaMap();
                        userParam.set("quIdx", quIdx); //견적요청번호
                        userParam.set("urIdx", urIdx);        //유져번호


                        paramUserInfoDto.setUrIdx(urIdx);
                        responseUserInfoDto = userinfoService.selectUserInfo(paramUserInfoDto);

                        reserveTypeCode = xpay.Response("LGD_RESPCODE", 0);
                        reserveStatusCode = "";
                        longTerm = "";

                        if ("장기".equals(quoteUserDto.getLongtermYn())) { //장기
                            longTerm = "LT";
                        } else { // 단기
                            longTerm = "ST";
                        }

                        logger.info("daylog");

                        reserveUserName = responseUserInfoDto.getUserName(); //예약자명
                        rentStartDay = quoteUserDto.getRentStartDay(); //대여시작일자
                        rentEndDay = quoteUserDto.getRentEndDay(); //대여종료일자
                        rentStartTime = quoteUserDto.getRentStartTime(); //"대여시작시간
                        rentEndTime = quoteUserDto.getRentEndTime(); //대여종료시간

                        deliveryTypeCode = quoteUserDto.getDeliveryTypeCode(); //배차방법
                        returnTypeCode = quoteUserDto.getReturnTypeCode(); //배차방법

                        deliveryAddr = null;
                        returnAddr = null; //배차주소

                        //배차방법이 지점방문이면 지점주소를 넣음
                        if ("OF".equals(deliveryTypeCode)) {
                            deliveryAddr = quoteUserDto.getCompanyAddress(); //지점주소
                            returnAddr = quoteUserDto.getCompanyAddress(); //지점주소
                        } else {
                            deliveryAddr = quoteUserDto.getDeliveryAddr(); //배차주소
                            returnAddr = quoteUserDto.getReturnAddr(); //반차주소
                        }

                        //반차방법이 지점방문이면 지점주소를 넣음
						/*
						if("OF".equals(returnTypeCode)) {
							returnAddr 	 = quoteUserDto.getCompanyAddress(); //지점주소
						}else {
							returnAddr 	 = quoteUserDto.getReturnAddr(); //반차주소
						}
						*/

                        crIdx = quoteUserDto.getCrIdx(); //반차방법
                        carTypeCode = quoteUserDto.getCartypeCode(); //반차주소
                        rtIdx = quoteUserDto.getRtIdx(); //차량idx
                        companyName = quoteUserDto.getCompanyName(); //차종code
                        reserveDate = ""; //제휴사idx

                        reserveDate = quoteUserDto.getReserveDate(); //예약일은 확인해야함...
                        paymentDate = LGD_TIMESTAMP;
                        carDeposit = quoteUserDto.getCarDeposit();


                        rentFee = quoteUserDto.getRentFee();
                        insuranceFee = quoteUserDto.getInsuranceFee();
                        discountFee = quoteUserDto.getDiscountFee();

                        quoteUserDto.setPaymentAmount(LGD_AMOUNT);
                        paymentAmount = quoteUserDto.getPaymentAmount();
                        cancelFee = quoteUserDto.getCancelFee();
                        cancelAmount = quoteUserDto.getCancelAmount();
                        cancelReason = quoteUserDto.getCancelReason();
                        firstDriverName = quoteUserDto.getUserName(); //제1 운전자 - 예약자 본인
                        ulIdx1 = quoteUserDto.getUrIdx();    //제1운전자 ur_idx 정보
                        secondDriverName = quoteUserDto.getSecondDriverName();
                        ulIdx2 = quoteUserDto.getUlIdx2();
                        reserveMEtc = ""; //master에서 비고로 쓰임.
                        regId = quoteUserDto.getRegId();
                        regDt = quoteUserDto.getRegDt();
                        modId = quoteUserDto.getModId();
                        modDt = quoteUserDto.getModDt();
                        delYn = quoteUserDto.getDelYn();


                        reserveStatusCode = "QC";

                        dochaPaymentReserveMasterDto.setRmIdx(rmIdx);
                        dochaPaymentReserveMasterDto.setReserveTypeCode(reserveTypeCode);
                        dochaPaymentReserveMasterDto.setReserveStatusCode(reserveStatusCode);
                        dochaPaymentReserveMasterDto.setLongTermYn(longTerm);
                        dochaPaymentReserveMasterDto.setUrIdx(payment.getUrIdx());
                        dochaPaymentReserveMasterDto.setReserveUserName(reserveUserName);
                        dochaPaymentReserveMasterDto.setRentStartDay(rentStartDay);
                        dochaPaymentReserveMasterDto.setRentEndDay(rentEndDay);
                        dochaPaymentReserveMasterDto.setRentStartTime(rentStartTime);
                        dochaPaymentReserveMasterDto.setRentEndTime(rentEndTime);
                        dochaPaymentReserveMasterDto.setDeliveryTypeCode(deliveryTypeCode);
                        dochaPaymentReserveMasterDto.setDeliveryAddr(deliveryAddr);
                        dochaPaymentReserveMasterDto.setReturnTypeCode(returnTypeCode);
                        dochaPaymentReserveMasterDto.setReturnAddr(returnAddr);
                        dochaPaymentReserveMasterDto.setCrIdx(crIdx);
                        dochaPaymentReserveMasterDto.setCarTypeCode(carTypeCode);
                        dochaPaymentReserveMasterDto.setRtIdx(rtIdx);
                        dochaPaymentReserveMasterDto.setCompanyName(companyName);
                        dochaPaymentReserveMasterDto.setReserveDate(reserveDate);
                        dochaPaymentReserveMasterDto.setPaymentDate(paymentDate);
                        dochaPaymentReserveMasterDto.setCarDeposit(carDeposit);
                        dochaPaymentReserveMasterDto.setRentFee(rentFee);
                        dochaPaymentReserveMasterDto.setInsuranceFee(insuranceFee);
                        dochaPaymentReserveMasterDto.setDiscountFee(discountFee);
                        dochaPaymentReserveMasterDto.setPaymentAmount(paymentAmount);
                        dochaPaymentReserveMasterDto.setCancelFee(cancelFee);
                        dochaPaymentReserveMasterDto.setCancelAmount(cancelAmount);
                        dochaPaymentReserveMasterDto.setCancelReason(cancelReason);
                        dochaPaymentReserveMasterDto.setQuIdx(quIdx);
                        dochaPaymentReserveMasterDto.setFirstDriverName(firstDriverName);
                        dochaPaymentReserveMasterDto.setUlIdx1(ulIdx1);
                        dochaPaymentReserveMasterDto.setSecondDriverName(secondDriverName);
                        dochaPaymentReserveMasterDto.setUlIdx2(ulIdx2);
                        dochaPaymentReserveMasterDto.setReserveMEtc(reserveMEtc);
                        dochaPaymentReserveMasterDto.setRegId(urIdx);
                        dochaPaymentReserveMasterDto.setRegDt(regDt);
                        dochaPaymentReserveMasterDto.setModId(modId);
                        dochaPaymentReserveMasterDto.setModDt(modDt);
                        dochaPaymentReserveMasterDto.setDelYn(delYn);

                    } else {
                        reserveTypeCode = xpay.Response("LGD_RESPCODE", 0);
                        reserveStatusCode = "";


                        longTerm = payment.getLongterm();

                        logger.info("daylog");

                        DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();

                        reserveUserName = loginSessionInfo.getUserName();
                        rentStartDay = payment.getRentStartDay();
                        rentEndDay = payment.getRentEndDay();
                        rentStartTime = payment.getRentStartTime();
                        rentEndTime = payment.getRentEndTime();


                        deliveryTypeCode = payment.getDeliveryTypeCode();
                        returnTypeCode = quoteUserDto.getReturnTypeCode(); //배차방법

                        crIdx = payment.getCrIdx();


                        deliveryAddr = null;
                        returnAddr = null;


                        String rentStartDt = rentStartDay + rentStartTime;
                        String rentEndDt = rentEndDay + rentEndTime;

                        DochaMap reqParam = new DochaMap();
                        reqParam.set("crIdx", crIdx);
                        reqParam.set("rentStartDate", rentStartDt);
                        reqParam.set("rentEndDate", rentEndDt);
                        List<DochaCarSearchPaymentDetailDto> carInfoList = carSearchService.selectCarSearchDetail(reqParam);
                        //배차방법이 지점방문이면 지점주소를 넣음
                        if ("OF".equals(deliveryTypeCode)) {
                            deliveryAddr = carInfoList.get(0).getCompanyAddress(); //지점주소
                            returnAddr = carInfoList.get(0).getCompanyAddress(); //지점주소
                        } else {
                            deliveryAddr = payment.getDeliveryAddr();
                            returnAddr = payment.getReturnAddr();
                        }

                        urIdx = loginSessionInfo.getUrIdx();
                        regId = loginSessionInfo.getUrIdx();
                        crIdx = payment.getCrIdx(); //반차방법
                        carTypeCode = carInfoList.get(0).getCartypeCode(); //반차주소
                        rtIdx = carInfoList.get(0).getRtIdx(); //차량idx
                        companyName = carInfoList.get(0).getCompanyName(); //차종code
                        reserveDate = payment.getReserveDate();
                        paymentDate = LGD_TIMESTAMP;

                        carDeposit = carInfoList.get(0).getLongTermDeposit() == null ? "0" : carInfoList.get(0).getLongTermDeposit();
                        rentFee = carInfoList.get(0).getRentFee();

                        insuranceFee = carInfoList.get(0).getInsuranceFee();
                        discountFee = carInfoList.get(0).getDisRentFee();
                        paymentAmount = LGD_AMOUNT;
                        cancelFee = "0";
                        cancelAmount = "0";
                        cancelReason = "0";
                        firstDriverName = loginSessionInfo.getUserName(); //제1 운전자 - 예약자 본인
                        ulIdx1 = loginSessionInfo.getUrIdx();    //제1운전자 ur_idx 정보
                        secondDriverName = "";
                        ulIdx2 = "";
                        reserveMEtc = ""; //master에서 비고로 쓰임.
                        regId = loginSessionInfo.getUrIdx();
                        regDt = reserveDate;
                        modId = "";
                        modDt = "";
                        delYn = "N";
                        reserveStatusCode = "RS"; //예약완료

                        dochaPaymentReserveMasterDto.setRmIdx(rmIdx);
                        dochaPaymentReserveMasterDto.setUrIdx(urIdx);
                        dochaPaymentReserveMasterDto.setReserveTypeCode(reserveTypeCode);
                        dochaPaymentReserveMasterDto.setReserveStatusCode(reserveStatusCode);
                        dochaPaymentReserveMasterDto.setLongTermYn(longTerm);
                        dochaPaymentReserveMasterDto.setUrIdx(payment.getUrIdx());
                        dochaPaymentReserveMasterDto.setReserveUserName(reserveUserName);
                        dochaPaymentReserveMasterDto.setRentStartDay(rentStartDay);
                        dochaPaymentReserveMasterDto.setRentEndDay(rentEndDay);
                        dochaPaymentReserveMasterDto.setRentStartTime(rentStartTime);
                        dochaPaymentReserveMasterDto.setRentEndTime(rentEndTime);
                        dochaPaymentReserveMasterDto.setDeliveryTypeCode(deliveryTypeCode);
                        dochaPaymentReserveMasterDto.setDeliveryAddr(deliveryAddr);
                        dochaPaymentReserveMasterDto.setReturnTypeCode(returnTypeCode);
                        dochaPaymentReserveMasterDto.setReturnAddr(returnAddr);
                        dochaPaymentReserveMasterDto.setCrIdx(crIdx);
                        dochaPaymentReserveMasterDto.setCarTypeCode(carTypeCode);
                        dochaPaymentReserveMasterDto.setRtIdx(rtIdx);
                        dochaPaymentReserveMasterDto.setCompanyName(companyName);
                        dochaPaymentReserveMasterDto.setReserveDate(reserveDate);
                        dochaPaymentReserveMasterDto.setPaymentDate(paymentDate);
                        dochaPaymentReserveMasterDto.setCarDeposit(carDeposit);
                        dochaPaymentReserveMasterDto.setRentFee(rentFee);
                        dochaPaymentReserveMasterDto.setInsuranceFee(insuranceFee);
                        dochaPaymentReserveMasterDto.setDiscountFee(discountFee);
                        dochaPaymentReserveMasterDto.setPaymentAmount(paymentAmount);
                        dochaPaymentReserveMasterDto.setCancelFee(cancelFee);
                        dochaPaymentReserveMasterDto.setCancelAmount(cancelAmount);
                        dochaPaymentReserveMasterDto.setCancelReason(cancelReason);
                        dochaPaymentReserveMasterDto.setQuIdx(quIdx);
                        dochaPaymentReserveMasterDto.setFirstDriverName(firstDriverName);
                        dochaPaymentReserveMasterDto.setUlIdx1(ulIdx1);
                        dochaPaymentReserveMasterDto.setSecondDriverName(secondDriverName);
                        dochaPaymentReserveMasterDto.setUlIdx2(ulIdx2);
                        dochaPaymentReserveMasterDto.setReserveMEtc(reserveMEtc);
                        dochaPaymentReserveMasterDto.setRegId(urIdx);
                        dochaPaymentReserveMasterDto.setRegDt(regDt);
                        dochaPaymentReserveMasterDto.setModId(modId);
                        dochaPaymentReserveMasterDto.setModDt(modDt);
                        dochaPaymentReserveMasterDto.setDelYn(delYn);
                    }


                    //PL_IDX를 가지고 있다가 결제정보를 업데이트 한 뒤에 최종 LOG테이블에
                    //PD_IDX를 업데이트 해야한다.

                    //2. insertReserveMaster
                    //3. insertReserve
                    //2번과 안겹치는 parameter만..
                    String reIdx = "";
                    //String quIdx 			= "";
                    String reserveChannel = "";


                    DochaPaymentReserveDto dochaPaymentReserveDto = new DochaPaymentReserveDto();

                    //rmIdx = KeyMaker.getInsetance().getKeyDeafult("RM");
                    reIdx = xpay.Response("LGD_OID", 0); //KeyMaker.getInsetance().getKeyDeafult("RE");

                    reserveStatusCode = "QC";
                    dochaPaymentReserveDto.setReIdx(reIdx);
                    dochaPaymentReserveDto.setRmIdx(rmIdx);
                    dochaPaymentReserveDto.setReserveStatusCode(reserveStatusCode);
                    dochaPaymentReserveDto.setRentStartDay(rentStartDay);
                    dochaPaymentReserveDto.setRentEndDay(rentEndDay);
                    dochaPaymentReserveDto.setRentStartTime(rentStartTime);
                    dochaPaymentReserveDto.setRentEndTime(rentEndTime);
                    dochaPaymentReserveDto.setCarDeposit(carDeposit);
                    dochaPaymentReserveDto.setRentFee(rentFee);
                    dochaPaymentReserveDto.setInsuranceFee(insuranceFee);
                    dochaPaymentReserveDto.setDiscountFee(discountFee);
                    dochaPaymentReserveDto.setPaymentAmount(paymentAmount);
                    dochaPaymentReserveDto.setCancelFee(cancelFee);
                    dochaPaymentReserveDto.setCancelAmount(cancelAmount);
                    dochaPaymentReserveDto.setReserveEtc("");
                    dochaPaymentReserveDto.setRegId(urIdx);
                    dochaPaymentReserveDto.setRegDt(regDt);

                    //4. insertPaymentDetail
                    ArrayList<DochaPaymentDetailDto> payDetailList = new ArrayList<DochaPaymentDetailDto>();

                    String paymentCode = xpay.Response("LGD_RESPCODE", 0);  //성공이면 0000
                    String approvalNumber = xpay.Response("LGD_TID", 0);

                    //금액별 PAYDETAIL 등록을 위한 로직

                    int intInsuranceFee = 0;
                    int intCarDeposit = 0;

                    //보험료 존재여부 확인
                    if (!StringUtil.isEmpty(dochaPaymentReserveMasterDto.getInsuranceFee())) {
                        //보혐료가 0보다 클경우 PAYDETAIL에 보험료 항목 등록
                        if (StringUtil.isEmptyConvertInt(dochaPaymentReserveMasterDto.getInsuranceFee(), 0) > 0) {

                            intInsuranceFee = StringUtil.isEmptyConvertInt(payment.getInsuranceFee(), 0);

                            DochaPaymentDetailDto dochaPaymentDetailDto = new DochaPaymentDetailDto();

                            String pdIdx = KeyMaker.getInsetance().getKeyAddRandomDigitForLoof("PD", 3);

                            dochaPaymentDetailDto.setPdIdx(pdIdx);
                            dochaPaymentDetailDto.setRmIdx(rmIdx);
                            dochaPaymentDetailDto.setUrIdx(payment.getUrIdx());
                            dochaPaymentDetailDto.setPaymentAmount(payment.getInsuranceFee());
                            //PG사에 따른 코드
                            dochaPaymentDetailDto.setPgCode("LG");
                            //결제수단 카드이므로 CD, 차후 결제수단 추가시 로직 수정 필요
                            dochaPaymentDetailDto.setPaymentTypeCode("CD");
                            //결제금액종류 보험금액 코드 등록
                            dochaPaymentDetailDto.setPaymentKindCode("IS");
                            dochaPaymentDetailDto.setApprovalNumber(approvalNumber);
                            dochaPaymentDetailDto.setPaymentDate(paymentDate);

                            payDetailList.add(dochaPaymentDetailDto);
                        }

                    }

                    //보증금 존재여부 확인
                    if (!StringUtil.isEmpty(dochaPaymentReserveMasterDto.getCarDeposit())) {
                        //보증금 0보다 클경우 PAYDETAIL에 보증금 항목 등록
                        if (StringUtil.isEmptyConvertInt(dochaPaymentReserveMasterDto.getCarDeposit(), 0) > 0) {

                            intCarDeposit = StringUtil.isEmptyConvertInt(payment.getCarDeposit(), 0);

                            DochaPaymentDetailDto dochaPaymentDetailDto = new DochaPaymentDetailDto();

                            String pdIdx = KeyMaker.getInsetance().getKeyAddRandomDigitForLoof("PD", 3);

                            dochaPaymentDetailDto.setPdIdx(pdIdx);
                            dochaPaymentDetailDto.setRmIdx(rmIdx);
                            dochaPaymentDetailDto.setUrIdx(payment.getUrIdx());
                            dochaPaymentDetailDto.setPaymentAmount(payment.getCarDeposit());
                            //PG사에 따른 코드
                            dochaPaymentDetailDto.setPgCode("LG");
                            //결제수단 카드이므로 CD, 차후 결제수단 추가시 로직 수정 필요
                            dochaPaymentDetailDto.setPaymentTypeCode("CD");
                            //결제금액종류 보증금 코드 등록
                            dochaPaymentDetailDto.setPaymentKindCode("DP");
                            dochaPaymentDetailDto.setApprovalNumber(approvalNumber);
                            dochaPaymentDetailDto.setPaymentDate(paymentDate);

                            payDetailList.add(dochaPaymentDetailDto);
                        }

                    }

                    //대여료 존재여부 확인
                    if (!StringUtil.isEmpty(dochaPaymentReserveMasterDto.getRentFee())) {
                        //대여료가 0보다 클경우 PAYDETAIL에 대여료 항목 등록
                        if (StringUtil.isEmptyConvertInt(dochaPaymentReserveMasterDto.getRentFee(), 0) > 0) {
                            DochaPaymentDetailDto dochaPaymentDetailDto = new DochaPaymentDetailDto();

                            String pdIdx = KeyMaker.getInsetance().getKeyAddRandomDigitForLoof("PD", 3);

                            int intPaymentAmount = StringUtil.isEmptyConvertInt(paymentAmount, 0) - intInsuranceFee - intCarDeposit;

                            dochaPaymentDetailDto.setPdIdx(pdIdx);
                            dochaPaymentDetailDto.setRmIdx(rmIdx);
                            dochaPaymentDetailDto.setUrIdx(payment.getUrIdx());
                            dochaPaymentDetailDto.setPaymentAmount(Integer.toString(intPaymentAmount));
                            //PG사에 따른 코드
                            dochaPaymentDetailDto.setPgCode("LG");
                            //결제수단 카드이므로 CD, 차후 결제수단 추가시 로직 수정 필요
                            dochaPaymentDetailDto.setPaymentTypeCode("CD");
                            //결제금액종류 대여금액 코드 등록
                            dochaPaymentDetailDto.setPaymentKindCode("RT");
                            dochaPaymentDetailDto.setApprovalNumber(approvalNumber);
                            dochaPaymentDetailDto.setPaymentDate(paymentDate);

                            payDetailList.add(dochaPaymentDetailDto);
                        }
                    }

                    //취소수수료 존재여부 확인
                    if (!StringUtil.isEmpty(dochaPaymentReserveMasterDto.getCancelFee())) {
                        //취소수수료 0보다 클경우 PAYDETAIL에 취소수수료 항목 등록
                        if (StringUtil.isEmptyConvertInt(dochaPaymentReserveMasterDto.getInsuranceFee(), 0) > 0) {

                            DochaPaymentDetailDto dochaPaymentDetailDto = new DochaPaymentDetailDto();

                            String pdIdx = KeyMaker.getInsetance().getKeyAddRandomDigitForLoof("PD", 3);

                            dochaPaymentDetailDto.setPdIdx(pdIdx);
                            dochaPaymentDetailDto.setRmIdx(rmIdx);
                            dochaPaymentDetailDto.setUrIdx(payment.getUrIdx());
                            dochaPaymentDetailDto.setPaymentAmount(payment.getCancelFee());
                            //PG사에 따른 코드
                            dochaPaymentDetailDto.setPgCode("LG");
                            //결제수단 카드이므로 CD, 차후 결제수단 추가시 로직 수정 필요
                            dochaPaymentDetailDto.setPaymentTypeCode("CD");
                            //결제금액종류 취소수수료 코드 등록
                            dochaPaymentDetailDto.setPaymentKindCode("CF");
                            dochaPaymentDetailDto.setApprovalNumber(approvalNumber);
                            dochaPaymentDetailDto.setPaymentDate(paymentDate);

                            payDetailList.add(dochaPaymentDetailDto);
                        }

                    }

                    //취소금액 존재여부 확인
                    if (!StringUtil.isEmpty(dochaPaymentReserveMasterDto.getCancelAmount())) {
                        //취소금액 0보다 클경우 PAYDETAIL에 취소금액 항목 등록
                        if (StringUtil.isEmptyConvertInt(dochaPaymentReserveMasterDto.getInsuranceFee(), 0) > 0) {

                            DochaPaymentDetailDto dochaPaymentDetailDto = new DochaPaymentDetailDto();

                            String pdIdx = KeyMaker.getInsetance().getKeyAddRandomDigitForLoof("PD", 3);

                            dochaPaymentDetailDto.setPdIdx(pdIdx);
                            dochaPaymentDetailDto.setRmIdx(rmIdx);
                            dochaPaymentDetailDto.setUrIdx(payment.getUrIdx());
                            dochaPaymentDetailDto.setPaymentAmount(payment.getCancelAmount());
                            //PG사에 따른 코드
                            dochaPaymentDetailDto.setPgCode("LG");
                            //결제수단 카드이므로 CD, 차후 결제수단 추가시 로직 수정 필요
                            dochaPaymentDetailDto.setPaymentTypeCode("CD");
                            //결제금액종류 취소금액 코드 등록
                            dochaPaymentDetailDto.setPaymentKindCode("CN");
                            dochaPaymentDetailDto.setApprovalNumber(approvalNumber);
                            dochaPaymentDetailDto.setPaymentDate(paymentDate);

                            payDetailList.add(dochaPaymentDetailDto);
                        }

                    }

                    //5.  CDT_RESERVE_MASTER의 PDIDX를  DochaPaymentDetailDto로부터 UPDATE 한다.

                    DochaMap insertPaymentRes = insertPaymentData(dochaPaymentReserveMasterDto, dochaPaymentReserveDto, payDetailList, qrIdx, payment);

                    boolean insertResult = insertPaymentRes.getBoolean("result");

                    //insert중 오류발생시 발생위치에 따라 retrun 파라미터 셋팅
                    if (!insertResult) {
                        Exception e = (Exception) insertPaymentRes.get("error");
                        int insertResultInt = insertPaymentRes.getInt("resultInt");

                        if (insertResultInt == 2) {
                            //결제내용저장중 오류
                            res = 3;
                        } else {
                            //예약내용 저장 및 수정중 오류
                            res = 4;
                        }

                        throw e;
                    }

                    //System.out.println("DochaPaymentLogDto update" + dochaPaymentDetailDto.getPlIdx());

                    //저장 성공 후 log Table pdidx update
                    //DochaPaymentLogDto LogParamDto = new DochaPaymentLogDto();
                    //LogParamDto.setPlIdx(PL_IDX);
                    //LogParamDto.setPdIdx(dochaPaymentDetailDto.getPdIdx());

                    System.out.println("DochaPaymentLogDto update : " + PL_IDX);

                    //paymentService.updatePaymentLog(LogParamDto);
                    result.put("crIdx", quoteUserDto.getCrIdx());
                    result.put("quIdx", quIdx);
                    result.put("urIdx", urIdx);

                    param.set("crIdx", quoteUserDto.getCrIdx());
                    param.set("quIdx", quIdx);
                    param.set("urIdx", urIdx);
                    param.set("urIdx", dochaPaymentReserveDto.getRmIdx());

                    payment.setRmIdx(dochaPaymentReserveDto.getRmIdx());

                } catch (Exception e) {
                    logger.info("최종결제요청 결과 실패 DB처리하시기 바랍니다.<br>");
                    logger.info("DB처리중 에러가 발생했습니다..<br>");
                    logger.info("Reserve Insert Error ==========================");
                    logger.error("ERROR", e);

                    isDBOK = false;

                } finally {
                    //최종결제요청 결과 성공 DB처리 실패시 Rollback 처리
                    if (!isDBOK) {
                        xpay.Rollback("상점 DB처리 실패로 인하여 Rollback 처리 [TID:" + xpay.Response("LGD_TID", 0) + ",MID:"
                                + xpay.Response("LGD_MID", 0) + ",OID:" + xpay.Response("LGD_OID", 0) + "]");

                        logger.info("TX Rollback Response_code = " + xpay.Response("LGD_RESPCODE", 0) + "<br>");
                        logger.info("TX Rollback Response_msg = " + xpay.Response("LGD_RESPMSG", 0) + "<p>");

                        //error Log

                        if ("0000".equals(xpay.m_szResCode)) {
                            logger.info("자동취소가 정상적으로 완료 되었습니다.<br>");
                        } else {
                            logger.info("자동취소가 정상적으로 처리되지 않았습니다.<br>");
                        }

                        int errCd = saveErrorLog(xpay, new DochaPaymentLogDto());

                        if (errCd > 0) {
                            logger.info("결제요청 실패 로그 DB저장 완료");
                        } else {
                            logger.info("결제요청 실패 로그 DB저장 실패");
                        }

                        //((PgService) getThis()).updatePaymentTransferNewTx(lgdDto, xpay);
                        //result.put(BaseConstants.RESULT_MESSAGE, xpay.Response("LGD_RESPMSG", 0));

                        param.set("result", res);
                        return param;
                    } else {
                        //pay결제 및 db insert 성공
                        res = 0;
                        param.set("result", res);
                    }
                }

            } else {
                //최종결제요청 결과 실패 DB처리
                logger.info("최종결제요청 결과 실패 DB처리하시기 바랍니다.<br>");

                logger.info("결제요청이 실패하였습니다.  <br>");
                logger.info("TX 결제요청 Response_code = " + xpay.m_szResCode + "<br>");
                logger.info("TX 결제요청 Response_msg = " + xpay.m_szResMsg + "<p>");

                int errCd = saveErrorLog(xpay, new DochaPaymentLogDto());

                if (errCd > 0) {
                    logger.info("결제요청 실패 로그 DB저장 완료");
                } else {
                    logger.info("결제요청 실패 로그 DB저장 실패");
                }

                //결제요청 실패의 경우 LGU+에서 별도 에러코드를 제공하지 않고 0000 제외 모든 에러 코드는 각 카드사마다 코드 및 양식이 다름
                //결제에시 결제 오류 PAGE에 내용을 보여주기 위해 해당 응답코드와 msg를 담아서 retrun

                res = 5;
                param.set("result", res);

                param.set("m_szResCode", xpay.m_szResCode);
                param.set("m_szResMsg", xpay.m_szResMsg);

                return param;

            }
        } else {
            //2)API 요청실패 화면처리
            logger.info("결제요청이 실패하였습니다.  <br>");
            logger.info("TX 결제요청 Response_code = " + xpay.m_szResCode + "<br>");
            logger.info("TX 결제요청 Response_msg = " + xpay.m_szResMsg + "<p>");

            //최종결제요청 결과 실패 DB처리
            logger.info("최종결제요청 결과 실패 DB처리하시기 바랍니다.<br>");

            //error Log
            DochaPaymentLogDto paymentLogDto = new DochaPaymentLogDto();

            int errCd = saveErrorLog(xpay, paymentLogDto);

            if (errCd > 0) {
                logger.info("결제요청 실패 로그 DB저장 완료");
            } else {
                logger.info("결제요청 실패 로그 DB저장 실패");
            }

            //결제요청 실패
            res = 6;
            param.set("result", res);
            param.set("m_szResCode", xpay.m_szResCode);
            param.set("m_szResMsg", xpay.m_szResMsg);

            return param;
        }

        return param;
    }

    /**
     * Null 문자 체크
     *
     * @param str
     * @return
     */
    public static String nvl(String str) {
        return nvl(str, "");
    }

    /**
     * Null 문자 체크
     *
     * @param str
     * @param replacer
     * @return
     */
    public static String nvl(String str, String replacer) {
        if (str == null || "".equals(str))
            return replacer;
        else
            return str;
    }

    /*
     * 1. insertLog (MUST)
     *
     * */
    public void insertLog(XPayClient xpay, String division) {

        DochaMap resData = new DochaMap();
        boolean result = false;
        int errCd = 1;
        String errMsg = null;


        if (division.equals("paymentSuccess")) {
            DochaPaymentLogDto paymentLogDto = new DochaPaymentLogDto();

            //PL_IDX 생성
            String PL_IDX = KeyMaker.getInsetance().getKeyDeafult("PL");

            String LGD_AMOUNT = xpay.Response("LGD_AMOUNT", 0);

            String APPROVAL_YN = "";
            String APPROVAL_NUMBER = xpay.Response("LGD_TID", 0);  //승인번호가 이거같은데 확인해봐야할듯 LGD_TID
            if ("0000".equals(xpay.m_szResCode)) {
                APPROVAL_YN = "Y";
            } else {
                APPROVAL_YN = "N";
            }

            String LGD_TIMESTAMP = xpay.Response("LGD_TIMESTAMP", 0);

            paymentLogDto.setPlIdx(PL_IDX);
            paymentLogDto.setPaymentAmount(LGD_AMOUNT);
            paymentLogDto.setPaymentRequestAmount(LGD_AMOUNT);
            paymentLogDto.setApprovalYn(APPROVAL_YN);
            paymentLogDto.setApprovalNumber(APPROVAL_NUMBER);
            paymentLogDto.setPaymentDate(LGD_TIMESTAMP);

            errCd = paymentService.insertPaymentLog(paymentLogDto);

            if (errCd == 1) {
                errMsg = "등록성공";
                result = true;
            } else if (errCd == 2) {
                errMsg = "면허정보 등록 실패";
            } else if (errCd == 3) {
                errMsg = "중복ID";
            } else if (errCd == 4) {
                errMsg = "유저등록에 실패했습니다";
            } else if (errCd == 5) {
                errMsg = "본인인증 미확인";
            } else {
                errMsg = "시스템 에러";
            }

            resData.put("res", result);
            resData.put("errCd", errCd);
            resData.put("errMsg", errMsg);
        } else if (division.equals("paymentResInsertFail")) {  //결제 후 디비저장 실패시

        }
        //return resData;
    }//end Insert Log

    public DochaMap insertPaymentData(DochaPaymentDto dochaPaymentReserveMasterDto, DochaPaymentReserveDto dochaPaymentReserveDto,
                                      ArrayList<DochaPaymentDetailDto> dochaPaymentDetailDtoList, String qrIdx, DochaPaymentLgdDto lgDto) {
        DochaMap resData = new DochaMap();

        boolean result = false;
        int resultInt = -1;

        //예약생성
        try {


            //마스터 생성
            paymentService.insertReserveMaster(dochaPaymentReserveMasterDto);

            //예약 로그 생성
            paymentService.insertReserve(dochaPaymentReserveDto);
        } catch (Exception e) {

            result = false;
            resultInt = 1;

            resData.put("result", result);
            resData.put("resultInt", resultInt);
            resData.put("error", e);

            return resData;
        }

        //payDetail 생성
        try {
            for (DochaPaymentDetailDto dto : dochaPaymentDetailDtoList) {
                paymentService.insertPaymentDetail(dto);
            }
        } catch (Exception e) {

            result = false;
            resultInt = 2;

            resData.put("result", result);
            resData.put("resultInt", resultInt);
            resData.put("error", e);

            return resData;
        }

        //TODO....
        //견적대기 결제완료
        //*    P_CODE : QTC
        // *    QO     : 견적요청
        // *    QP     : 결제요청
        // *    QC     : 결제완료
        // *    QB     : 견적취소
        //USER, CDT_QUOTE_RENT_COMPANY테이블에 UPDATE
        //CDT_QUOTE_USER

        //CDT_RESERVE_MASTER RESERVE_TYPE_CODE : AD 어드민, UR  사용자, RESERVE_STATUS_CODE :RE, AP

        try {

            if (!"Y".equals(lgDto.getCarSearchYn())) {

                //CDT_QUOTE_USER UPDATE
                //예약완료된 견적 QC로 상태 업데이트 및 선택된 회원사 견적 IDX 등록
                DochaMap paramQuoteUser = new DochaMap();
                paramQuoteUser.set("quoteStatus", "QC");
                paramQuoteUser.set("urIdx", dochaPaymentReserveMasterDto.getUrIdx());
                paramQuoteUser.set("quIdx", dochaPaymentReserveMasterDto.getQuIdx());
                paramQuoteUser.set("qrIdx", qrIdx);
                quoteUserservice.updateQuoteUser(paramQuoteUser);

                //CDT_QUOTE_RENT_COMPANY UPDATE
                //선택된 회원사 견적 QC로 상태 업데이트

                DochaMap paramRentCar = new DochaMap();
                paramRentCar.set("quoteStatus", "QC");
                paramRentCar.set("urIdx", dochaPaymentReserveMasterDto.getUrIdx());
                paramRentCar.set("quIdx", dochaPaymentReserveMasterDto.getQuIdx());
                paramRentCar.set("qrIdx", qrIdx);
                paymentService.updateCompliteQuoteRentCompany(paramRentCar);

                //선택된 회원사 견적 제외 나머지 QB로 상태 업데이트

                DochaMap paramNotChoiceRentCar = new DochaMap();

                paramNotChoiceRentCar.set("quoteStatus", "QB");
                paramNotChoiceRentCar.set("quIdx", dochaPaymentReserveMasterDto.getQuIdx());

                paymentService.updateNotChoiseQuoteRentCompany(paramNotChoiceRentCar);
            }
        } catch (Exception e) {

            result = false;
            resultInt = 3;

            resData.put("result", result);
            resData.put("resultInt", resultInt);
            resData.put("error", e);

            return resData;
        }

        result = true;
        resultInt = 0;

        resData.put("result", result);
        resData.put("resultInt", resultInt);

        return resData;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private int saveErrorLog(XPayClient xpay, DochaPaymentLogDto paymentLogDto) {

        int errCd = 0;

        try {
            //PL_IDX 생성
            String plIdx = KeyMaker.getInsetance().getKeyAddRandomDigit("PL", 3);
            String APPROVAL_NUMBER = null;
            String LGD_TIMESTAMP = null;
            String APPROVAL_YN = null;

            try {
                APPROVAL_NUMBER = xpay.Response("LGD_TID", 0);
                LGD_TIMESTAMP = xpay.Response("LGD_TIMESTAMP", 0);
            } catch (Exception e) {
                logger.error("========================== Xpay Parsing ERROR ");
                logger.error("ERROR MSG", e);
            }

            if ("0000".equals(xpay.m_szResCode)) {
                APPROVAL_YN = "Y";
            } else {
                APPROVAL_YN = "N";
            }

            paymentLogDto.setPlIdx(plIdx);
            paymentLogDto.setApprovalYn(APPROVAL_YN);
            paymentLogDto.setApprovalNumber(APPROVAL_NUMBER);
            paymentLogDto.setErrCode(xpay.m_szResCode);
            paymentLogDto.setFailMsg(xpay.m_szResMsg);
            paymentLogDto.setPaymentDate(LGD_TIMESTAMP);

            errCd = paymentService.insertPaymentLog(paymentLogDto);

        } catch (Exception e) {
            logger.error("========================== PAY LOG INSERT ERROR");
            logger.error("ERROR MSG", e);
        }

        return errCd;
    }

}
