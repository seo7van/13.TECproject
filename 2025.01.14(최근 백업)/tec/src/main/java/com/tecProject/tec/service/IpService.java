package com.tecProject.tec.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tecProject.tec.auth.IpUtil;
import com.tecProject.tec.auth.JWTUtil;
import com.tecProject.tec.domain.Ip;
import com.tecProject.tec.domain.User;
import com.tecProject.tec.repository.IpRepository;
import com.tecProject.tec.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IpService {
	
	private final IpRepository ipRepository;
	private final IpUtil ipUtil;
	private final JWTUtil jwtUtil;
	 
    private static final int MEMBER_REQUEST_LIMIT = 50; // 회원 요청 제한
    private static final int GUEST_REQUEST_LIMIT = 30;  // 비회원 요청 제한
	
	//회원 비회원 확인 후 클라이언트 IP가져오기
	public String checkMembershipAndIp(String token, HttpServletRequest request) {
		
		String username = "비회원"; //기본값: 비회원
		String userType = "ROLE_GUEST"; //기본값: 비회원 역할
		boolean isMember = false; //기본값: 비회원
		
		//1. JWT 토큰 검증 및 사용자 정보 추출
		if (token != null && token.startsWith("Bearer ")) {
			try {
				token = token.replace("Bearer ", "");
				username = jwtUtil.getUsername(token); //사용자 이름 추출
	            userType = jwtUtil.getRole(token); //사용자 역할 추출
	            isMember = "ROLE_ADMIN".equals(userType) || "ROLE_USER".equals(userType); //회원 여부 확인
	            } catch (Exception e) {
	            	//JWT 검증 실패 시 비회원으로 처리
	            	System.err.println("JWT 처리 오류: " + e.getMessage());
	            }
			}
		
		//2. 클라이언트 IP 가져오기
		String ipAddress = ipUtil.getClientIp(request);
        
        //3. 결과 메시지 반환
        return String.format("클라이언트 IP: %s, 사용자: %s, 회원 여부: %s",
        		ipAddress, username, isMember ? "회원" : "비회원");
        
	}
	
	//요청 제한 확인
    @Transactional
    public boolean isRequestAllowed(String ipAddress, boolean isMember) {
        //1. 요청 제한 설정
        int maxRequestLimit = isMember ? MEMBER_REQUEST_LIMIT : GUEST_REQUEST_LIMIT;

        //2. IP 정보 가져오기
        Optional<Ip> optionalIp = ipRepository.findByIpAddress(ipAddress);
        Ip ipEntity = optionalIp.orElseGet(() -> createNewIp(ipAddress));

        //3. 요청 제한 초과 확인
        if (ipEntity.getIpCount() >= maxRequestLimit) {
            return false; // 요청 제한 초과
        }

        //4. 요청 횟수 증가 및 저장
        ipEntity.setIpCount(ipEntity.getIpCount() + 1);
        ipEntity.setLastRequest(LocalDateTime.now());
        ipRepository.save(ipEntity);

        return true; // 요청 가능
    }
    
	//IP정보를 생성
    private Ip createNewIp(String ipAddress) {
    	Ip newIp = new Ip();
    	newIp.setIpAddress(ipAddress);
    	newIp.setIpCount(0); //초기 요청 횟수는 0
    	newIp.setLastRequest(LocalDateTime.now()); //현재 시간으로 초기화
    	return ipRepository.save(newIp);
	}

}