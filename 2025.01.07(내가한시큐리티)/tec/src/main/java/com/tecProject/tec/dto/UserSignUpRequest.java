package com.tecProject.tec.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {

    private String userId;      // 사용자 아이디
    private String userName;    // 사용자 이름
    private String phone;       // 전화번호
    private String email;       // 이메일
    private String userPwd;     // 비밀번호
    private int userType;       // 사용자 타입 (1=관리자, 2=회원 등)
    private String ssnFirst;    // 주민등록번호 앞자리
    private String ssnSecond;   // 주민등록번호 뒷자리 (암호화 필요)
}
