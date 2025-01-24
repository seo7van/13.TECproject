import axios from "axios";
import { useEffect, useState } from "react";
import { useLocation, useNavigate, useSearchParams } from "react-router-dom";

function UserSupportDetail() {
    const [inquiry, setInquiry] = useState(null);
    const location = useLocation();
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const inquiryNo = searchParams.get("inquiryNo");
    const { userId, inquiries } = location.state || {};

    useEffect(() => {
        const fetchInquiryDetails = async () => {
            try {
                const response = await axios.get(`/api/user-support/${inquiryNo}`);
                setInquiry(response.data);
            } catch (e) {
                console.error("문의내역 상세조회 실패: ", e);
                alert("문의내역 불러오기에 실패했습니다.");
            }
        }
        if (inquiryNo) fetchInquiryDetails();
    }, [inquiryNo]);

    const handleDelete = async () => {
        const confirmed = window.confirm("삭제하시겠습니까?");
        if (!confirmed) return;
        try {
            await axios.delete(`/api/user-support/${inquiryNo}`)
            alert("삭제되었습니다.");
            navigate("/UserSupportList");
        } catch (e) {
            console.error("문의삭제 실패: ", e);
            alert("삭제에 실패했습니다.");
        }
    }

    const formatDateTime = (dateString) => {
        const date = new Date(dateString); // 문자열을 Date 객체로 변환
        const formattedDate = date.toISOString().split('T')[0]; // 날짜 부분 추출
        const formattedTime = date.toTimeString().slice(0, 5); // 시간 부분 추출
        return `${formattedDate} ${formattedTime}`;
    }

    if (!inquiry) return <p>로딩 중...</p>;

    const handleBack = () => {
        navigate("/UserSupportList", { state: { userId, inquiries } });
    };

    return (
        <div className="userSupport-detail-all">
            <h2 className="userSupport-detail-title">문의 내용</h2>
            <div className="userSupport-detail-container">
                <div className="userSupport-detail-title">
                    <strong>{inquiry.title}</strong>
                </div>
                <div className="userSupport-detail-info">
                    <strong>{inquiry.userId}</strong>
                    <strong>{formatDateTime(inquiry.createdDate)}</strong>
                </div>
                <div className="userSupport-detail-content">
                    <strong>{inquiry.content}</strong>
                </div>
            </div>
            <div className="userSupport-detail-replyContainer">
                <div className="userSupport-detail-info">
                    <strong>{inquiry.reply != null ? formatDateTime(inquiry.modifiedDate) : ''}</strong>
                </div>
                <div className="userSupport-detail-info">
                    {inquiry.reply || ''}
                </div>
            </div>
            <div className="userSupport-detail-btn">
                <button onClick={() => navigate(`/UserSupportEdit?inquiryNo=${inquiry.inquiryNo}`, { state: { inquiry, userId, inquiries } })}>수정</button>
                <button onClick={handleDelete}>삭제</button>
                <button onClick={handleBack}>목록</button>
            </div>
        </div>
    )
}
export default UserSupportDetail;