import React, {useState, useEffect} from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const [message, setMessage] = useState("");

  useEffect(() => {
    axios.get("/api/test")
      .then(response => {
        console.log(response.data);
        setMessage(response.data);
      })
      .catch(error => {
        console.error("Error fetching data:", error);
      });
  }, []);

  return (
    <div className="App">
      <header className="App-header">
        <h1 className="App-title">{message}</h1>
      </header>
      <p className="App-intro">
        프로젝트 시작
      </p>
    </div>
  );
}

export default App;
