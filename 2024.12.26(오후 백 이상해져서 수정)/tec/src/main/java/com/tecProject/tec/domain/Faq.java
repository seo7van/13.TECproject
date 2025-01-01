package com.tecProject.tec.domain;

import java.time.LocalDate;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "FAQ")

public class Faq {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FAQ_NO", nullable = false)
	private Long faqNo;
	
		@Column(name = "USER_ID" , nullable = false , length = 12)
		private String userId;
		  
		
		@Column(name = "QUESTION", nullable = false, length = 50)
		private String question;
		  
		@Column(name = "ANSWER", nullable = false, length = 400)
		private String answer;
		  
		@Column(name = "CREATED_DATE", nullable = false)
		private LocalDate createDate;
		  
		@Column(name = "MODIFIED_DATE", nullable = false)
		private LocalDate modifiedDate;
		  
		@Column(name = "IS_DELETED", nullable = false, length = 2)
		private String isDeleted;
		  
		@Column(name = "DELETED_DATE", nullable = false)
		private LocalDate deletedDate;
}