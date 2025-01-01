package com.tecProject.tec.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import com.tecProject.tec.domain.User;

// 로그인
public class UserDetail implements UserDetails {

   private final User user;
   
    public UserDetail(User user) {
        this.user = user; // 생성할 때 사용자 정보 저장
    }
    
    // 사용자가 어떤 권한을 가졌는지 알려줌
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자의 권한을 리스트로 만들어 반환
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getUserType()));
    }
    
    // 사용자 비밀번호 반환
    @Override
    public String getPassword() {
        return user.getUserPwd();
    }
    
    // 사용자 아이디 반환
    @Override
    public String getUsername() {
        return user.getUserId();
    }
    
    // 계정 만료 확인
    @Override
    public boolean isAccountNonExpired() {
        return true; // 언제나 계정이 만료되지 않은 상태 유지
    }
    
    // 계정이 잠기지 않았는지 확인 (항상 true로 설정: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true; // 언제나 계정이 잠기지 않은 상태로 유지합니다.
    }
    
    // 비밀번호 만료 확인 (항상 true로 설정: 잠기지 않음)
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 언제나 비밀번호가 만료되지 않은 상태로 유지합니다.
    }
    
    // 계정 활성화 상태 확인 (항상 true로 설정: 활성화됨)
    @Override
    public boolean isEnabled() {
        return true; // 계정이 항상 활성화된 상태로 유지합니다.
    }
}
