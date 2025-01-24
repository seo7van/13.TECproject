package com.tecProject.tec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tecProject.tec.domain.SaveHistory;

@Repository
public interface SaveHistoryRepository extends JpaRepository<SaveHistory, Integer> {

}
