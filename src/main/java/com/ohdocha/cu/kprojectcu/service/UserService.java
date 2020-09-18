package com.ohdocha.cu.kprojectcu.service;


import com.ohdocha.cu.kprojectcu.domain.DochaUserActionDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import com.ohdocha.cu.kprojectcu.util.ServiceMessage;

public interface UserService {

    //void insertUserInfo(DochaUserInfoDto dto) throws Exception;
    public DochaUserInfoDto selectUserInfo(DochaUserInfoDto paramDto);

    public int insertUserInfo(DochaUserInfoDto paramDto);

    public int updateUserInfo(DochaUserInfoDto paramDto);

    public int deleteUserInfo(DochaUserInfoDto paramDto);

    public int selectUserInfoCnt(DochaUserInfoDto paramDto);
    public int insertUserActionData(DochaUserActionDto paramDto);

}
