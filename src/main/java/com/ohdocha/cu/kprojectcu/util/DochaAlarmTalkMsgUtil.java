package com.ohdocha.cu.kprojectcu.util;

import com.ohdocha.cu.kprojectcu.domain.DochaAlarmTalkDto;
import com.ohdocha.cu.kprojectcu.service.DochaKakaoAlramLogServiceImpl;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.MultipartBody;
import kong.unirest.Unirest;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.Resource;
import java.io.IOException;

@Configuration
@EnableAsync
public class DochaAlarmTalkMsgUtil {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/*
	@Value("${kakao.alert.talk.key}")
	private String kakao_alert_api_key;
	
	@Value("${kakao.alert.talk.id}")
	private String kakao_alert_api_id;
	
	@Value("${kakao.alert.callback.number}")
	private String kakao_alert_callback_number;
	
	@Value("${kakao.alert.fail.type}")
	private String kakao_alert_fail_type;
	*/


    @Resource(name = "dochaKakaoAlramLogService")
    DochaKakaoAlramLogServiceImpl kakaoAlramLogService;

    /*
     * todo
     * 카카오 알림톡 발송용 템플릿을 생성
     * 단기/장기인경우
     * 코드템플릿별 분기처리
     *
     * */

    /**
     * sendAlramTalk
     * //	 * @param  HttpServletRequest
     * //	 * @param  HttpServletResponse
     * //	 * @param  imp_uid 본인인증 후 고유 imp_uid
     *
     * @return DochaUserInfoDto
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */

