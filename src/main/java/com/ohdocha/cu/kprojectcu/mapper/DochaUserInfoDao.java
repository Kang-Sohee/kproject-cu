package com.ohdocha.cu.kprojectcu.mapper;

import com.ohdocha.cu.kprojectcu.domain.DochaUserActionDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserInfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DochaUserInfoDao {
	
	public DochaUserInfoDto selectUserInfo(DochaUserInfoDto paramDto);
	
	public int insertUserInfo(DochaUserInfoDto paramDto);
	
	public int updateUserInfo(DochaUserInfoDto paramDto);

	public int deleteUserInfo(DochaUserInfoDto paramDto);
	
	
	public int selectUserInfoCnt(DochaUserInfoDto paramDto);
	public int insertUserActionData(DochaUserActionDto paramDto);
	public List<DochaUserInfoDto> selectOperatorList();

}
