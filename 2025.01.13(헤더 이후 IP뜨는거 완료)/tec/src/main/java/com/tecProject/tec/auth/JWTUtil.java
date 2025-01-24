package com.tecProject.tec.auth;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JWTUtil {

	private SecretKey secretKey;
	
	public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
		this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
	}
	
	public String getUsername(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
	}
	
	public String getRole(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userType", String.class);
	}
	
	public Boolean isExpired(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
	}
	
	public String createJwt(String username, String userType, Long expiredMs) {
		return Jwts.builder()
				.claim("username", username)
				.claim("userType", userType)
				.issuedAt(new Date(System.currentTimeMillis())) // 현재발행시간
				.expiration(new Date(System.currentTimeMillis() + expiredMs)) // 남은시간
				.signWith(secretKey)
				.compact();
	}
	
	public Claims getClaims(String token) {
	    return Jwts.parser()
	               .verifyWith(secretKey)
	               .build()
	               .parseSignedClaims(token)
	               .getPayload();
	}
}