package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaUserActionDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import com.ohdocha.cu.kprojectcu.mapper.DochaUserInfoDao;
import com.ohdocha.cu.kprojectcu.util.KeyMaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userInfo")
@Slf4j
@AllArgsConstructor
@Transactional
public class DochaUserInfoServiceImpl implements DochaUserInfoService {

    @Autowired
    private final DochaUserInfoDao dao;

    @Override
    public DochaUserInfoDto selectUserInfo(DochaUserInfoDto paramDto) {

        return dao.selectUserInfo(paramDto);
    }

    @Override
    public int updateUserInfo(DochaUserInfoDto paramDto) {
        int queryResult = 0;

        queryResult = dao.updateUserInfo(paramDto);

        return queryResult;
    }

    @Override
    public int insertUserInfo(DochaUserInfoDto paramDto) {
        // paramDto.modId = "";
        return dao.insertUserInfo(paramDto);
    }

    @Override
    public int deleteUserInfo(DochaUserInfoDto paramDto) {
        // TODO Auto-generated method stub
        return dao.deleteUserInfo(paramDto);
    }

    @Override
    public int selectUserInfoCnt(DochaUserInfoDto paramDto) {
        // TODO Auto-generated method stub

        return dao.selectUserInfoCnt(paramDto);
    }

    @Override
    public int insertUserActionData(DochaUserActionDto paramDto) {
        int resultCnt = 0;
        resultCnt += dao.insertUserActionData(paramDto);
        return resultCnt;
    }

    @Override
    public int insertUserLicense(DochaUserInfoDto paramDto) {
        //DochaUserInfoDto dto = new DochaUserInfoDto();

        int queryResult = 0;

        if (paramDto.getUlIdx() == null) {

            String ulIdx = KeyMaker.getInsetance().getKeyDeafult("UL");
            paramDto.setUlIdx(ulIdx);

            queryResult = dao.insertUserLicense(paramDto);
        } else {
            System.out.println("========== else ==========");
        }

        return queryResult;
    }

    @Override
    public int updateUserLicense(DochaUserInfoDto paramDto) {
        int queryResult = 0;

        queryResult = dao.updateUserLicense(paramDto);

        return queryResult;
    }

    @Override
    public int insertUserCard(DochaUserInfoDto paramDto) {
        //DochaUserInfoDto dto = new DochaUserInfoDto();

        int queryResult = 0;

        if (paramDto.getPmIdx() == null) {
/*
            String pmIdx = KeyMaker.getInsetance().getKeyDeafult("UC");
            paramDto.setPmIdx(pmIdx);
*/

            queryResult = dao.insertUserCard(paramDto);
        } else {
            System.out.println("========== else ==========");
        }

        return queryResult;
    }

    @Override
    public List<DochaUserInfoDto> selectCardInfo(DochaUserInfoDto paramDto) {

        return dao.selectCardInfo(paramDto);
    }

    public int deleteCardInfo(DochaUserInfoDto paramDto) {

        return dao.deleteCardInfo(paramDto);
    }

    @Override
    public int selectLicenseCnt(DochaUserInfoDto paramDto) {

        return dao.selectLicenseCnt(paramDto);
    }

    @Override
    public DochaUserInfoDto selectLicenseInfo(DochaUserInfoDto paramDto) {
        return dao.selectLicenseInfo(paramDto);
    }
}
