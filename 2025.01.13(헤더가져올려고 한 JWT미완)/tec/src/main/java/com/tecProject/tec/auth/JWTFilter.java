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
			System.out.println("Authorization 헤더가 누락되었거나 잘못된 형식입니다.");
			filterChain.doFilter(request, response);
			//조건이 해당되면 메소드 종료 (필수)
			return;
		}
		
		//Bearer 부분 제거 후 순수 토큰만 획득
	    String token = authorization.replace("Bearer "," ").trim();
	    
	    try {
            // 토큰 만료 확인
            if (jwtUtil.isExpired(token)) {
                System.out.println("JWT 토큰이 만료되었습니다.");
                filterChain.doFilter(request, response);
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
			 Authentication authentication = new UsernamePasswordAuthenticationToken(
		                customUserDetails, null, customUserDetails.getAuthorities());
			
			//SecurityContextHolder에 설정
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        System.out.println("JWT 인증 성공: 사용자 이름 = " + username + ", 역할 = " + role);

	    } catch (Exception e) {
	        System.out.println("JWT 인증 실패: " + e.getMessage());
	    }
	        // 최종적으로 필터 체인을 호출하여 요청 처리 계속
	        filterChain.doFilter(request, response);
	   }
}