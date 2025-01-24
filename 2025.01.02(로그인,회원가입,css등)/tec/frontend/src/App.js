import React, {useState} from 'react';
import { Routes, Route } from 'react-router-dom';
import './App.css';

/* 컴포넌트 IMPORT */
import Header from './TEC/Header'; //Header항상 상단에 고정
import TranslateComponent from './TEC/TranslateComponent';
import CodeManagement from './TEC/Admin/CodeManagement';
import Faq from './TEC/Admin/Faq';
import Login from './TEC/User/Login';
import SignUp from './TEC/User/SignUp';


function App() {
  // 로그인 상태 관리
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  
  return (
    <div className="App">
      <Header />
      <div className="pages">
        <Routes>
          <Route path='/' element={<TranslateComponent />} />
          <Route path="/Admin/CodeManagement" element={<CodeManagement />} />
          <Route path="/Admin/Faq" element={<Faq />} />
          <Route path="/User/Login" element={<Login setIsLoggedIn={setIsLoggedIn} />} />
          <Route path="/User/SignUp" element={<SignUp />} />
        </Routes>
      </div>
    </div>
  );
}

export default App;