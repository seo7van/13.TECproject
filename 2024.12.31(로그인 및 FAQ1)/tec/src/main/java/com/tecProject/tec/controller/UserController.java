package com.tecProject.tec.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tecProject.tec.domain.User;
import com.tecProject.tec.service.UserService;

@Controller
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 회원가입
    @PostMapping("/SignUp")
    public ResponseEntity<String> userInsert(@RequestBody User user) {
    	
        System.out.println("User ID: " + user.getUserId());
        System.out.println("User Password: " + user.getUserPwd());
        System.out.println("User Name: " + user.getUserName());

        try {
            if (user.getUserPwd() == null || user.getUserPwd().isEmpty()) {
                throw new IllegalArgumentException("비밀번호를 입력해야 합니다.");
            }

            user.setUserPwd(passwordEncoder.encode(user.getUserPwd())); // 비밀번호 암호화
            userService.userInsert(user); // 사용자 저장

            return ResponseEntity.ok("회원가입 성공!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }
    
    // 로그인
    @PostMapping("/UserLogin")
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        // 로그인 로직 (예: 사용자 인증)
        if ("testuser".equals(username) && "password123".equals(password)) {
            return ResponseEntity.ok("로그인 성공!");
        } else {
            return ResponseEntity.status(401).body("로그인 실패: 아이디나 비밀번호가 잘못되었습니다.");
        }
    }
}
