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


public interface DochaUserInfoService {

    //void insertUserInfo(DochaUserInfoDto dto) throws Exception;
    public DochaUserInfoDto selectUserInfo(DochaUserInfoDto paramDto);

    public DochaUserInfoDto selectMypageUserInfo(DochaUserInfoDto paramDto);

    public int insertUserInfo(DochaUserInfoDto paramDto);

    public int updateUserInfo(DochaUserInfoDto paramDto);

    public int deleteUserInfo(DochaUserInfoDto paramDto);

    public int selectUserInfoCnt(DochaUserInfoDto paramDto);

    public int insertUserActionData(DochaUserActionDto paramDto);

    public int insertUserLicense(DochaUserInfoDto paramDto);

    public int updateUserLicense(DochaUserInfoDto paramDto);

    public int insertUserCard(DochaUserInfoDto paramDto);

    public List<DochaUserInfoDto> selectCardInfo(DochaUserInfoDto paramDto);

    public int deleteCardInfo(DochaUserInfoDto paramDto);

    public int selectLicenseCnt(DochaUserInfoDto paramDto);

    public DochaUserInfoDto selectLicenseInfo(DochaUserInfoDto paramDto);
}
