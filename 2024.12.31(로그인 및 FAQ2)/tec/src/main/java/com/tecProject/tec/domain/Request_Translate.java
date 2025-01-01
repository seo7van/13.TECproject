package com.tecProject.tec.domain;

import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "REQUEST_TRANSLATE")

public class Request_Translate {
	
	//고유요청식별자(시퀀스)_Primary Key
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "REQUEST_NO", nullable = false)
	private Integer requestNo;
	
		//사용자ID
		@Column(name = "USER_ID", length = 30)
	    private String userId;
		
		//번역요청시간
		@Column(name = "REQUEST_TIME", nullable = false)
	    private LocalTime requestTime;
		
		//번역할 코드언어
		@Column(name = "TYPE_CODE", length = 20, nullable = false)
	    private String typeCode;
		
		//요청된 원본코드 및 메타데이터
		@Lob // 긴 내용을 저장할 때 사용
		@Column(name = "REQUEST_CODE", columnDefinition = "JSON")//요청코드와 메타데이를 JSON형태로 저장
	    private String requestCode;
		
		//요청코드에 대한 응답 데이터
		@Lob
		@Column(name = "RESPONSE_CODE", columnDefinition = "JSON")
	    private String responseCode;
		
		//코드 실행 흐름 또는 코드 역할 설명
		@Lob //긴 설명 저장하는데 사용
		@Column(name = "CODE_DESCRIPTION", columnDefinition = "TEXT")
	    private String codeDescription;
}