package com.tecProject.tec.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecProject.tec.auth.JWTUtil;
import com.tecProject.tec.service.IpService;
import com.tecProject.tec.service.TranslationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/code")
public class TranslationController {
	
	private final TranslationService translationService;
	private final IpService ipService;
	private final JWTUtil jwtUtil;
	
    // 서비스 사용 준비
    public TranslationController(TranslationService transltionService, IpService ipService, JWTUtil jwtUtil) {
    	this.translationService = transltionService;
        this.ipService = ipService;
        this.jwtUtil = jwtUtil;
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
    
    @GetMapping("/check-ip")
    public ResponseEntity<String> checkIpAndMembership(HttpServletRequest request) {
        try {
        	
            // Authorization 헤더에서 JWT 토큰 가져오기
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Authorization 헤더가 누락되었거나 잘못되었습니다.");
            }
            String token = authorization.replace("Bearer ", "").trim();
            
            // JWT에서 사용자 정보 가져오기
            String token = request.getHeader("Authorization").replace("Bearer ", "");
            String username = jwtUtil.getUsername(token);
            String userType = jwtUtil.getRole(token);

            System.out.println("요청 사용자 이름: " + username);
            System.out.println("요청 사용자 유형: " + userType);

            // 회원 여부 확인
            boolean isMember = "ROLE_MEMBER".equals(userType);
            System.out.println("회원 여부 확인: " + (isMember ? "회원" : "비회원"));

            // 클라이언트 IP 가져오기
            String ipAddress = ipService.getClientIp(request);
            System.out.println("클라이언트 IP: " + ipAddress);

            // 추가 메시지 출력
            //System.out.println("메시지: 클라이언트 IP 확인 성공");

            return ResponseEntity.ok("로그 확인 완료");

        } catch (Exception e) {
            // 오류 처리
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생: " + e.getMessage());
        }
    }
    

    
//
//    //사용자 아이피 확인(테스트용)
//    @GetMapping("/check-ip")
//    //사용자의 아이피를 가져오는 코드
//    public ResponseEntity<String> checkIp() {
//    	String ipAddress = request.getHeader("X-Forwarded-For"); //프록시를 사용하는 경우
//        //String ipAddress = translationService.getClientIp();
//        
//        return ResponseEntity.ok("사용자 아이피 주소는: " + ipAddress);
//    }
    
}