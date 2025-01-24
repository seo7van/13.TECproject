import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../Styles/Faq.css';

function Faq() {
  const [faqs, setFaqs] = useState([]); // FAQ 목록
  const [filteredFaqs, setFilteredFaqs] = useState([]); // 검색된 FAQ 목록
  const [searchTerm, setSearchTerm] = useState(''); // 검색어
  const [question, setQuestion] = useState(''); // 질문 내용
  const [answer, setAnswer] = useState(''); // 답변 내용
  const [message, setMessage] = useState(''); // 성공/실패 메시지
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지
  const itemsPerPage = 5; // 한 페이지에 표시할 항목 수

  // FAQ 목록 가져오기
  useEffect(() => {
    fetchFaqs();
  }, []);
  
   // 검색어 변경 시 필터링
   useEffect(() => {
    if (searchTerm) {
      const filtered = faqs.filter(
        (faq) =>
          faq.question.toLowerCase().includes(searchTerm.toLowerCase()) ||
          faq.answer.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setFilteredFaqs(filtered);
    } else {
      setFilteredFaqs(faqs);
    }
    setCurrentPage(1); // 검색 시 페이지를 1로 초기화
  }, [searchTerm, faqs]);

  // 서버에서 FAQ 목록 가져오기
  const fetchFaqs = async () => {
    try {
      const response = await axios.get('/Admin/faqs');
      if (Array.isArray(response.data)) {
        setFaqs(response.data);
      } else {
        setMessage('FAQ 데이터가 올바르지 않습니다.');
      }
    } catch (error) {
      setMessage('FAQ 목록을 가져오지 못했습니다.');
    }
  };

  // FAQ 등록
  const handleAddFaq = async () => {
    console.log('현재 질문:', question); // 상태 확인
    console.log('현재 답변:', answer);  // 상태 확인
    if (!question || !answer) {
      setMessage('질문과 답변을 모두 입력하세요.');
      return;
    }
    try {
      const newFaq = {
        question,
        answer,
        createdDate: new Date().toISOString().split('T')[0],
      };
      console.log('전송 데이터:', newFaq); // 서버로 전송할 데이터 확인
      const response = await axios.post('/Admin/faqs', newFaq);
      setFaqs([...faqs, response.data]);
      setMessage('FAQ 등록 성공!');
      setQuestion('');
      setAnswer('');
    } catch (error) {
      console.error('FAQ 등록 실패:', error.response || error.message);
      setMessage('FAQ 등록 실패!');
    }
  };
  
  // 페이지 계산
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = filteredFaqs.slice(indexOfFirstItem, indexOfLastItem);
  const totalPages = Math.ceil(filteredFaqs.length / itemsPerPage);

  // 페이지 변경 핸들러
  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  return (
    <div className="faq-all">
      <h1 className="faq-title">자주 묻는 질문</h1>
      <div className="faq-container">
        <input
          type="text"
          className="faq-search"
          placeholder="질문 또는 답변 검색"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />

        {message && <p className="faq-message">{message}</p>} {/* 성공실패메시지 */}
          <>

            <div className="faq-list">
              {currentItems.map((faq, index) => (

                <div key={index} className="faq-item">
                  <div className="faq-question">
                    <strong>질문:</strong> {faq.question}
                  </div>
                  <div className="faq-answer">
                    <strong>답변:</strong> {faq.answer}
                  </div>
                  <div className="faq-date">
                    <strong>생성일자:</strong> {faq.createdDate || '알 수 없음'}
                  </div>
                </div>
              ))}
            </div>

            {/* 페이지네이션 */}
            <div className="pagination">
              {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
                <button
                  key={page}
                  className={`pagination-button ${currentPage === page ? 'active' : ''}`}
                  onClick={() => handlePageChange(page)}
                >
                  {page}
                </button>
              ))}
            </div>
          </>
        

        <div className="faq-form">
          <h2>FAQ 등록</h2>
          <input
            type="text"
            className="faq-input"
            placeholder="질문"
            value={question}
            onChange={(e) => setQuestion(e.target.value)}
          />
          <input
            type="text"
            className="faq-input"
            placeholder="답변"
            value={answer}
            onChange={(e) => setAnswer(e.target.value)}
          />
          <button className="faq-submit-button" onClick={handleAddFaq}>
            등록하기
          </button>
        </div>
      </div>
    </div>
  );
}


export default Faq;
