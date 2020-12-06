package com.ohdocha.cu.kprojectcu.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Alias("alarmTalkDto")
public class DochaAlarmTalkDto {
	
	//카카오 기본 parameter
	private String phone;						//알림톡보낼 연락처
	private String callBack;					//발신번호
	private String msg;							//보낼 MSG
	private String templateCode;	//사전등록된 알림톡 템플릿 코드
	private String btnTypes;					//버튼타임 (웹링크, 앱링크, 봇키워드, 메시지전달, 배송조회)
	private String btnTxts;						//버튼명
	private String btnUrls1;					//웹링크1
	private String btnUrls2;					//웹링크2
	
	private String rentDate;					//대여일시
	private String periodDt;					//대여기간
	private String returnDate;					//반납일시
	private String cancelDate;					//취소일시
	private String alarmYn;						//알맅목 수신여부
	private String carType;						//차종
	private String rentAddr;					//대여위치
	private String returnAddr;					//반납위치
	
	private String insurancerate;				//자차보험
	private String insurancecopayment;			//면책금
	
	private String rentAmount;					//대여금액
	private String discountAmount;				//할인금액
	private String payAmount;					//결제금액
	private String cancelAmount;				//취소금액
	
	private String carDeposit;					//보증금
	
	
	private String companyName;					//회원사 이름
	private String companyContact;				//회원사 연락처
	private String companyAddr;					//회원사 주소
	
		
	private String userName;					//예약자
	private String userBirthday;				//예약자 생년월일
	private String userDriverLience;			//면허종류
	
	private String failedType;					//sms전송 타입(알림톡 전송 실패시 sms로 )
	private String failedSubject;				//sms 제목
	private String failedMsg;					//발송 실패 타입

	private String reserveDate;
	private String discountFee;
	private String carName;
	
	private String rtIdx; //회원사 idx
	
	private String deliveryTypeCode; //대여방법
	
	private String tmp_Code;
	
	private String userAge;
	
	private String carNumber;
	
	private String userContact; //예약자연락처
	private String urIdx;
	//KAKAO_LOG_DTO 시작
	private String kaIdx;      
	private String resultCode; 
	private String resultFull;
	private String resultMsg;
	private String cmid;
	
	private String rmIdx;
	private String quIdx;
	private int    rsIdx;
	private String contact;
	private String division;
	
	private String bookDate;					//예약일시
	
	private String paymentDate;
	private String amount;
	private String userContact1;
	
	

	
}
