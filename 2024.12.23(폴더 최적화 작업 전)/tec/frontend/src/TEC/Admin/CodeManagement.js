import React, { useState } from 'react';
import Header from '../Header';

import Main from './../Main';

function CodeManagement() {

    const [clips, setClips] = useState([
        { id: 1, name: "Clip1" },
        { id: 2, name: "Clip2" }
    ]);

    const handleInputChange = (e, id) => {
        const newClips = clips.map((clip) =>
            clip.id === id ? { ...clip, name: e.target.value } : clip
        );
        setClips(newClips);
    };
    
    return (
        <div className="all">
            <Header />
            <div className="main-all">
                <div className="content">
                    <div className="download-content">                
                        <button className="download-button">다운로드</button>
                        <button className="download-button">저장</button>
                    </div>
                    <div>
                        {clips.map((clip) => (
                            <div key={clip.id}>
                                <input
                                    value={clip.name} // 현재 Clip 
                                    onChange={(e) => handleInputChange(e, clip.id)} // 값 변경 처리
                                />
                            </div>
                        ))}
                    </div>
                </div>

                <div className="buttons">
                    <button className="compare-button">COMPARE</button>
                </div>
            </div>
        </div>
    );
}
export default CodeManagement;