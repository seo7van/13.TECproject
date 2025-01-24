package com.tecProject.tec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tecProject.tec.domain.UserSupport;

@Repository
public interface SupportRepository extends JpaRepository<UserSupport, Integer> {

	// 문의내역 전체조회(사용자)
	List<UserSupport> findByUsernameAndIsDeleted(String username, String isDeleted);
	
    // 제목 키워드로 삭제되지 않은 문의 조회
    List<UserSupport> findByTitleContainingAndUsernameAndIsDeleted(String title, String username, String isDeleted);

}
