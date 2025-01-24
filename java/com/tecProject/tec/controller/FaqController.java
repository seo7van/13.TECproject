package com.tecProject.tec.controller;

import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RestController;

import com.tecProject.tec.domain.Faq;
import com.tecProject.tec.service.FaqService;

@RestController
@RequestMapping("/Admin/faqs/")

public class FaqController {
	
	private final FaqService faqService;
	
    public FaqController(FaqService faqService) {
        this.faqService = faqService;    
    }
    
    // 모든 FAQ 가져오기
    @GetMapping
    public ResponseEntity<List<Faq>> getAllFaqs() {
        List<Faq> faqs = faqService.getAllFaqs();
        return ResponseEntity.ok(faqs);
    }
    
    // 특정 FAQ 가져오기
    @GetMapping("/{id}")
    public ResponseEntity<Faq> getFaqById(@PathVariable Long id) {
        Optional<Faq> faq = faqService.findFaqById(id); // 수정된 findById 호출
        return faq.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
	 // 새로운 FAQ 추가 (관리자 권한 확인)
	 // 관리자 여부 확인 API
	 @GetMapping("/check-admin")
	 public ResponseEntity<?> checkAdmin() {
	     try {
	         // 현재 인증된 사용자 정보 가져오기
	         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	
	         if (authentication == null || !authentication.isAuthenticated()) {
	             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
	         }
	
	         // ROLE_ADMIN 확인
	         boolean isAdmin = authentication.getAuthorities().stream()
	             .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
	
	         return ResponseEntity.ok(Map.of("isAdmin", isAdmin));
	     } catch (Exception e) {
	         // 예외 처리
	         return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한 확인 실패: " + e.getMessage());
	     }
	 }


    // FAQ 수정
    @PutMapping("/{id}")
    public ResponseEntity<Faq> updateFaq(@PathVariable Long id, @RequestBody Faq faq) {
        Optional<Faq> updatedFaq = faqService.editFaq(id, faq);
        return updatedFaq.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
    
    // FAQ 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFaq(@PathVariable Long id) {
        Optional<Faq> deletedFaq = faqService.deleteFaq(id);
        if (deletedFaq.isPresent()) {
            return ResponseEntity.ok("FAQ 삭제 성공: ID = " + id);
        } else {
            return ResponseEntity.status(404).body("FAQ를 찾을 수 없습니다: ID = " + id);
        }
    }
}