package com.tecProject.tec.repository;

import com.tecProject.tec.domain.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

// 시큐리티
public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByUserId(String userId);
}
