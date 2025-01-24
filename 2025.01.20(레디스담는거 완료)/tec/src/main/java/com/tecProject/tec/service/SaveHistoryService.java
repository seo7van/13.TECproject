package com.tecProject.tec.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.tecProject.tec.domain.SaveHistory;
import com.tecProject.tec.dto.SaveHistoryDTO;
import com.tecProject.tec.repository.SaveHistoryRepository;

@Service
public class SaveHistoryService {
	
	private final SaveHistoryRepository saveHistoryRepository;
	
	public SaveHistoryService(SaveHistoryRepository saveHistoryRepository) {
		this.saveHistoryRepository = saveHistoryRepository;
	}

	// 히스토리 저장
	public void createHistory(SaveHistoryDTO saveDTO, String username) {
        // 입력값 검증
        if (saveDTO.getRequestCode() == null || saveDTO.getResponseCode() == null || saveDTO.getTypeCode() == null) {
            throw new IllegalArgumentException("필수 값이 누락되었습니다.");
        }
     // 엔티티 생성 및 저장
		SaveHistory entity = SaveHistory.builder()
				.username(username)
				.requestCode(saveDTO.getRequestCode())
				.responseCode(saveDTO.getResponseCode())
				.typeCode(saveDTO.getTypeCode())
				.saveTime(LocalDateTime.now())
				.build();
		saveHistoryRepository.save(entity);
		
	}

}
