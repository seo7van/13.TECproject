package com.tecProject.tec.security;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtTokenUtil {
	
	//토큰을 만들때 필요한 키
	private final SecretKey SECRET_KEY;
	
    // 생성자에서 키 초기화 (환경 변수 또는 설정 파일에서 가져오기)
    public JwtTokenUtil(@Value("${jwt.secret}") String secretKey) {
    	if (secretKey.length() < 32) {
    		throw new IllegalArgumentException("JWT 비밀 키는 최소 32자 이상이어야 합니다.");
    	}
        // secretKey를 바이트 배열로 변환하여 안전한 키 생성
    	this.SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    
    //토큰을 만들어주는 함수
    public String generateToken(String userName) {
        return Jwts.builder()
        		.setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 유효
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }
    
    //토큰에서 '이름'을 꺼내는 함수
    public String extractUserName(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    //JWT 유효성 확인
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid JWT: " + e.getMessage());
            return false;
        }
    }
}