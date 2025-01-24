package com.tecProject.tec.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.tecProject.tec.auth.IpUtil;
import com.tecProject.tec.auth.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IpService {
	
	private final IpUtil ipUtil;
	private final JWTUtil jwtUtil;
	private final RedisTemplate<String, Integer> redisTemplate; //Redis 작업을 처리하기 위한 템플릿
	 
    private static final int MEMBER_REQUEST_LIMIT = 50; //회원 요청 제한
    private static final int GUEST_REQUEST_LIMIT = 30;  //비회원 요청 제한
    private static final long RATE_LIMIT_TIME_WINDOW = 30L * 24 * 60 * 60; // 한 달(초 단위)
	
//    //요청 횟수를 저장하는 Map (회원 요청 기록)
//    private final Map<String, Integer> memberRequestCounts = new HashMap<>();
//    
//    //요청 횟수를 저장하는 Map (비회원 요청 기록)
//    private final Map<String, Integer> guestRequestCounts = new HashMap<>();
		
    //회원/비회원/관리자 확인 후 Ip 반환
    public String checkMembershipAndIp(String token, HttpServletRequest request) {
		String username = "비회원"; //기본값: 비회원
		String userType = "ROLE_GUEST"; //기본값: 비회원 역할
		boolean isMember = false; //기본값: 비회원
		boolean isAdmin = false; // 기본값: 관리자 아님
		
		//1. JWT 토큰 검증 및 사용자 정보 추출
		if (token != null && token.startsWith("Bearer ")) {
			try {
				token = token.replace("Bearer ", "");
				username = jwtUtil.getUsername(token); //사용자 이름 추출
	            userType = jwtUtil.getRole(token); //사용자 역할 추출
	            isAdmin = "ROLE_ADMIN".equals(userType); // 관리자 여부 확인
	            isMember = isAdmin || "ROLE_USER".equals(userType); // 회원 여부 확인 (관리자 포함)
	            } catch (Exception e) {
	            	//JWT 검증 실패 시 비회원으로 처리
	            	System.err.println("JWT 처리 오류: " + e.getMessage());
	            }
			}
		
		//2. 클라이언트 IP 가져오기
		String ipAddress = ipUtil.getClientIp(request);
        
	    //3. 회원 여부와 IP 정보 설정
		System.out.println("감지된 모든 클라이언트 IP: " + ipAddress);
		
        //4. 결과 메시지 반환
        return String.format("클라이언트 IP: %s, 사용자: %s, 회원 여부: %s",
        		ipAddress, username, userType);
        
	}
    
    //Redis 키 생성 메서드
    private String generateKey(String ipAddress, boolean isMember) {
        String userType = isMember ? "MEMBER" : "GUEST";
        return "RATE_LIMIT:" + userType + ":" + ipAddress;
    }
    
    //회원/비회원만 요청 제한 확인(관리자는 무제한)
    public boolean isRequestAllowed(HttpServletRequest request, boolean isMember) {
        // 회원 여부에 따라 식별자 결정
        String ipAddress = isMember
                ? jwtUtil.getUsername(request.getHeader("Authorization").replace("Bearer ", ""))
                : ipUtil.getClientIp(request);

        //Redis에서 사용할 키 생성
        String key = generateKey(ipAddress, isMember);

        //Redis에서 현재 요청 횟수 가져오기
        Integer currentCount = redisTemplate.opsForValue().get(key);
        
        // 디버깅용 로그 추가
        System.out.println("Redis 키: " + key);
        System.out.println("현재 요청 횟수: " + (currentCount != null ? currentCount : 0));

        //회원 여부에 따라 최대 요청 제한 설정
        int maxLimit = isMember ? MEMBER_REQUEST_LIMIT : GUEST_REQUEST_LIMIT;

        //요청 기록이 없으면 Redis에 초기화하고 요청 가능 처리
        if (currentCount == null) {
            redisTemplate.opsForValue().set(key, 1, RATE_LIMIT_TIME_WINDOW, TimeUnit.SECONDS);
            System.out.println("Redis 데이터 초기화: 1");
            return true;
        }
        
        //요청 제한 초과 여부 확인
        if (currentCount >= maxLimit) {
            return false; // 요청 제한 초과
        }

        //요청 횟수 증가
        redisTemplate.opsForValue().increment(key);
        return true; // 요청 가능
    }

    //요청 횟수 조회 메서드
    public int getRequestCount(HttpServletRequest request, String token, boolean isMember) {
        String ipAddress;

        //회원 여부에 따라 식별자 결정
        if (isMember) {
            //JWT 토큰에서 사용자 이름 추출
        	ipAddress = jwtUtil.getUsername(token.replace("Bearer ", ""));
        } else {
            //클라이언트 IP 주소 추출
        	ipAddress = ipUtil.getClientIp(request);
        }

        //요청 횟수 조회
        String key = generateKey(ipAddress, isMember);
        Integer currentCount = redisTemplate.opsForValue().get(key);
        return currentCount == null ? 0 : currentCount;
    }
    
/*	
	//회원/비회원만 요청 제한 확인(관리자는 무제한) - 서버 Map에 저장
    public boolean isRequestAllowed(String ipAddress, String userType, boolean isMember) {
    	if ("ROLE_ADMIN".equals(userType)) {
    		return true; //관리자는 무제한
    	}
        //회원 여부에 따라 요청 제한 횟수 설정
        int maxRequestLimit = isMember ? MEMBER_REQUEST_LIMIT : GUEST_REQUEST_LIMIT;

        //요청 횟수를 저장하는 Map 선택 (회원/비회원 구분)
        Map<String, Integer> requestCounts = isMember ? memberRequestCounts : guestRequestCounts;

        //현재 요청 횟수 가져오기 (없으면 0으로 초기화)
        int currentCount = requestCounts.getOrDefault(ipAddress, 0);

        //요청 제한 초과 여부 확인
        if (currentCount >= maxRequestLimit) {
            System.err.println("요청 제한 초과: 현재 요청 횟수 " + currentCount + ", 제한 " + maxRequestLimit);
            return false; //요청 제한 초과
        }

        //요청 횟수 증가
        requestCounts.put(ipAddress, currentCount + 1);
        return true; //요청 가능
	}
    
    public String getUsernameFromToken(String token) {
        try {
            // "Bearer " 접두사를 제거한 후 JWTUtil의 getUsername 메서드 호출
            return jwtUtil.getUsername(token.replace("Bearer ", ""));
        } catch (Exception e) {
            throw new IllegalArgumentException("잘못된 토큰: " + e.getMessage());
        }
    }
    
    //JWT에서 username 추출
    public String getRoleFromToken(String token) {
        try {
            // "Bearer " 접두사를 제거한 후 JWTUtil의 getRole 메서드 호출
            return jwtUtil.getRole(token.replace("Bearer ", ""));
        } catch (Exception e) {
            throw new IllegalArgumentException("잘못된 토큰: " + e.getMessage());
        }
    }
    
    //요청 횟수 조회(회원)
    public int getMemberRequestCount(String username) {
        return memberRequestCounts.getOrDefault(username, 0); //기본값 0 반환
    }
    
    //요청 횟수 증가(회원) 
    public boolean incrementMemberRequestCount(String username) {
        int currentCount = memberRequestCounts.getOrDefault(username, 0);

        if (currentCount >= MEMBER_REQUEST_LIMIT) {
            System.err.println("요청 제한 초과: 회원 " + username + "의 현재 요청 횟수는 " + currentCount + "입니다.");
            return false; //요청 제한 초과
        }

        memberRequestCounts.put(username, currentCount + 1); //요청 횟수 증가
        return true; //요청 가능
    }
    
    //요청 횟수 조회(비회원)
    public int getGuestRequestCount(String ipAddress) {
        return guestRequestCounts.getOrDefault(ipAddress, 0);
    }
    
    //요청 횟수 증가(비회원) 
    public boolean incrementGuestRequestCount(String ipAddress) {
        int currentCount = guestRequestCounts.getOrDefault(ipAddress, 0);

        if (currentCount >= GUEST_REQUEST_LIMIT) {
            return false; //요청 제한 초과
        }

        guestRequestCounts.put(ipAddress, currentCount + 1);
        return true; //요청 가능
    }
    */
}