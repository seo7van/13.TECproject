package com.tecProject.tec.config;

import java.util.List;

import javax.xml.transform.Source;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.tecProject.tec.security.JwtRequestFilter;
import com.tecProject.tec.security.JwtTokenUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final JwtRequestFilter jwtRequestFilter;
	private final JwtTokenUtil jwtTokenUtil;

	 // JWT 필터를 SecurityConfig에 연결할 수 있도록 생성자에서 가져옴
	public SecurityConfig(JwtRequestFilter jwtRequestFilter, JwtTokenUtil jwtTokenUtil) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.jwtTokenUtil = jwtTokenUtil;
    }

	//비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 안전하게 암호화
    }
    
    //보안설정 관리 함수
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()); //CSRF 보안 기능 끔 (테스트 용으로 끔)
        http
            //CORS 설정 : 다른 서버에서 내 서버에 접근할 수 있도록 허용
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("http://localhost:3000")); //React 개발 서버 연결 허용
                config.setAllowedMethods(List.of("*")); //허용된 요청 방식
                config.setAllowedHeaders(List.of("*")); //요청 사용할 수 있는 모든 헤더 허용
                config.setAllowCredentials(true); //인증 정보 같이 보낼 수 있게 함
                return config;
            }));
        
        http
	        // 기본 로그인 폼 비활성화 및 REST API 방식 지원
	        .formLogin(form -> form.disable());
        
        http
	        //로그인 설정
	        .formLogin(form -> form
	            .loginProcessingUrl("/UserLogin") //로그인 요청을 처리하는 URL
	            .permitAll() //로그인 페이지는 누구나 접근 가능
	        );
        
        http
	        //로그아웃 설정
	        .logout(logout -> logout
	            .logoutUrl("/logout") //로그아웃 요청을 처리하는 URL
	            .permitAll() //로그아웃 페이지는 누구나 접근 가능
	        );
        
        http
            //요청에 대한 허가 규칙 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                		"/api/code", "/admin/code/**", 
                		"/user/login", "/user/signup", 
                		"/Admin/faqs", "/user/check-id", 
                		"/user/check-email", "/api/user-support/**",
                		"/Admin/faqs/check-admin"
                ).permitAll() // 접근 할 수 있는 URL
                .requestMatchers("/admin/**","/Admin/**","/Admin/faqs/check-admin").hasRole("ADMIN") // 관리자 전용 API
                .anyRequest().authenticated() // 위에서 허용하지 않은 나머지 요청은 로그인해야 접근 가능
            );
            
        http
            //JWT 필터 추가(요청 들어오면 JWT 확인하도록 필터 설정)
        	.addFilterBefore(new JwtRequestFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter.class);
        
        http
	        //JWT 토근 사용으로 세션정책 STATELESS 설정(JWT 사용시 준필수)
	    	.sessionManagement(session -> session
	    			.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	 

        return http.build(); // 설정완료 후 반환
    }
}