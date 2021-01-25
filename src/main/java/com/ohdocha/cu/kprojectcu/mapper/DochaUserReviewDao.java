package com.ohdocha.cu.kprojectcu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import com.ohdocha.cu.kprojectcu.domain.DochaUserReviewDto;
import com.ohdocha.cu.kprojectcu.domain.DochaUserReviewFileDto;

@Mapper
@Component
public interface DochaUserReviewDao {
	
	public int insertUserReview(DochaUserReviewDto param);
	public int insertUserReviewFile(DochaUserReviewFileDto param);
	  
}
