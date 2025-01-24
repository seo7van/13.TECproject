import axios from "axios";
import { useState } from "react";
import '../Styles/UserSupportList.css';
import { useLocation, useNavigate } from "react-router-dom";


function UserSupportList() {
    const navigate = useNavigate();
    const location = useLocation();
    const [userId, setUserId] = useState(location.state?.userId || ""); // 조회 아이디 상태
    const [inquiries, setInquiries] = useState(location.state?.inquiries || []); // 문의내역 상태

    const fetchInquiries = async () => {
        try {
            const response = await axios.get(`/api/user-support?userId=${userId}`); // 아이디로 문의내역 조회회
            setInquiries(response.data); // 아이디에 해당하는 문의내역 담기
        } catch (e) {
            console.error("문의내역 조회 실패: ", e);
            alert("문의내역 조회에 실패했습니다.");
        }        
    }

    const formatDateTime = (dateString) => {
        const date = new Date(dateString); // 문자열을 Date 객체로 변환
        const formattedDate = date.toISOString().split('T')[0]; // 날짜 부분 추출
        const formattedTime = date.toTimeString().slice(0, 5); // 시간 부분 추출
        return `${formattedDate} ${formattedTime}`;
    }
    
    return (
        <div className="userSupport-all">
            <h1 className="userSupport-title">1:1 문의</h1>
            <div className="userSupport-container">
                <div className="userSupport-searchBar">
                    <input
                        className="userSupport-search"
                        placeholder="아이디를 입력하세요."
                        value={userId}
                        onChange={e => setUserId(e.target.value)}
                    />&emsp;
                    <button onClick={fetchInquiries}>조회</button>&emsp;
                    <button onClick={() => {navigate('/UserSupportForm')}}>문의</button>
                </div>
                <div className="userSupport-list">
                    {inquiries.map((inquiry, index) => (
                        <div key={index} 
                            className="userSupport-item"
                            onClick={() => navigate(`/UserSupportDetail?inquiryNo=${inquiry.inquiryNo}`, { state: { userId, inquiries } })}
                        >
                            <div className="userSupport-inquiryTitle">
                                <strong>[{inquiry.status}] </strong> {inquiry.title}
                            </div>
                            <div className="userSupport-createdAt">
                                <strong>작성일: </strong> {formatDateTime(inquiry.createdDate)}
                            </div>
                            <div className="userSupport-userId">
                                <strong>작성자: </strong> {inquiry.userId}
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    )
}

export default UserSupportList;