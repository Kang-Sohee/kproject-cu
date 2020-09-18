package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaUserActionDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;

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


public interface DochaUserInfoService {
	
	//void insertUserInfo(DochaUserInfoDto dto) throws Exception;
	public DochaUserInfoDto selectUserInfo(DochaUserInfoDto paramDto);
	 
	public int insertUserInfo(DochaUserInfoDto paramDto);
	
	public int updateUserInfo(DochaUserInfoDto paramDto);

	public int deleteUserInfo(DochaUserInfoDto paramDto);
	
	public int selectUserInfoCnt(DochaUserInfoDto paramDto);
	public int insertUserActionData(DochaUserActionDto paramDto);
}
