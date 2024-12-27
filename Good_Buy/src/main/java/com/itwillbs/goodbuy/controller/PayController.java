package com.itwillbs.goodbuy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.goodbuy.aop.LoginCheck;
import com.itwillbs.goodbuy.aop.LoginCheck.MemberRole;
import com.itwillbs.goodbuy.aop.PayTokenCheck;
import com.itwillbs.goodbuy.service.PayService;
import com.itwillbs.goodbuy.vo.PayToken;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class PayController {

	@Autowired
	PayService service;
	
	@LoginCheck(memberRole = MemberRole.USER)
	@GetMapping("GoodPay")
	public String goodPay(HttpSession session, Model model) {
		PayToken token = (PayToken)session.getAttribute("token");
//		Map<String, Object> bankUserInfo = service.getPayUserInfo(token);
//		String fintech_use_num = service.getRepresentAccountNum(token.getUser_seq_no());
//		
//		model.addAttribute("bankUserInfo", bankUserInfo);
//		model.addAttribute("fintech_use_num", fintech_use_num);
		
		return "pay/pay_list";
	}
	@LoginCheck(memberRole = MemberRole.USER)
	@GetMapping("MyAccount")
	public String myAccount(HttpSession session, Model model) {
		// 엑세스토큰 관련 정보가 저장된 BankToken 객체(token)를 세션에서 꺼내기
		PayToken token = (PayToken)session.getAttribute("token");
		
		/*
		if(token == null || token.getAccess_token() == null) {
			model.addAttribute("msg", "계좌 인증 필수!");
			model.addAttribute("targetURL", "MyAccount");
			return "result/fail";
		}
		*/
		// PayService - getBankUserInfo() 메서드 호출하여 핀테크 사용자 정보 조회
		Map<String, Object> bankUserInfo = service.getPayUserInfo(token);
		log.info(">>>>> 핀테크 사용자 정보 : " + bankUserInfo);
		
		Object resListObj = bankUserInfo.get("res_list");
		List<Map<String, Object>> resList = (List<Map<String, Object>>) resListObj;
		List<String> fintech_use_nums = resList.stream()
                							 .map(map -> (String) map.get("fintech_use_num"))
                							 .collect(Collectors.toList());

		String fintech_use_num = service.getRepresentAccountNum(token.getUser_seq_no());
		
		// API 응답코드(rsp_code)가 "A0000" 이 아닐 경우 요청 처리 실패
		if(!bankUserInfo.get("rsp_code").equals("A0000")) {
			model.addAttribute("msg", bankUserInfo.get("rsp_message"));
			return "result/fail";
		}
		
		model.addAttribute("fintech_use_num", fintech_use_num);
		model.addAttribute("bankUserInfo", bankUserInfo);
		return "pay/my_account";
	}
	
	// 사용자 인증 요청에 대한 콜백 처리
	@LoginCheck(memberRole = MemberRole.USER)
	@GetMapping("Callback")
	public String callback(@RequestParam Map<String, String> authResponse, HttpSession session, Model model) {
		// System.out.println("callback 잘되나? " + authResponse); 
		
		// 임시) 메인페이지에서 엑세스토큰 요청을 별도로 수행하기 위해 세션에 인증코드 저장		
		session.setAttribute("code", authResponse.get("code"));
		
		// 토큰발급 API - 사용자 토큰발급 API (3-legged) 요청
		PayToken token = service.getAccessToken(authResponse);
		log.info(authResponse + ">>>>>> 엑세스토큰 정보 : " + token);
		
		// 요청 결과 판별
		if(token == null || token.getAccess_token() == null) {
			model.addAttribute("msg", "토큰 발급 실패! 재인증 필요!");
			// 인증 화면이 새 창에 표시되어 있으며, 해당 창 닫기 위해 "isClose" 속성값에 true 저장
			model.addAttribute("isClose", true);
			return "result/fail";
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", session.getAttribute("sId"));
		map.put("token", token);
		service.registAccessToken(map);
		
		// 세션에 엑세스토큰 관리 객체(BankToken) 객체 저장
		session.setAttribute("token", token);
		model.addAttribute("msg", "계좌 연결 완료!");
//				model.addAttribute("targetURL", "BankMain");
		model.addAttribute("isClose", true);
		return "result/success";
	}
	
	// 2.2. 사용자/서비스 관리 - 2.2.1. 사용자정보조회 API (GET)
	@LoginCheck(memberRole = MemberRole.USER)
	@GetMapping("BankUserInfo")
	public String bankUserInfo(HttpSession session, Model model) {
		// 엑세스토큰 관련 정보가 저장된 BankToken 객체(token)를 세션에서 꺼내기
		PayToken token = (PayToken)session.getAttribute("token");
		System.out.println("토큰 정보 : " + token);
		
		if(token == null || token.getAccess_token() == null) {
			model.addAttribute("msg", "계좌 인증 필수!");
			model.addAttribute("targetURL", "BankMain");
			return "result/fail";
		}
		
		// BankService - getBankUserInfo() 메서드 호출하여 핀테크 사용자 정보 조회
		Map<String, Object> bankUserInfo = service.getPayUserInfo(token);
		log.info(">>>>> 핀테크 사용자 정보 : " + bankUserInfo);
		
		// API 응답코드(rsp_code)가 "A0000" 이 아닐 경우 요청 처리 실패
		if(!bankUserInfo.get("rsp_code").equals("A0000")) {
			model.addAttribute("msg", bankUserInfo.get("rsp_message"));
			return "result/fail";
		}

		model.addAttribute("bankUserInfo", bankUserInfo);
		return "pay/my_account";
	}
	
	// 2.3. 계좌조회 서비스(사용자) - 2.3.1. 잔액조회 API 서비스 요청 (POST)
	@LoginCheck(memberRole = MemberRole.USER)
	@PayTokenCheck // 엑세스토큰 존재 여부 체크하는 사용자 어노테이션
	@PostMapping("PayAccountDetail")
	public String payAccountDetail(@RequestParam Map<String, Object> map, HttpSession session, Model model) {
		log.info(">>>>>>>>>>>>>>>계좌 잔액조회 요청 파라미터 : " + map);
		
		
		// 엑세스토큰 정보가 저장된 BankToken 객체를 세션에서 꺼내기
		PayToken token = (PayToken)session.getAttribute("token");
		// => @PayTokenCheck 어노테이션으로 사전 검사 완료됐기 때문에 별도의 체크 불필요
		// ------------------------------------------------------------------------------
		map.put("token", token); // Map<String, Object> 필요
			
		// PayService - getAccountDetail() 메서드 호출하여 핀테크 계좌 잔액조회 요청
		Map<String, String> accountDetail = service.getAccountDetail(map);
			
		// ----------------------------------------------------------------------
		// API 응답코드(rsp_code)가 "A0000" 이 아닐 경우 요청 처리 실패이므로
		// (단, 이체과정에서는 "A0001"(이체처리중) 이 응답으로 전송될 수 있음)
		// API 응답메세지(rsp_message) 를 Model 객체에 저장 후 fail.jsp 포워딩
		if(!accountDetail.get("rsp_code").equals("A0000")) {
			model.addAttribute("msg", accountDetail.get("rsp_message"));
			return "result/fail";
		}
		// ----------------------------------------------------------------------
		// Model 객체에 응답데이터가 저장된 Map 객체(accountDetail)와
		// 요청 파라미터로 전달받은(Map 객체 - map) 예금주명, 계좌번호(마스킹) 파라미터로 저장
		model.addAttribute("accountDetail", accountDetail);
		model.addAttribute("account_holder_name", map.get("account_holder_name"));
		model.addAttribute("account_num_masked", map.get("account_num_masked"));
		
//		return "redirect:/pay_account_detail";
		return "pay/pay_account_detail";
	}
	
	// 사용자 조회 화면에서 계좌 목록 중 대표계좌로설정 버튼 클릭 시 해당 정보를 DB 에 등록 요청
	@LoginCheck(memberRole = MemberRole.USER)
	@PayTokenCheck
	@PostMapping("PayRegistRepresentAccount")
	public String payRegistRepresentAccount(@RequestParam Map<String, Object> map, Model model, HttpSession session) {
		// 세션 "token" 속성에서 PayToken 객체에 저장된사용자번호 (user_seq_no) 꺼낸 후
		// Map 객체에 추가
		PayToken token = (PayToken)session.getAttribute("token");
		map.put("token", token); // Map<String, Object> 필요
		
		log.info(">>>>>>>>>> 대표계좌정보 : " + map);
		
		// BankService - registRepresentAccount() 메서드 호출하여 대표계좌정보 DB 등록요청
		// => 파라미터 : Map 객체  , 리턴타입 : int(count)
		int count = service.registRepresentAccount(map);
		
		
		if(count > 0) {
//			System.out.println("대표계좌  조회 되나? " + map);
			return "redirect:/MyAccount";
		} else {
			model.addAttribute("msg","대표계좌 등록실패\\n관리자에게 문의하시오");
			return "result/fail";
		}
	}
	@LoginCheck(memberRole = MemberRole.USER)
	@PayTokenCheck
	@PostMapping("PayWithdraw")
	public String payWithdraw(@RequestParam Map<String, Object> map, HttpSession session, Model model) {
		// 엑세스토큰 정보가 저장된 BankToken 객체를 세션에서 꺼내기
		PayToken token = (PayToken)session.getAttribute("token");

		map.put("token", token); // Map<String, Object> 필요
		map.put("id", session.getAttribute("sId"));
		log.info(">>>>>>>> 출금 이체 요청 파라미터 정보 : " + map);
			
		// BankService - requestWithdraw() 메서드 호출하여 출금이체 요청
		Map<String, String> withdrawResult = service.requestWithdraw(map);
		log.info(">>>>>>>>>>>>>출금 이체 요청 결과 : " + withdrawResult);
			
		if(!withdrawResult.get("rsp_code").equals("A0000") || !withdrawResult.get("bank_rsp_code").equals("000")) { // 출금실패
			model.addAttribute("msg", "출금실패 : " + withdrawResult.get("rsp_message"));
			return "result/fail";
		}
		
		// 사용자 번호를 출금이체 결과 객체에 추가 
		withdrawResult.put("user_seq_no", token.getUser_seq_no());
		
//		// 임시) withdrawResult 객체의 api_tran_dtm 속성값(문자열) 뒷자리 3자리 (밀리초)제거
		withdrawResult.put("api_tran_dtm", withdrawResult.get("api_tran_dtm").substring(0, withdrawResult.get("api_tran_dtm").length() - 3));
		
		// 출금이체 성공 시 결과를 DB에 저장
		 service.registWithdrawResult(withdrawResult);
		
//		// 출금이체 결과 정보 Map 객체 중 api_tran_id 값을 Model에 저장
		model.addAttribute("bank_tran_id", withdrawResult.get("bank_tran_id"));
		
		return "redirect:/PayWithdrawResult";
	}
	
	@LoginCheck(memberRole = MemberRole.USER)
	@GetMapping("PayWithdrawResult")
	public String payWithdrawResult(String bank_tran_id, Model model) {
		Map<String, String> withdrawResult = service.getWithdarwResult(bank_tran_id);
		model.addAttribute("withdrawResult", withdrawResult);

		return "pay/pay_withdraw_result";
	}
	
	
	// 입금이체(= 기관출금)
	@LoginCheck(memberRole = MemberRole.USER)
	@PayTokenCheck
	@PostMapping("PayDeposit")
	public String PayDeposit(@RequestParam Map<String, Object> map, HttpSession session, Model model) {
		PayToken token = (PayToken)session.getAttribute("token");

		map.put("id", session.getAttribute("sId"));
		log.info(">>>>>>>> 입금 이체 요청 파라미터 정보 : " + map);
		
		// PayService - requestDeposit() 메서드 호출하여 입금이체 요청
		Map<String, Object> depositResult = service.requestDeposit(map);
		
		log.info(">>>>>>>> 입금이체 요청 결과 : " + depositResult);
		
		List<Map<String, String>> res_list = (List<Map<String, String>>) depositResult.get("res_list");
		
		if(!depositResult.get("rsp_code").equals("A0000") || !res_list.get(0).get("bank_rsp_code").equals("000")) {
			model.addAttribute("msg", "입금 실패! - " + depositResult.get("rsp_message"));
			return "result/fail";
		}
				
		// 사용자번호를 입금이체 결과 객체에 추가
		depositResult.put("user_seq_no", token.getUser_seq_no());
				
		// 출금이체 성공 시 결과를 DB 에 저장
		service.registDepositResult(depositResult);
				
		// 출금이체 결과 정보 Map 객체 중 bank_tran_id 값을 Model 에 저장
		model.addAttribute("bank_tran_id", res_list.get(0).get("bank_tran_id"));
		
		return "redirect:/PayDepositResult";
	}
	
	// 입금이체 결과
	@LoginCheck(memberRole = MemberRole.USER)
	@PayTokenCheck
	@GetMapping("PayDepositResult")
	public String payDepositResult(String bank_tran_id, Model model) {
		
		Map<String, String> depositResult = service.getDepositResult(bank_tran_id);
		model.addAttribute("depositResult", depositResult);
		

		return "pay/pay_deposit_result";
	}
}
