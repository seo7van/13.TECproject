import React from 'react';
import Header from './Header';
import './Main.css';

function Main1() {
    return (
        <div className="all">
            <Header />
            <div className="main-all">
                <header className="main-container">
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
                                    <p>1</p>
                                    <p>2</p>
                                    <p>3</p>
                                    <p>4</p>
                                </div>
                            </div>
                        <div className="clip-footer-line"></div>
                    </div>

                    <div className="clip">
                        <div className="clip-header">Clip2</div>
                            <div className="clip-body">
                                <div className="numbers">
                                    <p>1</p>
                                    <p>2</p>
                                    <p>3</p>
                                </div>
                            </div>
                        <div className="clip-footer-line"></div>
                    </div>
                </div>
                
        
                {/* 버튼 영역 */}
                <div className="buttons">
                    <button className="compare-button">COMPARE</button>
                </div>
            </div>
        </div>
    );
}
export default Main1;