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
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_NO")
	private int userNo;
	
		@Column(name = "USER_TYPE", nullable = false)
		private int userType;
		
		@Column(name = "USER_ID", nullable = false, unique = true, length = 50)
		private String userId;
		
		@Column(name = "USER_PWD", nullable = false, length = 255)
		private String userPwd;
		
		@Column(name = "USER_NAME", nullable = false, length = 50)
		private String userName;
		
		@Column(name = "EMAIL", nullable = false, unique = true, length = 100)
		private String email;
		
		@Column(name = "PHONE", length = 20)
		private String phone;
		
		@Column(name = "CREATE_DATE", nullable = false)
		@CreatedDate
		private LocalDateTime createDate;
		
		@Column(name = "CHANGE_PW_DATE")
		@LastModifiedDate
		private LocalDateTime changePwDate;
}