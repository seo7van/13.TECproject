package com.tecProject.tec.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.tecProject.tec.domain.User;
import com.tecProject.tec.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 생성
    public User createUser(User user) {
        if (userRepository.existsByUserId(user.getUserId())) {
            throw new IllegalArgumentException("아이디가 이미 존재합니다.");
        }
        user.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
        User savedUser = userRepository.save(user);
        System.out.println("User Created: " + savedUser);
        return savedUser;
    }

    // 아이디로 사용자 검색
    public Optional<User> findByUserId(String userId) {
        return Optional.ofNullable(userRepository.findByUserId(userId));
    }

    // 비밀번호 확인
    public boolean checkPassword(String userId, String rawPassword) {
        Optional<User> userOpt = findByUserId(userId);
        if (userOpt.isPresent()) {
            boolean matches = passwordEncoder.matches(rawPassword, userOpt.get().getUserPwd());
            System.out.println("Password Match: " + matches);
            return matches;
        }
        System.out.println("User Not Found for ID: " + userId);
        return false;
    }

    // 아이디 중복확인
    public boolean isUserIdAvailable(String userId) {
        boolean isAvailable = !userRepository.existsByUserId(userId);
        System.out.println("Checking availability for userId: " + userId + " - Available: " + isAvailable);
        return isAvailable;
    }
    
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

}