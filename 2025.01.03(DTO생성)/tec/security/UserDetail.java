package com.tecProject.tec.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tecProject.tec.domain.User;

// Spring Security의 UserDetails 구현
public class UserDetail implements UserDetails {

    private final User user;

    public UserDetail(User user) {
        this.user = user; // 생성할 때 사용자 정보를 저장
    }

    // 사용자의 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자의 유형에 따라 권한 부여
        String role = user.getUserType() == 1 ? "ADMIN" : "USER";
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
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
        // 계정이 만료되지 않았다고 가정 (필요 시 사용자 상태 추가로 확인 가능)
        return true;
    }

    // 계정이 잠기지 않았는지 확인
    @Override
    public boolean isAccountNonLocked() {
        // 계정이 잠기지 않았다고 가정 (추가 상태 필드가 있으면 활용 가능)
        return true;
    }

    // 비밀번호 만료 확인
    @Override
    public boolean isCredentialsNonExpired() {
        // 비밀번호가 만료되지 않았다고 가정
        return true;
    }

    // 계정 활성화 상태 확인
    @Override
    public boolean isEnabled() {
        // 계정이 활성화되었다고 가정
        return true;
    }

    // 사용자 객체 반환 (필요 시 활용 가능)
    public User getUser() {
        return this.user;
    }
}
