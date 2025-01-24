package com.tecProject.tec.auth;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tecProject.tec.domain.User;
import com.tecProject.tec.dto.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTFilter extends OncePerRequestFilter{

	private final JWTUtil jwtUtil;
	
	public JWTFilter(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//request에서 Authorization 헤더를 찾음
		String authorization= request.getHeader("Authorization");
		
		//Authorization 헤더 검증
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			System.out.println("token null");
			filterChain.doFilter(request, response);
			
			//조건이 해당되면 메소드 종료 (필수)
			return;
		}
		
		//Bearer 부분 제거 후 순수 토큰만 획득
	    String[] parts = authorization.split(" ");
	    if (parts.length != 2 || parts[1].isEmpty()) {
	        System.out.println("Invalid token format");
	        filterChain.doFilter(request, response);
	        return;
	    }

	    String token = parts[1];

	    try {
	    	// 토큰 소멸 시간 검증
	        if (jwtUtil.isExpired(token)) {
	            System.out.println("Token expired");
	            filterChain.doFilter(request, response);
	            // 조건이 해당되면 메소드 종료 (필수)
	            return;
	        }
		
			//토큰에서 username과 role 획득
			String username = jwtUtil.getUsername(token);
			String role = jwtUtil.getRole(token);
			
			//user를 생성하여 값 set
			User user = new User();
			user.setUsername(username);
			user.setPassword("temppassword");// 매번 db 조회할 필요 없게 임의값 넣어둠
			user.setUserType(role);
			
			//UserDetails에 회원 정보 객체 담기
			CustomUserDetails customUserDetails = new CustomUserDetails(user);
			
			//스프링 시큐리티 인증 토큰 생성
			Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
			//세션에 사용자 등록
			SecurityContextHolder.getContext().setAuthentication(authToken);
	    } catch (Exception e) {
	    	System.out.println("jwt parsing failed: " + e.getMessage());
	    }
 		filterChain.doFilter(request, response);
		
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}