package com.ohdocha.cu.kprojectcu.service;



import com.ohdocha.cu.kprojectcu.domain.DochaCommonUtilDto;
import com.ohdocha.cu.kprojectcu.util.DochaMap;

import java.util.List;

/**
 * <pre>
 * Statements
 * </pre>
 *
 * @ClassName   : DochaDashboardService.java
 * @Description : 서비스 인터페이스.
 * @since 2019. 11. 13.
 * @version 1.0
 * @see
 * @Modification Information
 */


public interface DochaCommonUtilService {

	List<DochaCommonUtilDto> selectCommonCodeList(DochaMap param) throws Exception;

	List<DochaCommonUtilDto> selectCodeList(DochaCommonUtilDto dto) throws Exception;
	
}

