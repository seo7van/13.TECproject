import React, { useState, useEffect } from "react";
import axios from "axios";
import Editor from "@monaco-editor/react";
import './Styles/TranslateComponent.css';

function TranslateComponent() {
  const [keyword, setKeyword] = useState(""); // 입력창 입력값
  const [translation, setTranslation] = useState(""); // 번역결과
  const [language, setLanguage] = useState("Java"); // 기본값 "JAVA"
  const [activeDiv, setActiveDiv] = useState(null); // 클릭된 번역 영역
  const [isModalOpen, setIsModalOpen] = useState(false); // 기록 모달 활성/비활성 상태

  const [ipInfo, setIpInfo] = useState({ userType: "로딩 중..." }); // 클라이언트 IP 정보 초기값
  const [loading, setLoading] = useState(true); // Ip로딩 상태 관리
  const [memberClickCount, setMemberClickCount] = useState(0); // 회원 요청 횟수
  const [guestClickCount, setGuestClickCount] = useState(0); // 비회원 요청 횟수
  const maxClicksForMember = 50; // 회원 최대 요청 횟수
  const maxClicksForGuest = 30; // 비회원 최대 요청 횟수

  const [historys, setHistorys] = useState([]); // 번역 히스토리
  const [selectedItem, setSelectedItem] = useState(null); // 선택된 히스토리 항목
  const [historyKeyword, setHistoryKeyword] = useState("");
  const [historyTranslation, setHistoryTranslation] = useState("");

  // Monaco Editor의 내용을 상태로 저장
  const handleEditorChange = (value) => {
    setKeyword(value);
  }

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
    } catch (error) {
      if (error.response?.status === 403) {
        alert("요청 제한을 초과했습니다.");
      } else {
        console.error("요청 증가 실패: ", error.response?.data || error.message);
      }
    }
  };

  // 번역 요청
  const handleTranslate = async () => {
    if (!keyword.trim()) {
      alert("번역할 텍스트를 입력해주세요.");
      return;
    }

    // 요청 제한 확인
    if (ipInfo.userType === "회원" && memberClickCount >= maxClicksForMember) {
      alert("회원 요청 제한을 초과했습니다.");
      return;
    }
    if (ipInfo.userType === "비회원" && guestClickCount >= maxClicksForGuest) {
      alert("비회원 요청 제한을 초과했습니다.");
      return;
    }

    // 요청 횟수 증가
    await incrementClicks();

    try {
      const response = await axios.get("/api/code", {
        params: { origin: keyword.trim(), language },
      });
      const newTranslation = { original: keyword, translated: response.data, language };
      
      setTranslation(response.data); // 번역 결과 저장
      updateHistory(newTranslation); // 히스토리 업데이트
    } catch (error) {
      console.error("번역 오류: ", error.response?.data || error.message);
      setTranslation("번역에 실패했습니다."); // 오류 처리
    }
  };

  // 요청 횟수 조회
  useEffect(() => {
    if (ipInfo.ip) fetchClickCounts();
  }, [ipInfo]);


  // 클릭된 번역 이벤트
  useEffect(() => {
    const savedHistory = JSON.parse(sessionStorage.getItem("translationHistory")) || [];// sessionStorage에서 히스토리 불러오기
    setHistorys(savedHistory);
    const handleOutsideClick = (event) => {
      if (!event.target.closest('.content-mid-translateDiv-left') && 
      !event.target.closest('.content-mid-translateDiv-right')) {
        setActiveDiv(null);
      }
  };

    // 컴포넌트 언마운트 시 이벤트 제거
    document.addEventListener('click', handleOutsideClick);
      return () => {
        document.removeEventListener('click', handleOutsideClick);
  };
  }, []);

  // 히스토리 업데이트 및 저장소 동기화
  const updateHistory = (newItem) => {
    const updatedHistory = [newItem, ...historys].slice(0, 10); // 최근 10개까지만 유지
    setHistorys(updatedHistory);
    sessionStorage.setItem("translationHistory", JSON.stringify(updatedHistory));
  };

  // 기록 모달 열기/닫기
  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);
  
  // 기록 선택 시 원본 및 번역 결과 복원
  const handleSelectHistory = (item) => {
    setSelectedItem(item); // 선택된 항목 저장
  };

  // 선택된 기록을 기록에디터에 반영
  const applySelectedHistory = () => {
    if (selectedItem) {
      setHistoryKeyword(selectedItem.original);
      setHistoryTranslation(selectedItem.translated);
      closeModal();
    } else {
      alert("기록을 선택해주세요!");
    }
  };

  const saveTranslation = async () => {
    const confirmed = window.confirm("선택한 기록을 저장하시겠습니까?");
    if (!confirmed) return;

    try {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("로그인이 필요합니다.");
        return;
      }

      const dataToSave = {
        requestCode: historyKeyword || keyword, // 원본 코드
        responseCode: historyTranslation || translation, // 번역 결과
        typeCode: language // 번역 언어
      };
      if (!dataToSave.requestCode || !dataToSave.responseCode) {
        alert("저장할 번역 데이터가 없습니다.");
        return;
      }

      // 저장 요청
      await axios.post("/api/history", dataToSave,
        { headers: { Authorization: `Bearer ${token}` }} // JWT 토큰 포함
      );
      alert("기록이 저장되었습니다.");
    } catch (e) {
      alert("저장 실패");
      console.error("기록 저장 실패: ", e);
    }
  }

  return (
    <div className="content-all">
      <div className="content-top-buttonDiv">
        
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
        <button className="content-top-btn" onClick={openModal}>기록</button>
        <button className="content-top-btn">다운로드</button>
        <button className="content-top-btn" onClick={saveTranslation}>저장</button>
      </div>

      {/* 모달 */}
      {isModalOpen && (
        <div className="modal-backdrop" onClick={closeModal}>
          <div
            className="modal"
            onClick={(e) => e.stopPropagation()} // 모달 내부 클릭 시 닫히지 않음
          >
            <h4>번역 요청 기록</h4>
            <ul className="history-list">
              {historys.map((item, index) => (
                <li 
                key={index}
                className={`history-item ${selectedItem === item ? "selected" : ""}`}
                onClick={() => handleSelectHistory(item)}
                >
                  <p className="historys-item">
                    <strong>원본:</strong>{" "}
                    {item.original.length > 50 ? `${item.original.slice(0, 50)}...` : item.original}
                  </p>
                </li>
              ))}
            </ul>
            <div className="modal-buttons">
              <button onClick={applySelectedHistory}>선택</button>
              <button onClick={saveTranslation}>저장</button>
              <button onClick={closeModal}>닫기</button>
            </div>
          </div>
        </div>
      )}

      <div className="content-mid-translateDiv"> 
        <div className="dcontent-translateDiv-left"> {/* 왼쪽 영역 */}
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
                value={translation} // Monaco Editor에 표시될 값 또는 메시지 표시
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

      <div className="content-bottom-historyDiv">
        <div className="content-historyDiv-title">
          <p className="content-bottom-historyDiv-title">History</p> 
        </div>
        <div className="content-historyDiv-left"> {/* 왼쪽 영역 */}
          <p className="content-title">Original text</p>
            <div
              className={`content-bottom-historyDiv-left ${activeDiv === 'left' ? 'active' : ''}`}
              onClick={() => setActiveDiv('left')}
            >
              <Editor 
                height="200px"
                border="1px black solid"
                defaultLanguage="java" // 기본언어 설정
                value={historyKeyword} // Monaco Editor에 표시될 값
                onChange={handleEditorChange} // 값이 변경될 때 마다 상태값 변경
                option ={{
                  readOnly: true,
                  minimap: { enabled: true } // 미니맵 비활성화
                }}
              />
            </div>
        </div>
        <div className="content-historyDiv-right">{/* 오른쪽 영역 */}
          <p className="content-title">Changed text</p>
            <div
              className={`content-bottom-historyDiv-right ${activeDiv === 'right' ? 'active' : ''}`}
              onClick={() => setActiveDiv('right')}
            >
              <Editor 
                height="200px"
                defaultLanguage="java" // 기본언어 설정
                value={historyTranslation} // Monaco Editor에 표시될 값            
                option ={{
                readOnly: true,
                  minimap: { enabled: true } // 미니맵 비활성화
                }}
              />
            </div>
        </div>
      </div>

    </div>
  )
}

export default TranslateComponent;