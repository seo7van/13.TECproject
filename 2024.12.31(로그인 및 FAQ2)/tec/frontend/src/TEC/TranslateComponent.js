import React, { useState } from "react";
import axios from "axios";
import './Styles/TranslateComponent.css';
import Editor from "@monaco-editor/react"; // Monaco Editor import

function TranslateComponent() {
  const [keyword, setKeyword] = useState(""); // 입력창 입력값
  const [translation, setTranslation] = useState(""); // 번역결과
  const [language, setLanguage] = useState("Java"); // 기본값 "JAVA"

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
  
  return (
    <div className="content-all-page">
      {/* 콘텐츠 전체 div */}
      <div className="content-all">
        
        {/* 콘텐츠 상단 버튼 */}
        <div className="content-top-buttonDiv">
          {/* 개발언어 선택 */}
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
          <button>다운로드</button>
          <button>저장</button>
        </div>

        {/* 번역칸 div Monaco Editor 적용 */}
        <div className="content-mid-translateDiv">
          <div className="content-mid-translateDiv-left">
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

          {/* 번역 결과 */}
          <div className="content-mid-translateDiv-right">
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

        {/* 번역 버튼 div */}
        <div className="content-buttom-translateBtn">
          {/* 번역 버튼 */}
          <button className="translate-Btn" onClick={handleTranslate}>
            번역하기
          </button>
        </div>
      </div>
    </div>
  )
}

export default TranslateComponent;
