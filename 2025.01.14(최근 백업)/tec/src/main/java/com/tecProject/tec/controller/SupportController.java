package com.tecProject.tec.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
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
    private ResponseEntity<UserSupport> createInquiry(@RequestBody UserSupport userSupport) {
    	return ResponseEntity.ok(supportService.createInquiry(userSupport));
    }
    
    // 문의 수정(사용자)
    @PutMapping("/{inquiryNo}")
    private ResponseEntity<?> updateInquiry(@PathVariable("inquiryNo") int inquiryNo,
                                            @RequestBody UserSupport userSupport) {
        if (userSupport.getTitle() == null || userSupport.getTitle().trim().isEmpty() ||
            userSupport.getContent() == null || userSupport.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("제목과 내용을 모두 입력해주세요.");
        }
        Optional<UserSupport> updatedInquiry = supportService.updateInquiry(inquiryNo, userSupport.getTitle(), userSupport.getContent());
        return updatedInquiry.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    // 문의 삭제(사용자)
    @DeleteMapping("/{inquiryNo}")
    private ResponseEntity<?> deleteInquiry(@PathVariable("inquiryNo") int inquiryNo) {
    	Optional<UserSupport> deleteInquiry = supportService.deleteInquiry(inquiryNo);
    	return deleteInquiry.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    // 문의내역 전체조회(사용자)
    @GetMapping
    private ResponseEntity<?> getInquiryByUserId(@RequestParam("userId") String userId) {
    	if (userId == null || userId.trim().isEmpty()) {
    		return ResponseEntity.badRequest().body("ID를 입력하세요");
    	}
    	List<UserSupport> inquiries = supportService.getInquiryByUserId(userId);
    	return ResponseEntity.ok(inquiries);
    }
    
    // 문의내역 상세조회(사용자)
    @GetMapping("/{inquiryNo}")
    private ResponseEntity<?> getInquiryById(@PathVariable("inquiryNo") int inquiryNo) {
    	Optional<UserSupport> inquiry = supportService.getInquiryById(inquiryNo);
    	return inquiry.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
