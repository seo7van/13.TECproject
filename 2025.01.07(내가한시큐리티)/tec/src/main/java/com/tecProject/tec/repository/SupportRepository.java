package com.tecProject.tec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tecProject.tec.domain.UserSupport;

@Repository
public interface SupportRepository extends JpaRepository<UserSupport, Integer> {

	// 문의내역 전체조회(사용자)
	List<UserSupport> findByUserIdAndIsDeleted(String userId, String isDeleted);

}
