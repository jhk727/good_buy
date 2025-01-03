<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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


</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/inc/header.jsp"></jsp:include>
	</header>
	<main>
		<section class="wrapper">
			<div class="page-inner">
				<!-- *********** 여기 안에 작업하세요. section.wrapper/div.page-inner 건들지말기 ******** -->
                <h2 class="page-ttl">마이페이지</h2>
				<section class="my-wrap">
					<aside class="my-menu">
						<h3>거래 정보</h3>
						<a href="MyStore">나의 상점</a>
						<a href="GoodPay" class="active">굿페이</a>
						<a href="MyOrder">구매내역</a>
						<a href="MySales">판매내역</a>
						<h3>나의 정보</h3>
						<a href="MyInfo">계정정보</a>
						<a href="MyWish">관심목록</a>
						<a href="MyReview">나의 후기</a>
						<a href="MySupport">1:1문의내역</a>
						<a href="">나의 광고</a>
					</aside>
		        
		        
		        
		        	<div class="my-container">
						<div class="contents-ttl">
							<h3>굿페이 > 굿페이 환불</h3>
                
                
                
                
							 <div class="goodpay-container">
							 	<h1>굿페이 환불 결과</h1>
								<h3>${account_holder_name} 고객님의 정보</h3>
								<table border="1">
									<tr>
										<th>사용자번호</th> <%-- 세션의 token 객체에 저장되어 있음 --%>
										<td>${token.user_seq_no}</td>
									</tr>
									<tr>
										<th>핀테크이용번호</th>
										<td>${depositResult.FINTECH_USE_NUM}</td>
									</tr>
									<tr>
										<th>상대방 계좌번호</th><!-- 사실 지금은 핀테크이용번호로 이체해서 상대방 계좌번호는 의미가 없다. -->
										<%-- 핀테크 이용번호로 출금했으므로 임의의 계좌번호가 출력됨 --%>
										<td>${depositResult.DPS_ACCOUNT_NUM_MASKED}</td>
									</tr>
									<tr>
										<th>출금금액</th>
										<td>￦ ${depositResult.TRAN_AMT}</td>
									</tr>
									<tr>
										<th>출금일시</th>
										<td>${depositResult.API_TRAN_DTM}</td>
									</tr>
									<tr>
										<th colspan="2"><input type="button" value="돌아가기" onclick="history.back()"></th>
									</tr>
								</table>
						    </div>
						</div>
					</div>
				</section>
				
				
			
			
				<!-- *********** // 여기 안에 작업하세요. section.wrapper/div.page-inner 건들지말기 ******** -->
			</div>
		</section>
	</main>
	<footer>
		<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
	</footer>
</body>
</html>