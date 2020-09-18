package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaImpLogDto;

/**
 * <pre>
 * Statements
 * </pre>
 *
 * @ClassName   : DochaServiceNameService.java
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


public interface DochaImpLogService {
	public int insertImpLog(DochaImpLogDto paramDto);
	
	public DochaImpLogDto selectImpLogData(DochaImpLogDto paramDto);
}
