import React, { useState, useEffect } from "react";
import axios from "axios";
import Editor from "@monaco-editor/react";
import './Styles/TranslateComponent.css';

function TranslateComponent() {
  const [keyword, setKeyword] = useState(""); // 입력창 입력값
  const [translation, setTranslation] = useState(""); // 번역결과
  const [language, setLanguage] = useState("Java"); // 기본값 "JAVA"
  const [activeDiv, setActiveDiv] = useState(null); // 클릭된 번역 영역

  const handleEditorChange = (value) => {
    setKeyword(value); // Monaco Editor의 내용을 상태로 저장
  }

  const handleTranslate = async () => {
    try {
      const response = await axios.get("/api/code", {
        params: { origin: keyword.trim(), language }, // 서버에 보낼 쿼리 파라미터
      });
      setTranslation(response.data); // 서버에서 반환된 번역 결과 저장
    } catch (error) {
      console.error("번역 오류: ", error.response?.data || error.message);
      setTranslation("번역에 실패했습니다."); // 오류 메시지 처리
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