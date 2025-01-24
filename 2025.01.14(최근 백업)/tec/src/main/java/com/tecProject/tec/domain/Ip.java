package com.tecProject.tec.domain;

import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table (name = "IP")

public class Ip {
	
	//아이피 ID (Primary Key)
	@Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IP_ID", nullable = false)
	private Long ipid;
		
		//IP주소 (IP 주소는 비어 있을 수 없고, 중복될 수 없음)
	    @Column(name = "IP_ADDRESS", nullable = false, unique = true)
	    private String ipAddress;

	    //IP당 요청횟수 (요청 횟수는 비어 있을 수 없음)
	    @Column(name = "IP_COUNT", nullable = false)
	    private int IpCount = 0; //기본값 설정

	    //IP라스트 요청시간 (마지막 요청 시간도 비어 있을 수 없음)
	    @Column(name = "LAST_REQUEST", nullable = false) 
	    private LocalDateTime lastRequest;
}