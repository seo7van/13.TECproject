package com.tecProject.tec.domain;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "USER_SUPPORT")

public class UserSupport {
	
	//고객문의번호
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "INQUIRY_NO", nullable = false)
	private int inquiryNo;
	
		//회원 ID
		@Column(name = "USER_ID", nullable = false, length = 12)
		private String userId;
		
		//카테고리 타입
		@Column(name = "TYPE", nullable = false)
		private int type;
		
		//문의글 제목
		@Column(name = "TITLE", nullable = false, length = 100)
		private String title;
		
		//문의 내용
		@Column(name = "INQUIRY", nullable = false, length = 4000)
		private String inquiry;
		
		//생성일자
		@Column(name = "CREATED_DATE", nullable = false, updatable = false)
		@CreatedDate
		private LocalDateTime createdDate;
		
		//수정 일자
		@Column(name = "MODIFIED_DATE", nullable = false)
		@LastModifiedDate
		private LocalDateTime modifiedDate;
		
		//글 삭제여부
		@Column(name = "IS_DELETED", nullable = false, length = 2)
		private String isDeleted;
		
		//글 삭제일자
		@Column(name = "DELETED_DATE")
		@LastModifiedDate
		private LocalDateTime deletedDate;
}