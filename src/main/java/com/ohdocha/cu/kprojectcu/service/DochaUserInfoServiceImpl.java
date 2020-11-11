package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaUserActionDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import com.ohdocha.cu.kprojectcu.mapper.DochaUserInfoDao;
import com.ohdocha.cu.kprojectcu.util.KeyMaker;
import com.ohdocha.cu.kprojectcu.util.PasswordEncoding;
import com.ohdocha.cu.kprojectcu.util.SHAPasswordEncoder;
import com.ohdocha.cu.kprojectcu.util.StringUtil;
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
        // TODO Auto-generated method stub

        int returnInt = 1;

        DochaUserInfoDto dto = new DochaUserInfoDto();

        if (paramDto != null) {
            dto.setUrIdx(paramDto.getUrIdx().trim());
            //아이디
            if (!StringUtil.isEmpty(paramDto.getUserId())) {
                dto.setUserId(paramDto.getUserId());
            }

            if (!StringUtil.isEmpty(paramDto.getUserPassword())) {
                //비밀번호 암호화 시작---------------------------------------------------------
                SHAPasswordEncoder shaPasswordEncoder = new SHAPasswordEncoder(512);
                shaPasswordEncoder.setEncodeHashAsBase64(true);
                PasswordEncoding passwordEncoding = new PasswordEncoding(shaPasswordEncoder);

                dto.setUserPassword(passwordEncoding.encode(paramDto.getUserPassword()));
            }

            //연락처
            if (!StringUtil.isEmpty(paramDto.getUserContact1())) {
                dto.setUserContact1(paramDto.getUserContact1());
            }

            //우편번호
            if (!StringUtil.isEmpty(paramDto.getUserZipCode())) {
                dto.setUserContact1(paramDto.getUserZipCode());
            }

            //주소
            if (!StringUtil.isEmpty(paramDto.getUserAddress())) {
                dto.setUserContact1(paramDto.getUserAddress());
            }

            //주소상세
            if (!StringUtil.isEmpty(paramDto.getUserAddressDetail())) {
                dto.setUserContact1(paramDto.getUserAddressDetail());
            }

            //userCi
            if (!StringUtil.isEmpty(paramDto.getUserCi())) {
                dto.setUserCi(paramDto.getUserCi());
            }

            returnInt = dao.updateUserInfo(dto);

            //사용자정보 업데이트 오류시
            if (returnInt < 1) {
                returnInt = 5;
                return returnInt;
            }
        }

        return returnInt;
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

    @Override
    public int selectLicenseCnt(DochaUserInfoDto paramDto) {

        return dao.selectLicenseCnt(paramDto);
    }

    @Override
    public DochaUserInfoDto selectLicenseInfo(DochaUserInfoDto paramDto) {
        return dao.selectLicenseInfo(paramDto);
    }
}
