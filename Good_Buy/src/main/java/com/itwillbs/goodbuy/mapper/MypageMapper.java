package com.itwillbs.goodbuy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.goodbuy.vo.MemberVO;
import com.itwillbs.goodbuy.vo.WishlistVO;

@Mapper
public interface MypageMapper {
	
	//위시리스트 조회
	List<WishlistVO> selectWishlist(@Param("id") String id);

	//위시리스트 삭제
	int deleteWish(int wishlist_id);
	
	//위시리스트 추가
	int insertWishlist(WishlistVO wishlist);
	//위시리스트 갯수세
	int selectWishlistCount(String id);
	

}
