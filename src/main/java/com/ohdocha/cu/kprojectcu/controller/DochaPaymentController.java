package com.ohdocha.cu.kprojectcu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdocha.cu.kprojectcu.domain.*;
import com.ohdocha.cu.kprojectcu.mapper.DochaPaymentDao;
import com.ohdocha.cu.kprojectcu.mapper.DochaRentcarDao;
import com.ohdocha.cu.kprojectcu.mapper.DochaScheduledDao;
import com.ohdocha.cu.kprojectcu.service.DochaCarSearchService;
import com.ohdocha.cu.kprojectcu.service.DochaPaymentService;
import com.ohdocha.cu.kprojectcu.service.DochaRentcarService;
import com.ohdocha.cu.kprojectcu.service.DochaUserInfoService;
import com.ohdocha.cu.kprojectcu.util.*;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Controller
public class DochaPaymentController extends ControllerExtension {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${import.url}")
    private String url;

    @Value("${import.imp_key}")
    private String impKey;

    @Value("${import.imp_secret}")
    private String impSecret;

    @Resource(name = "dochaRentcarService")
    DochaRentcarService rentCarService;

    @Resource(name = "carSearch")
    DochaCarSearchService carSearchService;

    @Autowired
    DochaUserInfoService userInfoService;

    @Resource(name = "payment")
    DochaPaymentService paymentService;

    @Autowired
    DochaPaymentDao paymentDao;

    @Autowired
    DochaScheduledDao scheduledDao;

    @Autowired
    DochaRentcarDao rentcarDao;

    @Autowired
    DochaAlarmTalkMsgUtil alarmTalk;

