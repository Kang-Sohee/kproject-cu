package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaUserActionDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;

import java.util.List;

/**
 * <pre>
 * Statements
 * </pre>
 *
 * @version 1.0
 * @ClassName : DochaServiceNameService.java
 * @Description : 서비스 인터페이스.
 * @Modification Information
 * @see
 * @since 2019. 11. 13.
 */


public interface DochaUserInfoService {

    //void insertUserInfo(DochaUserInfoDto dto) throws Exception;
    DochaUserInfoDto selectUserInfo(DochaUserInfoDto paramDto);

    DochaUserInfoDto selectMypageUserInfo(DochaUserInfoDto paramDto);

    int insertUserInfo(DochaUserInfoDto paramDto);

    int updateUserInfo(DochaUserInfoDto paramDto);

    int deleteUserInfo(DochaUserInfoDto paramDto);

    int selectUserInfoCnt(DochaUserInfoDto paramDto);

    int insertUserActionData(DochaUserActionDto paramDto);

    int insertUserLicense(DochaUserInfoDto paramDto);

    int updateUserLicense(DochaUserInfoDto paramDto);

    int insertUserCard(DochaUserInfoDto paramDto);

    List<DochaUserInfoDto> selectCardInfo(DochaUserInfoDto paramDto);

    int deleteCardInfo(DochaUserInfoDto paramDto);

    int selectLicenseCnt(DochaUserInfoDto paramDto);

    DochaUserInfoDto selectLicenseInfo(DochaUserInfoDto paramDto);
}
