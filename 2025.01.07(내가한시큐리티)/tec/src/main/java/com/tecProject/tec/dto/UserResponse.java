package com.tecProject.tec.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	
    private int userNo;        // 사용자 번호
    private String userId;     // 사용자 아이디
    private String userName;   // 사용자 이름
    private String email;      // 이메일
    private String phone;      // 전화번호
    private int userType;      // 회원 타입 (1=관리자, 2=회원)
}
