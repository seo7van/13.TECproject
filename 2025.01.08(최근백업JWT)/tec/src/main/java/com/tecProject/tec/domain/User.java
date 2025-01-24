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
@Table(name = "USER")

public class User {
	
	//사용자 번호
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_NO")
	private int userNo;
		
		//회원타입 (1=관리자, 2=회원)
		@Column(name = "USER_TYPE", nullable = false)
		private int userType;
		
		//사용자 아이디
		@Column(name = "USER_ID", nullable = false, unique = true, length = 50)
		private String userId;
		
		//비밀번호(암호화)
		@Column(name = "USER_PWD", nullable = false, length = 255)
		private String userPwd;
		
		//이름(5자 까지)
		@Column(name = "USER_NAME", nullable = false, length = 50)
		private String userName;
		
		//이메일(아이디로 겸용가능)
		@Column(name = "EMAIL", nullable = false, unique = true, length = 100)
		private String email;
		
		//전화번호(전화번호 인증가능)
		@Column(name = "PHONE", length = 20)
		private String phone;
		
		//회원가입 일자
		@Column(name = "CREATE_DATE", nullable = false)
		@CreatedDate
		private LocalDateTime createDate;
		
		//비밀번호 변경일자
		@Column(name = "CHANGE_PW_DATE")
		@LastModifiedDate
		private LocalDateTime changePwDate;
		
		//주민등록번호 앞자리
	    @Column(name = "SSN_FIRST", nullable = false, length = 6)
	    private String ssnFirst;

	    //주민등록번호 뒷자리
	    @Column(name = "SSN_SECOND", nullable = false, length = 7)
	    private String ssnSecond;
}