package com.tecProject.tec.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tecProject.tec.domain.Faq;
import com.tecProject.tec.repository.FaqRepository;

@Service
public class FaqService {
	
	private final FaqRepository faqrepository;
	
	// 생성자를 통해 faqrepository 초기화
    public FaqService(FaqRepository faqrepository) {
        this.faqrepository = faqrepository;
    }
    
    // 삭제되지 않은 FAQ를 가져오는 함수
    public List<Faq> getAllFaqs() {
        return faqrepository.findByDeletedDateIsNullOrderByFaqNo();
    }
    
    // FAQ 추가
    public Faq submitFaq(Faq faq) {
    	faq.setIsDeleted("N"); // 추가되는 FAQ는 삭제되지 않은 상태로 설정
    	faq.setCreateDate(LocalDate.now()); 
    	return faqrepository.save(faq); // 받은 FAQ 데이터 저장 저장된 데이터 그대로 반환
    }
    
    // 특정 FAQ 조회
    public Optional<Faq> findFaqById(Long id) {
        return faqrepository.findById(id);
    }
    
    // FAQ 수정
    public Optional<Faq> editFaq(Long no, Faq faq) {

        Optional<Faq> fOption = faqrepository.findById(no); // 수정하려는 FAQ가 데이터베이스에 있는지 찾음
        
        // FAQ가 있으면 내용을 수정하고 저장
        if (fOption.isPresent()) {
            Faq f = fOption.get(); // 찾은 FAQ를 가져옴
            f.setQuestion(faq.getQuestion()); // 질문 업데이트
            f.setAnswer(faq.getAnswer());     // 답변 업데이트

            return Optional.of(faqrepository.save(f)); // 수정된 데이터 저장 후 반환
        }
        return Optional.empty(); //FAQ 못 찾았으면 빈 Optional 반환
    }
    
    // FAQ 삭제
    public Optional<Faq> deleteFaq(Long no) {

        Optional<Faq> fOption = faqrepository.findById(no); // 삭제하려는 FAQ가 데이터베이스에 있는지 찾음

        // FAQ가 있으면 삭제 상태 설정
        if (fOption.isPresent()) {
            Faq f = fOption.get();
            f.setIsDeleted("Y"); // 삭제된 상태로 설정
            return Optional.of(faqrepository.save(f));
        }
        return Optional.empty(); //FAQ 못 찾았으면 빈 Optional 반환
    }
}