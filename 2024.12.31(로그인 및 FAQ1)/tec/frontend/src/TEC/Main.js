import React from 'react';
import './Main.css';

function Main() {
    return (
        <div className="main-container">
            <header className="container-header">
                <h1 className="title">JAVA 코드 한글화 번역 프로그램</h1>
                    <div className="auth">
                        <input type="text" placeholder="EMAIL 및 ID" className="input" />
                        <input type="password" placeholder="PASSWORD" className="input" />
                        <button className="login-button">로그인</button>
                    </div>
            </header>
    
            {/* 콘텐츠 영역 */}
            <div className="content">
                <div className="download-content">                
                    <button className="download-button">다운로드</button>
                    <button className="download-button">저장</button>
                </div>
                <div className="clip">
                    <div className="clip-header">Clip1</div>
                        <div className="clip-body">
                            <div className="numbers">

                            </div>
                        </div>
                    <div className="clip-footer-line"></div>
                </div>

                <div className="clip">
                    <div className="clip-header">Clip2</div>
                        <div className="clip-body">
                            <div className="numbers">

                            </div>
                        </div>
                    <div className="clip-footer-line"></div>
                </div>
            </div>
    
            {/* 버튼 영역 */}
            <div className="buttons">
                <button className="compare-button">COMPARE</button>
            </div>
    
            {/* 푸터 */}
            <footer className="footer">
                &copy; 최서진 김현규 김광훈 JAVA 코드 한글화 번역 프로그램
            </footer>
        </div>
    );
}
export default Main;