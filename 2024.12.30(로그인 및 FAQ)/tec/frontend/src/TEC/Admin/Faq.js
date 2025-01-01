import React, { useState, useEffect } from 'react';
import axios from 'axios';

import Header from '../Header';

function Faq() {
  const [faqs, setFaqs] = useState([]); // FAQ 목록
  const [question, setQuestion] = useState(''); // 질문 내용
  const [answer, setAnswer] = useState(''); // 답변 내용
  const [message, setMessage] = useState(''); // 성공/실패 메시지
  const [isLoading, setIsLoading] = useState(true); // 로딩 상태

  // FAQ 목록 가져오기
  useEffect(() => {
    fetchFaqs();
  }, []);

  
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
    } finally {
      setIsLoading(false);
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

  return (
    <div>
      <Header />
      <div>
        <h1>FAQ 관리</h1>
        {message && <p>{message}</p>}

        <h2>등록된 FAQ</h2>
        {isLoading ? (
          <p>로딩 중...</p>
        ) : (
          <ul>
            {faqs.map((faq, index) => (
              <li key={index}>
                <strong>질문:</strong> {faq.question} <br />
                <strong>답변:</strong> {faq.answer} <br />
                <strong>생성일자:</strong> {faq.createdDate || '알 수 없음'}
              </li>
            ))}
          </ul>
        )}

        <h2>FAQ 등록</h2>
        <div>
        <input
          type="text"
          placeholder="질문"
          value={question}
          onChange={(e) => {
            console.log('질문 입력값:', e.target.value); // 입력값 확인
            setQuestion(e.target.value);
          }}
        />
          <input
            type="text"
            placeholder="답변"
            value={answer}
            onChange={(e) => {
              console.log('답변 입력값:', e.target.value); // 입력값 확인
              setAnswer(e.target.value);
            }}
          />
          <button onClick={handleAddFaq}>FAQ 등록</button>
        </div>
      </div>
    </div>
  );
}






export default Faq;