    public HttpResponse<JsonNode> sendAlramTalk(DochaAlarmTalkDto dto) {

		/*
		logger.info("PHONE=" + dto.getPhone());
		logger.info("CALLBACK=" + dto.getCallBack());
		logger.info("REQDATE=" + dto.getRentDate());
		logger.info("MSG=" + dto.getMsg());
		logger.info("TEMPLATE_CODE=" + dto.getTemplateCode());
		logger.info("failed_type=" + dto.getFailedType());

		logger.info("failed_subject=" + dto.getFailedSubject());
		logger.info("failed_msg=" + dto.getMsg());
		logger.info("BTN_TYPES=" + dto.getBtnTypes());
		logger.info("BTN_TXTS=" + dto.getBtnTxts());
		logger.info("BTN_URLS1=" + dto.getBtnUrls1());
		*/

        String failed_subject = "";
        String msg = "";

        DochaTemplateCodeProvider template = DochaTemplateCodeProvider.valueOfCode(dto.getTemplateCode());

        //A000001	[고객] 예약완료 ( 일대여 / 지점방문 )
        if (DochaTemplateCodeProvider.A000001 == template) {
            failed_subject = "[예약완료]";
            msg = rentCompeliteAlarmTalkDailyOFforCU(dto);
        }
        //A000002	[고객] 예약완료 ( 월대여 / 지점방문 )
        if (DochaTemplateCodeProvider.A000002 == template) {
            failed_subject = "[예약완료]";
            msg = rentCompeliteAlarmTalkMonthlyOFforCU(dto);
        }

        //A000003	[고객] 예약완료 ( 일대여 / 배달대여 )
        if (DochaTemplateCodeProvider.A000003 == template) {
            failed_subject = "[예약완료]";
            msg = rentCompeliteAlarmTalkDailyDLforCU(dto);
        }

        //A000004	[고객] 예약완료 ( 월대여 / 배달대여 )
        if (DochaTemplateCodeProvider.A000004 == template) {
            failed_subject = "[예약완료]";
            msg = rentCompeliteAlarmTalkMonthlyDLforCU(dto);
        }

        //A000005	[고객] 취소 요청
        if (DochaTemplateCodeProvider.A000005 == template) {
            failed_subject = "[예약완료]";
            msg = cancelRequestAlarmTalk(dto);
        }


        //A000007	[고객] 취소 확정
        if (DochaTemplateCodeProvider.A000007 == template) {
            failed_subject = "[취소완료]";
            msg = cancelCompeliteAlarmTalk(dto);

        }
        //A000020	[관리자] 취소 확정
        if (DochaTemplateCodeProvider.A000020 == template) {
            failed_subject = "[취소완료]";
            msg = cancelCompeliteAlarmTalkForAdmin(dto);

        }


        //A000012	[관리자] 일대여 / 지점방문
        if (DochaTemplateCodeProvider.A000012 == template) {
            failed_subject = "[예약완료]";
            msg = rentCompeliteAlarmTalkDailyOFforAdmin(dto);
        }

        //A000013	[관리자] 월대여 / 지점방문
        if (DochaTemplateCodeProvider.A000013 == template) {
            failed_subject = "[예약완료]";
            msg = rentCompeliteAlarmTalkMonthlyOFforAdmin(dto);
        }

        //A000014	[관리자] 월대여 / 지점방문
        if (DochaTemplateCodeProvider.A000014 == template) {
            failed_subject = "[예약완료]";
            msg = rentCompeliteAlarmTalkDailyDLforAdmin(dto);
        }

        //A000015	[관리자] 일대여 / 배달대여
        if (DochaTemplateCodeProvider.A000015 == template) {
            failed_subject = "[예약완료]";
            msg = rentCompeliteAlarmTalkMonthlyDLforAdmin(dto);
        }






        //application.properties 에서 받아온 정보로 처리할 경우 유니코드 관련 오류발생하여 일단 소스에 key및 app id, callback number을 넣어서 처리
        MultipartBody request = Unirest.post("http://api.apistore.co.kr/kko/1/msg/mobilityk")
                .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .header("x-waple-authorization", "MTMzMDMtMTU5NDg2MzAwNjYzMy1hNmU1ZjIxYS04MWRiLTQ5NTItYTVmMi0xYTgxZGJhOTUyOTk=")
                .field("PHONE", dto.getPhone())
                .field("CALLBACK", "01099477228")
                .field("REQDATE", "")
                .field("MSG", msg)
                .field("TEMPLATE_CODE", dto.getTemplateCode())
                .field("failed_type", "lms")
                .field("failed_subject", failed_subject)
                .field("failed_msg", msg);

        if (dto.getBtnTypes() != null) {
            request.field("BTN_TYPES", dto.getBtnTypes());
        }

        if (dto.getBtnTxts() != null) {
            request.field("BTN_TXTS", dto.getBtnTxts());
        }

        if (dto.getBtnUrls1() != null) {
            request.field("BTN_URLS1", dto.getBtnUrls1());
        }

        if (dto.getBtnUrls2() != null) {
            request.field("BTN_URLS2", dto.getBtnUrls2());
        }

        HttpResponse<JsonNode> response = request.asJson();


        return response;
    }

    // 0001
    private String rentCompeliteAlarmTalkDailyOFforCU(DochaAlarmTalkDto dto) {
        return String.format(DochaTemplateCodeProvider.A000001.getMsg()
                , dto.getBookDate()//예약일
                , dto.getRentDate()//렌트시작일
                , dto.getReturnDate()//렌트종료일
                , dto.getCarName()//차량명
                , dto.getInsurancecopayment() //보험료
                , dto.getRentAmount()//대여료
                , dto.getDiscountAmount()//할인료
                , dto.getPayAmount()//총결제금액
                , dto.getDeliveryTypeCode()//대여방법
                , dto.getCompanyName()//대여점명
                , dto.getCompanyContact()//대여점 연락처
                , dto.getCompanyAddr());//대여점 위치
    }

