package com.ohdocha.cu.kprojectcu.service;



import com.ohdocha.cu.kprojectcu.domain.DochaCommonUtilDto;
import com.ohdocha.cu.kprojectcu.util.DochaMap;

import java.util.List;

/**
 * <pre>
 * Statements
 * </pre>
 *
 * @ClassName   : CarssumDashboardService.java
 * @Description : 서비스 인터페이스.
 * @author pws
 * @since 2019. 11. 13.
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 *     since          author              description
 *  ===========    =============    ===========================
 *  2019. 11. 13.     pws         	최초 생성
 * </pre>
 */


public interface DochaCommonUtilService {

	List<DochaCommonUtilDto> selectCommonCodeList(DochaMap param) throws Exception;

	List<DochaCommonUtilDto> selectCodeList(DochaCommonUtilDto dto) throws Exception;
	
}

