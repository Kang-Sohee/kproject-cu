package com.ohdocha.cu.kprojectcu.domain;

import org.apache.ibatis.type.Alias;

//import gcp.oms.model.base.BaseOmsPaymentif;

@Alias("DochaUserPaymentLgdDto")
public class DochaPaymentLgdDto {
	
	private String storeId;	
	private String claimNo;	
	private String orderTypeCd;
	private String deviceTypeCd;
	private String mobileOs;
	private String paymentMethodCd;
	
	private String DEVICE_TYPE_CD_MW; 			//PC OR MOBILE 구분
	
	private String	CST_PLATFORM;		        //LG유플러스 결제서비스 선택(test:테스트, service:서비스)
	private String	CST_MID;					//LG유플러스으로 부터 발급받으신 상점아이디를 입력하세요.
	private String	LGD_MID;					 //테스트 아이디는 't'를 제외하고 입력하세요.
	//상점아이디(자동생성)
	private String	LGD_OID;					//주문번호(상점정의 유니크한 주문번호를 입력하세요)
	private String	LGD_AMOUNT;				    //결제금액("," 를 제외한 결제금액을 입력하세요)
	private String	LGD_MERTKEY;				//상점MertKey(mertkey는 상점관리자 -> 계약정보 -> 상점정보관리에서 확인하실수 있습니다)
	private String	LGD_BUYER;				    //구매자명
	private String	LGD_PRODUCTINFO;	        //상품명
	private String	LGD_BUYEREMAIL;		        //구매자 이메일
	private String	LGD_TIMESTAMP;		        //타임스탬프
	private String	LGD_CUSTOM_SKIN;	        //상점정의 결제창 스킨(red, purple, yellow)
	private String	LGD_WINDOW_VER;		        //결제창 버젼정보
	private String	LGD_BUYERID;			    //구매자 아이디
	private String	LGD_BUYERIP;			    //구매자IP
	private String LGD_CUSTOM_USABLEPAY;		//상점정의 초기결제수단
	
	private String LGD_ENCODING;				//결제창 호출문자 인코딩방식
	private String LGD_ENCODING_RETURNURL;		//결제창수신페이지 호출문자 인코딩방식.
	private String LGD_ENCODING_NOTEURL;		//가상계좌수신페이지 인코딩방식.
	
	/* 가상계좌(무통장) 결제 연동을 하시는 경우 아래 LGD_CASNOTEURL 을 설정하여 주시기 바랍니다. */ 	 
	private String LGD_CASNOTEURL;
	/*
     * LGD_RETURNURL 을 설정하여 주시기 바랍니다. 반드시 현재 페이지와 동일한 프로트콜 및  호스트이어야 합니다. 아래 부분을 반드시 수정하십시요.
     */
	private String LGD_RETURNURL;				// FOR MANUAL
	
	private String LGD_HASHDATA;
	private String LGD_CUSTOM_PROCESSTYPE;
	
	private String LGD_PAYKEY;					//LG유플러스 인증KEY
	
	////// crossplatform
	private String LGD_WINDOW_TYPE;				//수정불가
	private String LGD_CUSTOM_SWITCHINGTYPE;	// 신용카드 카드사 인증 페이지 연동 방식
	private String LGD_OSTYPE_CHECK;			//값 P: XPay 실행(PC 결제 모듈): PC용과 모바일용 모듈은 파라미터 및 프로세스가 다르므로 PC용은 PC 웹브라우저에서 실행 필요.
	
	////// 계좌이체
	private String LGD_AUTOFILLYN_BUYER;		//구매자명 자동채움.
	private String LGD_USABLEBANK;				//선택은행
	
	
	////// 자체창(신용카드)	
//	private String LGD_CARDTYPE;				//카드종류
//	private String LGD_INSTALL;					//할부개월수
	private String LGD_SELF_CUSTOM;				//자체창
	private String LGD_SP_CHAIN_CODE;			//간편결제사용여부		
	private String LGD_SP_ORDER_USER_ID;		//삼성카드간편결제 key id
	private String LGD_POINTUSE;				//포인트사용여부
	private String LGD_MERTNAME;				//상점명
	
	private String LGD_AUTHTYPE;				//인증유형(ISP인경우만  'ISP')  
	private String LGD_CARDTYPE;        		//카드사코드                 
	private String LGD_CURRENCY;        		//통화코드                  	
	private String LGD_PAN;				 		//카드번호                                  
	private String LGD_INSTALL;              	//할부개월수                             
	private String LGD_NOINT;             		//무이자할부여부('1':상점부담무이자할부,'0':일반할부)       
	private String LGD_EXPYEAR;              	//유효기간년(YY)                         
	private String LGD_EXPMON;              	//유효기간월 (MM)                            
	private String VBV_ECI;             		//안심클릭ECI                               
	private String VBV_CAVV;            		//안심클릭CAVV                                  
	private String VBV_XID;             		//안심클릭XID                               
	private String VBV_JOINCODE;            	//안심클릭JOINCODE                          
	private String KVP_QUOTA;           		//할부개월수                               
	private String KVP_NOINT;           		//무이자할부여부('1':상점부담무이자할부,'0':일반할부)     
	private String KVP_CARDCODE;        		//ISP카드코드                             
	private String KVP_SESSIONKEY;      		//ISP세션키                          
	private String	KVP_ENCDATA;				//ISP암호화데이터		

