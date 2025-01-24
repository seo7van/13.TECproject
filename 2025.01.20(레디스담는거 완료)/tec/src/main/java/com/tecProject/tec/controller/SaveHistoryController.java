package com.tecProject.tec.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tecProject.tec.domain.SaveHistory;
import com.tecProject.tec.dto.CustomUserDetails;
import com.tecProject.tec.dto.SaveHistoryDTO;
import com.tecProject.tec.service.SaveHistoryService;

@RestController
@RequestMapping("/api/history")
public class SaveHistoryController {

	private final SaveHistoryService saveHistoryService;
	
	public SaveHistoryController(SaveHistoryService saveHistoryService) {
		this.saveHistoryService = saveHistoryService;
	}
	
	// 히스토리 저장
	@PostMapping
	public ResponseEntity<?> saveTranslationHistory(@RequestBody SaveHistoryDTO saveDTO) {
		// 인증 정보 가져오기    
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 이용 가능합니다.");
        }
        String username = authentication.getName(); // JWT에서 username 추출
		try {
			saveHistoryService.createHistory(saveDTO, username);
			return ResponseEntity.ok("기록이 저장되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("기록 저장 실패: " + e.getMessage());
		}
	}
}
