package com.itwillbs.goodbuy.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.goodbuy.mapper.ProductMapper;

import com.itwillbs.goodbuy.vo.ProductVO;

@Service
public class ProductService {
	@Autowired
	private ProductMapper mapper;

	public int registProduct(ProductVO product, String id) {
		return mapper.insertProduct(product, id);
	}
		
	//판매 상품목록 조회
	public List<ProductVO> getProductList(String id) {
		return mapper.selectProductList(id);
	}
	
	//판매 목록 갯수 조회
	public int salesCount(String id) {
		return mapper.salesListCount(id);
	}

	public List<Map<String, Object>> searchProductList(String pRODUCT_CATEGORY) {
		return mapper.searchProductList(pRODUCT_CATEGORY);
	}
		
	public List<ProductVO> getOrderList(String id) {
		return mapper.selectOrderList(id);
	}

	//구매 목록 갯수 조회
	public int orderListCount(String id) {
		return mapper.selectOrderCount(id);
	}

	public List<Map<String, Object>> searchFliterList(int product_price, String product_category) {
		return mapper.searchFliterList(product_price, product_category);
	}

}
