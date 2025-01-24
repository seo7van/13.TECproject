import React, { useState, useEffect } from "react";
import axios from "axios";

function History() {
  const [historys, setHistorys] = useState([]); // 번역 히스토리
  const [selectedItem, setSelectedItem] = useState(null); // 선택된 히스토리 항목
  const [historyKeyword, setHistoryKeyword] = useState("");
  const [historyTranslation, setHistoryTranslation] = useState("");
  
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
  
    document.addEventListener('click', handleOutsideClick);
  
    // 컴포넌트 언마운트 시 이벤트 제거
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
    
  )
}

export default History;