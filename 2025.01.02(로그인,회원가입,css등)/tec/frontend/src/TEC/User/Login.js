import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Login = ({ setIsLoggedIn }) => {
  const [credentials, setCredentials] = useState({
    userId: "",
    userPwd: "",
  });
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();
  
  useEffect(() => {
    // sessionStorage에서 userId를 가져옴
    const sessionUserId = sessionStorage.getItem("userId");
  
    if (sessionUserId) {
      // sessionStorage에 userId가 있는 경우 상태를 설정
      setCredentials((prevState) => ({ ...prevState, userId: sessionUserId }));
    } else {
      // sessionStorage에 userId가 없는 경우 localStorage에서 userId를 삭제
      localStorage.removeItem("userId");
    }
  }, []);
  
  const handleChange = (e) => {
    setCredentials({ ...credentials, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsLoading(true);
    setError("");
    try {
      const response = await axios.post(
        "http://localhost:8080/user/login",
        credentials,
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        }
      );
  
      console.log("Response data:", response.data);
  
      if (response.data && response.data.success) {
        setIsLoggedIn(true);
        console.log("로그인 성공:", response.data);
  
        // sessionStorage와 localStorage에 사용자 ID 저장
        sessionStorage.setItem("userId", credentials.userId);
        localStorage.setItem("userId", credentials.userId);
  
        navigate("/");
      } else {
        const errorMessage = response.data.message || "로그인 실패";
        console.warn("로그인 실패:", errorMessage);
        setError(errorMessage);
      }
    } catch (error) {
      if (error.response) {
        console.error("서버 오류:", error.response.data);
        setError(error.response.data.message || "서버 오류 발생");
      } else {
        console.error("네트워크 오류:", error.message);
        setError("네트워크 연결 실패");
      }
    } finally {
      setIsLoading(false);
    }
  };
  
  const handleSignUp = () => {
    navigate("/User/SignUp");
  };

  return (
    <div style={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100vh", backgroundColor: "#f0f0f0" }}>
      <form
        onSubmit={handleSubmit}
        style={{
          width: "300px",
          padding: "20px",
          backgroundColor: "#fff",
          border: "1px solid #ccc",
          borderRadius: "5px",
          boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
        }}
      >
        <h2 style={{ textAlign: "center", marginBottom: "20px" }}>로그인</h2>

        {error && (
          <div style={{ color: "red", marginBottom: "10px", textAlign: "center" }}>
            {error}
          </div>
        )}

        <div style={{ marginBottom: "10px" }}>
          <label htmlFor="userId" style={{ display: "block", marginBottom: "5px" }}>아이디</label>
          <input
            type="text"
            name="userId"
            id="userId"
            value={credentials.userId}
            onChange={handleChange}
            required
            style={{
              width: "100%",
              padding: "8px",
              border: "1px solid #ccc",
              borderRadius: "4px",
            }}
          />
        </div>

        <div style={{ marginBottom: "20px" }}>
          <label htmlFor="userPwd" style={{ display: "block", marginBottom: "5px" }}>비밀번호</label>
          <input
            type="password"
            name="userPwd"
            id="userPwd"
            value={credentials.userPwd}
            onChange={handleChange}
            required
            style={{
              width: "100%",
              padding: "8px",
              border: "1px solid #ccc",
              borderRadius: "4px",
            }}
          />
        </div>

        <button
          type="submit"
          disabled={isLoading}
          style={{
            width: "100%",
            padding: "10px",
            backgroundColor: "#007bff",
            color: "#fff",
            border: "none",
            borderRadius: "4px",
            cursor: isLoading ? "not-allowed" : "pointer",
          }}
        >
          {isLoading ? "로딩중..." : "로그인"}
        </button>

        <button
          type="button"
          onClick={handleSignUp}
          style={{
            width: "100%",
            padding: "10px",
            marginTop: "10px",
            backgroundColor: "#6c757d",
            color: "#fff",
            border: "none",
            borderRadius: "4px",
            cursor: "pointer",
          }}
        >
          회원가입
        </button>
      </form>
    </div>
  );
};

export default Login;