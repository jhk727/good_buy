<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/img/g_favicon.ico" type="image/x-icon">
<link rel="icon" href="${pageContext.request.contextPath}/resources/img/g_favicon.ico" type="image/x-icon">

<title>굿바이 - 중고거래, 이웃과 함께 더 쉽게!</title>

<!-- default -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/default.css">
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.7.1.js"></script>

<!-- font-awesome -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/fontawesome/all.min.css" />
<script src="${pageContext.request.contextPath}/resources/fontawesome/all.min.js"></script>

<!-- ******************* 아래 CSS와 JS는 페이지별로 알맞게 Import 해주세요 ****************** -->
<!-- CSS for Page -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mypage.css">

<!-- JS for Page -->
<script src="${pageContext.request.contextPath}/resources/js/slick.js"></script>

</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/inc/header.jsp"></jsp:include>
	</header>
	<main>
		<!-- 계정설정 -->
		<section class="wrapper">
			<div class="page-inner">
				<!-- *********** 여기 안에 작업하세요. section.wrapper/div.page-inner 건들지말기 ******** -->
				<!-- -->
				<h2 class="page-ttl">마이페이지</h2>
				<section class="my-wrap">
				<aside class="my-menu">
					<h3>거래 정보</h3>
					<a href="MyStore">나의 상점</a>
					<a href="GoodPay">굿페이</a>
					<a href="MyOrder">구매내역</a>
					<a href="MySales">판매내역</a>
					<h3>나의 정보</h3>
					<a href="MyInfo">계정정보</a>
					<a href="MyWish">관심목록</a>
					<a href="MyReview">나의 후기</a>
					<a href="MyReviewHistory">내가 쓴 후기</a>
					<a href="MySupport"  class="active">1:1문의내용</a>
					<a href="">나의 광고</a>
				</aside>
				<div class="my-container">
					<div class="contents-ttl">1:1문의 게시판</div>
				</div>
				</section>
				<!-- *********** // 여기 안에 작업하세요. section.wrapper/div.page-inner 건들지말기 ******** -->
				<!-- contents -->
					<section class="inq-wrap">
						<form action="RequestModifyForm" method="post" enctype="multipart/form-data" class="inq-frm">
							<input type="hidden" name="mem_id" value="${sessionScope.sId}">
							<div class="row">
								<select name="support_category">
									<option value="1" <c:if test="${support.support_category == 1}">selected</c:if>>이용문의</option>
		                    		<option value="2" <c:if test="${support.support_category == 2}">selected</c:if>>결제문의</option>
		                    		<option value="3" <c:if test="${support.support_category == 3}">selected</c:if>>기타</option>
								</select>	
							</div>
		                    <div class="row">
			                    <input type="text" name="support_subject" required="required" value="${support.support_subject}">
		                    </div>
		                    <div class="row">
		                    	<textarea name="support_content" rows="15" cols="40" required="required">${support.support_content}</textarea>
		                     	<input type="hidden" name="support_id" value="${support.support_id}">
		                    </div>
		                    <!-- 파일 첨부 -->
		                    <div class="row">
		                    	<input type="file" name="file1">
		                    </div>
		                     <div class="btns">
		                    	<button type="button" onclick="history.back()">취소</button>
		                    	<button type="submit">수정하기</button>
		                    </div>
		                </form>
					</section>
					<!-- // contents -->
		</div>
		</section>
	</main>
	<footer>
		<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
	</footer>
<!-- 	<script type="text/javascript"> -->
// 	function requestModify(supportId) {
// 		$.ajax({
// 			url : "RequestModifyForm",
// 			type : "POST",
// 			contentType: "application/json",
// 			data : JSON.stringify({
// 				support_id: supportId,
// 			    support_subject: "${support.support_subject}",
// 			    support_content: "${support.support_content}"
// 			}),
// 			success : function (response) {
// 				if(response == "success"){
// 					alert("문의사항이 수정되었습니다");
// 					location.href="MySupport";
// 				}
// 			}
// 		})
// 	}
	
	
<!-- 	</script> -->
</body>
</html>