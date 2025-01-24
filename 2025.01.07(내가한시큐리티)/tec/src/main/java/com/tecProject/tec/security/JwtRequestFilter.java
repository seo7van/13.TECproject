package com.tecProject.tec.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	//JWT 관련 작업을 처리하는 도구 클래스
    private final JwtTokenUtil jwtTokenUtil;

    //JwtRequestFilter 클래스 생성자 (JwtTokenUtil 객체를 초기화)
    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    //사용자가 서버로 요청을 보낼 때마다 실행되는 필터 함수
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
    	
        //사용자가 보낸 요청에서 Authorization라는 이름의 정보를 헤더에서 JWT 추출
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization 헤더 값: " + authorizationHeader);
        
        String userName = null; //사용자의 이름
        String jwt = null; //JWT 토큰
        
        //Authorization 헤더가 "Bearer "로 시작하는지 확인
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // "Bearer " 이후의 실제 토큰 값 추출
            
            try {
                //JWT에서 사용자 이름 추출
                userName = jwtTokenUtil.extractUserName(jwt);
                System.out.println("JWT에서 추출한 username: " + userName);
            } catch (Exception e) {
                System.out.println("JWT 추출 오류: " + e.getMessage());
            }
        } else {
            System.out.println("Authorization 헤더가 없거나 형식이 잘못되었습니다.");
        }

        //SecurityContext에 인증 정보가 없고, userName이 유효한 경우 처리
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //사용자 인증 객체 생성
            UsernamePasswordAuthenticationToken authentication =
            		new UsernamePasswordAuthenticationToken(userName, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

            //요청의 세부 정보를 인증 객체에 설정
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            //SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            System.out.println("SecurityContext에 인증 정보가 이미 설정되어 있거나 username이 null입니다.");
        }

        //다음 필터로 요청 전달
        chain.doFilter(request, response);
    }
}
