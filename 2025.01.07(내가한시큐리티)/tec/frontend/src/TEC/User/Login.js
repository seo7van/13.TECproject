import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Login = () => {
  const [credentials, setCredentials] = useState({ userId: "", userPwd: "" });
  const [error, setError] = useState("");
  const navigate = useNavigate();
  
  // 입력값 변경 핸들러
  const handleChange = (e) => {
    setCredentials({ ...credentials, [e.target.name]: e.target.value });
  };

  // 로그인 요청 핸들러
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      // 로그인 요청
      const response = await axios.post(
        "http://localhost:8080/user/login",
        credentials,
        {
          headers: { "Content-Type": "application/json" },
        }
      );

      if (response.data && response.data.token) {
        const token = response.data.token;
        localStorage.setItem("jwtToken", token); // JWT 저장
        console.log("로그인 성공! 저장된 토큰:", token);
        navigate("/"); // 로그인 성공 후 대시보드로 이동
      } else {
        setError("로그인에 실패했습니다.");
      }
    } catch (error) {
      setError("서버 오류: " + (error.response?.data?.message || error.message));
    }
  };

  // 인증된 요청 보내기
  const sendAuthorizedRequest = async () => {
    const token = localStorage.getItem("jwtToken"); // JWT 가져오기
    if (!token) {
      setError("JWT가 없습니다. 다시 로그인하세요.");
      return;
    }

    try {
      // 인증된 요청
      const response = await axios.get(
        "http://localhost:8080/Admin/faqs/check-admin",
        {
          headers: {
            Authorization: `Bearer ${token}`, // JWT를 Authorization 헤더에 추가
          },
        }
      );

      console.log("관리자 데이터:", response.data);
    } catch (error) {
      if (error.response?.status === 401) {
        setError("인증 실패: 다시 로그인하세요.");
        localStorage.removeItem("jwtToken"); // 유효하지 않은 JWT 제거
        navigate("/login"); // 로그인 페이지로 이동
      } else {
        setError("인증된 요청 실패: " + (error.response?.data?.message || error.message));
      }
    }
  };

  // 회원가입 페이지로 이동
  const handleSignUp = () => {
    navigate("/User/SignUp"); // "/User/SignUp" 경로로 이동
  };

  return (
    <div className="login-container">
      <form onSubmit={handleSubmit}>
        <h2>로그인</h2>
        {error && <p className="error">{error}</p>}
        <input
          type="text"
          name="userId"
          placeholder="아이디"
          value={credentials.userId}
          onChange={handleChange}
          required
        />
        <input
          type="password"
          name="userPwd"
          placeholder="비밀번호"
          value={credentials.userPwd}
          onChange={handleChange}
          required
        />
        <button type="submit">로그인</button>
      </form>
      <button onClick={sendAuthorizedRequest} className="auth-request-button">
        인증된 요청 테스트
      </button>
      <button onClick={handleSignUp} className="signup-button">
        회원가입
      </button>
    </div>
  );
};

export default Login;