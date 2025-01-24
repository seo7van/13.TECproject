package com.tecProject.tec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tecProject.tec.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	
	Boolean existsByUserName(String userName);

	User findByUserName(String userName);


	boolean existsByEmail(String email);
}