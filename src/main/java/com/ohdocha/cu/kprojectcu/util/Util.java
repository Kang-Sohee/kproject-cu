package com.ohdocha.cu.kprojectcu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class Util {

	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/*
	 * Unicode 한글 → 한글문자열 변환
	 * IamPort에서 Unicode형식으로 이름 데이터를 주기 때문에 변환 필요
	 * 
	 * */
	public static String unicodeConvert(String str) {
        StringBuilder sb = new StringBuilder();
        char ch;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            ch = str.charAt(i);
            if (ch == '\\' && str.charAt(i+1) == 'u') {
                sb.append((char) Integer.parseInt(str.substring(i+2, i+6), 16));
                i+=5;
                continue;
            }
            sb.append(ch);
        }
        return sb.toString();
    }
	
	/*
	 * Unix timestamp 를 Date String으로 변환하는 함수
	 * IamPort에서 생년월일을 Unix timestamp형식으로 준다.
	 * 
	 * */
	public static String getTimestampToDate(String timestampStr){
		long unixSeconds = Long.parseLong(timestampStr);
		Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); // the format of your date
		String formattedDate = sdf.format(date);
		
		return formattedDate;
	}

	
    public static String numberGen(int len, int dupCd ) {
        
        Random rand = new Random();
        String numStr = ""; //난수가 저장될 변수
        
        for(int i=0;i<len;i++) {
            
            //0~9 까지 난수 생성
            String ran = Integer.toString(rand.nextInt(10));
            
            if(dupCd==1) {
                //중복 허용시 numStr에 append
                numStr += ran;
            }else if(dupCd==2) {
                //중복을 허용하지 않을시 중복된 값이 있는지 검사한다
                if(!numStr.contains(ran)) {
                    //중복된 값이 없으면 numStr에 append
                    numStr += ran;
                }else {
                    //생성된 난수가 중복되면 루틴을 다시 실행한다
                    i-=1;
                }
            }
        }
        return numStr;
    }
    
	/*

	 * 공백 또는 null 체크

	 */

	public static boolean isEmpty(Object obj) {

		if(obj == null) return true;

		if ((obj instanceof String) && (((String)obj).trim().length() == 0)) { return true; }
	        if (obj instanceof Map) { return ((Map<?, ?>) obj).isEmpty(); }
	        if (obj instanceof Map) { return ((Map<?, ?>)obj).isEmpty(); } 
	        if (obj instanceof List) { return ((List<?>)obj).isEmpty(); }
	        if (obj instanceof Object[]) { return (((Object[])obj).length == 0); }

		return false;
	}
	
	
	 /**
     * 
     * 기능 설명 : IP취득 
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("WL-Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_CLIENT_IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getRemoteAddr(); 
        }
        return ip;
    }
    
	 /**
     * 
     * 기능 설명 : DTO를 OBJECT로 변환하여 로그로 출력합니다. 
//     * @param request
     * @return
     */
	public void getDtoLog(Object obj) {
	       Map<String, Object> param = new HashMap<>();
	        try {
	            Field[] fields = obj.getClass().getDeclaredFields();

	            for (int i = 0; i <= fields.length - 1; i++) {
	                fields[i].setAccessible(true);
	                param.put(fields[i].getName(), fields[i].get(obj));
	            }
	        }catch (IllegalAccessException e){
	            e.printStackTrace();
	        }
	        
	        logger.info("dto Log Start=================================");
	        logger.info(param.toString());
	        logger.info("dto Log END=================================");
	}//getLogger
	
	 /**
     * 
     * 기능 설명 : 생년월일 
//     * @param request
     * @return
     */	
	public int getAge(int birthYear, int birthMonth, int birthDay)
	{
		
		
		
		
	         Calendar current = Calendar.getInstance();
	         int currentYear  = current.get(Calendar.YEAR);
	         int currentMonth = current.get(Calendar.MONTH) + 1;
	         int currentDay   = current.get(Calendar.DAY_OF_MONTH);
	        
	         int age = currentYear - birthYear;
	         // 생일 안 지난 경우 -1
	         if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay)  
	             age--;
	        
	         return age;
	}
	
	/**
	* 만나이 계산
	* @param ssn : 주민등록번호 13자리 (예: "999999-1234567" or "9999991234567")
	* @return int 만나이
	*/
	public  String calculateManAge(String ssn){
	
		String today = ""; //오늘 날짜
		int manAge = 0; //만 나이
	
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	
		today = formatter.format(new Date()); //시스템 날짜를 가져와서 yyyyMMdd 형태로 변환
	
		//today yyyyMMdd
		int todayYear = Integer.parseInt( today.substring(0, 4) );
		int todayMonth = Integer.parseInt( today.substring(4, 6) );
		int todayDay = Integer.parseInt( today.substring(6, 8) );
	
		int ssnYear = Integer.parseInt( ssn.substring(0, 4) );
		int ssnMonth = Integer.parseInt( ssn.substring(4, 6) );
		int ssnDay = Integer.parseInt( ssn.substring(6, 8) );
	
		System.out.println(ssnYear);
		System.out.println(ssnMonth);
		System.out.println(ssnDay);
		
		if( ssn.charAt( 6 ) == '0' || ssn.charAt( 6 ) == '9' ){
			ssnYear += 1800;
		}else if( ssn.charAt( 6 ) == '1' || ssn.charAt( 6 ) == '2' ||
				ssn.charAt( 6 ) == '5' || ssn.charAt( 6 ) == '6' ){
			ssnYear += 1900;
		}else { //3, 4, 7, 8
			ssnYear += 2000;
		}
	
		manAge = todayYear - ssnYear;
	
		if( todayMonth < ssnMonth ){ //생년월일 "월"이 지났는지 체크
			manAge--;
		}else if( todayMonth == ssnMonth ){ //생년월일 "일"이 지났는지 체크
			if( todayDay < ssnDay ){
				manAge--; //생일 안지났으면 (만나이 - 1)
			}
		}
	
		return Integer.toString(manAge);
	}

	
	public int getAgeFromBirthDay(String birth)	{	
		
		
		if( birth == null || birth.equals("") || birth.isEmpty() ) {
			return 0;
		}
		
		int birthYear = Integer.parseInt(birth.substring(0, 4));
		int birthMonth = Integer.parseInt(birth.substring(4, 6));
		int birthDay = Integer.parseInt(birth.substring(6, 8));
		
		Calendar current = Calendar.getInstance();
		int currentYear  = current.get(Calendar.YEAR);
		int currentMonth = current.get(Calendar.MONTH) + 1;
		int currentDay   = current.get(Calendar.DAY_OF_MONTH);
		
		int age = currentYear - birthYear;
		// 생일 안 지난 경우 -1
		if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay)  
		    age--;
		
		return age;
	}


}
