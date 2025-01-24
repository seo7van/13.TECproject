package com.tecProject.tec.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tecProject.tec.auth.JWTUtil;
import com.tecProject.tec.service.IpService;
import com.tecProject.tec.service.TranslationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ip")
@RequiredArgsConstructor
public class IpController {
	
	private final IpService ipService;
	
	//회원/비회원 확인 후 Ip반환
    @GetMapping("/check-ip")
    public ResponseEntity<String> checkIpAndMembership(
            @RequestHeader(value = "Authorization", required = false) String token,
            HttpServletRequest request
    ) {
        try {
            // 서비스 계층에 로직 위임
            String result = ipService.checkMembershipAndIp(token, request);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            // 오류 처리
            System.err.println("서버 처리 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("오류 발생: " + e.getMessage());
        }
    }

//    //특정 IP의 요청 횟수 초기화
//    @GetMapping("/reset")
//    public ResponseEntity<String> resetRequestCount(HttpServletRequest request) {
//        try {
//            // 클라이언트 IP를 가져와 요청 횟수를 초기화
//        	ipService.resetRequestCount(request);
//            return ResponseEntity.ok("요청 횟수가 초기화되었습니다!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("오류 발생: " + e.getMessage());
//        }
//    }
//  //사용자 아이피 확인(테스트용)
//  @GetMapping("/check-ip")
//  //사용자의 아이피를 가져오는 코드
//  public ResponseEntity<String> checkIp() {
//  	String ipAddress = request.getHeader("X-Forwarded-For"); //프록시를 사용하는 경우
//      //String ipAddress = translationService.getClientIp();
//      
//      return ResponseEntity.ok("사용자 아이피 주소는: " + ipAddress);
//  }
}