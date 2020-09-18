package com.ohdocha.cu.kprojectcu.util;



import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdocha.cu.kprojectcu.domain.DochaAlarmTalkDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.apache.tomcat.util.json.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/*
 * 아임포트에서 https://api.iamport.kr/certifications/ 에서 결과값을 받아오는 부분
 * 
 * */
public class SmsAuthUtil {

	private final String USER_AGENT = "Mozilla/5.0";
	
	@Value("${iamport.imp_key}")
	private String imp_key;
	
	@Value("${iamport.imp_secret}")
	private String imp_secret;

	@Value("${iamport.getToken.url}")
	private String imp_getTokenUrl;
	
	@Value("${kakao.alert.talk.key}")
	private String kakao_api_key;
	
	@Value("${kakao.alert.callback.number}")
	private String kakao_callback_number;
	
	@Value("${kakao.alert.fail.type}")
	private String kakao_fail_type;

	

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String getToken(HttpServletRequest request, HttpServletResponse response, JSONObject json , String requestURL) throws Exception{


		// requestURL 아임퐅크 고유키, 시크릿 키 정보를 포함하는 url 정보 
		String _token = "";

		try{

			String requestString = "";
			URL url = new URL(requestURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true); 						
			connection.setInstanceFollowRedirects(false);  
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			OutputStream os= connection.getOutputStream();
			os.write(json.toString().getBytes());
			connection.connect();
			StringBuilder sb = new StringBuilder(); 
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
				String line = null;  
				while ((line = br.readLine()) != null) {  
					sb.append(line + "\n");  
				}
				br.close();
				requestString = sb.toString();		
			}
			os.flush();
			connection.disconnect();

			System.out.println(requestString);
			
			JSONParser jsonParser = new JSONParser(requestString);
			JSONObject jsonObj = (JSONObject) jsonParser.parse();

			if((Long)jsonObj.get("code")  == 0){
				JSONObject getToken = (JSONObject) jsonObj.get("response");
				System.out.println("getToken==>>"+getToken.get("access_token") );
				_token = (String)getToken.get("access_token");
			}

		}catch(Exception e){
			e.printStackTrace();
			_token = "";
		}

		return _token;

	}//end gettoken

	public String getJSON(String url, int timeout) {
	    HttpURLConnection c = null;
	    try {
	        URL u = new URL(url);
	        c = (HttpURLConnection) u.openConnection();
	        c.setRequestMethod("GET");
	        c.setRequestProperty("Content-length", "0");
	        c.setUseCaches(false);
	        c.setAllowUserInteraction(false);
	        c.setConnectTimeout(timeout);
	        c.setReadTimeout(timeout);
	        c.connect();
	        int status = c.getResponseCode();

	        switch (status) {
	            case 200:
	            case 201:
	                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
	                StringBuilder sb = new StringBuilder();
	                String line;
	                while ((line = br.readLine()) != null) {
	                    sb.append(line+"\n");
	                }
	                br.close();
	                
	              //  String encodeResult = URLEncoder.encode(sb.toString(), "UTF-8");

	                
	                return sb.toString();
	        }

	    } catch (MalformedURLException ex) {
	       // log.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
	    } catch (IOException ex) {
	    	//log.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
	    } finally {
	       if (c != null) {
	          try {
	              c.disconnect();
	          } catch (Exception ex) {
	        	//  log.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
	          }
	       }
	    }
	    return null;
	}//end getJson
	

