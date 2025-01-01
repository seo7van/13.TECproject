package com.tecProject.tec.controller;

import java.lang.StackWalker.Option;
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
    @GetMapping("/{id}")
    public ResponseEntity<Faq> getFaqById(@PathVariable Long id) {
        Optional<Faq> faq = faqService.findFaqById(id); // 수정된 findById 호출
        return faq.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    // 새로운 FAQ 추가
    @PostMapping
    public ResponseEntity<Faq> addFaq(@RequestBody Faq faq) {
        Faq savedFaq = faqService.submitFaq(faq);
        return ResponseEntity.ok(savedFaq);
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