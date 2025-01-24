package com.tecProject.tec.controller;

import java.util.Map;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tecProject.tec.domain.User;
import com.tecProject.tec.dto.UserResponse;
import com.tecProject.tec.dto.UserSignUpRequest;
import com.tecProject.tec.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserSignUpRequest request) {
        try {
            // DTO -> User 변환
            User user = User.builder()
                    .userId(request.getUserId())
                    .userPwd(request.getUserPwd()) // 암호화는 Service에서 처리
                    .userName(request.getUserName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .userType(request.getUserType())
                    .ssnFirst(request.getSsnFirst())
                    .ssnSecond(request.getSsnSecond())
                    .build();
            // UserService를 통해 회원 생성
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok("회원가입 성공: " + createdUser.getUserId());
        } catch (IllegalArgumentException e) {
            // ID 중복 오류 처리
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.internalServerError().body("회원가입 중 문제가 발생했습니다.");
        }
    }
    
    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            boolean isValid = userService.checkPassword(user.getUserId(), user.getUserPwd());
            if (isValid) {
                String token = userService.generateToken(user.getUserId()); // JWT 생성
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "token", token,
                        "message", "로그인 성공"
                ));
            } else {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "잘못된 아이디 또는 비밀번호입니다."
                ));
            }
        } catch (Exception e) {
        	 e.printStackTrace(); // 예외 로그 출력
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "로그인 중 문제가 발생했습니다: " + e.getMessage()
            ));
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
    
    //회원 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) {
        try {
            UserResponse userResponse = userService.getUserById(userId);
            return ResponseEntity.ok(userResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("사용자 조회 실패: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("사용자 조회 중 문제가 발생했습니다.");
        }
    }
}