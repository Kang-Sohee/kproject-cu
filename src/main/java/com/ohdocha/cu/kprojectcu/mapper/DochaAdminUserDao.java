package com.ohdocha.cu.kprojectcu.mapper;

import com.ohdocha.cu.kprojectcu.domain.DochaAdminUserResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DochaAdminUserDao {

	//운영직원 리스트 조회(알림톡 발송용)
	public List<DochaAdminUserResponse> selectAdminUserList();

}
