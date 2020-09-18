package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaLoginLogDto;

import javax.servlet.http.HttpServletRequest;

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


public interface DochaLoginLogService {

    //로그인 로그 등록
    public int insertLoginLog(DochaLoginLogDto LoginlogDto);

    /**
     * 로그인 로그를 등록한다.
     *
     * @param loginType LOGIN 채널
     * @param LoginYn   LOGIN 여부
     * @param userId    LOGIN 시도  USER
     * @param request   IP 확인을 위한 REQUEST
     * @return LOG INSERT 여부
     */
    public int saveLoginLog(String loginType, String LoginYn, String userId, HttpServletRequest request);
}
