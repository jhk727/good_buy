package com.itwillbs.goodbuy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.goodbuy.mapper.MyReviewMapper;
import com.itwillbs.goodbuy.vo.MyReviewVO;
import com.itwillbs.goodbuy.vo.ProductVO;

@Service
public class MyReviewService {
	@Autowired MyReviewMapper mapper;

	public List<MyReviewVO> getReview(String id) {
		return mapper.selectReview(id);
	}
	//리뷰 갯수 조회
	public int myReviewCount(String id) {
		return mapper.selectReviewCount(id);
	}
	
	//리뷰 등록 저장
	public int saveReviewData(String id, String review, String productTitle, String productId) {
		return mapper.insertReview(id,review,productTitle,productId);
	}
	//리뷰 갯수확인(후기버튼 비활성화)
	public int reviewCountCheck(int product_id) {
		return mapper.selectReviewCountCheck(product_id);
	}
	
	
	
}
