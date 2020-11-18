package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaUserActionDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;

import java.util.List;

/**
 * <pre>
 * Statements
 * </pre>
 *
 * @author pws
 * @version 1.0
 * @ClassName : DochaServiceNameService.java
 * @Description : 서비스 인터페이스.
 * @Modification Information
 * <pre>
 *     since          author              description
 *  ===========    =============    ===========================
 *  2019. 11. 13.     pws         	최초 생성
 * </pre>
 * @see
 * @since 2019. 11. 13.
 */


public interface DochaMenuService {

	 public List<?> getNoticeList();
}
