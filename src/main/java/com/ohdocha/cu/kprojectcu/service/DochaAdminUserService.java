package com.ohdocha.cu.kprojectcu.service;

import com.ohdocha.cu.kprojectcu.domain.DochaAdminUserResponse;

import java.util.List;

public interface DochaAdminUserService {

	//운영직원 리스트 조회(알림톡 발송용)
	public List<DochaAdminUserResponse> selectAdminUserList();
}
