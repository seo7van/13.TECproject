import React from 'react';
import { Routes, Route } from 'react-router-dom';
import './App.css';

/* 컴포넌트 IMPORT */
/* import Main from './TEC/Main'; */
<<<<<<< Updated upstream
import TranslateComponent from './TEC/TranslateComponent';
import CodeManagement from './TEC/Admin/CodeManagement';
=======
import Main1 from './TEC/Main1';
>>>>>>> Stashed changes

function App() {
  return (
    <div className="App">
      <Routes>
<<<<<<< Updated upstream
        <Route path='/' element={<TranslateComponent />} /> {/* Main 컴포넌트를 렌더링 */}
        <Route path="/Admin/CodeManagement" element={<CodeManagement />} />
=======
        <Route path='/' element={<Main1 />} /> {/* Main 컴포넌트를 렌더링 */}
>>>>>>> Stashed changes
      </Routes>
    </div>
  );
}

export default App;