import React from 'react';
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import './App.css';

/* 컴포넌트 IMPORT */
/* import Main from './TEC/Main'; */
import TranslateComponent from './TEC/TranslateComponent';
import CodeManagement from './TEC/Admin/CodeManagement';
import Faq from './TEC/Admin/Faq';
import Login from './TEC/User/Login';
import SignUp from './TEC/User/SignUp';

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path='/' element={<TranslateComponent />} /> {/* Main 컴포넌트를 렌더링 */}
        <Route path="/Admin/CodeManagement" element={<CodeManagement />} />
        <Route path="/Admin/Faq" element={<Faq />} />
        <Route path="/User/Login" element={<Login />} />
        <Route path="/User/SignUp" element={<SignUp />} />
      </Routes>
    </div>
  );
}

export default App;