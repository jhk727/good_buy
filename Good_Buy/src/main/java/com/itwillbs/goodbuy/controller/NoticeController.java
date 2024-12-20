package com.itwillbs.goodbuy.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.goodbuy.service.NoticeService;
import com.itwillbs.goodbuy.vo.NoticeVO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class NoticeController {
	@Autowired
	private NoticeService service;
	
	String uploadPath = "/resources/upload";
	
	//	글쓰기 폼 이동
	@GetMapping("NoticeWrite")
	public String noticeWriteForm() {
		return "notice/notice_write";
	}
	
	//	글쓰기 비즈니스 로직
	@PostMapping("NoticeWrite")
	public String noticeWrite(NoticeVO notice, HttpSession session) {
		//	실제 경로
		String realPath = getRealPath(session);
		//	서브 디렉토리 생성
		String subDir = createDirectories(realPath);
		realPath += "/" + subDir;
		String fileName = addFileProcess(notice, realPath, subDir);
		notice.setNotice_file(fileName);
		
		service.insertNotice(notice);
		
		return "redirect:/NoticeMain";
	}
	
	//	공지사항 메인페이지(리스트)
	@GetMapping("NoticeMain")
	public String noticeMain(Model model) {
		//	모든 공지사항 게시글 조회
		List<NoticeVO> noticeList = service.getNoticeList();
		model.addAttribute("noticeList", noticeList);
		
		return "notice/notice_list";
	}
	
	//	공지사항 글 상세보기
	@GetMapping("NoticeDetail")
	public String noticeDetail(int notice_id, Model model) {
		//	notice_id 로 DB에서 공지사항 제목, 내용 등 가져오기
		NoticeVO notice = service.getNoticeBoard(notice_id, true);
		//	파일 원본명 세팅
		String originalFile = notice.getNotice_file().substring(notice.getNotice_file().indexOf("_") + 1);
		
		model.addAttribute("notice", notice);
		model.addAttribute("originalFile", originalFile);
		
		return "notice/notice_detail";
	}
	
	//	공지사항 게시글 삭제
	@GetMapping("NoticeDelete")
	public String noitceDelete(int notice_id, Model model, HttpSession session) {
		//	notice_id 로 DB에서 공지사항 제목, 내용 등 가져오기
		NoticeVO notice = service.getNoticeBoard(notice_id, false);
		if (notice == null) {	// 게시글이 존재하지 않을 경우
			model.addAttribute("msg", "존재하지 않는 게시글입니다.");
			return "result/fail";
		}
		//	DB에서 게시글 삭제 작업
		int deleteCount = service.deleteNotice(notice_id);
		
//		게시글 삭제 성공 시 첨부파일 제거 작업
		if (deleteCount > 0) {	
			String realPath = getRealPath(session);
			Path path = Paths.get(realPath, notice.getNotice_file());
			try {
				Files.delete(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "redirect:/NoticeMain";
		} else {	//	실패 시
			model.addAttribute("msg", "삭제 실패!");
			return "result/fail";
		}
	}
	
	//	공지사항 수정폼 이동
	@GetMapping("NoticeModify")
	public String noticeModifyForm(int notice_id, Model model) {
		//	notice_id 로 DB에서 공지사항 제목, 내용 등 가져오기
		NoticeVO notice = service.getNoticeBoard(notice_id, false);
		//	파일 원본명 세팅
		String originalFile = notice.getNotice_file().substring(notice.getNotice_file().indexOf("_") + 1);
		
		model.addAttribute("notice", notice);
		model.addAttribute("originalFile", originalFile);
		
		return "notice/notice_modify";
	}
	
	//	공지사항 수정 비즈니스 로직
	@PostMapping("NoticeModify")
	public String noticeModify(NoticeVO notice, Model model, HttpSession session) {
		System.out.println(notice);
		
		if(!notice.getFile().equals("")) {
			String realPath = getRealPath(session);
			String subDir = createDirectories(realPath);
			realPath += "/" + subDir;
			String fileName = addFileProcess(notice, realPath, subDir);
			notice.setNotice_file(fileName);
		}
		
		int updateCount = service.updateNotice(notice);
		
		if(updateCount < 0) {
			model.addAttribute("msg", "글 수정에 실패했습니다.");
			return "result/fail";
		}
		
		return "redirect:/NoticeDetail?notice_id=" + notice.getNotice_id();
	}
	
	//	공지사항 수정폼에서 첨부파일 삭제(AJAX)
	@ResponseBody
	@PostMapping("NoticeDeleteFile")
	public String noticeDeleteFile(@RequestParam Map<String, String> map, HttpSession session) {
		System.out.println(map);
		String result = "false";
		
		int deleteCount = service.deleteNoticeFile(map);
		if (deleteCount > 0) {
			String realPath = session.getServletContext().getRealPath(uploadPath);
			if (!map.get("file").equals("")) {
				Path path = Paths.get(realPath, map.get("file"));
				try {
					Files.delete(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
				result = "true";
			}
		}
		
		return result;
	}
	
	
	
	//	============================================================================
	//	실제 업로드 경로 메서드
	public String getRealPath(HttpSession session) {
		String realPath = session.getServletContext().getRealPath(uploadPath);
		return realPath;
	}
	
	//	서브디렉토리 생성
	public String createDirectories(String realPath) {
		//	현재 시스템 날짜
		LocalDate today = LocalDate.now();
		//	날짜 포맷 패턴
		String datePattern = "yyyy/MM/dd";
		//	패턴 문자열 전달
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(datePattern);
		
		//	날짜 형식으로 경로 저장
		String subDir = today.format(dtf);
		//	기존 실제 업로드 경로
		realPath += "/" + subDir;
		//	실제 경로 전달
		Path path = Paths.get(realPath);
		
		try {
			//	실제 경로 생성
			Files.createDirectories(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return subDir;
	}
	
	public String addFileProcess(NoticeVO notice, String realPath, String subDir) {
		MultipartFile muti = notice.getFile();
		notice.setNotice_file("");
		String fileName = "";
		
		if(!muti.getOriginalFilename().equals("")) {
			String temp = UUID.randomUUID().toString().subSequence(0, 8) + "_" + muti.getOriginalFilename();
			try {
				muti.transferTo(new File(realPath, temp));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileName += subDir + "/" + temp;
		}
		
		return fileName;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

















