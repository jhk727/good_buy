<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
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
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css">
<!-- JS for Page -->
<!-- 카카오 스크립트 -->
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>


</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/inc/header.jsp"></jsp:include>
	</header>
	<main>
		<section class="wrapper">
			<div class="page-inner">
				<!-- *********** 여기 안에 작업하세요. section.wrapper/div.page-inner 건들지말기 ******** -->
				<div class="login-wrap">
				    <h1 class="login-ttl">LOGIN</h1>
				    <div class="login-box">
		    			<a href="MemberLogin">ID/PW로 로그인</a>
		    			<a class="sns-login" onclick="kakaoLogin()">
						   <i class="fa-solid fa-comment"></i>카카오톡으로 간편로그인
						</a>
		    			<a href="#" class="sns-login"><img alt="네이버로그인" src="${pageContext.request.contextPath}/resources/img/naver-icon.png" > 네이버로 로그인</a>
			       </div>
		       </div>
		       <!-- *********** // 여기 안에 작업하세요. section.wrapper/div.page-inner 건들지말기 ******** -->
			</div>
		</section>
	</main>
	<footer>
		<jsp:include page="/WEB-INF/views/inc/footer.jsp"></jsp:include>
	</footer>
	<script th:inline="javascript">
		<!-- 카카오 로그인 연동 -->
		function kakaoLogin() {
			$.ajax({
				url:'/memberLoginForm/getKakaoAuthUrl',
				type:'post',
				async: false,
				dataType: 'text',
				success: function (res) {
					location.href = res;
			 	}
			});
		}
	</script>
</body>
</html>