    /**
     * 결제전 화면 호출
     *
     * @param reqParam
     * @param mv
     * @param request
     * @param authentication
     * @param principal
     * @return
     */
    @RequestMapping(value = "/user/payment.do", method = RequestMethod.POST, produces = "application/x-www-form-urlencoded")
    public ModelAndView paymentDo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);

        mv.addObject("preParam", param);

        mv.setViewName("user/estimation/payment.html");
        return mv;
    }

    @RequestMapping(value = "/user/paymentExtension.do", method = RequestMethod.POST, produces = "application/x-www-form-urlencoded")
    public ModelAndView paymentExtensionDo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);

        mv.addObject("preParam", param);

        mv.setViewName("user/estimation/extension_payment.html");
        return mv;
    }

    /**
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

    @RequestMapping(value = "/user/payment/complete.do", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/x-www-form-urlencoded")
    public ModelAndView paymentCompleteDo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);

        List<DochaPaymentDto> reserveInfo = paymentDao.selectReserveInfo(param);

        mv.addObject("preParam", param);
        mv.addObject("reserveInfo", reserveInfo);

        mv.setViewName("user/estimation/payment_complete_detail_day.html");
        return mv;
    }

    @RequestMapping(value = "/user/payment/complete.json")
    @ResponseBody
    public Object paymentDetailJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {

        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        DochaMap resData = new DochaMap();

        List<DochaPaymentDto> reserveInfo = paymentDao.selectReserveInfo(param);

        resData.put("reserveInfo", reserveInfo);

        return resData;
    }

    // 예약 리스트 화면
    @RequestMapping(value = "/user/payment/completeDetail.do", method = RequestMethod.GET)
    public ModelAndView paymentCompleteDetailDo(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {

        mv.setViewName("user/estimation/payment_complete.html");
        return mv;
    }

    // 예약 리스트
    @RequestMapping(value = "/user/payment/completeList.json")
    @ResponseBody
    public Object carListJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        DochaMap resData = new DochaMap();
        DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
        param.set("urIdx", loginSessionInfo.getUrIdx());

        List<DochaPaymentDto> reserveInfo = paymentDao.selectReserveInfoList(param);
        resData.put("result", reserveInfo);

        return resData;
    }

    // 정기 결제 Webhook
    @RequestMapping(value = "/payment/webhook", method = RequestMethod.POST)
    public Object paymentsWebhook(@RequestBody Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) throws Exception {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);

        paymentOne(param, url, impKey, impSecret);

        Map<String, Object> payData = null;
        //아임포트 결제 key값을 셋팅
        String impUid = param.getString("imp_uid");
        //결제검증을 위해 아임포트 AccessToken 발급
        String accessToken = getAccessToken(impKey, impSecret, url);

        //결제검증전문
        String orgMsg = null;
        //결제검증 결과
        Map<String, Object> result = null;

        //아임포트 결제 검증 호출부분
        try {
            //아임포트 AccessToken, 결제 key값을 전달하여 결제데이터 호출
            result = getPaymentInfo(impUid, accessToken, url);

            //결제전문 중 결제관련한 데이터를 가져옴
            payData = (Map<String, Object>) result.get("response");

            //결제전문을 JSONString형태로 변환
            ObjectMapper mapper = new ObjectMapper();
            orgMsg = mapper.writeValueAsString(result);
        } catch (Exception e) {
            //에러발생시 로그처리 후 에러 throws
            logger.error("Import Connect Error", e);
            throw e;
        }

        String receiptUrl = (String) payData.get("receipt_url");

        // 스케쥴 상태, imp_uid 업데이트
        paymentDao.updateScheduleByMerchantUid(param);

        List<DochaScheduledDto> schduleInfo = paymentDao.selectSchduleInfo(param);
        param.put("rmIdx", schduleInfo.get(0).getRmIdx());

        // 어떤 예약 정보인지 가져온다.
        List<DochaPaymentDto> reserveInfoList = paymentDao.selectReserveInfoByMerchantUid(param);
        DochaPaymentDto reserveInfo = reserveInfoList.get(0);
        String nextPaymentDay;

        DochaPaymentDto paymentDto = new DochaPaymentDto();


        List<DochaScheduledDto> scheduledDtoList = paymentDao.selectNextSchduleInfo(param);
        // 다음 결제일이 없을 경우
        if (scheduledDtoList.size() == 0) {
            paymentDto.setNextPaymentDay("납부완료");
        }
        // 다음 결제 일이 있을 경우 다음 결제일 설정
        else {
            nextPaymentDay = scheduledDtoList.get(0).getPaymentDate();

            nextPaymentDay = Util.getUnixTimeToDate(nextPaymentDay);
            paymentDto.setNextPaymentDay(nextPaymentDay);
        }

        // 결제 된 금액 가져옴
        int amount = (int) payData.get("amount");

        int sumPaymentAmount = Integer.parseInt(reserveInfo.getSumPaymentAmount());
        int payCount = reserveInfo.getPayCount();
        String merchantUid = (String) param.get("merchant_uid");
        int balance = reserveInfo.getBalance();

        sumPaymentAmount = sumPaymentAmount + amount;
        payCount = payCount + 1;
        balance = balance - amount;

        paymentDto.setRmIdx(reserveInfo.getRmIdx());
        paymentDto.setSumPaymentAmount(Integer.toString(sumPaymentAmount));
        paymentDto.setPayCount(payCount);
        paymentDto.setImpUid(impUid);
        paymentDto.setMerchantUid(merchantUid);
        paymentDto.setBalance(balance);
        paymentDto.setReceiptUrl(receiptUrl);

        // 예약 테이블 업데이트
        paymentDao.updateReserveMasterByMerchantUid(paymentDto);

        // PAYMENT_DETAIL TABLE INSERT
        DochaPaymentDetailDto paymentDetailDto = new DochaPaymentDetailDto();
        String pdIdx = KeyMaker.getInsetance().getKeyDeafult("PD");
        paymentDetailDto.setPdIdx(pdIdx);
        paymentDetailDto.setRmIdx(reserveInfo.getRmIdx());
        paymentDetailDto.setUrIdx(reserveInfo.getUrIdx());
        paymentDetailDto.setPgCode("");
        paymentDetailDto.setPaymentTypeCode("");
        paymentDetailDto.setPaymentKindCode("");
        paymentDetailDto.setPaymentAmount(Integer.toString(amount));
        paymentDetailDto.setApprovalNumber("");
        paymentDao.insertPaymentDetail(paymentDetailDto);

        // PAYMENT_LOG TABLE INSERT
        DochaPaymentLogDto payLog = new DochaPaymentLogDto();
        Calendar calendar = GregorianCalendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
            calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) + 1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

        long accountExpMillis = calendar.getTimeInMillis();
        Date accountExpDate = new Date(accountExpMillis);

        String plIdx = KeyMaker.getInsetance().getKeyDeafult("PL");
        payLog.setRmIdx(reserveInfo.getRmIdx());
        payLog.setApprovalNumber("");
        payLog.setPaymentAmount(Integer.toString(amount));
        payLog.setOrgMsg(orgMsg);
        payLog.setApprovalYn("");
        payLog.setPaymentRequestAmount(Integer.toString(amount));
        payLog.setPlIdx(plIdx);
        payLog.setPdIdx(pdIdx);
        payLog.setMerchantUid(merchantUid);
        payLog.setImpUid(impUid);
        payLog.setReceiptUrl(receiptUrl);
        payLog.setAccountExpDt(accountExpDate);
        paymentDao.insertPaymentLog(payLog);

        DochaMap resData = new DochaMap();

        return resData;
    }


    @RequestMapping(value = "/payments/cancel")
    @ResponseBody
    public Object paymentsCancel(@RequestBody Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) throws Exception {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        DochaMap resData = new DochaMap();
        Map<String, Object> result = null;
        Map<String, Object> payData = null;

        List<DochaPaymentDto> reserveInfoList = paymentDao.selectReserveInfo(param);
        DochaPaymentDto reserveInfo = reserveInfoList.get(0);

        double cancelAmountDouble = param.getDouble("cancel_request_amount");
        int cancelAmount = (int) Math.floor(cancelAmountDouble);
        param.set("cancel_request_amount", cancelAmount);

        DecimalFormat numberFormat = new DecimalFormat("###,###");
        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String reserveDate = reserveInfo.getRegDt();

        if (param.get("cancelType").equals("immediately")) {            // 즉시 취소 일시
            if (reserveInfo.getSumPaymentAmount().equals("0")) {       // 결제 금액이 0원 일 경우 스케쥴만 취소
                paymentService.paymentCancelSchdule(param, url, impKey, impSecret);
                scheduledDao.updateCancelScheduleStatus(param);
                paymentDao.updateCancelScheduleReserve(param);

            } else {                                                    // 결제 금액이 있어서 결제금액을 환불해야 할 경우
                result = paymentService.paymentCancel(param, url, impKey, impSecret);
                payData = (Map<String, Object>) result.get("response");
                param.set("receiptUrl", payData.get("receipt_url"));

                paymentDao.updateCancelReserve(param);
                double totalPayCount = param.getDouble("totalPayCount");
                if (totalPayCount > 1.0) {        // 2회 이상 납부 하는 예약이면 스케쥴도 취소한다.
                    paymentService.paymentCancelSchdule(param, url, impKey, impSecret);
                }
            }


            try {
                // 즉시 취소 알림톡발송
                DochaAlarmTalkDto dto = new DochaAlarmTalkDto();

                dto.setBookDate(reserveDate.substring(0, 10) + "(" + Util.getWeekByString(reserveDate.substring(0, 10), "yyyy-MM-dd") + ") " + reserveDate.substring(11, 16)); //예약일
                dto.setCancelDate(nowDate + "(" + Util.getWeekByString(nowDate, "yyyy-MM-dd") + ") " + nowTime); //취소일시
                dto.setRentDate(reserveInfo.getRentStartDay() + "(" + Util.getWeekByString(reserveInfo.getRentStartDay(), "yyyy-MM-dd") + ") " + reserveInfo.getRentStartTime()); //렌트시작일
                dto.setReturnDate(reserveInfo.getRentEndDay() + "(" + Util.getWeekByString(reserveInfo.getRentEndDay(), "yyyy-MM-dd") + ") " + reserveInfo.getRentEndTime()); //렌트종료일
                dto.setCarName(reserveInfo.getModelName() + " " + reserveInfo.getModelDetailName());            // 차량명
                dto.setPayAmount(numberFormat.format(Integer.parseInt(reserveInfo.getSumPaymentAmount())));                                            // 결제금액
                dto.setCancelAmount(numberFormat.format(cancelAmount));                                            // 환불금액
                dto.setPhone(reserveInfo.getFirstDriverContact());                                              //알림톡 전송할 번호

                dto.setTemplateCode(DochaTemplateCodeProvider.A000007.getCode());
                //알림 톡 발송 후 로깅
                HttpResponse<JsonNode> response = alarmTalk.sendAlramTalk(dto);
                if (response.getStatus() == 200) {
                    logger.info("AlarmTalk Send Compelite");
                    logger.info(response.getBody().toPrettyString());
                } else {
                    logger.info("AlarmTalk Send Fail");
                    logger.error(response.getBody().toPrettyString());
                }
            } catch (Exception ex) {
                //알림톡 발송을 실패하더라도 오류발생시키지 않고 결제처리 완료를 위해 오류는 catch에서 로깅처리만 함
                logger.error("Error", ex);
            }


            param.put("rtIdx", reserveInfo.getRtIdx());

            // 관리자들 에게 알림톡 전송
            List<DochaRentCompanyDto> rentCompanyDtoList = rentcarDao.selectCompanyContactListForAlarmTalk(param);
            for (int i = 0; i < rentCompanyDtoList.size(); i++) {
                try {
                    DochaAlarmTalkDto dto = new DochaAlarmTalkDto();

                    dto.setBookDate(reserveDate.substring(0, 10) + "(" + Util.getWeekByString(reserveDate.substring(0, 10), "yyyy-MM-dd") + ") " + reserveDate.substring(11, 16)); //예약일
                    dto.setCancelDate(nowDate + "(" + Util.getWeekByString(nowDate, "yyyy-MM-dd") + ") " + nowTime); //취소일시
                    dto.setRentDate(reserveInfo.getRentStartDay() + "(" + Util.getWeekByString(reserveInfo.getRentStartDay(), "yyyy-MM-dd") + ") " + reserveInfo.getRentStartTime()); //렌트시작일
                    dto.setReturnDate(reserveInfo.getRentEndDay() + "(" + Util.getWeekByString(reserveInfo.getRentEndDay(), "yyyy-MM-dd") + ") " + reserveInfo.getRentEndTime()); //렌트종료일
                    dto.setCarName(reserveInfo.getModelName() + " " + reserveInfo.getModelDetailName());            // 차량명
                    dto.setCarNumber(reserveInfo.getCarNumber());            // 차량번호
                    dto.setPayAmount(numberFormat.format(Integer.parseInt(reserveInfo.getSumPaymentAmount())));                                            // 결제금액
                    dto.setCancelAmount(numberFormat.format(cancelAmount));                                            // 환불금액
                    dto.setPhone(rentCompanyDtoList.get(i).getCompanyContact1());                                              //알림톡 전송할 번호

                    dto.setTemplateCode(DochaTemplateCodeProvider.A000020.getCode());

                    //알림 톡 발송 후 로깅
                    HttpResponse<JsonNode> response = alarmTalk.sendAlramTalk(dto);
                    if (response.getStatus() == 200) {
                        logger.info("AlarmTalk Send Compelite");
                        logger.info(response.getBody().toPrettyString());
                    } else {
                        logger.info("AlarmTalk Send Fail");
                        logger.error(response.getBody().toPrettyString());
                    }
                } catch (Exception ex) {
                    //알림톡 발송을 실패하더라도 오류발생시키지 않고 결제처리 완료를 위해 오류는 catch에서 로깅처리만 함
                    logger.error("Error", ex);
                }
            }


        } else {                                            // 취소 요청 일시
            paymentDao.updateCancelRequest(param);

            try {
                // 취소 요청 알림톡발송
                DochaAlarmTalkDto dto = new DochaAlarmTalkDto();

                dto.setBookDate(reserveDate.substring(0, 10) + "(" + Util.getWeekByString(reserveDate.substring(0, 10), "yyyy-MM-dd") + ") " + reserveDate.substring(11, 16)); //예약일
                dto.setCancelDate(nowDate + "(" + Util.getWeekByString(nowDate, "yyyy-MM-dd") + ") " + nowTime); //예약일
                dto.setRentDate(reserveInfo.getRentStartDay() + "(" + Util.getWeekByString(reserveInfo.getRentStartDay(), "yyyy-MM-dd") + ") " + reserveInfo.getRentStartTime()); //렌트시작일
                dto.setReturnDate(reserveInfo.getRentEndDay() + "(" + Util.getWeekByString(reserveInfo.getRentEndDay(), "yyyy-MM-dd") + ") " + reserveInfo.getRentEndTime()); //렌트종료일
                dto.setCarName(reserveInfo.getModelName() + " " + reserveInfo.getModelDetailName());            // 차량명
                dto.setPayAmount(numberFormat.format(Integer.parseInt(reserveInfo.getSumPaymentAmount())));                                            // 결제금액
                dto.setPhone(reserveInfo.getFirstDriverContact());                                              //알림톡 전송할 번호

                dto.setTemplateCode(DochaTemplateCodeProvider.A000005.getCode());
                //알림 톡 발송 후 로깅
                HttpResponse<JsonNode> response = alarmTalk.sendAlramTalk(dto);
                if (response.getStatus() == 200) {
                    logger.info("AlarmTalk Send Compelite");
                    logger.info(response.getBody().toPrettyString());
                } else {
                    logger.info("AlarmTalk Send Fail");
                    logger.error(response.getBody().toPrettyString());
                }
            } catch (Exception ex) {
                //알림톡 발송을 실패하더라도 오류발생시키지 않고 결제처리 완료를 위해 오류는 catch에서 로깅처리만 함
                logger.error("Error", ex);
            }

        }

        return resData;
    }


    @RequestMapping(value = "/user/payment/extension.do", method = RequestMethod.GET)
    public ModelAndView paymentExtensionDo(ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {


        mv.setViewName("extension_payment.html");
        return mv;
    }

    @RequestMapping(value = "/user/payment/extensionDay.do", method = RequestMethod.POST)
    public ModelAndView paymentExtensionDayDo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        mv.addObject("preParam", param);

        List<DochaPaymentDto> reserveInfo = paymentDao.selectReserveInfo(param);

        mv.addObject("preParam", param);
        mv.addObject("reserveInfo", reserveInfo);

        mv.setViewName("extension_payment_day.html");
        return mv;
    }

    @RequestMapping(value = "/user/payment/extensionInfo.json")
    @ResponseBody
    public Object carDetailJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        DochaMap resData = new DochaMap();

        List<DochaPaymentDto> reserveInfo = paymentDao.selectReserveInfo(param);

        resData.put("reserveInfo", reserveInfo);

        return resData;
    }


    @RequestMapping(value = "/user/payment/review.do", method = RequestMethod.GET)
    public ModelAndView reviewDo(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);

        DochaPaymentDto reserveInfo = paymentDao.selectReserveInfoOne(param);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDtm = LocalDateTime.parse(reserveInfo.getRentStartDay() + " " + reserveInfo.getRentStartTime(), formatter);
        LocalDateTime endDtm = LocalDateTime.parse(reserveInfo.getRentEndDay() + " " + reserveInfo.getRentEndTime(), formatter);
        long milsecDiff = endDtm.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - startDtm.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd일 HH시간");
        Date date = new Date(milsecDiff);
        String formmat = sdf.format(date);

        mv.addObject("timeDiff" , formmat);
        mv.addObject("reserveInfo", reserveInfo);
        mv.setViewName("review_register.html");
        return mv;
    }
    
    /**
     * 후기 작성
     *
     * @param reqParam
     * @param mv
     * @param request
     * @param authentication
     * @param principal
     * @return
     */
    @RequestMapping(value = "/payment/reviewReg.json")
    @ResponseBody
    public Object reviewRegJson(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        DochaMap resData = new DochaMap();
        Integer resultCnt = 0;
        DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
        param.set("urIdx", loginSessionInfo.getUrIdx());
        
        resultCnt =+ paymentService.insertUserReview(request, param);

        resData.put("response_code", resultCnt <= 0 ? 201 : 200);
        resData.put("response_msg", resultCnt <= 0 ? "실패하였습니다." : "등록하였습니다.");

        return resData;
    }

    /**
     * 후기 작성 가능여부
     *
     * @param reqParam
     * @param mv
     * @param request
     * @param authentication
     * @param principal
     * @return
     */
    @RequestMapping(value = "/user/payment/reviewAbleCheck.json")
    @ResponseBody
    public Object reviewAbleCheck(@RequestParam Map<String, Object> reqParam, ModelAndView mv, HttpServletRequest request, Authentication authentication, Principal principal) {
        DochaMap param = new DochaMap();
        param.putAll(reqParam);
        DochaMap resData = new DochaMap();
        Integer resultCnt = 0;
        DochaUserInfoDto loginSessionInfo = (DochaUserInfoDto) authentication.getPrincipal();
        param.set("urIdx", loginSessionInfo.getUrIdx());
        param.set("rmIdx", param.getString("rmIdx"));
        
        resultCnt =+ paymentService.selectMyReviewCnt(param);

        resData.put("response_code", resultCnt > 0 ? 201 : 200);
        resData.put("response_msg", resultCnt > 0 ? "이미 등록하셨습니다." : "리뷰작성가능.");

        return resData;
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


    private Map<String, Object> getPaymentInfo(String impUid, String token, String url) throws JsonMappingException, JsonProcessingException, Exception {
        Map<String, String> body = new LinkedHashMap<String, String>();

        //헤더에 AccessToken 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> resultMap = mapper.readValue(connectImport(url + "/payments/" + impUid, headers, HttpMethod.GET, null), Map.class);

        return resultMap;

    }

    private String getAccessToken(String impKey, String impSecret, String url) throws JsonMappingException, JsonProcessingException, Exception {
        Map<String, String> body = new LinkedHashMap<String, String>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //파라미터로 imp_key, imp_secret 설정
        body.put("imp_key", impKey);
        body.put("imp_secret", impSecret);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> resultMap = mapper.readValue(connectImport(url + "/users/getToken", headers, HttpMethod.POST, body), Map.class);
        Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("response");

        return (String) dataMap.get("access_token");

    }

    private String connectImport(String url, HttpHeaders headers, HttpMethod method, Map body) throws Exception {

        RestTemplate connect = new RestTemplate();

        HttpEntity<Map> entity = new HttpEntity<Map>(body, headers);
        ResponseEntity<String> payResponse = null;
        try {
            payResponse = connect.exchange(url, method, entity, String.class);
        } catch (HttpServerErrorException ex) {

            logger.info("ImportConnect Error");
            logger.info("Error Request Url : " + url);
            logger.info("Error Request Body : " + body);
            logger.info("Error Response : " + ex.getResponseBodyAsString());
            logger.error("Error Request Url : " + url);
            logger.error("Error Request Body : " + body);
            logger.error(ex.getMessage());
            logger.error(ex.getResponseBodyAsString());
            logger.error("Error", ex);

            throw new Exception("Import Connection Error", ex);

        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error("Error", e);

            throw new Exception("Import Connection Error");
        }

        String responseBody = payResponse.getBody();

        logger.info("Response Body : " + responseBody);

        return responseBody;
    }

    public void paymentOne(DochaMap paramMap, String url, String impKey, String impSecret) throws JsonMappingException, JsonProcessingException, Exception {
        //결제검증전문
        String orgMsg = null;
        //결제검증 결과
        Map<String, Object> result = null;
        //결제 중 paydata
        Map<String, Object> payData = null;
        //아임포트 결제 key값을 셋팅
        String impUid = paramMap.getString("imp_uid");
        //결제검증을 위해 아임포트 AccessToken 발급
        String accessToken = getAccessToken(impKey, impSecret, url);
        //아임포트 결제 검증 호출부분
        try {
            //아임포트 AccessToken, 결제 key값을 전달하여 결제데이터 호출
            result = getPaymentInfo(impUid, accessToken, url);

            //결제전문 중 결제관련한 데이터를 가져옴
            payData = (Map<String, Object>) result.get("response");

            //결제전문을 JSONString형태로 변환
            ObjectMapper mapper = new ObjectMapper();
            orgMsg = mapper.writeValueAsString(result);
        } catch (Exception e) {
            //에러발생시 로그처리 후 에러 throws
            logger.error("Import Connect Error", e);
            throw e;
        }
    }

}