    // 0002
    private String rentCompeliteAlarmTalkMonthlyOFforCU(DochaAlarmTalkDto dto) {
        return String.format(DochaTemplateCodeProvider.A000002.getMsg()
                , dto.getBookDate()//예약일
                , dto.getRentDate()//렌트시작일
                , dto.getReturnDate()//렌트종료일
                , dto.getCarName()//차량명
                , dto.getInsurancecopayment() //보험료
                , dto.getRentAmount()//대여료
                , dto.getDiscountAmount()//할인료
                , dto.getPayAmount()//총결제금액
                , dto.getCarDeposit()//보증금
                , dto.getDeliveryTypeCode()//대여방법
                , dto.getCompanyName()//대여점명
                , dto.getCompanyContact()//대여점 연락처
                , dto.getCompanyAddr());//대여점 위치
    }

    // 0003
    private String rentCompeliteAlarmTalkDailyDLforCU(DochaAlarmTalkDto dto) {
        return String.format(DochaTemplateCodeProvider.A000003.getMsg()
                , dto.getBookDate()//예약일
                , dto.getRentDate()//렌트시작일
                , dto.getReturnDate()//렌트종료일
                , dto.getCarName()//차량명
                , dto.getInsurancecopayment() //보험료
                , dto.getRentAmount()//대여료
                , dto.getDiscountAmount()//할인료
                , dto.getPayAmount()//총결제금액
                , dto.getDeliveryTypeCode()//대여방법
                , dto.getCompanyName()//대여점명
                , dto.getCompanyContact()//대여점 연락처
                , dto.getRentAddr()//대여위치
                , dto.getReturnAddr());//반납위치
    }

    // 0004
    private String rentCompeliteAlarmTalkMonthlyDLforCU(DochaAlarmTalkDto dto) {
        return String.format(DochaTemplateCodeProvider.A000004.getMsg()
                , dto.getBookDate()//예약일
                , dto.getRentDate()//렌트시작일
                , dto.getReturnDate()//렌트종료일
                , dto.getCarName()//차량명
                , dto.getInsurancecopayment() //보험료
                , dto.getRentAmount()//대여료
                , dto.getDiscountAmount()//할인료
                , dto.getCarDeposit()//보증금
                , dto.getPayAmount()//총결제금액
                , dto.getDeliveryTypeCode()//대여방법
                , dto.getCompanyName()//대여점명
                , dto.getCompanyContact()//대여점 연락처
                , dto.getRentAddr()//대여위치
                , dto.getReturnAddr());//반납위치
    }

    // 0005 취소 요청
    private String cancelRequestAlarmTalk(DochaAlarmTalkDto dto) {

        return String.format(DochaTemplateCodeProvider.A000005.getMsg()
                , dto.getBookDate()//예약일시
                , dto.getCancelDate()//취소일시
                , dto.getRentDate()//렌트시작일
                , dto.getReturnDate()//렌트종료일
                , dto.getCarName()//차량명
                , dto.getPayAmount());//결제금액

    }

    // 0007 취소 완료
    private String cancelCompeliteAlarmTalk(DochaAlarmTalkDto dto) {

        return String.format(DochaTemplateCodeProvider.A000007.getMsg()
                , dto.getBookDate()//예약일시
                , dto.getCancelDate()//취소일시
                , dto.getRentDate()//렌트시작일
                , dto.getReturnDate()//렌트종료일
                , dto.getCarName()//차량명
                , dto.getPayAmount()//결제금액
                , dto.getCancelAmount());//환불금액

    }

    // 0007 취소 완료
    private String cancelCompeliteAlarmTalkForAdmin(DochaAlarmTalkDto dto) {

        return String.format(DochaTemplateCodeProvider.A000020.getMsg()
                , dto.getBookDate()//예약일시
                , dto.getCancelDate()//취소일시
                , dto.getRentDate()//렌트시작일
                , dto.getReturnDate()//렌트종료일
                , dto.getCarName()//차량명
                , dto.getCarNumber()//차량명
                , dto.getPayAmount()//결제금액
                , dto.getCancelAmount());//환불금액

    }



