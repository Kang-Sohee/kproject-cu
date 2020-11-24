package com.ohdocha.cu.kprojectcu.util;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public enum DochaTemplateCodeProvider {

	
	  A000001
	  ("[예약완료]\r\n" + 
	  		"렌터카는 두차!\r\n" + 
	  		"렌터카 예약이 완료되었습니다.\n" + 
	  		"\n" + 
	  		"예약일시 : %s\r\n" + 
	  		"대여일시 : %s\r\n" + 
	  		"반납일시 : %s\r\n" + 
	  		"\n" + 
	  		"차량 : %s\r\n" + 
	  		"\n" + 
	  		"자차보험 : %s\r\n" + 
	  		"대여금액 : %s\r\n" + 
	  		"할인금액 : %s\r\n" + 
	  		"결제금액 : %s\r\n" + 
	  		"대여방법 : %s\r\n" + 
	  		"\n" + 
	  		"회원사 : %s\n" + 
	  		"회원사연락처 : %s\r\n" + 
	  		"회원사 위치 : %s\r\n" + 
	  		"\r\n" + 
	  		"조기반납 안내\r\n" + 
	  		"---------------\r\n" + 
	  		"조기반납 시 취소 수수료 및 환불처리는 대여지점의 내규에 따릅니다.\r\n" + 
	  		"\r\n" + 
	  		"두차를 이용해주셔서 감사합니다."
	  		,"0001")//[고객] 견적알림
	
	, A000002 
	("", "")//[고객] 견적수신
	, A000003 
	("", "")//[고객] 결제완료 알림 (단기)
	, A000004 
	("", "")//[고객] 결제완료 알림 (월)
	, A000005 
	("", "")//[회원사] 결제완료 알림 (단기)
	, A000006
	("", "")//[회원사] 결제완료 알림 (월)
	, A000007 
	("", "")//[고객] 예약취소
	, A000008 
	("", "")//[회원사] 예약취소
	
	, A000010
	("", "")//[고객] 견적수신

	, A000011 
	("", "")//[고객]대여요청완료
	, A000012 
	("", "")//[회원사] 대여요청도착
	, A000013 
	("", "")//[고객] 차량수신
	, A000014 
	("", "")//직원발신용 알림톡
	, A000015
	("", "");//[회원사] 대여요청도착(A000015)
	
	
	@Getter
	private String msg;
	
	@Getter
	private String code;
	
	private DochaTemplateCodeProvider(String msg, String code) {
		
		this.msg = msg;
		this.code = code;
		
	}
	
	public static DochaTemplateCodeProvider valueOfCode(String value) {
		
		List<DochaTemplateCodeProvider> enumList = Arrays.asList(values());
		DochaTemplateCodeProvider returnTemplate = null;
		
		for(DochaTemplateCodeProvider template : enumList) {
			
			if(template.code.equals(value)) {
				returnTemplate = template;
			}
			
		}
		
		if(returnTemplate == null) {
			throw new IllegalArgumentException("No enum constant Search Value");
		}
		
		return returnTemplate;
		
	}

	
}
