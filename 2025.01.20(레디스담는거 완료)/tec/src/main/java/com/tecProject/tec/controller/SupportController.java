package com.tecProject.tec.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecProject.tec.domain.UserSupport;
import com.tecProject.tec.service.SupportService;

@RestController
@RequestMapping("/api/user-support")
public class SupportController {

	private final SupportService supportService;
	
    // 서비스 사용 준비
    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }
    
    // 문의 등록(사용자)
    @PostMapping
    private ResponseEntity<?> createInquiry(@RequestBody UserSupport userSupport) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 이용 가능합니다.");
        }
        String username = authentication.getName(); // JWT에서 username 추출
        userSupport.setUsername(username); // 사용자 ID 설정
        UserSupport savedInquiry = supportService.createInquiry(userSupport);

        return ResponseEntity.ok(savedInquiry);
    }
    
    // 문의 수정(사용자)
    @PutMapping("/{inquiryNo}")
    private ResponseEntity<?> updateInquiry(@PathVariable("inquiryNo") int inquiryNo,
                                            @RequestBody UserSupport updatedInquiry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 이용 가능합니다.");
        }

        String username = authentication.getName(); // JWT에서 username 추출

        // 서비스 호출하여 문의 수정
        Optional<UserSupport> inquiry = supportService.updateInquiry(inquiryNo, username, updatedInquiry.getTitle(), updatedInquiry.getContent());
        if (inquiry.isPresent()) {
            return ResponseEntity.ok(inquiry.get());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("문의 수정 권한이 없습니다.");
        }
    }
    
    // 문의 삭제(사용자)
    @DeleteMapping("/{inquiryNo}")
    private ResponseEntity<?> deleteInquiry(@PathVariable("inquiryNo") int inquiryNo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 이용 가능합니다.");
        }

        String username = authentication.getName(); // JWT에서 username 추출
        Optional<UserSupport> deletedInquiry = supportService.deleteInquiry(inquiryNo, username);

        if (deletedInquiry.isPresent()) {
            return ResponseEntity.ok("문의가 성공적으로 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("문의 삭제 권한이 없습니다.");
        }
    }
    
    // 문의내역 전체조회(사용자)
    @GetMapping
    private ResponseEntity<?> getInquiryByUsername() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (authentication == null || !authentication.isAuthenticated()) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 이용 가능합니다.");
    	}
    	
    	String username = authentication.getName(); // JWT에서 username 추출
    	List<UserSupport> inquiries = supportService.getInquiryByUsername(username);
    	
    	if (inquiries.isEmpty()) {
    		return ResponseEntity.ok("문의 내역이 없습니다.");
    	}
    	return ResponseEntity.ok(inquiries);
    }
    
	// 문의내역 키워드 조회(사용자)
    @GetMapping("/search")
    public ResponseEntity<?> getInquiriesByKeyword(@RequestParam(value = "keyword", required = false) String keyword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 이용 가능합니다.");
        }

        String username = authentication.getName(); // JWT에서 username 추출
        List<UserSupport> inquiries = supportService.getInquiriesByKeyword(username, keyword);

        return ResponseEntity.ok(inquiries);
    }
    
    // 문의내역 상세조회(사용자)
    @GetMapping("/{inquiryNo}")
    private ResponseEntity<?> getInquiryById(@PathVariable("inquiryNo") int inquiryNo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 이용 가능합니다.");
        }

        String username = authentication.getName(); // JWT에서 username 추출
        Optional<UserSupport> inquiry = supportService.getInquiryById(inquiryNo, username);

        if (inquiry.isPresent()) {
            return ResponseEntity.ok(inquiry.get());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("문의 상세 정보를 조회할 수 없습니다.");
        }
    }
}
