package com.itwillbs.goodbuy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.goodbuy.mapper.SupportMapper;
import com.itwillbs.goodbuy.vo.SupportVO;

@Service
public class SupportService {
	@Autowired SupportMapper mapper;
	
	//문의사항 리스트 조회
	public List<SupportVO> getSupporList(String id) {
		return mapper.selectSupportList(id);
	}

	public SupportVO getSupportDetail(int support_id) {
		return mapper.selectSupportDetail(support_id);
	}
}
