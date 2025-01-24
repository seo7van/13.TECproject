package com.tecProject.tec.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tecProject.tec.domain.Ip;

@Repository
public interface IpRepository extends JpaRepository<Ip, Long>{
	//Ip 주소로 데이터를 찾는 메서드
	Optional<Ip> findByIpAddress(String ipAddress);

}
