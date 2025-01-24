package com.tecProject.tec.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tecProject.tec.dto.LoginDTO;
import com.tecProject.tec.jwt.JWTUtil;
import com.tecProject.tec.service.LoginService;

@RestController
@RequestMapping("/user")
public class LoginController {

	private final LoginService loginService;
	private final JWTUtil jwtUtil;

	public LoginController(LoginService loginService, JWTUtil jwtUtil) {
		this.loginService = loginService;
		this.jwtUtil = jwtUtil;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
		try {
			Map<String, String> loginResponse = loginService.authenticateUser(loginDTO);
			return ResponseEntity.ok(loginResponse);
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(401).body("아이디 또는 비밀번호가 잘못되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
		}
	}
}

