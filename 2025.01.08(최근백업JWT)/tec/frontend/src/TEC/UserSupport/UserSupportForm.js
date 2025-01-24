import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

function UserSupportForm() {
    const [userId, setUserId] = useState("");
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const confirmed = window.confirm("등록하시겠습니까?");
        if (confirmed) {
            try {
                const response = await axios.post("/api/user-support", {userId, title, content, type: 1});
                alert("등록되었습니다.");
                setUserId("");
                setTitle("");
                setContent("");
                navigate("/UserSupportList");
            } catch (e) {
                console.error("문의 등록 실패: ", e);
                alert("문의 등록에 실패했습니다.");
            }
        }
    }

    const handleCancel = () => {
        const confirmed = window.confirm("취소하시겠습니까?");
        if (confirmed) navigate("/UserSupportList"); 
    }

    return (
        <div className="userSupport-write-all">
            <h2 className="userSupport-write-title">1:1 문의 작성</h2>
            <div className="userSupport-write-container">
                <p>
                    <input
                        placeholder="회원ID"
                        value={userId}
                        onChange={e => {setUserId(e.target.value)}}
                        required
                    />
                </p>
                <p>
                    <input
                        placeholder="제목"
                        value={title}
                        onChange={e => {setTitle(e.target.value)}}
                        required
                    />
                </p>
                <p>
                    <textarea
                        placeholder="문의 내용"
                        value={content}
                        onChange={e => {setContent(e.target.value)}}
                        required
                    ></textarea>
                </p>
                <div>
                    <button onClick={handleSubmit}>작성</button>
                    <button onClick={handleCancel}>취소</button>
                </div>
            </div>
        </div>
    )
}
export default UserSupportForm;