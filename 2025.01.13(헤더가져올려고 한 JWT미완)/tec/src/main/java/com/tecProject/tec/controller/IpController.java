package com.tecProject.tec.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tecProject.tec.service.IpService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ip")
@RequiredArgsConstructor
public class IpController {
	
    private final IpService ipService;
    
    // 클라이언트 IP 요청 허용 여부 확인
    @GetMapping("/check")
    public ResponseEntity<String> checkRequestAllowed(
        HttpServletRequest request,
        @RequestHeader(value = "username", required = true) String username
    ) {
        try {
            // IpService를 사용해 요청 가능 여부 확인
        	boolean isAllowed = ipService.isRequestAllowed(request, username);

            if (isAllowed) {
                return ResponseEntity.ok("요청 가능합니다!");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("요청 제한 초과!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("오류 발생: " + e.getMessage());
        }
    }

    //특정 IP의 요청 횟수 초기화
    @GetMapping("/reset")
    public ResponseEntity<String> resetRequestCount(HttpServletRequest request) {
        try {
            // 클라이언트 IP를 가져와 요청 횟수를 초기화
        	ipService.resetRequestCount(request);
            return ResponseEntity.ok("요청 횟수가 초기화되었습니다!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("오류 발생: " + e.getMessage());
        }
    }
}