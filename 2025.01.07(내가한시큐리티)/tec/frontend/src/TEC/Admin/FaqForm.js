import React, { useState } from 'react';
import axios from 'axios';
import '../Styles/Faq.css';

function FaqForm() {
  const [faqs, setFaqs] = useState([]); // FAQ 목록
  const [question, setQuestion] = useState(''); // 질문 내용
  const [answer, setAnswer] = useState(''); // 답변 내용
  const [message, setMessage] = useState(''); // 성공/실패 메시지

  const handleAddFaq = async () => {
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
      const response = await axios.post('/Admin/faqs/check-admin', newFaq);
      setFaqs([...faqs, response.data]);
      setMessage('FAQ 등록 성공!');
      setQuestion('');
      setAnswer('');
    } catch (error) {
      setMessage('FAQ 등록 실패!');
    }
  };

  return (
    <div className="faq-form">
      <h2>FAQ 등록</h2>
      {message && <p className="faq-message">{message}</p>}
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
  );
}

export default FaqForm;
