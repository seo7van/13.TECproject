import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from 'react-router-dom';

function SignUp() {
    const [userId, setUserId] = useState("");
    const [userPwd, setUserPwd] = useState("");
    const [userName, setUserName] = useState("");
    const [message, setMessage] = useState("");

    const handleSubmit = async (event) => {
        event.preventDefault(); // 기본 폼 제출 방지
        try {
            const response = await axios.post("http://localhost:8080/SignUp", {
                userId: userId,
                userPwd: userPwd,
                userName: userName,
            });

            // 회원가입 성공 처리
            setMessage("회원가입 성공! 로그인 페이지로 이동하세요.");
        } catch (err) {
            console.error("회원가입 실패:", err);
            if (err.response && err.response.data) {
                setMessage(err.response.data.message || "회원가입 실패! 다시 시도해주세요.");
            } else {
                setMessage("서버와 통신에 실패했습니다.");
            }
        }
    };

    return (
        <div style={{ textAlign: "center", marginTop: "50px" }}>
            <h1>회원가입</h1>
            <form onSubmit={handleSubmit} style={{ display: "inline-block", textAlign: "left" }}>
                <div>
                    <label htmlFor="userId">아이디:</label>
                    <input
                        type="text"
                        id="userId"
                        name="userId"
                        value={userId}
                        onChange={(e) => setUserId(e.target.value)}
                        required
                    />
                </div>
                <div style={{ marginTop: "10px" }}>
                    <label htmlFor="userPwd">비밀번호:</label>
                    <input
                        type="password"
                        id="userPwd"
                        name="userPwd"
                        value={userPwd}
                        onChange={(e) => setUserPwd(e.target.value)}
                        required
                    />
                </div>
                <div style={{ marginTop: "10px" }}>
                    <label htmlFor="userName">이름:</label>
                    <input
                        type="text"
                        id="userName"
                        name="userName"
                        value={userName}
                        onChange={(e) => setUserName(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" style={{ marginTop: "20px" }}>회원가입</button>
                {message && <p style={{ color: message.includes("성공") ? "green" : "red" }}>{message}</p>}
            </form>
        </div>
    );
}

export default SignUp;
