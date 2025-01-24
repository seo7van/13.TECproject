package com.tecProject.tec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tecProject.tec.domain.Faq;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long>{
	
    // deletedDate가 null인 데이터만 가져오고 faqNo로 정렬
    List<Faq> findByDeletedDateIsNullOrderByFaqNo();
}