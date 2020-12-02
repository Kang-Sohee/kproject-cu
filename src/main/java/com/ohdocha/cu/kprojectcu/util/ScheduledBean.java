package com.ohdocha.cu.kprojectcu.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ohdocha.cu.kprojectcu.controller.DochaCarSearchController;
import com.ohdocha.cu.kprojectcu.domain.DochaAlarmTalkDto;
import com.ohdocha.cu.kprojectcu.domain.DochaScheduledDto;
import com.ohdocha.cu.kprojectcu.mapper.DochaScheduledDao;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ScheduledBean {
	
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(DochaCarSearchController.class);
	
	@Autowired
    private final DochaAlarmTalkMsgUtil alarmTalk;
	
	@Autowired
    private final DochaScheduledDao dao;
	
	//@Scheduled(cron = "0 0 9 * * *") // 매일 9시 스케쥴 실행
	//@Scheduled(fixedDelay = 1000) // 테스트용 1초단위 실행
    public void sendPaymentScheduledAlarmTalk() {
		
		logger.info("Start Send Payment AlarmTalk.");
		
		DochaMap map = new DochaMap();
		
		//인터벌 숫자만큼 쿼리에서 당일날짜만큼 추가 혹은 차감된 날짜의 스케쥴을 불러옴 EX) 0 : 당일, 1 : 내일, -1 : 어제 -> 입력하는 숫자만큼 변동됨
		map.put("interval", 0);
		
		//결제스케쥴의 원하는 날짜의 스케쥴 목록을 가져옴
		List<DochaScheduledDto> sendList = dao.selectScheduledList(map);
		//발송실패목록
		ArrayList<DochaScheduledDto> failList = new ArrayList<DochaScheduledDto>();
		
		sendList.stream().forEach(action -> {
			try {
				//알림톡발송메서드에 불러온 스케쥴 데이터를 넣음
				//sendAlarmTark(action);
			} catch (Exception e) {
				//전송실패시 실패리스트에 저장
				failList.add(action);
			}
		});
		
		//이하 문자발송 후 처리 로직 추가필요
		
    }
	
	private void sendAlarmTark(DochaScheduledDto messageDto) throws Exception {
		
		try {
        
			//DochaScheduledDto 데이터를 가지고 알림톡 전송처리, 알림톡 전송내용 및 템플맃 미정으로, 템플릿 등록되면 발송내용 추가 처리 필요
            DochaAlarmTalkDto dto = new DochaAlarmTalkDto();
         
            dto.setTemplateCode(DochaTemplateCodeProvider.A000001.getCode());

            //알림 톡 발송 후 로깅
            HttpResponse<JsonNode> response = alarmTalk.sendAlramTalk(dto);
            if (response.getStatus() == 200) {
                logger.info("AlarmTalk Send Compelite");
                logger.info(response.getBody().toPrettyString());
            } else {
                logger.info("AlarmTalk Send Fail");
                logger.error(response.getBody().toPrettyString());
                
                throw new Exception();
            }
        } catch (Exception ex) {
            //알림톡 발송을 실패하더라도 오류발생시키지 않고 결제처리 완료를 위해 오류는 catch에서 로깅처리만 함
            logger.error("Error", ex);
            throw new Exception();
        }
		
	}

}