//	/**
//	 *
//	 * @param ApiKey 알림톡API KEY(@(Valuse("${kakao.alert.talk.key}") 사용)
//	 * @param sendPhoneNumber 알림톡을 보낼 연락처
//	 * @param callbackPhoneNumber 발신번호
//	 * @param sendMsg 보낼 MSG
//	 * @param templetCode 사전등록된 알림톡 템플릿 코드
//	 * @param failType 실패 (SMS, LMS, N(전송안함))
//	 * @param buttonType 알림톡 버튼타입 (웹링크, 앱링크, 봇키워드, 메시지전달, 배송조회)
//	 * @param buttonTxt 버튼이름
//	 * @param btnUrl1 연결URL 1 웹링크, 앱링크는 URL 이 필수이며, 기타(배송조회, 봇키워드, 메시지전달)은 null 값임.
//	 * @param btnUrl2 연결URL 2 웹링크, 앱링크는 URL 이 필수이며, 기타(배송조회, 봇키워드, 메시지전달)은 null 값임.
//	 * @return
//	 * @throws JsonGenerationException
//	 * @throws JsonMappingException
//	 * @throws IOException
//	 */
	public HashMap<String, Object> sendKakaoTalk(String msg) throws JsonGenerationException, JsonMappingException, IOException {
        
        HashMap<String, String> paramMap = new HashMap<String, String>();
        
		paramMap = new HashMap<String, String>();
        String apiUrl = "http://api.apistore.co.kr/kko/1/msg/Docha";
        //String apiUrlTest = "http://api.apistore.co.kr/kko/1/msg_test/Docha";
        
        
        ObjectMapper mapper = new ObjectMapper();
		paramMap.put("PHONE", "01076820622");
	    paramMap.put("CALLBACK","01076820622");
	    paramMap.put("MSG", "");
	    paramMap.put("TEMPLATE_CODE", "RU01");
	    paramMap.put("FAILED_TYPE", "SMS");
	    paramMap.put("BTN_TYPES ", "웹링크");
	    paramMap.put("BTN_TXTS", "견적바로확인");
	    paramMap.put("BTN_URLS1", "https://www.Docha.com/user/userEstDetail.do");
	    paramMap.put("BTN_URLS2", "");
        String jsonData = mapper.writeValueAsString(paramMap);
        
        HttpHeaders headers = new HttpHeaders();
        
        //고정
        headers.set("x-waple-authorization", "MTE5NDktMTU3NDM5MzQyMDQzNC1lYThlMTJjYi1mZWIwLTQxZDQtOGUxMi1jYmZlYjBjMWQ0MmM");
        headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        
        HttpEntity<String> request = new HttpEntity<>(jsonData, headers);


        HashMap<String, Object> responseMap = new HashMap<String, Object>();
        
        RestTemplate restTemplate = new RestTemplate();
        
        String resultCd = "3";
        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        try{
            responseMap = restTemplate.postForObject(apiUrl, request, HashMap.class);
            //responseMap = restTemplate.postForObject(apiUrlTest, request, HashMap.class);
        }catch (HttpStatusCodeException e){
            
            logger.error("==========KAKAO Api Call Error : " + e.getStatusText() + "==========");
            
            if(e.getStatusCode() == HttpStatus.CONFLICT){
                logger.error("==========KAKAO Api Call Error : To Many Request Error==========");
                e.printStackTrace();
                returnMap.put("errCd", "3");
            }else if(e.getStatusCode() == HttpStatus.FORBIDDEN){
                logger.error("==========KAKAO Api Call Error : Block OR Fail Error==========");
                e.printStackTrace();
                returnMap.put("errCd", "3");
            }else if(e.getStatusCode() == HttpStatus.UNAUTHORIZED){
                logger.error("==========KAKAO Api Call Error : Not Authentication Error==========");
                e.printStackTrace();
                returnMap.put("errCd", "3");
            }else if(e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
                logger.error("==========KAKAO Api Call Error : Internal Server Error==========");
                e.printStackTrace();
                returnMap.put("errCd", "3");
            }else{
                e.printStackTrace();
                returnMap.put("errCd", "3");
            }
            
            return returnMap;
        }
        
        /**
         *  100 : User Error
			200 : OK
			300 : Parameter Error
			301 : failed_type 이 "N" 이 아닌 경우에 실	패SMS 제목과 내용이 없으면 에러
			400 : Etc Error
			500 : 발신번호 사전 등록제에 의한 미등록차단 ( 3.4 발신번호 인증/등록 참조)
			600 : 충전요금 부족
			700 : 템플릿코드 사전 승인제에 의한 미승인차단
			800 : 템플릿코드에러
			900 : 프로파일이 존재하지 않음
         */
        
        resultCd =  (String) responseMap.get("result_code");
        /*
        if("200".equals(resultCd)){
            //로직필요...
        }else if("300".equals(resultCd)){
            returnMap.put("errCd", "1");
        }else{
            returnMap.put("errCd", "2");
        }
        */
        
        returnMap.put("errCd", "1");
        returnMap.put("result", responseMap);
        
        return returnMap;

		
	}		
	
