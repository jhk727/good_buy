<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
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
<link rel="stylesheet" href="../../resources/css/product.css">
<link rel="stylesheet" href="../../resources/css/pay.css">
<!-- JS for Page -->
<script src="../../resources/js/product.js"></script>
<script src="../../resources/js/pay.js"></script>


</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/inc/header.jsp"></jsp:include>
	</header>
	<main>
		<section class="wrapper">
			<div class="page-inner">
				<!-- *********** 여기 안에 작업하세요. section.wrapper/div.page-inner 건들지말기 ******** -->
                <h2 class="page-title">굿페이 > 홈</h2>
                
				 <div class="goodpay-container">
			        <!-- 상단: 계좌 정보 -->
			        <div class="account-box">
			        	<div class="info">
				        	<h1><i class="fa-solid fa-money-bill"></i>&nbsp;&nbsp;굿페이</h1>
			            	<button class="my-account" onclick="location.href='MyAccount'">내 계좌</button>
			        	</div>
			            <div class="balance">
			                <h1>30,000원</h1>
			                <i class="fa-solid fa-arrow-rotate-right"></i>
			            </div>
			            <div class="buttons">
			                <button class="charge-btn"  onclick="openModal()">+ 충전</button>
			                <button class="transfer-btn" onclick="location.href='pay_remit.jsp'">￦계좌송금</button>
			                
			                
			                <%-- 충전버튼 모달창 --%>
			                <div id="pay-account-modal" class="pay-account-modal">
								<div class="pay-account-modal-content">
									<div class="pay-account-modal-header">
										<span class="pay-account-modal-close " onclick="closeModal()">&times;</span>
										<h2>어느 계좌에서 충전 하시겠습니까?</h2>
									</div>
									<div class="pay-account-modal-body">
										<c:forEach var="account" items="${bankUserInfo.res_list}" varStatus="status">
											<form action="PayAccountDetail" method="POST" id="PayAccountDetail-${account.fintech_use_num}">
											        <input type="hidden" name="fintech_use_num" value="${account.fintech_use_num}">
											        <input type="hidden" name="account_holder_name" value="${account.account_holder_name}">
											        <input type="hidden" name="account_num_masked" value="${account.account_num_masked}">
											</form>
											<a href="#" 
											       title="${account.account_num_masked}계좌의 상세정보 보기" 
											       data-form-id="PayAccountDetail-${account.fintech_use_num}" 
											       onclick="submitForm(this);">
									        <div class="linked-account">
									            <div class="account-info">
									                <div class="icon"><i class="fa-solid fa-building-columns"></i></div>
									                <span class="account-number">${account.bank_name}<strong>${account.account_num_masked}</strong></span>
									            </div>
									            <c:choose>
				  										<c:when test="${account.fintech_use_num eq fintech_use_num}">
										            	<button class="primary-account-btn">대표계좌</button>
											        </c:when>
											        <c:otherwise>
											    		<form action="PayRegistRepresentAccount" method="POST" id="PayRegistRepresentAccount">
															<input type="hidden" name="fintech_use_num" value="${account.fintech_use_num}">
															<input type="hidden" name="account_holder_name" value="${account.account_holder_name}">
															<input type="hidden" name="account_num_masked" value="${account.account_num_masked}">
														</form>    
											        </c:otherwise>
											    </c:choose>
									        </div>
									    </a>
							        </c:forEach>
								</div>
							</div>
			            </div>
			        </div>
			
			        <!-- 최근 이용 내역 -->
			        <div class="history">
			            <h3>최근 이용내역 <a href="pay_use_list.jsp" class="see-all">전체보기 ></a></h3>
			            <div class="history-item">
			                <div class="icon"></div>
			                <div class="details">
			                    <span>쌀국수</span>
			                    <span class="date">12.03 12:10 | 송금</span>
			                </div>
			                <div class="amount">-5,000원</div>
			            </div>
			            <div class="history-item">
			                <div class="icon"></div>
			                <div class="details">
			                    <span>믹스커피</span>
			                    <span class="date">12.03 12:10 | 송금</span>
			                </div>
			                <div class="amount">-15,000원</div>
			            </div>
			            <div class="history-item">
			                <div class="icon"></div>
			                <div class="details">
			                    <span>겨울코드</span>
			                    <span class="date">12.03 12:10 | 송금</span>
			                </div>
			                <div class="amount">-50,000원</div>
			            </div>
			            <div class="history-item">
			                <div class="icon"></div>
			                <div class="details">
			                    <span>우체국 1234</span>
			                    <span class="date">12.03 12:10 | 충전</span>
			                </div>
			                <div class="amount">+100,000원</div>
			            </div>
			        </div>
			    </div>
				
				
				
				
				
			
			
				<!-- *********** // 여기 안에 작업하세요. section.wrapper/div.page-inner 건들지말기 ******** -->
			</div>
		</section>
	</main>
	<footer>
		<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
	</footer>
</body>
</html>