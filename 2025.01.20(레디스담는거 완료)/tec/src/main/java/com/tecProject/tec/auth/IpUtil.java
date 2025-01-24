package com.tecProject.tec.auth;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class IpUtil {
	
	private final JWTUtil jwtUtil;
	
    public IpUtil(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
	
	public String getClientIp(HttpServletRequest request) {
		
		String ipAddress = request.getHeader("X-Forwarded-For"); //프록시 뒤에서 클라이언트 IP를 가져옴
		
		//다른 헤더를 확인
    	if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
    		ipAddress = request.getHeader("Proxy-Client-IP"); //Proxy-Client-IP 헤더 확인
    	}
    	if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
    		ipAddress = request.getHeader("WL-Proxy-Client-IP"); //WebLogic 헤더 확인
    	}
    	if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
    		ipAddress = request.getRemoteAddr(); //최종적으로 HttpServletRequest에서 가져옴
    	}
    	//꼴뵈기 싫어서 진짜 내 아이피주소로 바꾸기
	    if ("0:0:0:0:0:0:0:1".equals(ipAddress) || "127.0.0.1".equals(ipAddress)) {
	        ipAddress = "192.168.0.1"; //로컬 IP를 사설 IP로 강제 설정
	    }
    
	    //여러 IP가 포함된 경우 로그로 출력
        if (ipAddress.contains(",")) {
            System.out.println("감지된 여러 IP: " + ipAddress);
        }
        return ipAddress;
	}
	   
    // JWT에서 사용자 이름 가져오기
    public String getUsernameFromToken(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        return jwtUtil.getUsername(token);
    }

    // JWT에서 역할(회원/비회원) 가져오기
    public String getRoleFromToken(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        return jwtUtil.getRole(token);
    }

    // JWT 토큰 만료 여부 확인
    public boolean isTokenExpired(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        return jwtUtil.isExpired(token);
    }

    // HTTP 요청에서 JWT 토큰 추출
    private String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }
        throw new IllegalArgumentException("유효하지 않은 Authorization 헤더");
    }
}