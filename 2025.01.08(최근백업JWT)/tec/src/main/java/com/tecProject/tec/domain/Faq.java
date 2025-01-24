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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "FAQ")

public class Faq {
	
	//FAQ 번호
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FAQ_NO", nullable = false)
	private Long faqNo;
		
		//회원 ID (항상 있어야하지만 일단 없앰)
		@Column(name = "USER_ID" , nullable = true , length = 12)
		private Long userId;
		  
		//질문 내용
		@Column(name = "QUESTION", nullable = false, length = 50)
		private String question;
		  
		//답변 내용
		@Column(name = "ANSWER", nullable = false, length = 400)
		private String answer;
		 
		//생성일자
		@Column(name = "CREATED_DATE", nullable = false)
		private LocalDateTime createDate;
		  
		//수정일자
		@Column(name = "MODIFIED_DATE", nullable = true)
		private LocalDateTime modifiedDate;
		  
		//글삭제여부
		@Column(name = "IS_DELETED", nullable = false, length = 2)
		private String isDeleted;
		 
		//글삭제일자
		@Column(name = "DELETED_DATE", nullable = true) //null값 안들어가서 바꿈
		private LocalDateTime deletedDate;
}