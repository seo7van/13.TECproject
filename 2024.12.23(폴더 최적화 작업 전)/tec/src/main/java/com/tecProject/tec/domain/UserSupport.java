package com.tecProject.tec.domain;

import java.time.LocalDate;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "USER_SUPPORT")
public class UserSupport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "INQUIRY_NO", nullable = false)
	private int inquiryNo;
	
	@Column(name = "USER_ID", nullable = false, length = 12)
	private String userId;
	
	@Column(name = "TYPE", nullable = false)
	private int type;
	
	@Column(name = "TITLE", nullable = false, length = 100)
	private String title;
	
	@Column(name = "INQUIRY", nullable = false, length = 4000)
	private String inquiry;
	
	@Column(name = "CREATED_DATE", nullable = false, updatable = false)
	@CreatedDate
	private LocalDate createdDate;
	
	@Column(name = "MODIFIED_DATE", nullable = false)
	@LastModifiedDate
	private LocalDate modifiedDate;
	
	@Column(name = "IS_DELETED", nullable = false, length = 2)
	private String isDeleted;
	
	@Column(name = "DELETED_DATE")
	@LastModifiedDate
	private LocalDate deletedDate;
}
