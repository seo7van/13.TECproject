import React, { useState } from "react";
import axios from "axios";

function TranslateComponent() {
  const [keyword, setKeyword] = useState(""); // 입력창 입력값
  const [translation, setTranslation] = useState(""); // 번역결과

  const handleTranslate = async () => {
    try {
      const response = await axios.get("/api/code", {
        params: { origin: keyword }, // 서버에 보낼 쿼리 파라미터
      });
      setTranslation(response.data); // 서버에서 반환된 번역 결과 저장
    } catch (error) {
      console.error("Error fetching translation:", error);
      setTranslation("번역에 실패했습니다."); // 오류 메시지 처리
    }
  };
  
  return (
    <div style = {{ padding: "20px" }}>
      <h1>글자번역 테스트</h1>
      <textarea
        rows="4" 
        cols="50" 
        placeholder="번역할 문장을 입력하시오." 
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
        style={{ marginBottom: "10px", padding: "5px" }}
      />
      <br />
      <button onClick={handleTranslate} style={{ padding: "5px 10px"}}>
        번역하기
      </button>
      <div style={{ marginTop: "20px", border: "1px black solid", height: "50px"}}>
        {translation && <p><strong>번역결과:</strong> {translation}</p>}
      </div>
    </div>
  )
}

export default TranslateComponent;
