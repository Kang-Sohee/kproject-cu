package com.ohdocha.cu.kprojectcu.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class KeyMaker {
	
	private static KeyMaker kerMaker = new KeyMaker();
	
	public static KeyMaker getInsetance() {
		
		if(kerMaker == null) {
			kerMaker = new KeyMaker();
		}
		
		return kerMaker;
		
	}
	
	/**
	 * 
	 * 테이블 idx값을 출력
	 * XX + yyyyMMddHHmmsss 의 idx값을 생성하여 반환합니다
	 * EX) LG201911211457059
	 * 
	 * @param header Key 앞의 문자
	 * @return 17자리 KEY값
	 */
	public String getKeyDeafult(String header) {
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsss");
		
		StringBuffer key = new StringBuffer();
		key.append(header);
		key.append(sdf.format(cal.getTime()));
		
		return key.toString();
		
	}
	
	/**
	 * 테이블 idx값을 출력
	 * XX + yyyyMMddHHmmsss + random 숫자의 idx값을 생성하여 반환합니다
	 * EX) LG20191121145705903905
	 * 
	 * @param header Key 앞의 문자
	 * @param digit 랜덤숫자의 자리수
	 * @return 17 + digit자리 KEY값
	 */
	public String getKeyAddRandomDigit(String header, int digit) {
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsss");
		Random rand = new Random();
		
		int digitResult = (int) Math.pow(10, digit);
						
		StringBuffer randomStr = new StringBuffer();
		
		for(int i=0; i<digit; i++) {
			randomStr.append(Integer.toString(rand.nextInt(10)));
		}
		
		String digitString = randomStr.toString();
		
		/*
		if(digitString.length() < digit) {
			
			for (int i=0; i<digit - digitString.length(); i++) {
				sb.append("0");
			}
			
		}*/
		
		StringBuffer key = new StringBuffer();
		key.append(header);
		key.append(sdf.format(cal.getTime()));
		key.append(digitString);
		
		return key.toString();
		
	}
	
	/**
	 * 테이블 idx값을 출력
	 * XX + yyyyMMddHHmm + random 숫자의 idx값을 생성하여 반환합니다
	 * EX) LG20191121145705903905
	 * 
	 * @param header Key 앞의 문자
	 * @param digit 랜덤숫자의 자리수
	 * @return 14 + digit +3 자리 KEY값
	 */
	public String getKeyAddRandomDigitForLoof(String header, int digit) {
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Random rand = new Random();
		
		StringBuffer randomStr = new StringBuffer();
		
		for(int i=0; i<digit + 3; i++) {
			randomStr.append(Integer.toString(rand.nextInt(10)));
		}
		
		String digitString = randomStr.toString();
		
		/*
		if(digitString.length() < digit) {
			
			for (int i=0; i<digit - digitString.length(); i++) {
				sb.append("0");
			}
			
		}*/
		
		StringBuffer key = new StringBuffer();
		key.append(header);
		key.append(sdf.format(cal.getTime()));
		key.append(digitString);
		
		return key.toString();
		
	}
}
