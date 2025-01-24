package com.tecProject.tec.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecProject.tec.service.IpService;
import com.tecProject.tec.service.TranslationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/code")
public class TranslationController {
	
	private final TranslationService translationService;
	private final IpService ipService;
	
    // 서비스 사용 준비
    public TranslationController(TranslationService transltionService, IpService ipService) {
    	this.translationService = transltionService;
    	this.ipService = ipService;

    }
    // 문장 번역 API
    @GetMapping
    public ResponseEntity<String> getTranslation(
    		@RequestParam("origin") String originSentence,
    		@RequestParam("language") String language
	) {
        String translatedSentence = translationService.translateSentence(originSentence, language);
        return ResponseEntity.ok(translatedSentence);
    }
    
    //요청 횟수 증가 API
    @GetMapping("/increment")
    public ResponseEntity<String> incrementRequestCount(
            @RequestParam("ip") String ipAddress,
            @RequestParam("isMember") boolean isMember) {
        try {
            //요청 횟수 증가 및 제한 확인
            boolean isAllowed = ipService.isRequestAllowed(ipAddress, isMember);
            if (!isAllowed) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("요청 제한을 초과했습니다.");
            }
            return ResponseEntity.ok("요청 횟수 증가 완료");

        } catch (Exception e) {
            System.err.println("서버 처리 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("오류 발생: " + e.getMessage());
        }
    }
}