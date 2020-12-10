package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaImpLogDto;

/**
 * <pre>
 * Statements
 * </pre>
 *
 * @ClassName   : DochaServiceNameService.java
 * @Description : 서비스 인터페이스.
 * @since 2019. 11. 13.
 * @version 1.0
 * @see
 * @Modification Information
 */


public interface DochaImpLogService {
	public int insertImpLog(DochaImpLogDto paramDto);
	
	public DochaImpLogDto selectImpLogData(DochaImpLogDto paramDto);
}
