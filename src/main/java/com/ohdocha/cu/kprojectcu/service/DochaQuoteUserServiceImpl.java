package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaPaymentReserveDto;
import com.ohdocha.cu.kprojectcu.domain.DochaQuoteUserDto;
import com.ohdocha.cu.kprojectcu.mapper.DochaQuoteUserDao;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import com.ohdocha.cu.kprojectcu.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("QuoteUser")
@Transactional
@Slf4j
@AllArgsConstructor
public class DochaQuoteUserServiceImpl implements DochaQuoteUserService{

	@Autowired
	private DochaQuoteUserDao dao;
	
	private final static Logger logger = LoggerFactory.getLogger(DochaQuoteUserServiceImpl.class);
	
	//견적요청(user/insertEstimate.do)
	@Override
	public int insertQuoteUser(DochaQuoteUserDto paramDto) {
		// TODO Auto-generated method stub
		return dao.insertQuoteUser(paramDto);
	}
	
	//견적요청리스트(user/estimate.do)
	@Override
	public List<DochaQuoteUserDto> selectUserQuoteList(DochaMap paramMap) {
		return dao.selectUserQuoteList(paramMap);
	}
	
	//견적요청 상세(user/userEstDetail.json)
	@Override
	public DochaQuoteUserDto selectUserQuoteInfo(DochaMap paramMap) {
		return dao.selectUserQuoteInfo(paramMap);
	}
	
	//결제완료리스트(user/userPaymentList.json)
	@Override
	public List<DochaQuoteUserDto> selectUserQuotePaymentList(DochaMap paramMap) {
		// TODO Auto-generated method stub
		return dao.selectUserQuotePaymentList(paramMap);
	}

	@Override
	public DochaQuoteUserDto selectRentCompanyQuoteUser(DochaMap paramMap) {
		// TODO Auto-generated method stub
		return dao.selectRentCompanyQuoteUser(paramMap);
	}

	@Override
	public int updateQuoteUser(DochaMap paramMap) {
		// TODO Auto-generated method stub
		return dao.updateQuoteUser(paramMap);
	}

	//견적요청 상세(user/userEstDetail.do)
	@Override
	public DochaPaymentReserveDto selectUserQuoteInfoUsingPayment(DochaMap paramMap) {
		// TODO Auto-generated method stub
		return dao.selectUserQuoteInfoUsingPayment(paramMap);
	}

	//user/estimate.do?tab=0 of 견적요청 List(user/getUserEstDetail.json)
	@Override
	public List<DochaPaymentReserveDto> selectQuoteUserDetail(DochaMap paramMap) {
		// TODO Auto-generated method stub
		return dao.selectQuoteUserDetail(paramMap);
	}
	
	@Override
	public int updateCancelQuoteUser(DochaMap paramMap) {

		logger.info("==================== CANCEL USER QUOTE START ====================");
		
		int result = -1;
		
		try {
			
			DochaMap selectParam = new DochaMap();
			selectParam.put("quIdx", paramMap.getString("quIdx"));
			
			DochaQuoteUserDto userQuoteInfo = dao.selectUserQuoteInfo(selectParam);
			
			//견적없음
			if(StringUtil.isEmpty(userQuoteInfo)) {
				result = 2;
				return result; 
			}
			
			//요청자와 견적등록자 일치여부 확인
			if(!paramMap.getString("urIdx").equals(userQuoteInfo.getUrIdx())) {
				result = 3;
				return result; 
			}
			
			//기취소
			if("QB".equals(userQuoteInfo.getQuoteStatus())) {
				result = 4;
				return result;
			}
			
			int updateResult = 0;
			
			updateResult = dao.updateQuoteUser(paramMap);
			
			if(updateResult > 0) {
				result = 1;
			}else{
				result = 5;
			}
			
		}catch(Exception e) {
			logger.info("==================== CANCEL USER QUOTE ERROR ====================");
			logger.error("==================== ERROR", e);
			
			result = -1;
		}
		
		logger.info("==================== CANCEL USER QUOTE END ====================");
		
		return result;
	}
	
}
