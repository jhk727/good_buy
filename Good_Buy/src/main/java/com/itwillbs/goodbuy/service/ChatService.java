package com.itwillbs.goodbuy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.goodbuy.mapper.ChatMapper;
import com.itwillbs.goodbuy.vo.ChatMessage;
import com.itwillbs.goodbuy.vo.ChatRoom;
import com.itwillbs.goodbuy.vo.MemberVO;

@Service
public class ChatService {
	@Autowired
	private ChatMapper mapper;
	
	//	기존 자신의 채팅방 목록 조회 요청
	public List<ChatRoom> selectChatRoomList(String sender_id) {
		return mapper.selectChatRoomList(sender_id);
	}
	
	//	해당 채팅방 제일 마지막 대화 조회
	public ChatMessage selectLastMessage(String room_id) {
		ChatMessage chatMessage = mapper.selectLastMessage(room_id);
		if(chatMessage.getType().equals("FILE")) {
			chatMessage.setMessage("사진을 보냈습니다");
		}
		if(chatMessage.getType().equals("REQUEST_PAY")) {
			chatMessage.setMessage(chatMessage.getMessage() + "원을 요청했어요");
		}
		return chatMessage;
	}
	
	//	읽지 않은 메세지 갯수 조회
	public int selectCountMessage(String room_id, String receiver_id) {
		return mapper.selectCountMessage(room_id, receiver_id);
	}
	
	
	//	기존의 상대방과의 채팅방 조회 요청
	public ChatRoom selectChatRoom(String sender_id, String receiver_id, int product_id) {
		ChatRoom chatRoom = mapper.selectChatRoom(sender_id, receiver_id, product_id);
		return chatRoom;
	}
	
	//	새 채팅방 정보 저장 요청
	public void insertChatRoom(List<ChatRoom> chatRoomList) {
		mapper.insertChatRoom(chatRoomList);
	}
	
	//	채팅 메세지 DB 저장 요청
	public void insertChatMessage(ChatMessage chatMessage) {
		mapper.insertChatMessage(chatMessage);
	}
	
	//	기존 채팅 내역 조회
	public List<ChatMessage> selectChatMessage(ChatMessage chatMessage) {
		List<ChatMessage> chatMessageList = mapper.selectChatMessage(chatMessage);
		return chatMessageList;
	}

	//	채팅방 회원 신고
	public int insertChatReport(String reporter_id, String reported_id, String reason, String room_id) {
//		return 0;
		return mapper.insertChatReport(reporter_id, reported_id, reason, room_id);
	}
	
	//	상품 가격 알아오기
	public int selectProductPrice(int product_id) {
		return mapper.selectProductPrice(product_id);
	}
	
	//	멤버 정보 가져오기
	public MemberVO selectMemberNick(String receiver_id) {
		return mapper.selectMemberNick(receiver_id);
	}
	
	//	멤버 프로필 받아오기
//	public String selectMemberProfile(String mem_id) {
//		return mapper.selectMemberProfile(mem_id);
//	}

	

	

}
