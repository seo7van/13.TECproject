package com.tecProject.tec.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tecProject.tec.domain.User;
import com.tecProject.tec.dto.UserResponse;
import com.tecProject.tec.repository.UserRepository;
import com.tecProject.tec.security.JwtTokenUtil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

 
    //사용자 이름(아이디)로 정보 가져오는 메서드
    public UserDetails loadUserByUserName(String username) throws UsernameNotFoundException {
        //데이터베이스에서 userName으로 사용자 정보를 찾음
        User user = userRepository.findByUserId(username);
        if (user == null) {
            //사용자가 없으면 에러
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }
        
        //사용자 타입에 따라 관리자 또는 일반 사용자 역할(권한)을 설정
        String role = user.getUserType() == 1 ? "ROLE_ADMIN" : "ROLE_USER";
        
        //Spring Security에서 사용하는 User 객체를 만들어 반환
        return new org.springframework.security.core.userdetails.User(
            user.getUserId(), //사용자 아이디
            user.getUserPwd(), //사용자 비밀번호
            Collections.singletonList(new SimpleGrantedAuthority(role)) //사용자의 역할(권한)
        );
    }
    
    //JWT 생성 메서드
    public String generateToken(String userId) {
        return jwtTokenUtil.generateToken(userId);
    }

    //새 사용자를 데이터베이스에 추가하는 메서드
    public User createUser(User user) {
        // 아이디가 이미 데이터베이스에 있는지 확인해요.
        if (userRepository.existsByUserId(user.getUserId())) {
            // 같은 아이디가 있다면 에러를 내요.
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        //비밀번호를 안전하게 암호화
        user.setUserPwd(passwordEncoder.encode(user.getUserPwd()));
        
        //데이터베이스에 사용자를 저장하고 저장된 사용자 정보를 반환해요.
        return userRepository.save(user);
    }
    
    private List<SimpleGrantedAuthority> getAuthoritiesFromUserType(int userType) {
        // userType에 따라 권한 설정
        if (userType == 1) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (userType == 2) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            throw new IllegalArgumentException("Unknown userType: " + userType);
        }
    }
    
    //아이디로 사용자 검색
    public Optional<User> findByUserId(String userId) {
        return Optional.ofNullable(userRepository.findByUserId(userId));
    }
    
    //아이디 중복확인
    public boolean isUserIdAvailable(String userId) {
        boolean isAvailable = !userRepository.existsByUserId(userId);
        System.out.println("Checking availability for userId: " + userId + " - Available: " + isAvailable);
        return isAvailable;
    }

    //이메일 중복 확인
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
    
    //UserResponse회원 조회
    public UserResponse getUserById(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        return UserResponse.builder()
                .userNo(user.getUserNo())
                .userId(user.getUserId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .userType(user.getUserType())
                .build();
    }
    
    // 비밀번호 검증 메서드
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        // 입력된 비밀번호(rawPassword)와 암호화된 비밀번호(encodedPassword) 비교
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}