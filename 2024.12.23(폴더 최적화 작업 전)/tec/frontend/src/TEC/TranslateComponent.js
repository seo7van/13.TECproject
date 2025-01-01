import React, { useState } from "react";
import axios from "axios";
import Header from './Header';

function TranslateComponent() {
  const [keyword, setKeyword] = useState(""); // 입력창 입력값
  const [translation, setTranslation] = useState(""); // 번역결과
  const [language, setLanguage] = useState("JAVA"); // 기본값 "JAVA"

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
          <textarea
            rows="10" 
            cols="60" 
            placeholder="번역할 코드를 입력하세요요." 
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            style={{ marginBottom: "10px", padding: "5px" }}
          />
          <br />
          <select
            value={language}
            onChange={(e) => setLanguage(e.target.value)}
            style={{ marginBottom: "10px" }}  
          >
            <option value="JAVA">JAVA</option>
            <option value="PYTHON">PYTHON</option>
            <option value="RUBY">RUBY</option>
          </select>
          <br />
          <button onClick={handleTranslate} style={{ padding: "5px 10px"}}>
            번역하기
          </button>
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
