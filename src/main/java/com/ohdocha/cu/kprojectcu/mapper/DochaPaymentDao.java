package com.ohdocha.cu.kprojectcu.mapper;


import com.ohdocha.cu.kprojectcu.domain.*;
import com.ohdocha.cu.kprojectcu.util.DochaMap;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper
@Component
public interface DochaPaymentDao {
	
	public int insertReserveMaster(DochaPaymentDto paramMap);
	
	public int insertReserve(DochaPaymentReserveDto paramMap);
	
	public int insertPaymentDetail(DochaPaymentDetailDto paramMap);
	
	@Transactional(propagation =  Propagation.REQUIRES_NEW)
	public int insertPaymentLog(DochaPaymentLogDto paramMap);
	
	public int updatePaymentLog(DochaPaymentLogDto paramMap);
	
	public int updateReserveMaster(DochaMap paramMap);
	
	public int updateReserve(DochaMap paramMap);
	
	public List<DochaQuoteUserInfoDto> selectQuotePaymentSuccessList(DochaMap paramMap);
		
	public DochaQuoteUserInfoDto selectQuotePaymentSuccessDetail(DochaMap paramMap);
	
	public int updateCompliteQuoteRentCompany(DochaMap paramMap);
	
	public int updateComplitQuoteRentCompany(DochaMap paramMap);
	
	public int updateNotChoiseQuoteRentCompany(DochaMap paramMap);

	// 예약 정보
	public List<DochaPaymentDto> selectReserveInfo(DochaMap param);

	// 예약 정보 리스트
	public List<DochaPaymentDto> selectReserveInfoList(DochaMap param);

	// 즉시 취소
	public int updateCancelReserve(DochaMap paramMap);

	// 즉시 취소
	public int updateCancelScheduleReserve(DochaMap paramMap);

	// 취소 요청
	public int updateCancelRequest(DochaMap paramMap);



	//region [ Webhook 관련 쿼리 ]
	// 예약 스케쥴 정보 불러오기 ( merchant_uid로)
	public List<DochaScheduledDto> selectSchduleInfo(DochaMap param);

	// 다음 정보 불러오기 ( merchant_uid로)
	public List<DochaScheduledDto> selectNextSchduleInfo(DochaMap param);

	// 예약 정보 리스트 불러오기 ( merchant_uid로)
	public List<DochaPaymentDto> selectReserveInfoByMerchantUid(DochaMap param);

	// 스케쥴 테이블 상태 변경
	public void updateScheduleByMerchantUid(DochaMap paramMap);

	// 스케쥴 테이블 상태 변경
	public void updateReserveMasterByMerchantUid(DochaPaymentDto paymentDto);

	//endregion
}
