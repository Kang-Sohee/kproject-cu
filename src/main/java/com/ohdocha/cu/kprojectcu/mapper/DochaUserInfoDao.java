package com.ohdocha.cu.kprojectcu.mapper;

import com.ohdocha.cu.kprojectcu.domain.DochaUserActionDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface DochaUserInfoDao {

    public DochaUserInfoDto selectUserInfo(DochaUserInfoDto paramDto);

    public DochaUserInfoDto selectMypageUserInfo(DochaUserInfoDto paramDto);

    public int insertUserInfo(DochaUserInfoDto paramDto);

    public int updateUserInfo(DochaUserInfoDto paramDto);

    public int deleteUserInfo(DochaUserInfoDto paramDto);

    public int selectUserInfoCnt(DochaUserInfoDto paramDto);

    public int insertUserActionData(DochaUserActionDto paramDto);

    public List<DochaUserInfoDto> selectOperatorList();

    public int insertUserLicense(DochaUserInfoDto paramDto);

    public int updateUserLicense(DochaUserInfoDto parmaDto);

    public int insertUserCard(DochaUserInfoDto paramDto);

    public List<DochaUserInfoDto> selectCardInfo(DochaUserInfoDto paramDto);

    public int deleteCardInfo(DochaUserInfoDto paramDto);

    public int selectLicenseCnt(DochaUserInfoDto paramDto);

    public DochaUserInfoDto selectLicenseInfo(DochaUserInfoDto paramDto);
}