	////// 자체창(가상계좌)
	private String LGD_METHOD;					//ASSIGN:할당, CHANGE:변경
	private String LGD_ACCOUNTOWNER;			//입금자명                          
	private String LGD_ACCOUNTPID;     			//구매자 개인식별변호 (6자리~13자리)(옵션)     
	private String LGD_BUYERPHONE;     			//구매자휴대폰번호	                  
	private String LGD_BANKCODE;				//입금계좌은행코드
	private String LGD_CASHRECEIPTYN;     	  	//현금영수증발행여부
	private String LGD_CASHRECEIPTUSE;     	  	//현금영수증 발행구분('1':소득공제, '2':지출증빙)       
	private String LGD_CASHCARDNUM;          	//현금영수증 카드번호                               
	private String LGD_CLOSEDATE;            	//입금 마감일                                   
	private String LGD_TAXFREEAMOUNT;        	//면세금액
	
	
	////// Billing
	private String LGD_BUYERSSN;				//인증요청자 생년월일 6자리 (YYMMDD) or 사업자번호 10자리
	private String LGD_CHECKSSNYN;				//인증요청자 생년월일,사업자번호 일치 여부 확인 플래그 ( 'Y'이면 인증창에서 고객이 입력한 생년월일,사업자번호 일치여부를 확인합니다.)
	private String LGD_PAYWINDOWTYPE;			// 인증요청구분
	private String LGD_VERSION;					// 사용타입 정보(수정 및 삭제 금지): 이 정보를 근거로 어떤 서비스를 사용하는지 판단할 수 있습니다.
	private String LGD_BILLKEY;
	
	private String LGD_PIN;						//비밀번호 앞2자리(옵션-주민번호를 넘기지 않으면 비밀번호도 체크 안함)
	private String LGD_PRIVATENO;				//생년월일 6자리 (YYMMDD) or 사업자번호(옵션)
	
	private String LGD_TXNAME;					//빌링일때 CardAuth
	
	
	//부분취소
	private String LGD_CANCELAMOUNT;			//부분취소금액
	private String LGD_REMAINAMOUNT;			//남은금액
	private String LGD_CANCELREASON;			//취소사유
	private String LGD_RFBANKCODE;				//환불계좌 은행코드 (가상계좌만 필수)
	private String LGD_RFACCOUNTNUM;			//환불계좌 번호 (가상계좌만 필수)
	private String LGD_RFCUSTOMERNAME;			//환불계좌 예금주 (가상계좌만 필수)
	private String LGD_RFPHONE;					//요청자 연락처 (가상계좌만 필수)
	
	
	////// return //////
	private String LGD_RESPCODE;	
	private String LGD_RESPMSG;		

	private String LGD_TID;						//승인번호
	private String LGD_PAYTYPE;					//
	private String LGD_PAYDATE;
	private String LGD_FINANCECODE;
	private String LGD_FINANCENAME;
	private String LGD_ESCROWYN;                //사용안함
	private String LGD_ESCROW_USEYN;			//사용안함
	private String LGD_ACCOUNTNUM;
	private String LGD_CASTAMOUNT;
	private String LGD_CASCAMOUNT;
	private String LGD_CASFLAG;
	private String LGD_CASSEQNO;
	private String LGD_CASHRECEIPTNUM;
	private String LGD_CASHRECEIPTSELFYN;
	private String LGD_CASHRECEIPTKIND;
	private String LGD_PAYER;
	private String LGD_BUYERADDRESS;
	private String LGD_PRODUCTCODE;
	private String LGD_RECEIVER;
	private String LGD_RECEIVERPHONE;
	private String LGD_DELIVERYINFO;
	
	
	///// mobile //////
	private String CST_WINDOW_TYPE;
	private String LGD_MPILOTTEAPPCARDWAPURL;
	private String LGD_KVPMISPWAPURL;
	private String LGD_KVPMISPCANCELURL;
	private String LGD_MTRANSFERWAPURL;
	private String LGD_MTRANSFERCANCELURL;
	private String LGD_KVPMISPAUTOAPPYN;
	private String LGD_MTRANSFERAUTOAPPYN;
	
	
	private String LGD_DISABLE_AGREE;			//약관동의 미표시여부
	
	private String LGD_FINANCEAUTHNUM;
	private String LGD_ACTIVEXYN;
	private String LGD_CUSTOM_TITLE;
	
	private String LGD_NOTEUR          ;
	private String LGD_CANCELURL       ;
	private String LGD_MTRANSFERNOTEURL;
	private String LGD_NOTEURL;
	
	private String LGD_DOMAIN_URL;
	
	//결제요청 처리
	private String OrderId;
	private String PaymentNo;
	private String LGD_AFFILIATECODE;
	private String LGD_DEVICE;
	private String LGD_CARDPREFIX;
	
	private String urIdx;
	private String quIdx;
	private String qrIdx;
	private String rmIdx;
	
	//결제금액(개별)
	private String rentFee;
	private String insuranceFee;
	private String carDeposit;
	private String cancelFee;
	private String cancelAmount;
	
	//carSearch 추가
	private String deviceTypeCode;
	private String deliveryAddr;
	private String returnAddr;
	
	private String rentStartDay;
	private String rentStartTime;
	
	private String rentEndDay;
	private String rentEndTime;
	
	private String carSearchYn;
	private String longterm;
	
