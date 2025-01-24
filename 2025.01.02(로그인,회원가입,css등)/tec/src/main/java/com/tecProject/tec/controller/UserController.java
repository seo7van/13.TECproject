package com.tecProject.tec.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tecProject.tec.domain.User;
import com.tecProject.tec.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            
            System.out.println("Sign-Up Successful for User: " + createdUser);
            
            return ResponseEntity.ok("Sign-Up Successful");
        } catch (Exception e) {
        	
            System.err.println("Sign-Up Error: " + e.getMessage());

            return ResponseEntity.badRequest().body("Sign-Up Failed");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        boolean isValid = userService.checkPassword(user.getUserId(), user.getUserPwd());
        if (isValid) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Login Successful"));
        } else {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid Credentials"));
        }
    }

    // 아이디 중복확인 API
    @PostMapping("/check-id")
    public ResponseEntity<?> checkId(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        System.out.println("Received ID Check Request for userId: " + userId);

        if (userId == null || userId.trim().isEmpty()) {
            System.out.println("Validation Error: Empty userId provided.");
            return ResponseEntity.badRequest().body("아이디를 입력해주세요.");
        }

        boolean isAvailable = userService.isUserIdAvailable(userId);
        System.out.println("ID Availability: " + isAvailable);
        return ResponseEntity.ok(Map.of("isAvailable", isAvailable));
    }
    
 // 이메일 중복확인 API
    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        System.out.println("Received Email Check Request for email: " + email);

        if (email == null || email.trim().isEmpty()) {
            System.out.println("Validation Error: Empty email provided.");
            return ResponseEntity.badRequest().body("이메일을 입력해주세요.");
        }

        boolean isAvailable = userService.isEmailAvailable(email);
        System.out.println("Email Availability: " + isAvailable);
        return ResponseEntity.ok(Map.of("isAvailable", isAvailable));
    }
}