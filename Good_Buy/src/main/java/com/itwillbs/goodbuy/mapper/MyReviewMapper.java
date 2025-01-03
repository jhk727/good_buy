package com.itwillbs.goodbuy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.goodbuy.vo.MyReviewVO;
import com.itwillbs.goodbuy.vo.ProductVO;

@Mapper
public interface MyReviewMapper {
	//리뷰 리스트 조회 
	List<MyReviewVO> selectReview(String id);
	//리뷰 갯수조회
	int selectReviewCount(String id);
	//리뷰저장
	int insertReview(@Param("mem_id") String id
			, @Param("review_content") String review
			, @Param("product_title") String productTitle
			, @Param("product_id") String productId);
	
	int selectReviewCountCheck(int product_id);
	
	//내가 쓴 리뷰
	List<MyReviewVO> selectReviewHistory(String id);
	//내가쓴 리뷰 수정
	int updateReview(
			@Param("review_content") String reviewContent
			,@Param("product_id") String productId);
	//리뷰삭제
	int deleteReview(int reviewId);

}
