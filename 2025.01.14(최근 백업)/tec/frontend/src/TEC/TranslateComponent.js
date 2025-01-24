import React, { useState, useEffect } from "react";
import axios from "axios";
import Editor from "@monaco-editor/react";
import './Styles/TranslateComponent.css';

function TranslateComponent() {
  const [keyword, setKeyword] = useState(""); // 입력창 입력값
  const [translation, setTranslation] = useState(""); // 번역결과
  const [language, setLanguage] = useState("Java"); // 기본값 "JAVA"
  const [activeDiv, setActiveDiv] = useState(null); // 클릭된 번역 영역
  const [ipInfo, setIpInfo] = useState({ ip: "로딩 중...", userType: "로딩 중..." }); // 클라이언트 IP 정보 초기값
  const [loading, setLoading] = useState(true); // 로딩 상태 관리

  // Monaco Editor의 내용을 상태로 저장
  const handleEditorChange = (value) => {
    setKeyword(value);
  };

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
  
      // 데이터 파싱
      const data = response.data;
      setIpInfo({
          ip: data.ip, // IP 주소
          isMember: data.isMember, // 회원 여부
      });
  
  const handleTranslate = async () => {
    try {
      // Step 1: 요청 횟수 증가
      const incrementResponse = await axios.get("/api/code/increment", {
        params: {
          ip: ipInfo.ip,
          isMember: ipInfo.isMember,
        },
      });
      console.log("요청 데이터 확인:", {
        ip: ipInfo.ip,
        isMember: ipInfo.isMember,
    });

      if (incrementResponse.status === 200) {
        // Step 2: 번역 요청
        const translationResponse = await axios.get("/api/code", {
          params: {
            origin: keyword,
            language,
          },
        });
        setTranslation(translationResponse.data);
      }
    } catch (error) {
      if (error.response?.status === 403) {
        alert("요청 제한 초과로 번역할 수 없습니다.");
      } else {
        console.error("오류 발생:", error.message);
      }
    }
  };

  // 클릭된 번역 이벤트
  useEffect(() => {
    const handleOutsideClick = (event) => {
      if (!event.target.closest('.content-mid-translateDiv-left') && 
      !event.target.closest('.content-mid-translateDiv-right')) {
        setActiveDiv(null);
      }
  };
    document.addEventListener('click', handleOutsideClick);
  // 컴포넌트 언마운트 시 이벤트 제거
      return () => {
        document.removeEventListener('click', handleOutsideClick);
  };

}, []);
  
  return (
    <div className="content-all">
      <div className="content-top-buttonDiv">
      {loading ? (
          <p>클라이언트 IP 정보 로딩 중...</p>
        ) : (
          <p>
            클라이언트 IP: {ipInfo.ip} / 사용자: {ipInfo.userType}
          </p>
        )}
        <select 
          className="select-language"
          value={language}
          onChange={(e) => setLanguage(e.target.value)}
        >
          <option value="Java">Java</option>
          <option value="Python">Python</option>
          <option value="JavaScript">JavaScript</option>
          <option value="CSharp">C#</option>
          <option value="CPlusPlus">C++</option>
          <option value="C">C</option>
          <option value="TypeScript">TypeScript</option>
          <option value="Kotlin">Kotlin</option>
          <option value="Ruby">Ruby</option>
          <option value="PHP">PHP</option>
        </select>
        <button className="content-top-btn">다운로드</button>
        <button className="content-top-btn">저장</button>
      </div>

      <div className="content-mid-translateDiv"> 
        <div className="content-translateDiv-left"> {/* 왼쪽 영역 */}
          <p className="content-title">Original text</p>
            <div
              className={`content-mid-translateDiv-left ${activeDiv === 'left' ? 'active' : ''}`}
              onClick={() => setActiveDiv('left')}
            >
              <Editor 
                height="200px"
                border="1px black solid"
                defaultLanguage="java" // 기본언어 설정
                value={keyword} // Monaco Editor에 표시될 값
                onChange={handleEditorChange} // 값이 변경될 때 마다 상태값 변경
                option ={{
                  minimap: { enabled: true } // 미니맵 비활성화
                }}
              />
            </div>
        </div>
        <div className="content-translateDiv-right">{/* 오른쪽 영역 */}
          <p className="content-title">Changed text</p>
            <div
              className={`content-mid-translateDiv-right ${activeDiv === 'right' ? 'active' : ''}`}
              onClick={() => setActiveDiv('right')}
            >
              <Editor 
                height="200px"
                defaultLanguage="java" // 기본언어 설정
                value={translation} // Monaco Editor에 표시될 값            
                option ={{
                readOnly: true,
                  minimap: { enabled: true } // 미니맵 비활성화
                }}
              />
            </div>
        </div>
      </div>

      <div className="content-buttom-translateBtn">
        <button className="translate-Btn" onClick={handleTranslate}>
          COMPARE
        </button>
      </div>
    </div>
  )
}

export default TranslateComponent;