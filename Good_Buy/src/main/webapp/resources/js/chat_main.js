//	채팅 메세지 타입 구분
const TYPE_ENTER = "ENTER";
const TYPE_LEAVE = "LEAVE";
const TYPE_TALK = "TALK";
const TYPE_INIT = "INIT";
const TYPE_INIT_COMPLETE = "INIT_COMPLETE";
const TYPE_START = "START";
const TYPE_REQUEST_CHAT_ROOM_LIST = "REQUEST_CHAT_ROOM_LIST";
const TYPE_REQUEST_CHAT_LIST = "REQUEST_CHAT_LIST";

//	채팅 메세지 정렬 위치
const ALIGN_CENTER = "center";
const ALIGN_LEFT = "other";
const ALIGN_RIGHT = "user";
//	====================================================================
var ws;
var receiver_id;
//var sId;
var product_id;

$(".sidebar-item").on("dblclick", function() {
	let index = $(this).data("index");
	let room_id = $("#room_id" + index).val();
	let title = $("#title" + index).val();
	let receiver_id = $("#receiver_id" + index).val();
	console.log("index =" + index);
	console.log("room_id =" + room_id);
	console.log("title =" + title);
	console.log("receiver_id =" + receiver_id);
	console.log("sender_id =" + sId);
	//	room_id, title, sender_id, receive_id
	let room = {
		room_id : room_id,
		title : title,
		receiver_id : receiver_id,
		sender_id : sId
	}
	
	showChatRoom(room);
})


$(function() {
	
	sId = $("#sId", opener.document).val();
	console.log("sId : " + sId);
	
	if (!sId) {
		alert("로그인이 필요합니다.\n로그인 페이지로 이동합니다.")
		opener.location.href = "MemberLogin";
		window.close();
	}
	
	console.log("부모창에서 받은 receiver_id : " + receiver_id);
	console.log("부모창에서 받은 product_id : " + product_id);
	
	
	window.onmessage = function(e) {
		console.log("부모창 메세지 : " + e.data);
		let data = JSON.parse(e.data);
		
		if (data.type == TYPE_INIT) {	//	채팅 윈도우 초기화
			showChatList(data);
		}
		if (data.type == TYPE_REQUEST_CHAT_LIST) {
			console.log("채팅내역 수신");
			console.log(data.message);
			for(let message of JSON.parse(data.message)) {
				appendMessage(message.type, message.sender_id, message.receiver_id, message.message, message.send_time);
			}
		}
		if (data.type == TYPE_TALK) {	// 채팅 입력
			appendMessage(data.type, data.sender_id, data.receiver_id, data.message, data.send_time);
		}
		
	};
	
	initChat();
	
});
//	=========================채팅방 목록 작업 시작===========================
function initChat() {
	let wsCheckInterval = setInterval(function() {
		ws = opener.ws;
		console.log("chat_main.js - 웹소켓 연결 상태 : " + ws.readyState);
		
		if(ws == null || ws.readyState != ws.OPEN || ws.readyState == ws.CLOSING || ws.readyState == ws.CLOSED) {
			opener.connect();
		} else {
			console.log("웹소켓 연결 완료!")
			//	부모창에게 메세지 전송(sendMessage 함수 호출)
			sendMessage(TYPE_INIT, "", "", "", "", "");
			
			clearInterval(wsCheckInterval);
		}
		
	}, 1000);
	
}
//	채팅방 목록 표시
function showChatList(data) {
	sendMessage(TYPE_INIT_COMPLETE, product_id, "", receiver_id, "", "");
}

