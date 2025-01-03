package com.itwillbs.goodbuy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.goodbuy.mapper.ChatMapper;
import com.itwillbs.goodbuy.vo.ChatMessage;
import com.itwillbs.goodbuy.vo.ChatRoom;

@Service
public class ChatService {
	@Autowired
	private ChatMapper mapper;
	
	//	기존 자신의 채팅방 목록 조회 요청
	public List<ChatRoom> selectChatRoomList(String sender_id) {
		return mapper.selectChatRoomList(sender_id);
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

}
