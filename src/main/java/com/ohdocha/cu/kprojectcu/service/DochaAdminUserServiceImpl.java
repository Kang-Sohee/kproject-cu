package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaAdminUserResponse;
import com.ohdocha.cu.kprojectcu.mapper.DochaAdminUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("AdminUserService")
@Transactional
public class DochaAdminUserServiceImpl implements DochaAdminUserService {

    private DochaAdminUserDao dao;

    //운영직원 리스트 조회(알림톡 발송용)
    @Override
    public List<DochaAdminUserResponse> selectAdminUserList() {
        // TODO Auto-generated method stub
        return dao.selectAdminUserList();
    }
}
