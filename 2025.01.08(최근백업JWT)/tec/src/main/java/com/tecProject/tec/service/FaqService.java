package com.tecProject.tec.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tecProject.tec.domain.Faq;
import com.tecProject.tec.domain.User;
import com.tecProject.tec.repository.FaqRepository;
import com.tecProject.tec.repository.UserRepository;

@Service
public class FaqService {
	
	private final FaqRepository faqrepository;
	private final UserRepository userrepository;
	
	// 생성자를 통해 faqrepository 초기화
    public FaqService(FaqRepository faqrepository, UserRepository userrepository) {
        this.faqrepository = faqrepository;
        this.userrepository = userrepository;
    }
    
    // 삭제되지 않은 FAQ를 가져오는 함수
    public List<Faq> getAllFaqs() {
        return faqrepository.findByDeletedDateIsNullOrderByFaqNo();
    }
    
 // FAQ 추가 (관리자 권한 확인 추가)
    public Faq submitFaq(Faq faq, String username) {
        // 사용자 조회
    	User user = userrepository.findByUsername(username);
    	if (user == null || user.getUserType() == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        
    	 // 관리자 여부 확인
        if (!"ROLE_ADMIN".equals(user.getUserType())) { // 안전한 String 비교
            throw new SecurityException("관리자 권한이 필요합니다.");
        }

        // FAQ 저장
        faq.setIsDeleted("N"); // 추가되는 FAQ는 삭제되지 않은 상태로 설정
        faq.setCreateDate(LocalDateTime.now()); 
        return faqrepository.save(faq); // 받은 FAQ 데이터 저장 후 반환
    }
    
    // 특정 FAQ 조회
    public Optional<Faq> findFaqById(Long id) {
        return faqrepository.findById(id);
    }
    
    // FAQ 수정
    public Optional<Faq> editFaq(Long no, Faq faq) {
        Optional<Faq> fOption = faqrepository.findByFaqNoAndDeletedDateIsNull(no); // 수정하려는 FAQ가 데이터베이스에 있는지 찾음
        
        // FAQ가 있으면 내용을 수정하고 저장
        if (fOption.isPresent()) {
            Faq f = fOption.get(); // 찾은 FAQ를 가져옴
            f.setDeletedDate(LocalDateTime.now()); // 현재 시간으로 삭제 일자 설정
            
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