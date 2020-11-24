package com.ohdocha.cu.kprojectcu.util;

import com.ohdocha.cu.kprojectcu.domain.DochaAlarmTalkDto;
import com.ohdocha.cu.kprojectcu.service.DochaKakaoAlramLogServiceImpl;

import kong.unirest.HttpRequestWithBody;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.MultipartBody;
import kong.unirest.Unirest;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
	
	
	@Resource(name="dochaKakaoAlramLogService")
	DochaKakaoAlramLogServiceImpl kakaoAlramLogService;
	
	/*
	 * todo
	 * 카카오 알림톡 발송용 템플릿을 생성
	 * 단기/장기인경우
	 * 코드템플릿별 분기처리
	 * 
	 * */
	public String makekakoAlramTalkTemplate(DochaAlarmTalkDto dto) {
		
		
		//A000011	[고객] 대여요청완료 
		//A000012	[회원사] 대여요청도착
		//A000002	[고객] 견적수신
		//A000003	[고객] 결제완료 알림 (단기)
		//A000004	[고객] 결제완료 알림 (월)
		//A000005	[회원사] 결제완료 알림 (단기)
		//A000006	[회원사] 결제완료 알림 (월)
		//A000007	[고객] 예약취소
		//A000008	[회원사] 예약취소

		//A000015 [회원사] 대여요청도착(A000015)
		//2020.02.28 대여위치 추가
		String appendMsg = "";

		//A000000 [고객] 견적알림 (지점방문/배달대여)
		if(DochaTemplateCodeProvider.A000011.name().equals(dto.getTemplateCode())) {  
		
			/*
			 [견적 요청 완료] 
		 	  카썸 견적 요청이 완료되었습니다. 

			    ▶대여일시 : #{Rent_Date}
			    ▶반납일시 : #{Return_Date}
			    ▶차종 : #{CarType}
			    ▶대여방법 :  #{DeliveryTypeCode}

			  카썸을 이용해주셔서 감사합니다.
			*/
		
			appendMsg += makeQuoteReminderMsg(dto);			
		}
		//A000012 [회원사] 대여요청도착
		if(DochaTemplateCodeProvider.A000012.name().equals(dto.getTemplateCode())) {   
		
			/*
				[견적 요청] 
				카썸 견적 요청이 도착했습니다.  
				
				▶대여일시 : #{Rent_Date}
				▶반납일시 : #{Return_Date}
				▶차종 : #{CarType}
				▶대여방법 : #{DeliveryTypeCode}
				
				▶예약자 : #{User_Name}
				▶연령 : #{User_Age}
				
				카썸을 이용해주셔서 감사합니다.

			*/
			appendMsg += makeQuoteReminderMsg(dto);
		}
		
		//A000002 [고객] 견적수신
		if(DochaTemplateCodeProvider.A000013.name().equals(dto.getTemplateCode())) {   
			/*
				appendMsg += "[견적 수신]";
				appendMsg += "#{message}";
				appendMsg += "\r\n\r\n";
				appendMsg += "해당 메시지는 고객님께서 요청하신 견적이 있을 경우 발송됩니다.";
			 */
			appendMsg += "[차량 수신]" ;
			appendMsg += "\r\n";	
			appendMsg += "이 차는 어떠세요?";
			appendMsg += "\r\n";	
			appendMsg += "\r\n";	
			appendMsg += "해당 메시지는 고객님의 대여요청이 있있던 경우 발송됩니다.";
		}	

		//A000003	[고객] 결제완료 알림 (단기)
		if(DochaTemplateCodeProvider.A000003.name().equals(dto.getTemplateCode())) {   
			/*
			[예약완료] 
			카썸 차량 예약이 완료되었습니다.  
			
			▶예약일시 : #{Reserve_Date}
			▶대여일시 : #{Rent_Date}
			▶반납일시 : #{Return_Date}
			▶차량 : #{CarType}
			▶자차보험 : #{Insurance_Rate}원 면책금 #{insurance_Copayment}원  
			
			▶대여금액 : #{Rent_Amount}원
			▶할인금액 : -#{Discount_Amount}원
			▶결제금액 : #{Pay_Amount}원
			▶대여방법 : #{DeliveryTypeCode} 
			
			▶회원사 : #{Company_Addr}
			▶회원사연락처 : #{Company_Contact}
			▶위치보기 : #{Company_Addr}
			
			※ 취소수수료 안내 ※  
			▶결제 후 1시간 이내 취소: 
			수수료 없음  
			▶픽업시간 24시간 이전 취소: 
			수수료 없음 
			▶24시간전 부터 픽업시간까지: 
			단기대여(1개월미만) - 결제 금액의 10% 
			장기대여(1개월이상) - 1개월 금액의 5%  
			▶픽업시간 이후 취소: 
			단기대여(1개월미만) - 결제 금액의 20% 
			장기대여(1개월이상) - 1개월 금액의 10%  
			
			※ 조기반납 안내 ※  
			▶조기반납 시 취소 수수료 및 환불처리는 대여지점의 내규에 따릅니다.  
			
			카썸을 이용해주셔서 감사합니다.
			 */			
			appendMsg += makePaymentFinishedMsg(dto);
			//System.out.println("A000003 START=============================");
			//System.out.println(appendMsg);

		}	

		//A000004	[고객] 결제완료 알림 (월)
		if(DochaTemplateCodeProvider.A000004.name().equals(dto.getTemplateCode())) {   
			/*
			[예약완료] 
			\r\n
			카썸 차량 예약이 완료되었습니다.  
			\r\n\r\n
			▶예약일시 : #{Reserve_Date}
			\r\n
			▶대여일시 : #{Rent_Date}
			\r\n
			▶반납일시 : #{Return_Date}
			\r\n▶차량 : #{CarType}
			\r\n▶자차보험 : #{Insurance_Rate}원 면책금 #{insurance_Copayment}원  
			\r\n\r\n
			▶대여금액 : #{Rent_Amount}원
			\r\n
			▶할인금액 : -#{Discount_Amount}원
			\r\n
			▶결제금액 : #{Pay_Amount}원
			\r\n
			▶보증금 : #{CarDeposit}원
			\r\n
			▶대여방법 :  #{DeliveryTypeCode} 
			\r\n\r\n
			▶회원사 : #{Company_Name}
			\r\n
			▶회원사연락처 : #{Company_Contact}
			\r\n
			▶위치보기 : #{Company_Addr}
			\r\n\r\n
			※ 취소수수료 안내 ※ 
			 \r\n
			 ▶결제 후 1시간 이내 취소: 
			 \r\n
			 수수료 없음  
			 \r\n
			 ▶픽업시간 24시간 이전 취소: 
			 \r\n
			 수수료 없음 
			 \r\n
			 ▶24시간전 부터 픽업시간까지: 
			 \r\n
			 단기대여(1개월미만) - 결제 금액의 10%
			 \r\n
			  장기대여(1개월이상) - 1개월 금액의 5%  
			 \r\n
			  ▶픽업시간 이후 취소: 
			 \r\n
			  단기대여(1개월미만) - 결제 금액의 20% 
			 \r\n
			  장기대여(1개월이상) - 1개월 금액의 10%  
			 \r\n\r\n
			  ※ 조기반납 안내 ※ 
			 \r\n
			   ▶조기반납 시 취소 수수료 및 환불처리는 대여지점의 내규에 따릅니다.  
			 \r\n\r\n카썸을 이용해주셔서 감사합니다.
			 */			
			appendMsg += makePaymentFinishedMsg(dto);
			
		}			
		
		//A000005	[회원사] 결제완료 알림 (단기)
		if(DochaTemplateCodeProvider.A000005.name().equals(dto.getTemplateCode())) {   
			/*
			[예약접수] 
			\r\n
			카썸 차량 예약이 접수되었습니다.  
			\r\n\r\n
			▶예약일시 : #{Reserve_Date}
			\r\n
			▶대여일시 : #{Rent_Date}
			\r\n
			▶반납일시 : #{Return_Date}
			\r\n▶차량 : #{CarType}
			\r\n
			▶자차보험 : #{Insurance_Rate}원 면책금 #{insurance_Copayment}원 
			\r\n\r\n
			▶대여금액 : #{Rent_Amount}원
			\r\n
			▶할인금액 : -#{Discount_Amount}원
			\r\n
			▶결제금액 : #{Pay_Amount}원
			\r\n
			▶대여방법 : #{DeliveryTypeCode}
			\r\n\r\n
			▶예약자 : #{User_Name}
			\r\n
			▶연락처 : #{User_Contact}
			\r\n
			▶연령 : #{User_Age}
			\r\n
			▶면허종류 : #{User_Dirver_Lience}
			\r\n\r\n
			예약에 문제가 있을 경우 담당 영업사원에게 연락 부탁드립니다.
			 */			
			appendMsg += makePaymentFinishedMsg(dto);
		}			
		
		//A000006	[회원사] 결제완료 알림 (월)
		if(DochaTemplateCodeProvider.A000006.name().equals(dto.getTemplateCode())) {   
			/*
			[예약접수] 
			\r\n
			카썸 차량 예약이 접수되었습니다.  
			\r\n\r\n
			▶예약일시 : #{Reserve_Date}
			\r\n
			▶대여일시 : #{Rent_Date}
			\r\n
			▶반납일시 : #{Return_Date}
			\r\n
			▶차량 : #{CarType}
			\r\n
			▶자차보험 : #{Insurance_Rate}원 면책금 #{insurance_Copayment}원  
			\r\n\r\n
			▶대여금액 : #{Rent_Amount}원
			\r\n
			▶할인금액 : -#{Discount_Amount}원
			\r\n
			▶결제금액 : #{Pay_Amount}원
			\r\n
			▶대여방법 : #{DeliveryTypeCode}
			\r\n\r\n
			▶예약자 : #{User_Name}
			\r\n
			▶연락처 : #{User_Contact}
			\r\n
			▶연령 : #{User_Age}
			\r\n
			▶면허종류 : #{User_Dirver_Lience}
			\r\n\r\n
			예약에 문제가 있을 경우 담당 영업사원에게 연락 부탁드립니다.
			\r\n
			보증금은 회원사에서 직접 안내, 입금 받으시기 바랍니다.
			
			 */			
			appendMsg += makePaymentFinishedMsg(dto);
		}		
		//A000007	[고객] 예약취소
		if(DochaTemplateCodeProvider.A000007.name().equals(dto.getTemplateCode())) {   
			/*
			[취소완료] 
			\r\n
			카썸 예약취소가 완료되었습니다.
			\r\n  \r\n
			▶예약일시 : #{Reserve_Date}
			\r\n
			▶취소일시 : #{Cancel_Date}
			\r\n
			▶대여일시 : #{Rent_Date}
			\r\n
			▶반납일시 : #{Return_Date}   
			\r\n
			▶차량 : #{CarType}
			\r\n\r\n
			▶결제금액 : #{Pay_Amount}원
			\r\n
			▶취소금액 : #{Cancel_Amount}원
			\r\n\r\n
			카썸을 이용해주셔서 감사합니다
			
			 */			
			appendMsg += makeCancelMsg(dto);
			
			return appendMsg;
		}		
		//A000008	[회원사] 예약취소
		if(DochaTemplateCodeProvider.A000008.name().equals(dto.getTemplateCode())) {   
			/*
			[취소완료] 
			\r\n
			카썸 예약취소가 완료되었습니다.  
			\r\n\r\n
			▶예약일시 : #{Reserve_Date}
			\r\n
			▶취소일시 : #{Cancel_Date}
			\r\n
			▶대여일시 : #{Rent_Date}
			\r\n
			▶반납일시 : #{Return_Date}   
			\r\n
			▶차량 : #{CarType}
			\r\n
			▶차량번호 : #{CarNumber}
			\r\n\r\n
			▶결제금액 : #{Pay_Amount}원
			\r\n
			▶취소금액 : #{Cancel_Amount}원
			\r\n\r\n
			▶예약자 : #{User_Name}
			\r\n\r\n
			카썸을 이용해주셔서 감사합니다.
			*/			
			
			return appendMsg;
		}
		
		//카썸 운영 직원용
		if(DochaTemplateCodeProvider.A000014.name().equals(dto.getTemplateCode())) {   
			/*
			 * 
			 *  ▶회원사 : #{Company_Name}
				▶회원사 연락처 : #{Company_Contact}
				▶고객명 : #{User_Name}
				▶고객 연락처 : #{User_Contact}
				▶대여일시 : #{Rent_Date}
				▶반납일시 : #{Return_Date}
				▶차량 : #{CarType}
				▶대여방법 : #{DeliveryTypeCode}
				▶대여위치 : #{DeliveryAddr}
				▶자차보험 : #{Insurance_Rate}원 면책금 #{insurance_Copayment}원
				▶대여금액 : #{Rent_Amount}원
				▶보증금 : #{CarDeposit}원
			 * */
			
			appendMsg += "▶회원사 : " + dto.getCompanyName();
			appendMsg += "\r\n";
			appendMsg += "▶회원사 연락처 : " + dto.getCompanyContact();
			appendMsg += "\r\n";
			appendMsg += "▶고객명 : " + dto.getUserName();
			appendMsg += "\r\n";
			appendMsg += "▶고객 연락처 : " + dto.getUserContact();
			appendMsg += "\r\n";
			//대여일시 반납일시
			appendMsg += "▶대여일시 : " + dto.getRentDate();
			appendMsg += "\r\n";
			appendMsg += "▶반납일시 : "+ dto.getReturnDate();
			appendMsg += "\r\n";
			appendMsg += "▶차량 : " + dto.getCarType();
			appendMsg += "\r\n";
			appendMsg += "▶대여방법 : " + dto.getDeliveryTypeCode(); 
			appendMsg += "\r\n";		
 			appendMsg += "▶대여위치 : " + dto.getRentAddr();
 			appendMsg += "\r\n";
			appendMsg += "▶자차보험 : " + dto.getInsurancerate() +" 원 면책금 " + dto.getInsurancecopayment() +" 원";
			appendMsg += "\r\n";
			appendMsg += "▶대여금액 : " + dto.getRentAmount() + " 원";
			appendMsg += "\r\n";
			appendMsg += "▶보증금 : " + dto.getCarDeposit() +" 원"; 
			appendMsg += "\r\n";

			
		}
		
		//카썸 운영 직원용
		if(DochaTemplateCodeProvider.A000015.name().equals(dto.getTemplateCode())) {   
			/*
			 * 
			 * [대여 요청]
			      카썸 대여 요청이 도착했습니다.
				
				▶대여일시 : #{Rent_Date}
				▶반납일시 : #{Return_Date}
				▶대여위치 : #{DeliveryAddr}
				▶차종 : #{CarType}
				▶대여방법 : #{DeliveryTypeCode}
				
				▶예약자 : #{User_Name}
				▶연령 : #{User_Age}
				
				카썸을 이용해주셔서 감사합니다.
			 */
			appendMsg += "[대여 요청] ";
			appendMsg += "\r\n";
			appendMsg += "카썸 대여 요청이 도착했습니다. ";
			appendMsg += "\r\n";
			appendMsg += "\r\n";
			appendMsg += "▶대여일시 : " + dto.getRentDate();
			appendMsg += "\r\n";
			appendMsg += "▶반납일시 : " + dto.getReturnDate();
			appendMsg += "\r\n";
			appendMsg += "▶대여위치 : " + dto.getRentAddr();
			appendMsg += "\r\n";			
			appendMsg += "▶차종 : " + dto.getCarType();
			appendMsg += "\r\n";
			appendMsg += "▶대여방법 : " + dto.getDeliveryTypeCode();
			appendMsg += "\r\n";
			appendMsg += "\r\n";
			appendMsg += "▶예약자 : " + dto.getUserName();
			appendMsg += "\r\n";
			appendMsg += "▶연령 : " + dto.getUserAge();
			appendMsg += "\r\n";
			appendMsg += "\r\n";
			appendMsg += "카썸을 이용해주셔서 감사합니다.";
		}
		
		if(DochaTemplateCodeProvider.A000001.name().equals(dto.getTemplateCode())) {   
			appendMsg = rentCompeliteAlarmTalk(dto);
		}
		
		return appendMsg;
	}//end 
	
	
	/**
	 * 
	 * sendAlramTalk
//	 * @param  HttpServletRequest
//	 * @param  HttpServletResponse
//	 * @param  imp_uid 본인인증 후 고유 imp_uid
	 * @return DochaUserInfoDto
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */	
	
	public HttpResponse<JsonNode> sendAlramTalk(DochaAlarmTalkDto dto)  {	
		
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
		String msg= "";
		
		DochaTemplateCodeProvider template = DochaTemplateCodeProvider.valueOfCode(dto.getTemplateCode());
		
		if(DochaTemplateCodeProvider.A000011.name().equals(dto.getTemplateCode())) {  
			// [견적 요청 완료] 
			
			failed_subject ="[대여 요청 완료]"; 			

		}
		//A000012 [회원사] 대여요청도착
		if(DochaTemplateCodeProvider.A000012.name().equals(dto.getTemplateCode())) {   
			failed_subject ="[대여 요청]"; 
		}
		
		//A000002 [고객] 견적수신
		if(DochaTemplateCodeProvider.A000013.name().equals(dto.getTemplateCode())) {   
			failed_subject ="[차량 수신]";
		}	

		//A000003	[고객] 결제완료 알림 (단기)
		if(DochaTemplateCodeProvider.A000003.name().equals(dto.getTemplateCode())) {   
			failed_subject ="[예약완료]";
		}	

		//A000004	[고객] 결제완료 알림 (월)
		if(DochaTemplateCodeProvider.A000004.name().equals(dto.getTemplateCode())) {    
			failed_subject ="[예약완료]";
		}			
		
		//A000005	[회원사] 결제완료 알림 (단기)
		if(DochaTemplateCodeProvider.A000005.name().equals(dto.getTemplateCode())) {   	
			failed_subject ="[예약접수]";
		}			
		
		//A000006	[회원사] 결제완료 알림 (월)
		if(DochaTemplateCodeProvider.A000006.name().equals(dto.getTemplateCode())) {   
			failed_subject ="[예약접수]";
		}		
		//A000007	[고객] 예약취소
		if(DochaTemplateCodeProvider.A000007.name().equals(dto.getTemplateCode())) {   
			failed_subject ="[취소완료]";

		}		
		//A000008	[회원사] 예약취소
		if(DochaTemplateCodeProvider.A000008.name().equals(dto.getTemplateCode())) {   
			failed_subject ="[취소완료]";
		}

		//A000010	[견적도착]
		if(DochaTemplateCodeProvider.A000008.name().equals(dto.getTemplateCode())) {   
			failed_subject ="[견적도착]";
		}

		
		if(DochaTemplateCodeProvider.A000014.name().equals(dto.getTemplateCode())) {   
			failed_subject ="[견적도착]";
		}
		
		if(DochaTemplateCodeProvider.A000015.name().equals(dto.getTemplateCode())) {   
			failed_subject ="[대여요청도착]";
		}
		
		if(DochaTemplateCodeProvider.A000001 == template) {   
			failed_subject ="[예약완료]";
			msg = rentCompeliteAlarmTalk(dto);
		}
		
		//application.properties 에서 받아온 정보로 처리할 경우 유니코드 관련 오류발생하여 일단 소스에 key및 app id, callback number을 넣어서 처리
		MultipartBody request = Unirest.post ("http://api.apistore.co.kr/kko/1/msg/mobilityk")
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
		
		if(dto.getBtnTypes() != null) {
			request.field("BTN_TYPES", dto.getBtnTypes());
		}
		
		if(dto.getBtnTxts() != null) {
			request.field("BTN_TXTS", dto.getBtnTxts());
		}
		
		if(dto.getBtnUrls1() != null) {
			request.field("BTN_URLS1", dto.getBtnUrls1());
		}
		
		if(dto.getBtnUrls2() != null) {
			request.field("BTN_URLS2", dto.getBtnUrls2());
		}
		
		HttpResponse<JsonNode> response =  request.asJson();

		return response;
	}


	/**
	 * 
	 * makeCancelMsg
	 * 취소완료 메세지
	 * 
	 * @return String
	 */	
	private String makeCancelMsg(DochaAlarmTalkDto dto) {
		String appendMsg = "";
		appendMsg += "[취소완료]"; 
		appendMsg += "\r\n";
		appendMsg += "카썸 예약취소가 완료되었습니다.";  
		appendMsg += "\r\n\r\n";
		appendMsg += "▶예약일시 : " + dto.getReserveDate();
		appendMsg += "\r\n";
		appendMsg += "▶취소일시 : " + dto.getCancelDate();
		appendMsg += "\r\n";
		appendMsg += makeCommonMsg(dto);
		appendMsg += "▶차량 : " + dto.getCarType();
		appendMsg += "\r\n";		
		if(DochaTemplateCodeProvider.A000008.name().equals(dto.getTemplateCode())) {
			appendMsg += "▶차량번호 : " + dto.getCarNumber();
			appendMsg += "\r\n";
		}
		appendMsg += "▶결제금액 : " + dto.getPayAmount() +"원";
		appendMsg += "\r\n";
		appendMsg += "▶취소금액 : " + dto.getCancelAmount() +"원";
		appendMsg += "\r\n\r\n";
		
		if(DochaTemplateCodeProvider.A000008.name().equals(dto.getTemplateCode())) {
			appendMsg += "▶예약자 : " + dto.getUserName();
			appendMsg += "\r\n";
		}
		
		appendMsg += "카썸을 이용해주셔서 감사합니다";
		
		return appendMsg;
	}//end makeCancelMsg
	
	/**
	 * makeQuoteReminderMsg
	 * [고객] 견적알림
	 * [회원사] 견적알림
	 * 
	 * @return String
	 */		
	private String makeQuoteReminderMsg(DochaAlarmTalkDto dto) {
		String appendMsg = "";

		if(DochaTemplateCodeProvider.A000011.name().equals(dto.getTemplateCode())) {
			appendMsg += "[대여 요청 완료]";
			appendMsg += "\r\n";
			appendMsg += "카썸 대여 요청이 완료되었습니다.";
		} else if(DochaTemplateCodeProvider.A000012.name().equals(dto.getTemplateCode())) {
			appendMsg += "[대여 요청]";
			appendMsg += "\r\n";
			appendMsg += "카썸 대여 요청이 도착했습니다.";			
		}
		
		appendMsg += "\r\n\r\n";
		appendMsg += makeCommonMsg(dto);
		appendMsg += "▶차종 : " + dto.getCarType();
		appendMsg += "\r\n";	
		appendMsg += "▶대여방법 : " + dto.getDeliveryTypeCode();
		appendMsg += "\r\n\r\n";
		
		//회원사용 메세지인 경우
		if(DochaTemplateCodeProvider.A000012.name().equals(dto.getTemplateCode())) {
			appendMsg += "▶예약자 : " + dto.getUserName();
			appendMsg += "\r\n";	
			appendMsg += "▶연령 : " + dto.getUserAge();
			appendMsg += "\r\n\r\n";	
		}
		
		if(DochaTemplateCodeProvider.A000011.name().equals(dto.getTemplateCode())) {
			appendMsg += "※영업 외 시간 배반차 요청 시 추가 금액이 발생될 수 있습니다.";
			appendMsg += "\r\n\r\n";
		}
		
		
		appendMsg += "카썸을 이용해주셔서 감사합니다.";	
		
		return appendMsg;
	}//end makeQuoteReminderMsg


	/**
	 * makePaymentFinishedMsg
	 * A000003	[고객] 결제완료 알림 (단기)
	 * A000004	[고객] 결제완료 알림 (월)
	 * A000005	[회원사] 결제완료 알림 (단기) 
	 * A000006	[회원사] 결제완료 알림 (월)
	 * 
	 * @return String
	 */	
	private String makePaymentFinishedMsg(DochaAlarmTalkDto dto) {
		
		System.out.println("makePaymentFinishedMsg");
		System.out.println(dto.getTemplateCode());
		
		String appendMsg = "";
		
		//TITLE
		if(DochaTemplateCodeProvider.A000003.name().equals(dto.getTemplateCode()) ||
		   DochaTemplateCodeProvider.A000004.name().equals(dto.getTemplateCode())) {
			appendMsg += "[예약완료] ";
			appendMsg += "\r\n";
			appendMsg += "카썸 차량 예약이 완료되었습니다.  ";
		}
		if(DochaTemplateCodeProvider.A000005.name().equals(dto.getTemplateCode()) ||
		   DochaTemplateCodeProvider.A000006.name().equals(dto.getTemplateCode())) {
			appendMsg += "[예약접수] "; 
			appendMsg += "\r\n";
			appendMsg += "카썸 차량 예약이 접수되었습니다.  ";
		}
 		
		appendMsg += "\r\n\r\n";
		appendMsg += "▶예약일시 : " + dto.getReserveDate();
		appendMsg += "\r\n";
		appendMsg += makeCommonMsg(dto);	
		appendMsg += "▶차량 : " + dto.getCarType();
		appendMsg += "\r\n";
		appendMsg += "▶자차보험 : " + dto.getInsurancerate() + "원 면책금 " + dto.getInsurancecopayment() + "원  ";
		appendMsg += "\r\n";
		appendMsg += "\r\n";
		appendMsg += "▶대여금액 : " + dto.getRentAmount() + "원";
		appendMsg += "\r\n";
		appendMsg += "▶할인금액 : -" + dto.getDiscountAmount() +"원";
		appendMsg += "\r\n";
		appendMsg += "▶결제금액 : " + dto.getPayAmount() +"원";
		appendMsg += "\r\n";
		
		//보증금
		if(DochaTemplateCodeProvider.A000004.name().equals(dto.getTemplateCode())) {
			appendMsg += "▶보증금 : " + dto.getCarDeposit() +"원";
			appendMsg += "\r\n";	
		}
		
		appendMsg += "▶대여방법 : " + dto.getDeliveryTypeCode();
		appendMsg += "\r\n\r\n";
		
		
		if(DochaTemplateCodeProvider.A000003.name().equals(dto.getTemplateCode()) ||
		   DochaTemplateCodeProvider.A000004.name().equals(dto.getTemplateCode())) {
			
			appendMsg += "▶회원사 : " + dto.getCompanyName();
			appendMsg += "\r\n";
			appendMsg += "▶회원사연락처 : " + dto.getCompanyContact();
			appendMsg += "\r\n";
			appendMsg += "▶위치보기 : " + dto.getCompanyAddr();
			appendMsg += "\r\n\r\n";
			appendMsg += notedCancelInforToString();
		} 
		
		if(DochaTemplateCodeProvider.A000005.name().equals(dto.getTemplateCode()) ||
		   DochaTemplateCodeProvider.A000006.name().equals(dto.getTemplateCode())) {
			appendMsg += "▶예약자 : " + dto.getUserName();
			appendMsg += "\r\n";
			appendMsg += "▶연락처 : " + dto.getUserContact();
			appendMsg += "\r\n";
			appendMsg += "▶연령 : " + dto.getUserAge();
			appendMsg += "\r\n";
			appendMsg += "▶면허종류 : " + dto.getUserDriverLience();
			appendMsg += "\r\n\r\n";
			appendMsg += "예약에 문제가 있을 경우 담당 영업사원에게 연락 부탁드립니다.";
			
		}

		if(DochaTemplateCodeProvider.A000006.name().equals(dto.getTemplateCode())) {
			appendMsg += "\r\n";
			appendMsg += "보증금은 회원사에서 직접 안내, 입금 받으시기 바랍니다.";
		}
		
		return appendMsg;
	}// end makePaymentFinishedMsg
	
	private String makeCommonMsg(DochaAlarmTalkDto dto) {
		String appendMsg = "";
		appendMsg += "▶대여일시 : " + dto.getRentDate();
		appendMsg += "\r\n";
		appendMsg += "▶반납일시 : "+ dto.getReturnDate();
		appendMsg += "\r\n";
		return appendMsg;
	}//makeCommonMsg

	
	
	/**
	 * 
	 * notedCancelInformation
	 * 취소수수료 안내
	 * 
	 * @return String

	 */	
	private String notedCancelInforToString () {
		String appendMsg = "";
		appendMsg += "※ 취소수수료 안내 ※  ";
		appendMsg += "\r\n";
		appendMsg +="▶결제 후 1시간 이내 취소: ";
		appendMsg += "\r\n";
		appendMsg +="수수료 없음  ";
		appendMsg += "\r\n"; 
		appendMsg +="▶픽업시간 24시간 이전 취소: ";
		appendMsg += "\r\n";
		appendMsg +="수수료 없음 ";
		appendMsg += "\r\n";
		appendMsg +="▶24시간전 부터 픽업시간까지: ";
		appendMsg += "\r\n";
		appendMsg +="단기대여(1개월미만) - 결제 금액의 10% ";
		appendMsg += "\r\n";
		appendMsg +="장기대여(1개월이상) - 1개월 금액의 5%  ";
		appendMsg += "\r\n";
		appendMsg +="▶픽업시간 이후 취소:";
		appendMsg += "\r\n";
		appendMsg +="단기대여(1개월미만) - 결제 금액의 20% ";
		appendMsg += "\r\n";
		appendMsg +="장기대여(1개월이상) - 1개월 금액의 10%  ";
		appendMsg += "\r\n";
		appendMsg += "\r\n";
		appendMsg += "※ 조기반납 안내 ※  ";
		appendMsg += "\r\n";
		appendMsg += "▶조기반납 시 취소 수수료 및 환불처리는 대여지점의 내규에 따릅니다.  ";
		appendMsg += "\r\n";
		appendMsg += "\r\n";
		appendMsg += "카썸을 이용해주셔서 감사합니다.";
		
		return appendMsg;
	}//end notedCancelInformation
	
	private String rentCompeliteAlarmTalk(DochaAlarmTalkDto dto) {
		
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
}
