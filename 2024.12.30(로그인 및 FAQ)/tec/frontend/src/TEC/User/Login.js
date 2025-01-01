import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from 'react-router-dom';

function Login() {
    const [userId, setUserId] = useState(""); // ID
    const [password, setPassword] = useState(""); // 비밀번호
    const [error, setError] = useState(''); // 에러 메시지

    const handleLogin = (event) => {
        event.preventDefault();

        try {
            // 서버에 로그인 요청 보내기
            axios.post("http://localhost:8080/UserLogin", {
                userId: userId,
                password: password,
            });

            // 성공 메시지 처리
            setError("로그인 성공! 🎉");
        } catch (err) {
            // 오류 처리
            if (err.response && err.response.status === 401) {
                setError("로그인 실패! 아이디 또는 비밀번호를 확인하세요.");
            } else {
                console.error("로그인 실패:", err);
                setError("서버와 통신에 실패했습니다. 나중에 다시 시도해주세요.");
            }
        }
    };

    return (
        <div style={{ textAlign: "center", marginTop: "50px" }}>
            <h1>로그인</h1>
            <form onSubmit={handleLogin} style={{ display: "inline-block", textAlign: "left" }}>
                <div>
                    <label htmlFor="username">아이디:</label>
                    <input
                        type="text"
                        id="userId"
                        name="username"
                        value={userId}
                        onChange={(e) => setUserId(e.target.value)}
                        required
                    />
                </div>
                <div style={{ marginTop: "10px" }}>
                    <label htmlFor="password">비밀번호:</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" style={{ marginTop: "20px" }}>로그인</button>
                {error && (
                    <p style={{ color: error.includes("성공") ? "green" : "red" }}>{error}</p>
                )}
            </form>
        </div>
    );
}

export default Login;