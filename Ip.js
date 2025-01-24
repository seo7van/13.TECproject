import React, { useState, useEffect } from "react";
import axios from "axios";
import './Styles/TranslateComponent.css';

function Ip ({ onIncrementClicks }) {

  const [ipInfo, setIpInfo] = useState({ userType: "로딩 중..." }); // 클라이언트 IP 정보 초기값
  const [loading, setLoading] = useState(true); // Ip로딩 상태 관리
  const [memberClickCount, setMemberClickCount] = useState(0); // 회원 요청 횟수
  const [guestClickCount, setGuestClickCount] = useState(0); // 비회원 요청 횟수 관리
  
  const maxClicksForMember = 50; // 회원 최대 요청 횟수
  const maxClicksForGuest = 30; // 비회원 최대 클릭 횟수
  
  // 서버로부터 IP와 회원 여부 정보 가져오기
  const fetchIpInfo = async () => {
    try {
      setLoading(true); // 로딩 시작
      const response = await axios.get("/ip/check-ip", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token") || ""}`, // JWT 토큰
        },
      });

      // 서버에서 반환된 데이터를 상태에 저장
      const data = response.data.split(", ");
      setIpInfo({
        ip: data[0].split(": ")[1], // 클라이언트 IP
        userType: data[2].split(": ")[1], // 회원/비회원 여부
      });
    } catch (error) {
      console.error("IP 정보 가져오기 실패: ", error.response?.data || error.message);
      setIpInfo({ ip: "오류 발생", userType: "비회원" }); // 실패 시 기본값 설정
    } finally {
      setLoading(false); // 로딩 종료
    }
  };

  // 컴포넌트 마운트 시 IP 정보 가져오기
  useEffect(() => {
    fetchIpInfo();
  }, []);

  // 요청 횟수 조회
  const fetchClickCounts = async () => {
    try {
      const response = await axios.get("/api/code/clicks", {
        headers: { Authorization: `Bearer ${localStorage.getItem("token") || ""}` },
      });
      if (ipInfo.userType === "회원") {
        setMemberClickCount(response.data.memberClicks);
      } else {
        setGuestClickCount(response.data.guestClicks);
      }
    } catch (error) {
      console.error("요청 횟수 조회 실패: ", error.response?.data || error.message);
    }
  };

  // 요청 횟수 증가
  const incrementClicks = async () => {
    try {
      await axios.post("/api/code/increment-clicks", null, {
        headers: { Authorization: `Bearer ${localStorage.getItem("token") || ""}` },
      });
      if (ipInfo.userType === "회원") {
        setMemberClickCount((prev) => prev + 1);
      } else {
        setGuestClickCount((prev) => prev + 1);
      }

      // 요청 횟수 증가가 완료된 후 콜백 호출
      onIncrementClicks && onIncrementClicks();
      
    } catch (error) {
      if (error.response?.status === 403) {
        alert("요청 제한을 초과했습니다.");
      } else {
        console.error("요청 증가 실패: ", error.response?.data || error.message);
      }
    }
  };

 // handleTranslate에 incrementClicks 전달
  useEffect(() => {
    incrementClicks();
  }, []);

  // 요청 횟수 조회
  useEffect(() => {
    if (ipInfo.ip) fetchClickCounts();
  }, [ipInfo]);  

  return (
    <div className="ip">
      {loading ? (
          <p>로딩 중...</p>
        ) : (
          <p>현재 사용자: {ipInfo.userType}</p>
        )}
        {ipInfo.userType === "회원" && (
          <p>회원 요청 횟수: {memberClickCount} / 최대: {maxClicksForMember}</p>
        )}
        {ipInfo.userType === "비회원" && (
          <p>비회원 요청 횟수: {guestClickCount} / 최대: {maxClicksForGuest}</p>
        )}
    </div>
  );
}

export default Ip;