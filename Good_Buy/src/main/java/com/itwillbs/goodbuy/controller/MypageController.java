package com.itwillbs.goodbuy.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.goodbuy.aop.LoginCheck;
import com.itwillbs.goodbuy.aop.LoginCheck.MemberRole;
import com.itwillbs.goodbuy.service.MemberService;
import com.itwillbs.goodbuy.service.MyPageService;
import com.itwillbs.goodbuy.vo.MemberVO;
import com.itwillbs.goodbuy.vo.MyPageVO;
import com.itwillbs.goodbuy.vo.WishlistVO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class MypageController {
	@Autowired MemberService memberService;
	@Autowired MyPageService myPageService;
	
//	[회원정보 수정]
//	@LoginCheck(memberRole = MemberRole.USER)
//	@GetMapping("MyInfo")
//	public String myInfo() {
//		return "mypage/mypage_info";
//		
//		
//	}
//	@LoginCheck(memberRole = MemberRole.USER)
//	@PostMapping("MyInfo")
//	public String myInfoForm(MemberVO member,HttpSession session , Model model) {
//		MemberVO memInfo = memberService.getMember(member);
////		System.out.println("회원정보"+ memInfo);
//		
//		return "mypage/mypage_info";
//	}
	
	
	@GetMapping("MyStore")
	public String myStore(MemberVO member,HttpSession session,Model model) {
		return "mypage/mypage_store";
	}
	
	@PostMapping("MyStore")
	public String myStoreIntro(Model model, HttpSession session, MemberVO member,HttpServletRequest request) {
		savePreviousUrl(request, session);
		
		String id = (String) session.getAttribute("sId");
	    member.setMem_id(id);  // 사용자 ID 설정
	    
	    int storeIntroCount = memberService.registStoreIntro(member);  // MemberVO 전달
	    
	    if (storeIntroCount > 0) {
	    	model.addAttribute("member", member);
	    	model.addAttribute("msg", "상점소개가 변경되었습니다.");
	    	System.out.println(member.getMem_intro());
	    	
	        return "result/success";
	    } else {
	        model.addAttribute("msg", "상점소개 변경 실패!");
	        return "result/fail";
	    }
	}
	
	@GetMapping("MyOrder")
	public String myOrder() {
		return "mypage/mypage_product_orders";
	}
	
	@GetMapping("MySales")
	public String mySale() {
		return "mypage/mypage_product_sales";
	}
	
	@GetMapping("MyReview")
	public String myReview() {
		return "mypage/mypage_review";
	}

	@GetMapping("MyWish")
	public String myWish(HttpSession session, Model model, HttpServletRequest request) {
	    savePreviousUrl(request, session);

	    // 세션에서 사용자 ID 가져오기
	    String id = (String) session.getAttribute("sId");
	    System.out.println("세션 ID: " + id);

	    // 위시리스트 조회
	    List<WishlistVO> wishlist = myPageService.getWishlist(id);
	    System.out.println("위시리스트: " + wishlist);
	    model.addAttribute("wishlist", wishlist);

	    // 위시리스트 개수 조회 => 수정중 
	    System.out.println("id???????????????"+id); //여기부터 안불러옴
	    int wishlistCount = myPageService.wishlistCount(id);
	    
	    model.addAttribute("wishlistCount", wishlistCount);
	    System.out.println("위시리스트 갯수: " + wishlistCount);

	    return "mypage/mypage_wishlist";
	}
	
	
	//[관심목록 추가]
	@GetMapping("MyWishAdd")
	public String myWishAdd(WishlistVO wishlist,HttpServletRequest request,HttpSession session ,Model model) {
		String id = (String)session.getAttribute("sId");
		wishlist.setMem_id(id);
		
		int insertCount = myPageService.addWishlist(wishlist);
		if(insertCount > 0) {
			
		}else {
			model.addAttribute("msg", "위시리스트 등록 실패!");
			return "result/fail";
		}
		
		return "";
	}
	
	//[관심목록 삭제]
	@PostMapping("MyWishDel")
	public String myWishDel(String wishlist_id,HttpServletRequest request,HttpSession session,Model model){
		System.out.println("위시리스트 아이디 : "+wishlist_id);
		
		int deleteCount = myPageService.cancleMyWish(wishlist_id);
		
		if(deleteCount > 0) {
			if(session.getAttribute("prevURL") == null) {
				return "redirect:/";
			} else {
				return "redirect:" + session.getAttribute("prevURL");
			}
		} else {
			model.addAttribute("msg", "삭제 실패!");
			return "result/fail";
		}
	}
	
	@GetMapping("MemberWithdraw")
	public String memberWithdraw() {
		return "mypage/mypage_withdraw";
	}
	
	@PostMapping("MemberWithdraw")
	public String memberWithdrawForm (MemberVO  member,HttpSession session , Model model ,BCryptPasswordEncoder passwordEncoder,String memPasswd) {
		String id = (String)session.getAttribute("sId");
		String dbPasswd = memberService.getMemberPasswd(id);
		if(dbPasswd == null || !passwordEncoder.matches(memPasswd, dbPasswd)) {
			model.addAttribute("msg", "권한이 없습니다.//n비밀번호를 다시 확인해주세요.");
			return "result/fail";
		}
		
		
		return"";
	}
	
	
	// ===========================================================================================
		// 이전 페이지 이동 저장
		private void savePreviousUrl(HttpServletRequest request, HttpSession session) {
			String prevURL = request.getServletPath();
			String queryString = request.getQueryString();
//			System.out.println("prevURL : " + prevURL);
//			System.out.println("queryString : " + queryString);
			
			if (queryString != null) {
				prevURL += "?" + queryString;
			}
			
			session.setAttribute("prevURL", prevURL);
		}

//	@GetMapping("MyInfo")
//	public String myInfo() {
//		return "mypage/mypage_info";
//	}
//	
//	@GetMapping("MyStore")
//	public String myStore() {
//		return "mypage/mypage_store";
//	}
//
//	@GetMapping("MyOrder")
//	public String myOrder() {
//		return "mypage/mypage_product_orders";
//	}
//
//	@GetMapping("MySales")
//	public String mySale() {
//		return "mypage/mypage_product_sales";
//	}
//	@GetMapping("MyReview")
//	public String myReview() {
//		return "mypage/mypage_review";
//	}
//	@GetMapping("MyWish")
//	public String myWish() {
//		return "mypage/mypage_wishlist";
//	}

}
