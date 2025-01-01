
import React, { useState } from "react";
import axios from "axios";

function CodeManagement() {
  const [keyword, setKeyword] = useState(""); // 입력 또는 수정할 키워드
  const [suggestions, setSuggestions] = useState([]); // 자동 완성 제안 목록
  const [translations, setTranslations] = useState({
    Java: "",
    Python: "",
    JavaScript: "",
    CSharp: "",
    CPlusPlus: "",
    C: "",
    TypeScript: "",
    Kotlin: "",
    Ruby: "",
    PHP: "",
  });

  // 키워드 입력 핸들러
  const handleKeywordChange = async (e) => {
    const value = e.target.value;
    setKeyword(value);

    // AJAX 요청으로 일치하는 키워드 검색
    if (value.trim()) {
      try {
        const response = await axios.get(`/admin/code/suggestions`, {
          params: { query: value },
        });
        setSuggestions(response.data); // 제안 목록 업데이트
      } catch (error) {
        console.error("자동 완성 데이터 가져오기 실패:", error);
        setSuggestions([]);
      }
    } else {
      setSuggestions([]);
    }
  };

  // 자동 완성 항목 선택 시
  const handleSuggestionClick = (selectedKeyword) => {
    setKeyword(selectedKeyword);
    setSuggestions([]); // 제안 목록 비우기
    fetchTranslations(selectedKeyword); // 선택된 키워드의 번역값 불러오기
  };

  // 선택된 키워드의 번역 결과 가져오기
  const fetchTranslations = async (selectedKeyword) => {
    try {
      const response = await axios.get(`/admin/code/details`, {
        params: { keyword: selectedKeyword },
      });
      setTranslations(response.data.translate_code); // 번역 데이터 설정
    } catch (error) {
      console.error("번역 데이터 가져오기 실패:", error);
      setTranslations({
        Java: "",
        Python: "",
        JavaScript: "",
        CSharp: "",
        CPlusPlus: "",
        C: "",
        TypeScript: "",
        Kotlin: "",
        Ruby: "",
        PHP: "",
      });
    }
  };

  // 서버로 데이터 전송 (POST 요청)
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!keyword.trim()) {
      alert("키워드를 입력하세요!");
      return;
    }

    try {
      const payload = {
        origin_code: keyword,
        translate_code: {
          Java: translations.Java,
          Python: translations.Python,
          JavaScript: translations.JavaScript,
          CSharp: translations.CSharp,
          CPlusPlus: translations.CPlusPlus,
          C: translations.C,
          TypeScript: translations.TypeScript,
          Kotlin: translations.Kotlin,
          Ruby: translations.Ruby,
          PHP: translations.PHP,
        },
      };

      const response = await axios.post("/admin/code", payload);
      alert("번역 데이터가 성공적으로 저장되었습니다!");
      console.log("서버 응답:", response.data);

      // 입력 필드 초기화
      setKeyword("");
      setTranslations({
        Java: "",
        Python: "",
        JavaScript: "",
        CSharp: "",
        CPlusPlus: "",
        C: "",
        TypeScript: "",
        Kotlin: "",
        Ruby: "",
        PHP: "",
      });
    } catch (error) {
      console.error("데이터 전송 중 오류 발생:", error);
      alert("번역 데이터를 저장하는 중 오류가 발생했습니다.");
    }
  };

  return (
    <div>
      <h3>번역 코드 입력</h3>
      <form onSubmit={handleSubmit}>
        <p>
          수정 키워드:{" "}
          <input
            type="text"
            value={keyword}
            onChange={handleKeywordChange}
            placeholder="입력 또는 변경할 코드를 입력하세요."
          />
        </p>

        {/* 자동 완성 제안 */}
        {suggestions.length > 0 && (
          <ul>
            {suggestions.map((item) => (
              <li key={item} onClick={() => handleSuggestionClick(item)}>
                {item}
              </li>
            ))}
          </ul>
        )}

        {/* 번역 결과 입력 필드 */}
        {Object.keys(translations).map((language) => (
          <p key={language}>
            {language}:{" "}
            <input
              type="text"
              value={translations[language]}
              onChange={(e) => setTranslations({ ...translations, [language]: e.target.value })}
              placeholder={`${language} 번역결과 입력`}
            />
          </p>
        ))}

        <button type="submit">입력하기</button>
      </form>
    </div>
  );
}

export default CodeManagement;