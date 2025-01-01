
import React, { useState } from "react";
import axios from "axios";
import "../Styles/CodeManagement.css";

function CodeManagement() {
  const [keyword, setKeyword] = useState(""); // 입력 또는 수정할 키워드
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 활성/비활성 상태
  const [searchQuery, setSearchQuery] = useState(""); // 검색 쿼리
  const [suggestions, setSuggestions] = useState([]); // 자동 완성 제안 목록
  const [translations, setTranslations] = useState({ // 번역결과 상태
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

  // 모달 열기/닫기
  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  // 검색창 입력
  const handleSearchChange = async (e) => {
    const value = e.target.value;
    setSearchQuery(value);

    if(value.trim()) {
        try {
            const response = await axios.get("/admin/code/suggestions", {
                params: { query: value }
            });
            if (response.data.length > 0) {
                setSuggestions(response.data); // 검색된 결과를 제안 목록으로 설정
            } else {
                setSuggestions([]); // 검색 결과가 없으면 목록 비우기
            }
        } catch (e) {
            console.error("자동완성 데이터 불러오기 실패: ", e);
            setSuggestions([]); // 오류 시 빈 목록
        }
    } else {
        setSuggestions([]); // 검색창이 비었을 때 초기화
    }
  };

  // 검색 제안 선택
  const handleSuggestionClick = (selectedKeyword) => {
    setSearchQuery(selectedKeyword);
  };

  // 확인 버튼 클릭 처리
  const handleConfirm = async () => {
    if(!searchQuery.trim()) {
        alert("키워드를 입력하세요");
        return;
    }
    setKeyword(searchQuery); // 수정 키워드 업데이트
    setIsModalOpen(false); // 모달 닫기

    try {
        // DB 번역 데이터 가져오기
        const response = await axios.get("/admin/code/details", {
            params: { keyword: searchQuery }
        });

        if ( response.data && response.data.translateCode) {
            const translationsData = JSON.parse(response.data.translateCode); // 번역값 JSON 파싱
            setTranslations(translationsData); // 파싱한 데이터 상태 업데이트       
        } else {
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
    } catch (e) {
        console.error("번역 데이터 로드 실패: ", e);
        alert("데이터 로드 중 오류가 발생했습니다: ");
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
  }
  
  // 저장
  const handleSave = async () => {
    if (!keyword.trim()) {
        alert("수정 키워드를 입력하세요.");
        return;
    }

    if(!window.confirm("정말 저장하시겠습니까?")) return;

    try {
        const payload = {
            originCode: keyword,
            translateCode: JSON.stringify(translations)
        };
        console.log("payload: ", payload);

        await axios.post("/admin/code/addKeyword", payload);
        alert("저장되었습니다.");

        setKeyword(""); // 저장 후 수정 키워드 상태 초기화
        setTranslations({ // 저장 후 번역결과 값 초기화화
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
    } catch (e) {
        console.error("키워드 저장 실패: ", e);
        alert("저장 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className="code-management">
      <h3>키워드 관리</h3>

      {/* 수정 키워드 입력 */}
      <div className="keyword-section">
        <label>수정 키워드:</label>
        <input value={keyword} readOnly />
        <button onClick={openModal}>검색</button>
      </div>

      {/* 번역 데이터 입력 */}
      <div className="translations-section">
        {Object.keys(translations).map((language) => (
          <div key={language} className="translation-row">
            <label>{language}:</label>
            <input
              value={translations[language]}
              onChange={(e) =>
                setTranslations({
                  ...translations,
                  [language]: e.target.value,
                })
              }
              placeholder={`${language} 번역 결과 입력`}
            />
          </div>
        ))}
      </div>

      {/* 저장/취소 버튼 */}
      <div className="button-section">
        <button onClick={handleSave}>저장</button>
        <button onClick={() => setKeyword("")}>취소</button>
      </div>

      {/* 모달 */}
      {isModalOpen && (
        <div className="modal-backdrop" onClick={closeModal}>
          <div
            className="modal"
            onClick={(e) => e.stopPropagation()} // 모달 내부 클릭 시 닫히지 않음
          >
            <h4>키워드 검색</h4>
            <input
              value={searchQuery}
              onChange={handleSearchChange}
              placeholder="키워드를 입력하세요"
            />
            <ul className="suggestions-list">
              {suggestions.map((item) => (
                <li
                  key={item}
                  onClick={() => handleSuggestionClick(item)}
                  className="suggestion-item"
                >
                  {item}
                </li>
              ))}
            </ul>
            {suggestions.length === 0 && searchQuery.trim() && (
              <p className="no-results">새로운 키워드를 추가할 수 있습니다.</p>
            )}
            <div className="modal-buttons">
              <button onClick={handleConfirm}>확인</button>
              <button onClick={closeModal}>취소</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default CodeManagement;
