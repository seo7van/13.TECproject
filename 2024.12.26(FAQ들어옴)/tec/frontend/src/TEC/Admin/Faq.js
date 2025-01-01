import React, { useState, useEffect } from 'react';
import axios from 'axios';

import Header from '../Header';

function Faq() {
  const [faqs, setFaqs] = useState([]); // FAQ 목록
  const [question, setQuestion] = useState(''); // 질문 내용
  const [answer, setAnswer] = useState(''); // 답변 내용
  const [message, setMessage] = useState(''); // 성공/실패 메시지

  // FAQ 목록 가져오기
  useEffect(() => {
    fetchFaqs();
  }, []);

  
  // 서버에서 FAQ 목록 가져오기
  const fetchFaqs = async () => {
    try {
      const response = await axios.get('/Admin/faqs');
      setFaqs(response.data);
    } catch (error) {
      console.error('Error fetching FAQs:', error);
      setMessage('FAQ 목록을 가져오지 못했습니다.');
    }
  };

  // FAQ 등록
  const handleAddFaq = async () => {
    if (!question || !answer) {
      setMessage('질문과 답변을 모두 입력하세요.');
      return;
    }

    try {
      const newFaq = {
        question,
        answer,
        createdDate: new Date().toISOString().split('T')[0], // 생성일자 추가 (yyyy-MM-dd)
      };
      const response = await axios.post('/Admin/faqs', newFaq); // 서버에 FAQ 등록 요청
      setFaqs([...faqs, response.data]); // 목록 갱신
      setMessage('FAQ 등록 성공!');
      setQuestion(''); // 입력 필드 초기화
      setAnswer('');
    } catch (error) {
      console.error('Error adding FAQ:', error);
      setMessage('FAQ 등록 실패!');
    }
  };

    return (
      <div>
        <Header />
        <div>
          <h1>FAQ 관리</h1>
          {message && <p>{message}</p>}

          {/* FAQ 목록 */}
          <h2>등록된 FAQ</h2>
          <ul>
            {faqs.map((faq, index) => (
              <li key={index}>
                <strong>질문:</strong> {faq.question} <br />
                <strong>답변:</strong> {faq.answer} <br />
                <strong>생성일자:</strong> {faq.createdDate || '알 수 없음'}
              </li>
            ))}
          </ul>

          {/* FAQ 추가 */}
          <h2>FAQ 등록</h2>
          <div>
            <input
              type="text"
              placeholder="질문"
              value={question}
              onChange={(e) => setQuestion(e.target.value)}
            />
            <input
              type="text"
              placeholder="답변"
              value={answer}
              onChange={(e) => setAnswer(e.target.value)}
            />
            <button onClick={handleAddFaq}>FAQ 등록</button>
          </div>
        </div>
      </div>
    );
}






export default Faq;
