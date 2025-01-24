package com.tecProject.tec.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tecProject.tec.domain.UserSupport;
import com.tecProject.tec.repository.SupportRepository;

@Service
public class SupportService {
	
	private final SupportRepository supportRepository;
	
	private SupportService(SupportRepository supportRepository) {
		this.supportRepository = supportRepository;
	}

	// 문의 등록
	public UserSupport createInquiry(UserSupport userSupport) {
		userSupport.setCreatedDate(LocalDateTime.now()); // 작성시간
		userSupport.setModifiedDate(LocalDateTime.now()); // 수정시간
		userSupport.setStatus("대기");
		userSupport.setIsDeleted("N");
		return supportRepository.save(userSupport);
	}

	// 문의 수정
	public Optional<UserSupport> updateInquiry(int inquiryNo, String title, String content) {
		Optional<UserSupport> existingInquiry = supportRepository.findById(inquiryNo);
		if (existingInquiry.isPresent()) {
			UserSupport updateInquiry = existingInquiry.get();
			updateInquiry.setTitle(title);
			updateInquiry.setContent(content);
			updateInquiry.setModifiedDate(LocalDateTime.now());
			return Optional.of(supportRepository.save(updateInquiry));
		}
		return Optional.empty();
	}

    // 문의 삭제
    public Optional<UserSupport> deleteInquiry(int inquiryNo) {
    	Optional<UserSupport> existingInquiry = supportRepository.findById(inquiryNo);
    	if (existingInquiry.isPresent()) {
    		UserSupport inquiryToDelete = existingInquiry.get();
    		inquiryToDelete.setIsDeleted("Y"); // 삭제상태 "Y"로 업데이트
    		inquiryToDelete.setDeletedDate(LocalDateTime.now()); 
    		return Optional.of(supportRepository.save(inquiryToDelete));
    	}
    	return Optional.empty();
    }

    // 문의내역 전체조회(사용자)
	public List<UserSupport> getInquiryByUserId(String userId) {
		return supportRepository.findByUserIdAndIsDeleted(userId, "N");
	}

	// 문의내역 상세조회(사용자)
	public Optional<UserSupport> getInquiryById(int inquiryNo) {
		return supportRepository.findById(inquiryNo);
	}
}