    // 0012
    private String rentCompeliteAlarmTalkDailyOFforAdmin(DochaAlarmTalkDto dto) {
        return String.format(DochaTemplateCodeProvider.A000012.getMsg()
                , dto.getBookDate()//예약일
                , dto.getRentDate()//대여일시
                , dto.getReturnDate()//반납일시
                , dto.getPeriodDt()//대여기간

                , dto.getCarName()//차량명
                , dto.getCarNumber()//차량 번호

                , dto.getInsurancecopayment() //보험료
                , dto.getRentAmount()//대여료
                , dto.getDiscountAmount()//할인료
                , dto.getPayAmount()//총결제금액
                , dto.getDeliveryTypeCode()//대여방법

                , dto.getUserName()             //예약자 이름
                , dto.getUserContact()          //예약자 번호
                , dto.getUserBirthday()         //예약자 생년월일
                , dto.getUserDriverLience());   //예약자 면허종류
    }


    // 0013
    private String rentCompeliteAlarmTalkMonthlyOFforAdmin(DochaAlarmTalkDto dto) {
        return String.format(DochaTemplateCodeProvider.A000013.getMsg()
                , dto.getBookDate()//예약일
                , dto.getRentDate()//대여일시
                , dto.getReturnDate()//반납일시
                , dto.getPeriodDt()//대여기간

                , dto.getCarName()//차량명
                , dto.getCarNumber()//차량 번호

                , dto.getInsurancecopayment() //보험료
                , dto.getRentAmount()//대여료
                , dto.getDiscountAmount()//할인료
                , dto.getPayAmount()//총결제금액
                , dto.getCarDeposit()//보증금금
                , dto.getDeliveryTypeCode()//대여방법

                , dto.getUserName()             //예약자 이름
                , dto.getUserContact()          //예약자 번호
                , dto.getUserBirthday()         //예약자 생년월일
                , dto.getUserDriverLience());   //예약자 면허종류
    }

    // 0014
    private String rentCompeliteAlarmTalkDailyDLforAdmin(DochaAlarmTalkDto dto) {
        return String.format(DochaTemplateCodeProvider.A000014.getMsg()
                , dto.getBookDate()//예약일
                , dto.getRentDate()//대여일시
                , dto.getReturnDate()//반납일시
                , dto.getPeriodDt()//대여기간

                , dto.getCarName()//차량명
                , dto.getCarNumber()//차량 번호

                , dto.getInsurancecopayment() //보험료
                , dto.getRentAmount()//대여료
                , dto.getDiscountAmount()//할인료
                , dto.getPayAmount()//총결제금액
                , dto.getDeliveryTypeCode()//대여방법

                , dto.getRentAddr()//대여위치
                , dto.getReturnAddr()//반납위치치

               , dto.getUserName()             //예약자 이름
                , dto.getUserContact()          //예약자 번호
                , dto.getUserBirthday()         //예약자 생년월일
                , dto.getUserDriverLience());   //예약자 면허종류
    }

    // 0015
    private String rentCompeliteAlarmTalkMonthlyDLforAdmin(DochaAlarmTalkDto dto) {
        return String.format(DochaTemplateCodeProvider.A000015.getMsg()
                , dto.getBookDate()//예약일
                , dto.getRentDate()//대여일시
                , dto.getReturnDate()//반납일시
                , dto.getPeriodDt()//대여기간

                , dto.getCarName()//차량명
                , dto.getCarNumber()//차량 번호

                , dto.getInsurancecopayment() //보험료
                , dto.getRentAmount()//대여료
                , dto.getDiscountAmount()//할인료
                , dto.getPayAmount()//총결제금액
                , dto.getCarDeposit()//보증금
                , dto.getDeliveryTypeCode()//대여방법

                , dto.getRentAddr()//대여위치
                , dto.getReturnAddr()//반납위치

                , dto.getUserName()             //예약자 이름
                , dto.getUserContact()          //예약자 번호
                , dto.getUserBirthday()         //예약자 생년월일
                , dto.getUserDriverLience());   //예약자 면허종류
    }






}
