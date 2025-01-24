package com.tecProject.tec.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.tecProject.tec.auth.IpUtil;
import com.tecProject.tec.auth.JWTFilter;
import com.tecProject.tec.auth.JWTUtil;
import com.tecProject.tec.auth.LoginFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final AuthenticationConfiguration authenticationConfiguration;
	//JWTUtil 주입
	private final JWTUtil jwtUtil;
	//IpUril 주입
	private final IpUtil ipUtil;
	
	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, IpUtil ipUtil) {
		this.authenticationConfiguration = authenticationConfiguration;
		this.jwtUtil = jwtUtil;
		this.ipUtil = ipUtil;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	//비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder BCryptPasswordEncoder() {
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
        	//http basic 인증 방식 disable
        	.httpBasic(auth -> auth.disable());
            
        http
            //로그아웃 설정
            .logout(logout -> logout
                .logoutUrl("/logout") //로그아웃 요청을 처리하는 URL
                .permitAll() //로그아웃 페이지는 누구나 접근 가능
            );
        
        http
	        //요청에 대한 허가 규칙 설정
	        .authorizeHttpRequests(auth -> auth
	        	.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	            .requestMatchers(
	            			     "/api/code","/api/code/check-ip","/api/code/ip",
	            			     "/admin/code/**", "/ip/check/**", 
	            			     "/user/**", "/admin/faqs", 
	            			     "/api/user-support/**", 
	            			     "/join/**","/faq/faqs",
	            			     "/faq/**","/faq/check-admin")
	            .permitAll() // 접근 할 수 있는 URL
	            .requestMatchers("/admin/**","/Admin/faqs").hasRole("ADMIN") // 관리자 전용 API
	            .anyRequest().authenticated() // 위에서 허용하지 않은 나머지 요청은 로그인해야 접근 가능
	        );
        
        http
        	//JWTFilter 등록
        	.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
        
        http
        	// 커스텀한 UsernamePasswordAuthenticationFilter 등록 (AuthenticationManager()와 JWTUtil 인수 전달)
        	.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
        
        http
        	//JWT 토근 사용으로 세션정책 STATELESS 설정(JWT 사용시 준필수)
        	.sessionManagement(session -> session
        			.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
     

        return http.build(); // 설정완료 후 반환
    }
}