package com.tecProject.tec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tecProject.tec.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    // 사용자 아이디로 검색
    User findByUserId(String userId);

    // 아이디 중복 여부 확인
    boolean existsByUserId(String userId);
    
    //이메일 중복 여부 확인 
    boolean existsByEmail(String email);
}