import React from 'react';
import { Routes, Route } from 'react-router-dom';
import './App.css';

/* 컴포넌트 IMPORT */
/* import Main from './TEC/Main'; */
import Main1 from './TEC/Main1';

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path='/' element={<Main1 />} /> {/* Main 컴포넌트를 렌더링 */}
      </Routes>
    </div>
  );
}

export default App;