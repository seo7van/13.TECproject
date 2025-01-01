import React, { useState } from "react";
import axios from "axios";
import Header from './Header';
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
        params: { origin: keyword, language }, // 서버에 보낼 쿼리 파라미터
      });
      setTranslation(response.data); // 서버에서 반환된 번역 결과 저장
    } catch (error) {
      console.error("번역 오류: ", error);
      setTranslation("번역에 실패했습니다."); // 오류 메시지 처리
    }
  };
  
  return (
    <div className="all">
      <Header />
        <div style = {{ padding: "20px" }}>
          <h1>코드 번역 테스트</h1>
          {/* Monaco Editor 적용 */}
          <Editor 
            height="300px"
            border="1px black solid"
            defaultLanguage="javascript" // 기본언어 설정
            value={keyword} // Monaco Editor에 표시될 값
            onChange={handleEditorChange} // 값이 변경될 때 마다 상태값 변경
            option ={{
              minimap: { enabled: true } // 미니맵 비활성화
            }}
          />

          {/*
          <textarea
            rows="10" 
            cols="60" 
            placeholder="번역할 코드를 입력하세요요." 
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            style={{ marginBottom: "10px", padding: "5px" }}
          />
          */}

          <br />
          {/* 개발언어 선택 */}
          <select
            value={language}
            onChange={(e) => setLanguage(e.target.value)}
            style={{ marginBottom: "10px" }}  
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
          <br />
          {/* 번역 버튼 */}
          <button onClick={handleTranslate} style={{ padding: "5px 10px"}}>
            번역하기
          </button>
          {/* 번역 결과 */}
          <div style={{ marginTop: "20px", border: "1px black solid", padding: "10px" }}>
            {translation && (
              <pre
                style={{
                  whiteSpace: "pre-wrap",
                  wordWrap: "break-word",
                  textAlign: "left", // 왼쪽 정렬
                  margin: 0, // 여백 제거
                  fontFamily: "monospace", // 코드를 표현하는 고정폭 글꼴
                  }}
                >
                <strong>번역 결과:</strong>
                {"\n"}
                {translation}
              </pre>
            )}
          </div>
        </div>
    </div>
  )
}

export default TranslateComponent;
