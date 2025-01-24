import axios from "axios";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

function UserSupportEdit() {
    const navigate = useNavigate();
    const location = useLocation();
    const inquiry = location.state?.inquiry; // 전달받은 상태
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [userId, setUserId] = useState(location.state?.userId || ""); // 조회 아이디 상태
    const [inquiries, setInquiries] = useState(location.state?.inquiries || []); // 문의내역 상태

    const inquiryNo = inquiry?.inquiryNo;

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
          await axios.put(`/api/user-support/${inquiryNo}`, { title, content });
          alert("문의가 수정되었습니다.");
          const updatedInquiries = inquiries.map((item) =>
            item.inquiryNo === inquiryNo
                ? { ...item, title, content }
                : item
          );
          navigate(`/UserSupportDetail?inquiryNo=${inquiryNo}`, { 
            state: { inquiry: { ...inquiry, title, content }, userId, inquiries: updatedInquiries } });
        } catch (e) {
          console.error("문의 수정 실패:", e);
          alert("문의 수정에 실패했습니다.");
        }
      };

    const handleCancel = () => {
        const confirmed = window.confirm("취소하시겠습니까?");
        if (confirmed) navigate(`/UserSupportDetail?inquiryNo=${inquiryNo}` , { state: { inquiry, userId, inquiries } }); 
    }

    return (
        <div className="userSupport-write-all">
        <h2 className="userSupport-write-title">1:1 문의 수정정</h2>
        <div className="userSupport-write-container">
            <p>
                <input
                    placeholder={inquiry.userId}
                    readOnly
                />
            </p>
            <p>
                <input
                    placeholder={inquiry.title}
                    value={title}
                    onChange={e => {setTitle(e.target.value)}}
                    required
                />
            </p>
            <p>
                <textarea
                    placeholder={inquiry.content}
                    value={content}
                    onChange={e => {setContent(e.target.value)}}
                    required
                ></textarea>
            </p>
            <div>
                <button onClick={handleSubmit}>수정</button>
                <button onClick={handleCancel}>취소</button>
            </div>
        </div>
    </div>
    )
}
export default UserSupportEdit;