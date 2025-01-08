package com.itwillbs.goodbuy.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.goodbuy.aop.AdminLog;
import com.itwillbs.goodbuy.aop.LoginCheck;
import com.itwillbs.goodbuy.aop.LoginCheck.MemberRole;
import com.itwillbs.goodbuy.service.AdminService;
import com.itwillbs.goodbuy.vo.CommonCodeVO;
import com.itwillbs.goodbuy.vo.MemberVO;
import com.itwillbs.goodbuy.vo.NoticeVO;
import com.itwillbs.goodbuy.vo.ProductOrderVO;
import com.itwillbs.goodbuy.vo.SupportVO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class AdminController {
	
	@Autowired
	private AdminService service;
	
	// 공지사항 첨부파일 URL
	String uploadPath = "/resources/upload";
	
	// datatables parameter
	List<String> intColumns = Arrays.asList("draw", "start", "length", "mem_status", "faq_cate", "list_status");
	
	// [ 관리자 메인 ]
	// 관리자 메인
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@GetMapping("AdmMain")
	public String admMain() {
		return "admin/index";
	}
	// ======================================================
	// [ 공통코드 관리 ]
	// 공통코드 관리 - 등록 폼 요청
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@GetMapping("AdmCommoncodeRegistForm")
	public String admCommoncodeRegistForm() {
		return "admin/code_regist";
	}
	
	// 공통코드 관리 - 등록
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@ResponseBody
	@PostMapping("AdmCommoncodeRegist")
	public Map<String, Object> admCommoncodeRegist(@RequestBody CommonCodeVO commonCodes, Model model, HttpSession session) {
		Map<String, Object> mainCode = commonCodes.getMainCode();
		List<Map<String, Object>> subCodes = commonCodes.getSubCodes();
		
		log.info(">>> mainCode : " + mainCode);
		log.info(">>> subCodes : " + subCodes);
		
		int insertCommonCode = service.registCommonCode(mainCode, subCodes);
		
		Map<String, Object> response = new HashMap<String, Object>();
		if(insertCommonCode > 0) {
			response.put("status", "success");
			response.put("message", "공통코드가 등록되었습니다.");
			response.put("redirectURL", "/AdmCommoncodeRegistForm");
		} else {
			response.put("status", "fail");
			response.put("message", "공통코드 등록에 실패했습니다. 입력값을 확인하세요.");
		}
		return response;
	}
	
	// 공통코드 관리 - 목록
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@GetMapping("AdmCommoncodeList")
	public String admCommoncodeList() {
		return "admin/code_list";
	}
	
	// 공통코드 관리 - 요청
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@ResponseBody
	@PostMapping("AdmCommoncodeListForm")
	public String admCommoncodeListForm(@RequestParam Map<String, String> param) {	
//		log.info(">>>> param: " + param);
		Map<String, Object> convertParam = convertMap(param);
		
		// 공통코드 전체 컬럼 수 조회
		int recordsTotal = service.getCommonCodesTotal();
		
		// 공통코드 검색 필터링 후 컬럼 수 조회
		int recordsFiltered = service.getCommonCodesFiltered(convertParam);
		
		// 공통코드 전체 목록 조회
		List<Map<String, Object>> commonCodes = service.getCommonCodes(convertParam);
		
		// 데이터를 map 객체에 담아서 JSON 객체로 변환하여 전달
		Map<String, Object> response = new HashMap<String, Object>();
		
		// draw, recordsTotal, recordsFiltered 값을 돌려주어야 서버사이드 페이징 작동함
		response.put("draw", convertParam.get("draw")); // 받은 draw 값 그대로 다시 전달(보안)
		response.put("recordsTotal", recordsTotal); // 전체 컬럼 수
		response.put("recordsFiltered", recordsFiltered); // 검색 필터링 후 컬럼 수
		response.put("commonCodes", commonCodes); // 컬럼 데이터
		
		JSONObject jo = new JSONObject(response);
		
		return jo.toString();
	}
	
	// 공통코드 관리 - 수정
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@PostMapping("AdmCommoncodeModify")
	public String admCommoncodeModify(@RequestParam Map<String, Object> param, Model model) {
//		log.info(">>> param : " + param);
		
		int updateResult = service.modifyCommonCode(param);
		if(updateResult > 0) {
			model.addAttribute("msg", "선택한 공통코드가 수정되었습니다.");
			return "redirect:/AdmCommoncodeList";
		} else {
			model.addAttribute("msg", "공통코드 수정에 실패했습니다. 입력값을 확인하세요.");
			return "result/fail";
		}
		
	}
	
	// 공통코드 관리 - 삭제
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@ResponseBody
	@PostMapping("AdmDeleteCommonCode")
	public Map<String, Object> admCommoncodeDelete(@RequestParam Map<String, Object> param) {
//		log.info(">>> param : " + param);
		
		int deleteResult = service.removeCommonCode(param);
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		if(deleteResult > 0) {
			response.put("status", "success");
			response.put("message", "선택한 공통코드가 삭제되었습니다.");
			response.put("redirectURL", "/AdmCommoncodeList");
		} else {
			response.put("status", "fail");
			response.put("message", "공통코드 삭제에 실패했습니다. 삭제할 데이터를 확인하세요.");
		}
		
		return response;
	}
	
	// ======================================================
	// [ 회원관리 ]
	// 회원 목록 - 뷰페이지 포워딩
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@GetMapping("AdmMemberList")
	public String admMemberList() {
		return "admin/member_list";
	}
	
	// 회원 목록 - 조회
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@ResponseBody
	@PostMapping("AdmMemberListForm")
	public String admMemberListForm(@RequestParam Map<String, String> param) {
		Map<String, Object> convertParam = convertMap(param);
		
		// 회원 목록 전체 컬럼 수 조회
		int recordsTotal = service.getMemberListTotal();
		
		// 회원 검색 필터링 후 컬럼 수 조회
		int recordsFiltered = service.getMemberListFiltered(convertParam);
		
		// 필터링 된 회원 목록 가져오기
		List<MemberVO> memberList = service.getMemberList(convertParam);
//		log.info(">>>>> 필터링 된 회원 : " + memberList);
		
		// 데이터를 map 객체에 담아서 JSON 객체로 변환하여 전달
		Map<String, Object> response = new HashMap<String, Object>();
		
		// draw, recordsTotal, recordsFiltered 값을 돌려주어야 서버사이드 페이징 작동함
		response.put("draw", convertParam.get("draw")); // 받은 draw 값 그대로 다시 전달(보안)
		response.put("recordsTotal", recordsTotal); // 전체 컬럼 수
		response.put("recordsFiltered", recordsFiltered); // 검색 필터링 후 컬럼 수
		response.put("memberList", memberList); // 컬럼 데이터
		
		JSONObject jo = new JSONObject(response);
		
		return jo.toString();
	}
	
	// 회원 상세 정보 - 조회
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@GetMapping("AdmMemberDetailForm")
	public String admMemberDetailForm(String mem_id, Model model) {
		log.info(">>> 상세 회원 정보 ID : " + mem_id);
		
		MemberVO dbMember = service.getMember(mem_id);
		String memStatus = memberStatusToString(dbMember.getMem_status());
		String snsStatus = snsStatusToString(dbMember.getSns_status());
		String authStatus = memberAuthToString(dbMember.getAuth_status());
		
		// 신고 기록 추가
		List<Map<String, Object>> reportHistory = service.getReportHistory(mem_id);
		log.info("reportHistory" + reportHistory);
		model.addAttribute("dbMember", dbMember);
		model.addAttribute("memStatus", memStatus);
		model.addAttribute("snsStatus", snsStatus);
		model.addAttribute("authStatus", authStatus);
		model.addAttribute("reportHistory", reportHistory);
		
		return "admin/member_modify";
	}
	
	
	// 회원 상세 정보 - 수정
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@PostMapping("AdmMemberModify")
	public String admMemberModify(MemberVO member, Model model) {
		log.info(">>> 수정할 회원 정보: " + member);
		
		int updateResult = service.modifyMemberInfo(member);
		
		if(updateResult > 0) {
			model.addAttribute("msg", "회원 상태를 수정하였습니다.");
			model.addAttribute("targetURL", "AdmMemberList");
			return "result/success";
		} else {
			model.addAttribute("msg", "회원 상태 수정에 실패하였습니다.");
			return "result/fail";
		}
	}
	
	// 회원 삭제
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@ResponseBody
	@PostMapping("AdmMemberDelete")
	public Map<String, Object> admMemberDelete(MemberVO member, Model model) {
		System.out.println(">>>>>>>>>>> 삭제할 회원: " + member);
		
		int deleteResult = service.removeMember(member.getMem_id());
		
		Map<String, Object> response = new HashMap<String, Object>();
		if(deleteResult > 0) {
			response.put("status", "success");
			response.put("message", "선택한 회원이 삭제되었습니다.");
			response.put("redirectURL", "/AdmMemberList");
		} else {
			response.put("status", "fail");
			response.put("message", "회원 삭제에 실패했습니다. 다시 시도해주세요.");
		}
		
		return response;
	}
	
	// ================================================
	// [ 결제 관리 ]
	// 상품 거래내역 - 포워딩
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@GetMapping("AdmProductOrderList")
	public String admProductOrderList() {
		return "admin/order_list";
	}
	
	// 상품 거래내역 테이블
	@ResponseBody
	@PostMapping("AdmOrderList")
	public String admOrderList(@RequestParam Map<String, String> param) {
		// Map 형변환 처리 메서드 호출
		Map<String, Object> convertParam = convertMap(param);
		System.out.println("convertParam: " + convertParam);
		System.out.println("거래상태: " + convertParam.get("status"));
		System.out.println("검색어: " + convertParam.get("searchValue"));
		
		// 거래내역 목록 전체 컬럼 수 조회
		int recordsTotal = service.getOrderListTotal();
		
		// 거래내역 검색 필터링 후 컬럼 수 조회
		int recordsFiltered = service.getOrderListFiltered(convertParam);
		
		// 필터링 된 거래내역 목록 가져오기
		List<ProductOrderVO> OrderList = service.getOrderList(convertParam);
		log.info(">>>>> 필터링 된 거래 목록 : " + OrderList);
		
		// 데이터를 map 객체에 담아서 JSON 객체로 변환하여 전달
		Map<String, Object> response = new HashMap<String, Object>();
		
		// draw, recordsTotal, recordsFiltered 값을 돌려주어야 서버사이드 페이징 작동함
		response.put("draw", convertParam.get("draw")); // 받은 draw 값 그대로 다시 전달(보안)
		response.put("recordsTotal", recordsTotal); // 전체 컬럼 수
		response.put("recordsFiltered", recordsFiltered); // 검색 필터링 후 컬럼 수
		response.put("OrderList", OrderList); // 컬럼 데이터
		
		JSONObject jo = new JSONObject(response);
		
		return jo.toString();
	}
	
	
	// ================================================
	// [ 신고 관리 ]
	// 신고 상품 목록 페이지 포워딩
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@GetMapping("AdmProductReportList")
	public String admProductReportListForm() {
		return "admin/product_report_list";
	}
	
	// 신고 상품 목록 - 필터링 및 검색
	@ResponseBody
	@PostMapping("AdmProductReportList")
	public String admProductReportList(@RequestParam Map<String, String> param) {
//		log.info(">>>> 신고 상품 목록 param : " + param);
		Map<String, Object> convertParam = convertMap(param);
		
		// 신고 상품 목록 전체 컬럼 수 조회
		int recordsTotal = service.getProductReportTotal();
		
		// 신고 상품 검색 필터링 후 컬럼 수 조회 (status, searchValue, searchDate)
		int recordsFiltered = service.getProductReportFiltered(convertParam);
		
		// 필터링 된 신고 상품 목록 가져오기
		List<Map<String, Object>> productReportList = service.getProductReportList(convertParam);
		log.info(">>>>> 필터링 된 신고 상품 목록 : " + productReportList);
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		response.put("draw", convertParam.get("draw"));
		response.put("recordsTotal", recordsTotal);
		response.put("recordsFiltered", recordsFiltered);
		response.put("productReportList", productReportList);
		
		JSONObject jo = new JSONObject(response);
		
		return jo.toString();
	}
	
	// 신고 상품 목록 - 조치하기(+ 수정하기)
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@PostMapping("AdmProductReportAction")
	public String admProductReportAction(@RequestParam Map<String, Object> param, Model model) {
//		log.info(">>> 신고 조치 : " + param);
		
		int updateResult = service.modifyProductReport(param);
		
		if(updateResult > 0) {
			model.addAttribute("msg", "신고사항 조치를 완료하였습니다.");
			model.addAttribute("targetURL", "AdmProductReportList");
			return "result/success";
		} else {
			model.addAttribute("msg", "신고사항 처리에 실패하였습니다.");
			return "result/fail";
		}
	}
	
	// 신고 회원 목록 - 포워딩
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@GetMapping("AdmMemberReportList")
	public String admMemberReportListForm() {
		return "admin/member_report_list";
	}
	
	// 신고회원 목록
	@ResponseBody
	@PostMapping("AdmMemberReportList")
	public String admMemberReportList(@RequestParam Map<String, String> param) {
		log.info(">>>> 신고 회원 목록 param : " + param);
		Map<String, Object> convertParam = convertMap(param);
		
		int recordsTotal = service.getUserReportTotal();
		int recordsFiltered = service.getUserReportFiltered(convertParam);
		List<Map<String, Object>> userReportList = service.getUserReportList(convertParam);
		log.info(">>>>> 필터링 된 신고 회원 목록 : " + userReportList);
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("draw", convertParam.get("draw"));
		response.put("recordsTotal", recordsTotal);
		response.put("recordsFiltered", recordsFiltered);
		response.put("userReportList", userReportList);
		
		JSONObject jo = new JSONObject(response);
		
		return jo.toString();
	}
	
	// 신고 회원 목록 - 조치하기(+ 수정하기)
	@PostMapping("AdmUserReportModify")
	public String admUserReportModify(@RequestParam Map<String, Object> param, Model model) {
		log.info(">>> 신고 조치 : " + param);
		
		int updateResult = service.modifyUserReport(param);
		
		if(updateResult > 0) {
			model.addAttribute("msg", "신고사항 조치를 완료하였습니다.");
			model.addAttribute("targetURL", "AdmMemberReportList");
			return "result/success";
		} else {
			model.addAttribute("msg", "신고사항 처리에 실패하였습니다.");
			return "result/fail";
		}
	}
	
	// 신고 채팅방 목록 페이지 포워딩
	@GetMapping("AdmReportedChatHistory")
	public String admReportedChatHistory(String room_id, Model model, HttpSession session) {
		System.out.println("신고 채팅방ID : " + room_id);
		Map<String, Object> chatDetail = service.getChatDetail(room_id);
		List<Map<String, Object>> chatHistory = service.getReportedChatHistory(room_id);
		
		model.addAttribute("chatDetail", chatDetail);
		model.addAttribute("chatHistory", chatHistory);
		
		return "admin/reported_chat_history";
	}
	// ======================================================
	// 공지사항 관리
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@GetMapping("AdmNoticeList")
	public String admNoticeListForm() {
		return "admin/notice_list";
	}
	
	// ======================================================
	// [ 고객지원 관리 ]
	// - 공지사항 관리	
	@ResponseBody
	@PostMapping("AdmNoticeList")
	public String admNoticeList(@RequestParam Map<String, String> param) {
		Map<String, Object> convertParam = convertMap(param);
		
		// 공지사항 목록 전체 컬럼 수 조회
		int recordsTotal = service.getNoticeListTotal();
		
		// 공지사항 검색 필터링 후 컬럼 수 조회
		int recordsFiltered = service.getNoticeListFiltered(convertParam);
		
		// 필터링 된 공지사항 목록 가져오기
		List<NoticeVO> noticeList = service.getNoticeList(convertParam);
//		log.info(">>>>> 필터링 된 공지사항 게시글 목록 : " + noticeList);
		
		// 데이터를 map 객체에 담아서 JSON 객체로 변환하여 전달
		Map<String, Object> response = new HashMap<String, Object>();
		
		// draw, recordsTotal, recordsFiltered 값을 돌려주어야 서버사이드 페이징 작동함
		response.put("draw", convertParam.get("draw")); // 받은 draw 값 그대로 다시 전달(보안)
		response.put("recordsTotal", recordsTotal); // 전체 컬럼 수
		response.put("recordsFiltered", recordsFiltered); // 검색 필터링 후 컬럼 수
		response.put("noticeList", noticeList); // 컬럼 데이터
		
		JSONObject jo = new JSONObject(response);
		
		return jo.toString();
	}
	
	// 공지사항 여러행 삭제
	@ResponseBody
	@PostMapping("AdmNoticeDelete")
	public Map<String, Object> admNoticeDelete(@RequestBody List<Integer> deleteItems, HttpSession session) {
		log.info(">>>>>> 삭제할 공지사항 번호 : " + deleteItems);
		Map<String, Object> response = new HashMap<String, Object>();
		
//		notice_id 로 DB에서 공지사항 제목, 내용 등 가져오기
		List<NoticeVO> noticeFileList = service.getNoticeBoardFileList(deleteItems);
		log.info(">>>>>> 삭제할 공지사항 목록 : " + noticeFileList);
		
		if (noticeFileList == null) {	// 게시글이 존재하지 않을 경우
			response.put("status", "fail");
			response.put("message", "게시글을 선택한 후 시도해주세요.");
			return response;
		}
		
		int deleteResult = service.removeNotice(deleteItems);
		
		if (deleteResult > 0) {	
			// 게시글 삭제 성공 시 첨부파일 제거 작업
			String realPath = getRealPath(session);
			for(NoticeVO item : noticeFileList) {
				Path path = Paths.get(realPath, item.getNotice_file());
				try {
					Files.delete(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// 뷰페이지 처리
			response.put("status", "success");
			response.put("message", "선택한 게시글이 삭제되었습니다.");
			response.put("redirectURL", "/AdmNoticeList");
		} else {	
			response.put("status", "fail");
			response.put("message", "게시글 삭제에 실패했습니다. 다시 시도해주세요.");
		}
		
		return response;
	}
	
	
	// ======================================================
	// [ 고객지원 관리 ]
	// 1:1문의 목록 - 포워딩
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@GetMapping("AdmSupportList")
	public String admEnquireList() {
		return "admin/support_list";
	}
	
	// 1:1 문의 목록
	@ResponseBody
	@PostMapping("AdmSupportList")
	public String admSupportList(@RequestParam Map<String, String> param) {
//		log.info(">>>> 문의 내역 목록 param : " + param);
		Map<String, Object> convertParam = convertMap(param);
		
		// 1:1 문의 목록 전체 컬럼 수 조회
		int recordsTotal = service.getEnquireListTotal();
		
		// 1:1 문의 검색 필터링 후 컬럼 수 조회
		int recordsFiltered = service.getEnquireListFiltered(convertParam);
		
		// 필터링 된 1:1 문의 목록 가져오기
		List<Map<String, Object>> EnquireList = service.getEnquireList(convertParam);
		log.info(">>>>> 필터링 된 1:1 문의 목록 : " + EnquireList);
		
		
		// 첨부파일 정보 저장 (전체회원)
//		for(Map<String, Object> support : EnquireList) {
//			System.out.println("suport_file: " + support.get("SUPPORT_FILE"));
//			String originalFileName = "";
//			if(support.get("SUPPORT_FILE") != null) {
////				originalFileName = support.get("SUPPORT_FILE").substring(support.get("SUPPORT_FILE").indexOf("_") + 1);
//				originalFileName = support.get("SUPPORT_FILE").toString();
//			} else {
//				originalFileName = null;
//			}
//			support.put("originalFileName", originalFileName);
//		}
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("draw", convertParam.get("draw"));
		response.put("recordsTotal", recordsTotal);
		response.put("recordsFiltered", recordsFiltered);
		response.put("EnquireList", EnquireList);
		
		JSONObject jo = new JSONObject(response);
		return jo.toString();
	}
	
	//----------------------------------------------------------------------------------------
	// 1:1 문의 - 답글달기(+ 수정하기)
	@AdminLog
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@PostMapping("AdmSupportAction")
	public String admSupportAction(@RequestParam Map<String, Object> param,  Model model) {
		log.info(">>> 답글 정보 : " + param);
		
		int updateResult = service.registReplyInfo(param);
		log.info(">>> 업데이트 갯수 : " + updateResult);
		
		if(updateResult > 0) {
			model.addAttribute("msg", "1:1 문의 답글 처리를 완료하였습니다.");
			model.addAttribute("targetURL", "AdmSupportList");
			return "result/success";
		} else {
			model.addAttribute("msg", "1:1 문의 답글 처리에 실패하였습니다.");
			return "result/fail";
		}
	}

	// ======================================================
	// [ 고객지원 관리 ]
	// - FAQ 관리
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@GetMapping("AdmFaqList")
	public String admFaqList() {
		return "admin/faq_list";
	}
	
	// [ FAQ 목록 조회 ]
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@ResponseBody
	@PostMapping("FaqListForm")
	public String admFaqListForm(@RequestParam Map<String, String> param) {
		Map<String, Object> convertParam = convertMap(param);
		
		// FAQ 전체 컬럼 수 조회
		int recordsTotal = service.getFaqTotal();
		
		// FAQ 검색 필터링 후 컬럼 수 조회
		// => 파라미터 : FAQ유형, 사용여부, 검색어
		int recordsFiltered = service.getFaqFiltered(convertParam); // faqCate, listStatus, searchValue
		
		// FAQ 전체 목록 조회
		List<Map<String, Object>> faqList = service.getFaqList(convertParam); // start, length, searchValue, faqCate, listStatus, orderColumn, orderDir
		System.out.println("faqList:" + faqList);
		
		// 데이터를 map 객체에 담아서 JSON 객체로 변환하여 전달
		Map<String, Object> response = new HashMap<String, Object>();
		
		// draw, recordsTotal, recordsFiltered 값을 돌려주어야 서버사이드 페이징 작동함
		response.put("draw", convertParam.get("draw")); // 받은 draw 값 그대로 다시 전달(보안)
		response.put("recordsTotal", recordsTotal); // 전체 컬럼 수
		response.put("recordsFiltered", recordsFiltered); // 검색 필터링 후 컬럼 수
		response.put("faqList", faqList); // 컬럼 데이터
		System.out.println("Map: "+ response); 
		
		JSONObject jo = new JSONObject(response);
		return jo.toString();
	}
	//-------------------------------------------------------------------------------------
	// [ FAQ 수정 ]
	@AdminLog
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@PostMapping("AdmFaqModify")
	public String admFaqModify(@RequestParam Map<String, Object> param, Model model) {
		log.info(">>> 수정할 faq 정보: " + param);
		int updateResult = service.modifyFaqInfo(param);
		
		if(updateResult > 0) {
			model.addAttribute("msg", "FAQ 수정하였습니다.");
			model.addAttribute("targetURL", "AdmFaqList");
			return "result/success";
		} else {
			model.addAttribute("msg", "FAQ 수정에 실패하였습니다.");
			return "result/fail";
		}
	}
	
	//-------------------------------------------------------------------------------------
	// [ FAQ 삭제 ]
	@AdminLog
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@ResponseBody
	@PostMapping("AdmFaqDelete")
	public Map<String, Object> admFaqDelete(@RequestBody List<Integer> faqIds) {
		log.info(">>>>>>> 전달받은 삭제할 faq_id(복수개): " + faqIds);
		
		int deleteResult = service.removeFaq(faqIds);
		
		Map<String, Object> response = new HashMap<> ();
		
		if(deleteResult > 0) {
			response.put("status", "success");
			response.put("message", "선택한 항목들이 삭제되었습니다.");
			response.put("redirectURL", "/AdmFaqList");
		} else {
			response.put("status", "fail");
			response.put("message", "항목 삭제에 실패했습니다.");
		}
		
		return response;
	}
	
	
	// ======================================================
	// [ 통계 ]
	// 통계 차트 페이지
	@GetMapping("AdmChartList")
	public String admChartList() {
		return "admin/chart_list";
	}
	
	
	// ======================================================
	// [ 로그 ]
	// 로그 기록 페이지 포워딩
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@GetMapping("AdmLogList")
	public String admLogListForm() {
		return "admin/log_list";
	}
	
	// 로그 기록 조회
	@LoginCheck(memberRole = MemberRole.ADMIN)
	@ResponseBody
	@PostMapping("AdmLogList")
	public String admLogList(@RequestParam Map<String, String> param) {
		Map<String, Object> convertParam = convertMap(param);
		
		int recordsTotal = service.getLogListTotal();
		int recordsFiltered = service.getLogListFiltered(convertParam);
		
		List<MemberVO> logList = service.getLogList(convertParam);
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("draw", convertParam.get("draw")); // 받은 draw 값 그대로 다시 전달(보안)
		response.put("recordsTotal", recordsTotal); // 전체 컬럼 수
		response.put("recordsFiltered", recordsFiltered); // 검색 필터링 후 컬럼 수
		response.put("logList", logList); // 컬럼 데이터
		
		JSONObject jo = new JSONObject(response);
		
		return jo.toString();
	}
	
	
	// ======================================================
	// ======================================================
	// ======================================================
	//	실제 업로드 경로 메서드
	public String getRealPath(HttpSession session) {
		String realPath = session.getServletContext().getRealPath(uploadPath);
		return realPath;
	}
	// Map 형변환 처리 메서드
	private Map<String, Object> convertMap(Map<String, String> param) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// int 변환
		for(Entry<String, String> entry : param.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			
			if(intColumns.contains(entry.getKey())) {
				value = Integer.parseInt((String)value);
			}
			
			resultMap.put(key, value);
		}
		
		// 정렬 컬럼 map에 추가 (orderable)
		// 넘겨받은 데이터 : columns[2][data]=mem_name, order[0][column]=2, order[0][dir]=desc
		// 컬럼명 추출하려면 columns[order[0][column]][data] 형태로 만들어줘야 함
		int orderColumnKey = Integer.parseInt((String)param.get("order[0][column]"));
		String orderColumn = param.get("columns[" + orderColumnKey + "][data]").toString();
		String orderDir = param.get("order[0][dir]").toString();
		
		resultMap.put("orderColumn", orderColumn);
		resultMap.put("orderDir", orderDir);
		System.out.println("변환된 map : " + resultMap);
		
		return resultMap;
	}
	
	// 회원상태 문자열로 변환
	private String memberStatusToString(int status) {
		switch(status) {
			case 1 : return "정상";
			case 2 : return "정지";
			case 3 : return "탈퇴";
			default: return "정상";
		}
	}
	
	// SNS 연동상태 문자열로 변환
	private String snsStatusToString(int status) {
		switch(status) {
			case 1 : return "연동완료";
			case 2 : return "-";
			default: return "-";
		}
	}
	
	// 회원인증상태 문자열로 변환
	private String memberAuthToString(int status) {
		switch(status) {
			case 1 : return "인증완료";
			case 2 : return "-";
			default: return "-";
		}
	}
	
}