//	/**
//	 *
//	 * Iamport 본인인증 후 인증값 셋팅
//	 * @param  HttpServletRequest
//	 * @param  HttpServletResponse
//	 * @param  imp_uid 본인인증 후 고유 imp_uid
//	 * @return DochaUserInfoDto
//	 * @throws JsonGenerationException
//	 * @throws JsonMappingException
//	 * @throws IOException
//	 */
	public DochaUserInfoDto setCertifications(HttpServletRequest request, HttpServletResponse response, String imp_uid, String impkey, String impScret, String impGetTokenUrl, String type) {
		
		DochaUserInfoDto paramDto = new DochaUserInfoDto();
		
		
		Util _util = new Util();
		
		String requestURL 	= "";
		String _token = "";
		//기업의 토큰값을 가져온다.
		try {
			
			impkey    = URLEncoder.encode(impkey, "UTF-8");
			impScret = URLEncoder.encode(impScret, "UTF-8");
			requestURL = impGetTokenUrl;
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JSONObject json = new JSONObject();
		json.put("imp_key", impkey);
		json.put("imp_secret", impScret);

		try {
			_token = getToken(request, response, json, requestURL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		String responseJson  = getJSON("https://api.iamport.kr/certifications/" + imp_uid +"?_token=" + _token, 0);
		HashMap parentMap = (HashMap) JsonUtil.convertJsonStrToClass(responseJson, HashMap.class); 
		
		
		//response json객체를 hsshmap에 매핑
		HashMap UserInfoMap =(HashMap)parentMap.get("response");
		

		
		//DC_USER_INFO 테이블 PK 생성
		
		if(type.equals("signup")) {
			String urIdx = KeyMaker.getInsetance().getKeyAddRandomDigit("UR", 3);
			paramDto.setUrIdx(urIdx);			
		}

		
		//이름
		String name = _util.unicodeConvert((String) UserInfoMap.get("name")); 	
		paramDto.setUserName(name);

		
		System.out.println("userName :" + name);
		//생일
		String birth = UserInfoMap.get("birth").toString();
		paramDto.setUserBirthday(_util.getTimestampToDate(birth));
		
		//성별
		String gender =  UserInfoMap.get("gender").toString();
		
		if(gender.equals("male")) {
			paramDto.setUserGender("M");
		}
		else if(gender.equals("female")) {
			paramDto.setUserGender("F");
		}
		
		String contract1 = UserInfoMap.get("phone").toString();
		paramDto.setUserContact1(StringUtil.removeMinusChar(StringUtil.removeWhitespace(contract1)));
		
		paramDto.setUserNationalCode("KO");
		paramDto.setUserRole("RU");
		paramDto.setSocialLoginPath("web");
		paramDto.setJoinChannel("web");
		paramDto.setUserCi((String)UserInfoMap.get("unique_in_site"));
		paramDto.setUserIdentityAuthYn("Y");
		
		//imp Log save end
		
		return paramDto;
	}

//	/**
//	 *
//	 * sendAlramTalk
//	 * @param  HttpServletRequest
//	 * @param  HttpServletResponse
//	 * @param  imp_uid 본인인증 후 고유 imp_uid
//	 * @return DochaUserInfoDto
//	 * @throws JsonGenerationException
//	 * @throws JsonMappingException
//	 * @throws IOException
//	 */
	public HttpResponse<JsonNode> sendAlramTalk(DochaAlarmTalkDto dto)  {
		logger.info("sendAlramTalk Start ===============================================");
		
		
		logger.info("PHONE=" + dto.getPhone());
		logger.info("CALLBACK=" + dto.getPhone());
		logger.info("REQDATE=" + dto.getRentDate());
		logger.info("MSG=" + dto.getMsg());
		logger.info("TEMPLATE_CODE=" + dto.getTemplateCode());
		logger.info("failed_type=" + dto.getFailedType());
		
		logger.info("failed_subject=" + dto.getFailedSubject());
		logger.info("failed_msg=" + dto.getMsg());
		logger.info("BTN_TYPES=" + "웹링크");
		logger.info("BTN_TXTS=" + "견적보기");
		logger.info("BTN_URLS1=" + dto.getBtnUrls1());

		return  Unirest.post ("http://api.apistore.co.kr/kko/1/msg/Docha")
		.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
		.header("x-waple-authorization", "MTE5NDktMTU3NDM5MzQyMDQzNC1lYThlMTJjYi1mZWIwLTQxZDQtOGUxMi1jYmZlYjBjMWQ0MmM=")
		.field("PHONE", dto.getPhone())
		.field("CALLBACK", kakao_callback_number)
		.field("REQDATE", "")
		.field("MSG", dto.getMsg())
		.field("TEMPLATE_CODE", dto.getTemplateCode())
		.field("failed_type", dto.getFailedType())
		.field("failed_subject", dto.getFailedMsg())
		.field("failed_msg", dto.getMsg())
		.field("BTN_TYPES", dto.getBtnTypes())
		.field("BTN_TXTS", dto.getBtnTxts())
		.field("BTN_URLS1", dto.getBtnUrls1())
		.asJson();

	}
}