//	채팅창 생성
function showChatRoom(room) {
	console.log("showChatRoom -  채팅화면 표시 - " + room);
	console.log(room);
	let url = new URL(location.href);
	
	let divRoom = 	  '<div class="extra-header">'
						+ '<button class="close-chat-button" onclick="closeChat()">'
							+ '<i class="fa-solid fa-arrow-left"></i>'
						+ '</button>'
						+ '<button class="report-chat-button" onclick="reportChat()">'
//							+ '<img src="${pageContext.request.contextPath}/resources/img/siren.png">'
							+ '<i class="fa-solid fa-land-mine-on"></i>&nbsp;신고하기'
						+ '</button>'
					+ '</div>'
					+ '<div class="chat-header">'
			           	+ '<a><img src="${pageContext.request.contextPath}/resources/img/testPicture.png" alt="item"></a>'
			           	+ '<div class="title">'+ room.title +' </div>'
			           	+ '<button class="item-button" onclick="openPayWindow(' + room.product_id + ', \'' + room.receiver_id + '\')">구매하기</button>'
			           + '</div>'
			           + '<div class="chat-body">'
			           + '</div>'
		           + '<div class="chat-footer">'
			           	+ '<input type="hidden" id="room_id" value="' + room.room_id +'">'
			           	+ '<input type="hidden" id="receiver_id" value="' + room.receiver_id +'">'
			           	+ '<input type="hidden" id="sId" value="' + room.sender_id +'">'
			           	+ '<input type="text" class="chatMessage" placeholder="메시지를 입력하세요...">'
			            + '<button class="btnSend">전송</button>'
		            + '</div>';
	
	//	chat-area 영역에 채팅창 div 출력
	$(".chat-area").html(divRoom);
		$(".chat-area").on("click", ".btnSend", function() {
		sendInputMessage();
	});
	
	$(".chat-area").on("keypress", ".chatMessage", function(event) {
		let keyCode = event.keyCode;
		if (keyCode == 13) {
			sendInputMessage();
		}
	});
	
	$(".btnSend").on("click", function() {
		sendInputMessage();
	});
	
	$(".chatMessage").on("keypress", function(event) {
		let keyCode = event.keyCode;
		if (keyCode == 13) {
			sendInputMessage();
		}
	});
	
	//	채팅목록 수신메세지 표시 제거 작업 예정
	
	//	기존 채팅 내역 불러오기 작업 예정
	sendMessage(TYPE_REQUEST_CHAT_LIST, product_id, sId, "", room.room_id, "");
	
}
//	=========================채팅방 목록 작업 끝===============================
//	===========================================================================
//	=========================메세지 보내기 작업 시작===========================
function appendMessage(type, sender_id, receiver_id, message, send_time) {
	//	send_time 추가 예정
	let div_message = "";
	
	if(sender_id == sId) {	// 자신이 보낸 메세지(송신자가 자신인 경우)
		div_message = '<div class="message user">'
						+ '<div class="bubble">' + message + '</div>'
					 + '</div>';
	}
	
	if(receiver_id == sId) {	// 자신이 보낸 메세지(송신자가 자신인 경우)
		div_message = '<div class="message other">'
						+ '<div class="bubble">' + message + '</div>'
					 + '</div>';
	}

	$(".chat-body").append(div_message);
	
	
}

function sendInputMessage() {
	let message = $(".chatMessage").val();
	
	if(message == "") {
		return;
	}
	
	let room_id = $(".chat-footer > #room_id").val();
	let receiver_id = $(".chat-footer > #receiver_id").val();
	console.log("TYPE_TALK에서 receiver_id 확인 - " + receiver_id);
	
	sendMessage(TYPE_TALK, "", sId, receiver_id, room_id, message);
	
	$(".chatMessage").val("");
	$(".chatMessage").focus();
	
}
//	=========================메세지 보내기 작업 끝============================
//	===============================================================================
//	부모창의 sendMessage() 함수 호출
function sendMessage(type, product_id, sender_id, receiver_id, room_id, message) {
	opener.sendMessage(type, product_id, sender_id, receiver_id, room_id, message);
}
function closeChat() {
	$(".chat-area").empty();
}
// ==============================================================================
// 결제창 열기 - 창을 작게 열려고 함수로 만들었음
function openPayWindow(product_id, receiver_id) {
	var url = "PayTransferRequest?product_id=" + encodeURIComponent(product_id) +
              "&receiver_id=" + encodeURIComponent(receiver_id) ;
    payWindow = window.open(url, "chat_window", "width=500,height=500");
}
