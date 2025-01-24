package com.tecProject.tec.controller;

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

import com.tecProject.tec.domain.Faq;
import com.tecProject.tec.service.FaqService;

@RestController
@RequestMapping("/Admin/faqs")
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
    @GetMapping("/{userId}")
    public ResponseEntity<Faq> getFaqById(@PathVariable Long id) {
        Optional<Faq> faq = faqService.findFaqById(id); // 수정된 findById 호출
        return faq.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    // 새로운 FAQ 추가
    @PostMapping
    public ResponseEntity<?> addFaq(@RequestBody Faq faq, @RequestParam String username) {
        try {
            Faq savedFaq = faqService.submitFaq(faq, username);
            return ResponseEntity.ok(savedFaq);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("잘못된 요청: " + e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("권한 없음: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 에러 발생: " + e.getMessage());
        }
    }
    
    //
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFaq(@PathVariable Long id, @RequestBody Faq faq) {
        try {
            // FAQ 수정 요청
            return faqService.editFaq(id, faq)
                .map(updatedFaq -> ResponseEntity.ok(updatedFaq)) // 수정 성공
                .orElseGet(() -> ResponseEntity.status(404).body(null)); // 404 응답
        } catch (IllegalArgumentException e) {
            // 잘못된 요청 처리
            return ResponseEntity.badRequest().body("잘못된 요청: " + e.getMessage());
        } catch (Exception e) {
            // 서버 에러 처리
            return ResponseEntity.status(500).body("서버 에러 발생: " + e.getMessage());
        }
    }

    // FAQ 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteFaq(@PathVariable Long id) {
        try {
            Optional<Faq> deletedFaq = faqService.deleteFaq(id);
            if (deletedFaq.isPresent()) {
                return ResponseEntity.ok("FAQ 삭제 성공: ID = " + id);
            } else {
                return ResponseEntity.status(404).body("FAQ를 찾을 수 없습니다: ID = " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 에러 발생: " + e.getMessage());
        }
    }
}