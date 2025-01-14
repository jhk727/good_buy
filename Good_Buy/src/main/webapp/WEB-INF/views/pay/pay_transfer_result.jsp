<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>굿바이 - 중고거래, 이웃과 함께 더 쉽게!</title>

<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/img/g_favicon.ico" type="image/x-icon">
<link rel="icon" href="${pageContext.request.contextPath}/resources/img/g_favicon.ico" type="image/x-icon">

<!-- default -->
<link rel="stylesheet" href="../../resources/css/common.css">
<link rel="stylesheet" href="../../resources/css/default.css">
<script src="../../resources/js/jquery-3.7.1.js"></script>

<!-- font-awesome -->
<link rel="stylesheet" href="../../resources/fontawesome/all.min.css" />
<script src="../../resources/fontawesome/all.min.js"></script>

<!-- ******************* 아래 CSS와 JS는 페이지별로 알맞게 Import 해주세요 ****************** -->
<!-- CSS for Page -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/product.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/pay.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mypage.css">
<!-- JS for Page -->
<script src="${pageContext.request.contextPath}/resources/js/product.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/pay.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/chat_main.js"></script>
<script>
	// 창이 닫히기 전에 부모 창 함수 호출
// 	window.onbeforeunload = function () {
// 		if (window.opener && !window.opener.closed) {
// 			window.opener.reloadParent(); // 부모 창의 함수 호출
// 		}
// 	};
	
window.onload = function() {
    // 부모 창에서 메시지 받기
    window.addEventListener("message", function(event) {
        if (event.origin !== window.location.origin) return;

        const data = event.data;
        console.log("Popup received:", data);

        // 데이터를 화면에 표시
//         document.getElementById("result").innerText =
	document.getElementsByClassName("bubble").innerText =
//         '송금 상태: ${data.status}\n메시지: ${data.message}\n금액: ${data.amount}\n받는 사람: ${data.receiver}'';
        '송금 상태: \N메시지: \N금액: \N받는 사람: ';
        // appendMessage로 보내는 게 낫지 않나?
    });
    
    sendMessage(TYPE_RESPONSE_PAY, 24 , 'apple123', 'bang', 'e02266e1-8082-4eaf-bede-c509166f1236', 2000);
};
</script>
</head>


<body>
	<main>
		<section class="wrapper">
			<div class="page-inner">
				<!-- *********** 여기 안에 작업하세요. section.wrapper/div.page-inner 건들지말기 ******** -->
		        
		        
		        
		        	<div class="my-container">
						<div class="contents-ttl">
						</div>
						<div class="contents">
							<div class="goodpay-container">
								<div class="account-box">
						        	<div class="info">
									 	<h1>굿페이 송금 완료</h1>
									</div>
						            <div class="account-user-type">
						            	<h3>송금결과</h3>
						            </div>
						            <div class="account-item">
						            	<div class="details">
						                    <span>출금계좌</span>
						                </div>
							            <div class="amount">${transferResult.depositResult.res_list[0].bank_name} ${transferResult.withdrawResult.account_num_masked}</div>
									</div>	
						            <div class="account-item">
							            <div class="details">
						                    <span>출금일시</span>
						                </div>
	                    				<div class="amount">
	                    					<fmt:parseDate value="${transferResult.withdrawResult.api_tran_dtm}" var="dateValue" pattern="yyyyMMddHHmmssSSS"/>	
											<fmt:formatDate value="${dateValue}" pattern="yyyy-MM-dd HH:mm"/>
										</div>
						            </div>
						            <div class="account-item">
							            <div class="details">
						                    <span>출금금액</span>
						                </div>
	                    				<div class="amount">￦ <fmt:formatNumber pattern="#,###">${transferResult.withdrawResult.tran_amt}</fmt:formatNumber></div>
						            </div>
						            <div class="account-item">
							            <div class="details">
						                    <span>수취인명</span>
						                </div>
	                    				<div class="amount">${transferResult.depositResult.res_list[0].account_holder_name}</div>
						            </div>
						            <div class="account-item">
							            <div class="details">
						                    <span>입금계좌</span>
						                </div>
	                    				<div class="amount">${transferResult.depositResult.res_list[0].bank_name} ${transferResult.depositResult.res_list[0].account_num_masked}</div>
						            </div>
<!-- 						            <div class="return-btn"><button onclick="location.href = document.referrer">돌아가기</button></div> -->
						            <div class="return-btn"><button onclick="closeWindow()">돌아가기</button></div>
								</div><!-- account-box -->
						    </div>
                		</div>
                	</div>
				<!-- *********** // 여기 안에 작업하세요. section.wrapper/div.page-inner 건들지말기 ******** -->
			</div>
		</section>
	</main>
</body>
</html>
