<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/img/g_favicon.ico" type="image/x-icon">
<link rel="icon" href="${pageContext.request.contextPath}/resources/img/g_favicon.ico" type="image/x-icon">
<title>굿바이 - 중고거래, 이웃과 함께 더 쉽게!</title>

<!-- default -->
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.7.1.js"></script>

<!-- font-awesome -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/fontawesome/all.min.css" />
<script src="${pageContext.request.contextPath}/resources/fontawesome/all.min.js"></script>

<!-- CSS for Page -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&family=Nunito:wght@200..1000&display=swap" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/adm/css/sb-admin-2.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/adm/css/adm.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/adm/vendor/datatables/dataTables.bootstrap4.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/adm/vendor/datatables/datatables.min.css" rel="stylesheet">

</head>
<body id="page-top">

    <!-- Page Wrapper -->
	<div id="wrapper">

		<!-- Sidebar -->
       	<jsp:include page="/WEB-INF/views/admin/inc/aside.jsp"></jsp:include>
		<!-- // Sidebar -->

        <!-- Content Wrapper -->
        <div id="content-wrapper" class="d-flex flex-column">

            <!-- Main Content -->
            <div id="content">
            
				<!-- Topbar -->
				<jsp:include page="/WEB-INF/views/admin/inc/top.jsp"></jsp:include>
				<!-- End of Topbar -->
               
                <!-- Begin Page Content -->
                <div class="container-fluid">
					<div class="row">
                        <div class="col-lg-12">
                            <div class="card shadow mb-4">
                                <div class="card-header py-3">
                                    <h5 class="m-0 font-weight-bold text-primary">공지사항 작성</h5>
                                </div>
                                <div class="card-body">
                                    <form action="AdmNoticeRegist" name="writeForm" method="post" enctype="multipart/form-data">
                                        <div class="mb-3">
                                            <label class="small mb-1" for="memId">작성자 ID</label>
                                            <input class="form-control" id="memId" name="mem_id" type="text" value="${sessionScope.sId}" readonly>
                                        </div>
                                        <div class="mb-3">
                                            <label class="small mb-1" for="noticeSubject">제목</label>
                                            <input class="form-control" id="noticeSubject" name="notice_subject" type="text" placeholder="제목 입력" required>
                                        </div>
                                        <div class="mb-3">
                                            <label class="small mb-1" for="noticeContent">내용</label>
                                            <textarea class="form-control" rows="15" cols="40" name="notice_content" id="noticeContent" placeholder="내용 입력"></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label class="small mb-1" for="file">파일첨부</label>
											<input type="file" class="form-control" name="file" id="file">
                                        </div>
                                        <div class="row align-items-center justify-content-center">
	                                        <button type="button" onclick="location.href='AdmNoticeList'" class="btn btn-dark btn-lg d-block col-3">취소</button>
	                                        <button type="submit" id="btnSubmitForm" class="btn btn-primary btn-lg d-block col-3 ml-2">등록</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                     </div>
                </div>
                <!-- /.container-fluid -->
            </div>
            <!-- End of Main Content -->
            <!-- Footer -->
            <footer class="sticky-footer bg-white">
                <jsp:include page="/WEB-INF/views/admin/inc/bottom.jsp"></jsp:include>
            </footer>
            <!-- End of Footer -->
        </div>
        <!-- End of Content Wrapper -->

    </div>
    <!-- End of Page Wrapper -->

    <!-- Scroll to Top Button-->
    <a class="scroll-to-top rounded" href="#page-top">
        <i class="fas fa-angle-up"></i>
    </a>
    
    <!-- 수정 모달 -->
    <div class="modal fade" id="updateMemberInfo" tabindex="-1" aria-labelledby="updateModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
		    <div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="updateModalLabel">회원 상태 변경</h5>
					<button type="button" class="close" data-dismiss="modal"><i class="fa-solid fa-xmark"></i></button>
				</div>
				<div class="modal-body">
					<form action="AdmMemberModify" id="memberModifyForm" method="post">
						<input type="hidden" id="memId" name="mem_id">
						<div class="mb-3">
							<label for="memId2" class="col-form-label">변경 대상 회원</label>
							<input type="text" class="form-control" id="memId2" name="mem_id2" readonly>
						</div>
						<div class="mb-3">
							<label class="small mb-1" for="memGrade">회원등급</label>
							<select class="custom-select" id="memGrade" name="mem_grade">
								<option value="일반">일반</option>
								<option value="관리자">관리자</option>
							</select>
						</div>
						<div class="mb-3">
							<label class="small mb-1" for="memStatus">회원상태</label>
							<select class="custom-select" id="memStatus" name="mem_status">
								<option value="1">정상</option>
								<option value="2">정지</option>
								<option value="3">탈퇴</option>
							</select>
						</div>
                    </form>
				</div>
				<div class="modal-footer justify-content-center">
					<button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
					<button type="submit" class="btn btn-primary" id="btnModifyForm" form="memberModifyForm">수정하기</button>
				</div>
			</div>
		</div>
	</div>

    <!-- Bootstrap core JavaScript-->
    <script src="${pageContext.request.contextPath}/resources/adm/vendor/jquery/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/adm/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    <!-- Core plugin JavaScript-->
    <script src="${pageContext.request.contextPath}/resources/adm/vendor/jquery-easing/jquery.easing.min.js"></script>

    <!-- Custom scripts for all pages-->
    <script src="${pageContext.request.contextPath}/resources/adm/js/sb-admin-2.min.js"></script>
    
    <!-- Page level plugins -->
    <script src="${pageContext.request.contextPath}/resources/adm/vendor/datatables/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/adm/vendor/datatables/dataTables.bootstrap4.min.js"></script>
<%--     <script src="${pageContext.request.contextPath}/resources/adm/vendor/datatables/datatables.min.js"></script> --%> <%-- 반응형 --%>

    <!-- Page level custom scripts -->

</body>


</html>