	private String deliveryTypeCode;
	private String companyAddress;
	private String companyName;
	private String crIdx;
	private String reserveDate;
	
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getOrderTypeCd() {
		return orderTypeCd;
	}
	public void setOrderTypeCd(String orderTypeCd) {
		this.orderTypeCd = orderTypeCd;
	}
	public String getDeviceTypeCd() {
		return deviceTypeCd;
	}
	public void setDeviceTypeCd(String deviceTypeCd) {
		this.deviceTypeCd = deviceTypeCd;
	}
	public String getMobileOs() {
		return mobileOs;
	}
	public void setMobileOs(String mobileOs) {
		this.mobileOs = mobileOs;
	}
	public String getPaymentMethodCd() {
		return paymentMethodCd;
	}
	public void setPaymentMethodCd(String paymentMethodCd) {
		this.paymentMethodCd = paymentMethodCd;
	}
	public String getDEVICE_TYPE_CD_MW() {
		return DEVICE_TYPE_CD_MW;
	}
	public void setDEVICE_TYPE_CD_MW(String dEVICE_TYPE_CD_MW) {
		DEVICE_TYPE_CD_MW = dEVICE_TYPE_CD_MW;
	}
	public String getCST_PLATFORM() {
		return CST_PLATFORM;
	}
	public void setCST_PLATFORM(String cST_PLATFORM) {
		CST_PLATFORM = cST_PLATFORM;
	}
	public String getCST_MID() {
		return CST_MID;
	}
	public void setCST_MID(String cST_MID) {
		CST_MID = cST_MID;
	}
	public String getLGD_MID() {
		return LGD_MID;
	}
	public void setLGD_MID(String lGD_MID) {
		LGD_MID = lGD_MID;
	}
	public String getLGD_OID() {
		return LGD_OID;
	}
	public void setLGD_OID(String lGD_OID) {
		LGD_OID = lGD_OID;
	}
	public String getLGD_AMOUNT() {
		return LGD_AMOUNT;
	}
	public void setLGD_AMOUNT(String lGD_AMOUNT) {
		LGD_AMOUNT = lGD_AMOUNT;
	}
	public String getLGD_MERTKEY() {
		return LGD_MERTKEY;
	}
	public void setLGD_MERTKEY(String lGD_MERTKEY) {
		LGD_MERTKEY = lGD_MERTKEY;
	}
	public String getLGD_BUYER() {
		return LGD_BUYER;
	}
	public void setLGD_BUYER(String lGD_BUYER) {
		LGD_BUYER = lGD_BUYER;
	}
	public String getLGD_PRODUCTINFO() {
		return LGD_PRODUCTINFO;
	}
	public void setLGD_PRODUCTINFO(String lGD_PRODUCTINFO) {
		LGD_PRODUCTINFO = lGD_PRODUCTINFO;
	}
	public String getLGD_BUYEREMAIL() {
		return LGD_BUYEREMAIL;
	}
	public void setLGD_BUYEREMAIL(String lGD_BUYEREMAIL) {
		LGD_BUYEREMAIL = lGD_BUYEREMAIL;
	}
	public String getLGD_TIMESTAMP() {
		return LGD_TIMESTAMP;
	}
	public void setLGD_TIMESTAMP(String lGD_TIMESTAMP) {
		LGD_TIMESTAMP = lGD_TIMESTAMP;
	}
	public String getLGD_CUSTOM_SKIN() {
		return LGD_CUSTOM_SKIN;
	}
	public void setLGD_CUSTOM_SKIN(String lGD_CUSTOM_SKIN) {
		LGD_CUSTOM_SKIN = lGD_CUSTOM_SKIN;
	}
	public String getLGD_WINDOW_VER() {
		return LGD_WINDOW_VER;
	}
	public void setLGD_WINDOW_VER(String lGD_WINDOW_VER) {
		LGD_WINDOW_VER = lGD_WINDOW_VER;
	}
	public String getLGD_BUYERID() {
		return LGD_BUYERID;
	}
	public void setLGD_BUYERID(String lGD_BUYERID) {
		LGD_BUYERID = lGD_BUYERID;
	}
	public String getLGD_BUYERIP() {
		return LGD_BUYERIP;
	}
	public void setLGD_BUYERIP(String lGD_BUYERIP) {
		LGD_BUYERIP = lGD_BUYERIP;
	}
	public String getLGD_CUSTOM_USABLEPAY() {
		return LGD_CUSTOM_USABLEPAY;
	}
	public void setLGD_CUSTOM_USABLEPAY(String lGD_CUSTOM_USABLEPAY) {
		LGD_CUSTOM_USABLEPAY = lGD_CUSTOM_USABLEPAY;
	}
	public String getLGD_ENCODING() {
		return LGD_ENCODING;
	}
	public void setLGD_ENCODING(String lGD_ENCODING) {
		LGD_ENCODING = lGD_ENCODING;
	}
	public String getLGD_ENCODING_RETURNURL() {
		return LGD_ENCODING_RETURNURL;
	}
	public void setLGD_ENCODING_RETURNURL(String lGD_ENCODING_RETURNURL) {
		LGD_ENCODING_RETURNURL = lGD_ENCODING_RETURNURL;
	}
	public String getLGD_ENCODING_NOTEURL() {
		return LGD_ENCODING_NOTEURL;
	}
	public void setLGD_ENCODING_NOTEURL(String lGD_ENCODING_NOTEURL) {
		LGD_ENCODING_NOTEURL = lGD_ENCODING_NOTEURL;
	}
	public String getLGD_CASNOTEURL() {
		return LGD_CASNOTEURL;
	}
	public void setLGD_CASNOTEURL(String lGD_CASNOTEURL) {
		LGD_CASNOTEURL = lGD_CASNOTEURL;
	}
	public String getLGD_RETURNURL() {
		return LGD_RETURNURL;
	}
	public void setLGD_RETURNURL(String lGD_RETURNURL) {
		LGD_RETURNURL = lGD_RETURNURL;
	}
	public String getLGD_HASHDATA() {
		return LGD_HASHDATA;
	}
	public void setLGD_HASHDATA(String lGD_HASHDATA) {
		LGD_HASHDATA = lGD_HASHDATA;
	}
	public String getLGD_CUSTOM_PROCESSTYPE() {
		return LGD_CUSTOM_PROCESSTYPE;
	}
	public void setLGD_CUSTOM_PROCESSTYPE(String lGD_CUSTOM_PROCESSTYPE) {
		LGD_CUSTOM_PROCESSTYPE = lGD_CUSTOM_PROCESSTYPE;
	}
	public String getLGD_PAYKEY() {
		return LGD_PAYKEY;
	}
	public void setLGD_PAYKEY(String lGD_PAYKEY) {
		LGD_PAYKEY = lGD_PAYKEY;
	}
	public String getLGD_WINDOW_TYPE() {
		return LGD_WINDOW_TYPE;
	}
	public void setLGD_WINDOW_TYPE(String lGD_WINDOW_TYPE) {
		LGD_WINDOW_TYPE = lGD_WINDOW_TYPE;
	}
	public String getLGD_CUSTOM_SWITCHINGTYPE() {
		return LGD_CUSTOM_SWITCHINGTYPE;
	}
	public void setLGD_CUSTOM_SWITCHINGTYPE(String lGD_CUSTOM_SWITCHINGTYPE) {
		LGD_CUSTOM_SWITCHINGTYPE = lGD_CUSTOM_SWITCHINGTYPE;
	}
	public String getLGD_OSTYPE_CHECK() {
		return LGD_OSTYPE_CHECK;
	}
	public void setLGD_OSTYPE_CHECK(String lGD_OSTYPE_CHECK) {
		LGD_OSTYPE_CHECK = lGD_OSTYPE_CHECK;
	}
	public String getLGD_AUTOFILLYN_BUYER() {
		return LGD_AUTOFILLYN_BUYER;
	}
	public void setLGD_AUTOFILLYN_BUYER(String lGD_AUTOFILLYN_BUYER) {
		LGD_AUTOFILLYN_BUYER = lGD_AUTOFILLYN_BUYER;
	}
	public String getLGD_USABLEBANK() {
		return LGD_USABLEBANK;
	}
	public void setLGD_USABLEBANK(String lGD_USABLEBANK) {
		LGD_USABLEBANK = lGD_USABLEBANK;
	}
	public String getLGD_SELF_CUSTOM() {
		return LGD_SELF_CUSTOM;
	}
	public void setLGD_SELF_CUSTOM(String lGD_SELF_CUSTOM) {
		LGD_SELF_CUSTOM = lGD_SELF_CUSTOM;
	}
	public String getLGD_SP_CHAIN_CODE() {
		return LGD_SP_CHAIN_CODE;
	}
	public void setLGD_SP_CHAIN_CODE(String lGD_SP_CHAIN_CODE) {
		LGD_SP_CHAIN_CODE = lGD_SP_CHAIN_CODE;
	}
	public String getLGD_SP_ORDER_USER_ID() {
		return LGD_SP_ORDER_USER_ID;
	}
	public void setLGD_SP_ORDER_USER_ID(String lGD_SP_ORDER_USER_ID) {
		LGD_SP_ORDER_USER_ID = lGD_SP_ORDER_USER_ID;
	}
	public String getLGD_POINTUSE() {
		return LGD_POINTUSE;
	}
	public void setLGD_POINTUSE(String lGD_POINTUSE) {
		LGD_POINTUSE = lGD_POINTUSE;
	}
	public String getLGD_MERTNAME() {
		return LGD_MERTNAME;
	}
	public void setLGD_MERTNAME(String lGD_MERTNAME) {
		LGD_MERTNAME = lGD_MERTNAME;
	}
	public String getLGD_AUTHTYPE() {
		return LGD_AUTHTYPE;
	}
	public void setLGD_AUTHTYPE(String lGD_AUTHTYPE) {
		LGD_AUTHTYPE = lGD_AUTHTYPE;
	}
	public String getLGD_CARDTYPE() {
		return LGD_CARDTYPE;
	}
	public void setLGD_CARDTYPE(String lGD_CARDTYPE) {
		LGD_CARDTYPE = lGD_CARDTYPE;
	}
	public String getLGD_CURRENCY() {
		return LGD_CURRENCY;
	}
	public void setLGD_CURRENCY(String lGD_CURRENCY) {
		LGD_CURRENCY = lGD_CURRENCY;
	}
	public String getLGD_PAN() {
		return LGD_PAN;
	}
	public void setLGD_PAN(String lGD_PAN) {
		LGD_PAN = lGD_PAN;
	}
	public String getLGD_INSTALL() {
		return LGD_INSTALL;
	}
	public void setLGD_INSTALL(String lGD_INSTALL) {
		LGD_INSTALL = lGD_INSTALL;
	}
	public String getLGD_NOINT() {
		return LGD_NOINT;
	}
	public void setLGD_NOINT(String lGD_NOINT) {
		LGD_NOINT = lGD_NOINT;
	}
	public String getLGD_EXPYEAR() {
		return LGD_EXPYEAR;
	}
	public void setLGD_EXPYEAR(String lGD_EXPYEAR) {
		LGD_EXPYEAR = lGD_EXPYEAR;
	}
	public String getLGD_EXPMON() {
		return LGD_EXPMON;
	}
	public void setLGD_EXPMON(String lGD_EXPMON) {
		LGD_EXPMON = lGD_EXPMON;
	}
	public String getVBV_ECI() {
		return VBV_ECI;
	}
	public void setVBV_ECI(String vBV_ECI) {
		VBV_ECI = vBV_ECI;
	}
	public String getVBV_CAVV() {
		return VBV_CAVV;
	}
	public void setVBV_CAVV(String vBV_CAVV) {
		VBV_CAVV = vBV_CAVV;
	}
	public String getVBV_XID() {
		return VBV_XID;
	}
	public void setVBV_XID(String vBV_XID) {
		VBV_XID = vBV_XID;
	}
	public String getVBV_JOINCODE() {
		return VBV_JOINCODE;
	}
	public void setVBV_JOINCODE(String vBV_JOINCODE) {
		VBV_JOINCODE = vBV_JOINCODE;
	}
	public String getKVP_QUOTA() {
		return KVP_QUOTA;
	}
	public void setKVP_QUOTA(String kVP_QUOTA) {
		KVP_QUOTA = kVP_QUOTA;
	}
	public String getKVP_NOINT() {
		return KVP_NOINT;
	}
	public void setKVP_NOINT(String kVP_NOINT) {
		KVP_NOINT = kVP_NOINT;
	}
	public String getKVP_CARDCODE() {
		return KVP_CARDCODE;
	}
	public void setKVP_CARDCODE(String kVP_CARDCODE) {
		KVP_CARDCODE = kVP_CARDCODE;
	}
	public String getKVP_SESSIONKEY() {
		return KVP_SESSIONKEY;
	}
	public void setKVP_SESSIONKEY(String kVP_SESSIONKEY) {
		KVP_SESSIONKEY = kVP_SESSIONKEY;
	}
	public String getKVP_ENCDATA() {
		return KVP_ENCDATA;
	}
	public void setKVP_ENCDATA(String kVP_ENCDATA) {
		KVP_ENCDATA = kVP_ENCDATA;
	}
	public String getLGD_METHOD() {
		return LGD_METHOD;
	}
	public void setLGD_METHOD(String lGD_METHOD) {
		LGD_METHOD = lGD_METHOD;
	}
	public String getLGD_ACCOUNTOWNER() {
		return LGD_ACCOUNTOWNER;
	}
	public void setLGD_ACCOUNTOWNER(String lGD_ACCOUNTOWNER) {
		LGD_ACCOUNTOWNER = lGD_ACCOUNTOWNER;
	}
	public String getLGD_ACCOUNTPID() {
		return LGD_ACCOUNTPID;
	}
	public void setLGD_ACCOUNTPID(String lGD_ACCOUNTPID) {
		LGD_ACCOUNTPID = lGD_ACCOUNTPID;
	}
	public String getLGD_BUYERPHONE() {
		return LGD_BUYERPHONE;
	}
	public void setLGD_BUYERPHONE(String lGD_BUYERPHONE) {
		LGD_BUYERPHONE = lGD_BUYERPHONE;
	}
	public String getLGD_BANKCODE() {
		return LGD_BANKCODE;
	}
	public void setLGD_BANKCODE(String lGD_BANKCODE) {
		LGD_BANKCODE = lGD_BANKCODE;
	}
	public String getLGD_CASHRECEIPTYN() {
		return LGD_CASHRECEIPTYN;
	}
	public void setLGD_CASHRECEIPTYN(String lGD_CASHRECEIPTYN) {
		LGD_CASHRECEIPTYN = lGD_CASHRECEIPTYN;
	}
	public String getLGD_CASHRECEIPTUSE() {
		return LGD_CASHRECEIPTUSE;
	}
	public void setLGD_CASHRECEIPTUSE(String lGD_CASHRECEIPTUSE) {
		LGD_CASHRECEIPTUSE = lGD_CASHRECEIPTUSE;
	}
	public String getLGD_CASHCARDNUM() {
		return LGD_CASHCARDNUM;
	}
	public void setLGD_CASHCARDNUM(String lGD_CASHCARDNUM) {
		LGD_CASHCARDNUM = lGD_CASHCARDNUM;
	}
	public String getLGD_CLOSEDATE() {
		return LGD_CLOSEDATE;
	}
	public void setLGD_CLOSEDATE(String lGD_CLOSEDATE) {
		LGD_CLOSEDATE = lGD_CLOSEDATE;
	}
	public String getLGD_TAXFREEAMOUNT() {
		return LGD_TAXFREEAMOUNT;
	}
	public void setLGD_TAXFREEAMOUNT(String lGD_TAXFREEAMOUNT) {
		LGD_TAXFREEAMOUNT = lGD_TAXFREEAMOUNT;
	}
	public String getLGD_BUYERSSN() {
		return LGD_BUYERSSN;
	}
	public void setLGD_BUYERSSN(String lGD_BUYERSSN) {
		LGD_BUYERSSN = lGD_BUYERSSN;
	}
	public String getLGD_CHECKSSNYN() {
		return LGD_CHECKSSNYN;
	}
	public void setLGD_CHECKSSNYN(String lGD_CHECKSSNYN) {
		LGD_CHECKSSNYN = lGD_CHECKSSNYN;
	}
	public String getLGD_PAYWINDOWTYPE() {
		return LGD_PAYWINDOWTYPE;
	}
	public void setLGD_PAYWINDOWTYPE(String lGD_PAYWINDOWTYPE) {
		LGD_PAYWINDOWTYPE = lGD_PAYWINDOWTYPE;
	}
	public String getLGD_VERSION() {
		return LGD_VERSION;
	}
	public void setLGD_VERSION(String lGD_VERSION) {
		LGD_VERSION = lGD_VERSION;
	}
	public String getLGD_BILLKEY() {
		return LGD_BILLKEY;
	}
	public void setLGD_BILLKEY(String lGD_BILLKEY) {
		LGD_BILLKEY = lGD_BILLKEY;
	}
	public String getLGD_PIN() {
		return LGD_PIN;
	}
	public void setLGD_PIN(String lGD_PIN) {
		LGD_PIN = lGD_PIN;
	}
	public String getLGD_PRIVATENO() {
		return LGD_PRIVATENO;
	}
	public void setLGD_PRIVATENO(String lGD_PRIVATENO) {
		LGD_PRIVATENO = lGD_PRIVATENO;
	}
	public String getLGD_TXNAME() {
		return LGD_TXNAME;
	}
	public void setLGD_TXNAME(String lGD_TXNAME) {
		LGD_TXNAME = lGD_TXNAME;
	}
	public String getLGD_CANCELAMOUNT() {
		return LGD_CANCELAMOUNT;
	}
	public void setLGD_CANCELAMOUNT(String lGD_CANCELAMOUNT) {
		LGD_CANCELAMOUNT = lGD_CANCELAMOUNT;
	}
	public String getLGD_REMAINAMOUNT() {
		return LGD_REMAINAMOUNT;
	}
	public void setLGD_REMAINAMOUNT(String lGD_REMAINAMOUNT) {
		LGD_REMAINAMOUNT = lGD_REMAINAMOUNT;
	}
	public String getLGD_CANCELREASON() {
		return LGD_CANCELREASON;
	}
	public void setLGD_CANCELREASON(String lGD_CANCELREASON) {
		LGD_CANCELREASON = lGD_CANCELREASON;
	}
	public String getLGD_RFBANKCODE() {
		return LGD_RFBANKCODE;
	}
	public void setLGD_RFBANKCODE(String lGD_RFBANKCODE) {
		LGD_RFBANKCODE = lGD_RFBANKCODE;
	}
	public String getLGD_RFACCOUNTNUM() {
		return LGD_RFACCOUNTNUM;
	}
	public void setLGD_RFACCOUNTNUM(String lGD_RFACCOUNTNUM) {
		LGD_RFACCOUNTNUM = lGD_RFACCOUNTNUM;
	}
	public String getLGD_RFCUSTOMERNAME() {
		return LGD_RFCUSTOMERNAME;
	}
	public void setLGD_RFCUSTOMERNAME(String lGD_RFCUSTOMERNAME) {
		LGD_RFCUSTOMERNAME = lGD_RFCUSTOMERNAME;
	}
	public String getLGD_RFPHONE() {
		return LGD_RFPHONE;
	}
	public void setLGD_RFPHONE(String lGD_RFPHONE) {
		LGD_RFPHONE = lGD_RFPHONE;
	}
	public String getLGD_RESPCODE() {
		return LGD_RESPCODE;
	}
	public void setLGD_RESPCODE(String lGD_RESPCODE) {
		LGD_RESPCODE = lGD_RESPCODE;
	}
	public String getLGD_RESPMSG() {
		return LGD_RESPMSG;
	}
	public void setLGD_RESPMSG(String lGD_RESPMSG) {
		LGD_RESPMSG = lGD_RESPMSG;
	}
	public String getLGD_TID() {
		return LGD_TID;
	}
	public void setLGD_TID(String lGD_TID) {
		LGD_TID = lGD_TID;
	}
	public String getLGD_PAYTYPE() {
		return LGD_PAYTYPE;
	}
	public void setLGD_PAYTYPE(String lGD_PAYTYPE) {
		LGD_PAYTYPE = lGD_PAYTYPE;
	}
	public String getLGD_PAYDATE() {
		return LGD_PAYDATE;
	}
	public void setLGD_PAYDATE(String lGD_PAYDATE) {
		LGD_PAYDATE = lGD_PAYDATE;
	}
	public String getLGD_FINANCECODE() {
		return LGD_FINANCECODE;
	}
	public void setLGD_FINANCECODE(String lGD_FINANCECODE) {
		LGD_FINANCECODE = lGD_FINANCECODE;
	}
	public String getLGD_FINANCENAME() {
		return LGD_FINANCENAME;
	}
	public void setLGD_FINANCENAME(String lGD_FINANCENAME) {
		LGD_FINANCENAME = lGD_FINANCENAME;
	}
	public String getLGD_ESCROWYN() {
		return LGD_ESCROWYN;
	}
	public void setLGD_ESCROWYN(String lGD_ESCROWYN) {
		LGD_ESCROWYN = lGD_ESCROWYN;
	}
	public String getLGD_ESCROW_USEYN() {
		return LGD_ESCROW_USEYN;
	}
	public void setLGD_ESCROW_USEYN(String lGD_ESCROW_USEYN) {
		LGD_ESCROW_USEYN = lGD_ESCROW_USEYN;
	}
	public String getLGD_ACCOUNTNUM() {
		return LGD_ACCOUNTNUM;
	}
	public void setLGD_ACCOUNTNUM(String lGD_ACCOUNTNUM) {
		LGD_ACCOUNTNUM = lGD_ACCOUNTNUM;
	}
	public String getLGD_CASTAMOUNT() {
		return LGD_CASTAMOUNT;
	}
	public void setLGD_CASTAMOUNT(String lGD_CASTAMOUNT) {
		LGD_CASTAMOUNT = lGD_CASTAMOUNT;
	}
	public String getLGD_CASCAMOUNT() {
		return LGD_CASCAMOUNT;
	}
	public void setLGD_CASCAMOUNT(String lGD_CASCAMOUNT) {
		LGD_CASCAMOUNT = lGD_CASCAMOUNT;
	}
	public String getLGD_CASFLAG() {
		return LGD_CASFLAG;
	}
	public void setLGD_CASFLAG(String lGD_CASFLAG) {
		LGD_CASFLAG = lGD_CASFLAG;
	}
	public String getLGD_CASSEQNO() {
		return LGD_CASSEQNO;
	}
	public void setLGD_CASSEQNO(String lGD_CASSEQNO) {
		LGD_CASSEQNO = lGD_CASSEQNO;
	}
	public String getLGD_CASHRECEIPTNUM() {
		return LGD_CASHRECEIPTNUM;
	}
	public void setLGD_CASHRECEIPTNUM(String lGD_CASHRECEIPTNUM) {
		LGD_CASHRECEIPTNUM = lGD_CASHRECEIPTNUM;
	}
	public String getLGD_CASHRECEIPTSELFYN() {
		return LGD_CASHRECEIPTSELFYN;
	}
	public void setLGD_CASHRECEIPTSELFYN(String lGD_CASHRECEIPTSELFYN) {
		LGD_CASHRECEIPTSELFYN = lGD_CASHRECEIPTSELFYN;
	}
	public String getLGD_CASHRECEIPTKIND() {
		return LGD_CASHRECEIPTKIND;
	}
	public void setLGD_CASHRECEIPTKIND(String lGD_CASHRECEIPTKIND) {
		LGD_CASHRECEIPTKIND = lGD_CASHRECEIPTKIND;
	}
	public String getLGD_PAYER() {
		return LGD_PAYER;
	}
	public void setLGD_PAYER(String lGD_PAYER) {
		LGD_PAYER = lGD_PAYER;
	}
	public String getLGD_BUYERADDRESS() {
		return LGD_BUYERADDRESS;
	}
	public void setLGD_BUYERADDRESS(String lGD_BUYERADDRESS) {
		LGD_BUYERADDRESS = lGD_BUYERADDRESS;
	}
	public String getLGD_PRODUCTCODE() {
		return LGD_PRODUCTCODE;
	}
	public void setLGD_PRODUCTCODE(String lGD_PRODUCTCODE) {
		LGD_PRODUCTCODE = lGD_PRODUCTCODE;
	}
	public String getLGD_RECEIVER() {
		return LGD_RECEIVER;
	}
	public void setLGD_RECEIVER(String lGD_RECEIVER) {
		LGD_RECEIVER = lGD_RECEIVER;
	}
	public String getLGD_RECEIVERPHONE() {
		return LGD_RECEIVERPHONE;
	}
	public void setLGD_RECEIVERPHONE(String lGD_RECEIVERPHONE) {
		LGD_RECEIVERPHONE = lGD_RECEIVERPHONE;
	}
	public String getLGD_DELIVERYINFO() {
		return LGD_DELIVERYINFO;
	}
	public void setLGD_DELIVERYINFO(String lGD_DELIVERYINFO) {
		LGD_DELIVERYINFO = lGD_DELIVERYINFO;
	}
	public String getCST_WINDOW_TYPE() {
		return CST_WINDOW_TYPE;
	}
	public void setCST_WINDOW_TYPE(String cST_WINDOW_TYPE) {
		CST_WINDOW_TYPE = cST_WINDOW_TYPE;
	}
	public String getLGD_MPILOTTEAPPCARDWAPURL() {
		return LGD_MPILOTTEAPPCARDWAPURL;
	}
	public void setLGD_MPILOTTEAPPCARDWAPURL(String lGD_MPILOTTEAPPCARDWAPURL) {
		LGD_MPILOTTEAPPCARDWAPURL = lGD_MPILOTTEAPPCARDWAPURL;
	}
	public String getLGD_KVPMISPWAPURL() {
		return LGD_KVPMISPWAPURL;
	}
	public void setLGD_KVPMISPWAPURL(String lGD_KVPMISPWAPURL) {
		LGD_KVPMISPWAPURL = lGD_KVPMISPWAPURL;
	}
	public String getLGD_KVPMISPCANCELURL() {
		return LGD_KVPMISPCANCELURL;
	}
	public void setLGD_KVPMISPCANCELURL(String lGD_KVPMISPCANCELURL) {
		LGD_KVPMISPCANCELURL = lGD_KVPMISPCANCELURL;
	}
	public String getLGD_MTRANSFERWAPURL() {
		return LGD_MTRANSFERWAPURL;
	}
	public void setLGD_MTRANSFERWAPURL(String lGD_MTRANSFERWAPURL) {
		LGD_MTRANSFERWAPURL = lGD_MTRANSFERWAPURL;
	}
	public String getLGD_MTRANSFERCANCELURL() {
		return LGD_MTRANSFERCANCELURL;
	}
	public void setLGD_MTRANSFERCANCELURL(String lGD_MTRANSFERCANCELURL) {
		LGD_MTRANSFERCANCELURL = lGD_MTRANSFERCANCELURL;
	}
	public String getLGD_KVPMISPAUTOAPPYN() {
		return LGD_KVPMISPAUTOAPPYN;
	}
	public void setLGD_KVPMISPAUTOAPPYN(String lGD_KVPMISPAUTOAPPYN) {
		LGD_KVPMISPAUTOAPPYN = lGD_KVPMISPAUTOAPPYN;
	}
	public String getLGD_MTRANSFERAUTOAPPYN() {
		return LGD_MTRANSFERAUTOAPPYN;
	}
	public void setLGD_MTRANSFERAUTOAPPYN(String lGD_MTRANSFERAUTOAPPYN) {
		LGD_MTRANSFERAUTOAPPYN = lGD_MTRANSFERAUTOAPPYN;
	}
	public String getLGD_DISABLE_AGREE() {
		return LGD_DISABLE_AGREE;
	}
	public void setLGD_DISABLE_AGREE(String lGD_DISABLE_AGREE) {
		LGD_DISABLE_AGREE = lGD_DISABLE_AGREE;
	}
	public String getLGD_FINANCEAUTHNUM() {
		return LGD_FINANCEAUTHNUM;
	}
	public void setLGD_FINANCEAUTHNUM(String lGD_FINANCEAUTHNUM) {
		LGD_FINANCEAUTHNUM = lGD_FINANCEAUTHNUM;
	}
	public String getLGD_ACTIVEXYN() {
		return LGD_ACTIVEXYN;
	}
	public void setLGD_ACTIVEXYN(String lGD_ACTIVEXYN) {
		LGD_ACTIVEXYN = lGD_ACTIVEXYN;
	}
	public String getLGD_CUSTOM_TITLE() {
		return LGD_CUSTOM_TITLE;
	}
	public void setLGD_CUSTOM_TITLE(String lGD_CUSTOM_TITLE) {
		LGD_CUSTOM_TITLE = lGD_CUSTOM_TITLE;
	}
	public String getLGD_NOTEUR() {
		return LGD_NOTEUR;
	}
	public void setLGD_NOTEUR(String lGD_NOTEUR) {
		LGD_NOTEUR = lGD_NOTEUR;
	}
	public String getLGD_CANCELURL() {
		return LGD_CANCELURL;
	}
	public void setLGD_CANCELURL(String lGD_CANCELURL) {
		LGD_CANCELURL = lGD_CANCELURL;
	}
	public String getLGD_MTRANSFERNOTEURL() {
		return LGD_MTRANSFERNOTEURL;
	}
	public void setLGD_MTRANSFERNOTEURL(String lGD_MTRANSFERNOTEURL) {
		LGD_MTRANSFERNOTEURL = lGD_MTRANSFERNOTEURL;
	}
	public String getLGD_NOTEURL() {
		return LGD_NOTEURL;
	}
	public void setLGD_NOTEURL(String lGD_NOTEURL) {
		LGD_NOTEURL = lGD_NOTEURL;
	}
	public String getLGD_DOMAIN_URL() {
		return LGD_DOMAIN_URL;
	}
	public void setLGD_DOMAIN_URL(String lGD_DOMAIN_URL) {
		LGD_DOMAIN_URL = lGD_DOMAIN_URL;
	}
	public String getOrderId() {
		return OrderId;
	}
	public void setOrderId(String orderId) {
		OrderId = orderId;
	}
	public String getPaymentNo() {
		return PaymentNo;
	}
	public void setPaymentNo(String paymentNo) {
		PaymentNo = paymentNo;
	}
	public String getLGD_AFFILIATECODE() {
		return LGD_AFFILIATECODE;
	}
	public void setLGD_AFFILIATECODE(String lGD_AFFILIATECODE) {
		LGD_AFFILIATECODE = lGD_AFFILIATECODE;
	}
	public String getLGD_DEVICE() {
		return LGD_DEVICE;
	}
	public void setLGD_DEVICE(String lGD_DEVICE) {
		LGD_DEVICE = lGD_DEVICE;
	}
	public String getLGD_CARDPREFIX() {
		return LGD_CARDPREFIX;
	}
	public void setLGD_CARDPREFIX(String lGD_CARDPREFIX) {
		LGD_CARDPREFIX = lGD_CARDPREFIX;
	}
	public String getUrIdx() {
		return urIdx;
	}
	public void setUrIdx(String urIdx) {
		this.urIdx = urIdx;
	}
	public String getQuIdx() {
		return quIdx;
	}
	public void setQuIdx(String quIdx) {
		this.quIdx = quIdx;
	}
	public String getQrIdx() {
		return qrIdx;
	}
	public void setQrIdx(String qrIdx) {
		this.qrIdx = qrIdx;
	}
	public String getRmIdx() {
		return rmIdx;
	}
	public void setRmIdx(String rmIdx) {
		this.rmIdx = rmIdx;
	}
	public String getRentFee() {
		return rentFee;
	}
	public void setRentFee(String rentFee) {
		this.rentFee = rentFee;
	}
	public String getInsuranceFee() {
		return insuranceFee;
	}
	public void setInsuranceFee(String insuranceFee) {
		this.insuranceFee = insuranceFee;
	}
	public String getCarDeposit() {
		return carDeposit;
	}
	public void setCarDeposit(String carDeposit) {
		this.carDeposit = carDeposit;
	}
	public String getCancelFee() {
		return cancelFee;
	}
	public void setCancelFee(String cancelFee) {
		this.cancelFee = cancelFee;
	}
	public String getCancelAmount() {
		return cancelAmount;
	}
	public void setCancelAmount(String cancelAmount) {
		this.cancelAmount = cancelAmount;
	}
	public String getDeviceTypeCode() {
		return deviceTypeCode;
	}
	public void setDeviceTypeCode(String deviceTypeCode) {
		this.deviceTypeCode = deviceTypeCode;
	}
	public String getDeliveryAddr() {
		return deliveryAddr;
	}
	public void setDeliveryAddr(String deliveryAddr) {
		this.deliveryAddr = deliveryAddr;
	}
	public String getReturnAddr() {
		return returnAddr;
	}
	public void setReturnAddr(String returnAddr) {
		this.returnAddr = returnAddr;
	}
	public String getRentStartDay() {
		return rentStartDay;
	}
	public void setRentStartDay(String rentStartDay) {
		this.rentStartDay = rentStartDay;
	}
	public String getRentStartTime() {
		return rentStartTime;
	}
	public void setRentStartTime(String rentStartTime) {
		this.rentStartTime = rentStartTime;
	}
	public String getRentEndDay() {
		return rentEndDay;
	}
	public void setRentEndDay(String rentEndDay) {
		this.rentEndDay = rentEndDay;
	}
	public String getRentEndTime() {
		return rentEndTime;
	}
	public void setRentEndTime(String rentEndTime) {
		this.rentEndTime = rentEndTime;
	}
	public String getDeliveryTypeCode() {
		return deliveryTypeCode;
	}
	public void setDeliveryTypeCode(String deliveryTypeCode) {
		this.deliveryTypeCode = deliveryTypeCode;
	}
	public String getLongterm() {
		return longterm;
	}
	public void setLongterm(String longterm) {
		this.longterm = longterm;
	}
	public String getCarSearchYn() {
		return carSearchYn;
	}
	public void setCarSearchYn(String carSearchYn) {
		this.carSearchYn = carSearchYn;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCrIdx() {
		return crIdx;
	}
	public void setCrIdx(String crIdx) {
		this.crIdx = crIdx;
	}
	public String getReserveDate() {
		return reserveDate;
	}
	public void setReserveDate(String reserveDate) {
		this.reserveDate = reserveDate;
	}
	
	
	
}