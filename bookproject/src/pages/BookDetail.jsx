import { Box, Typography, Button, Divider } from "@mui/material";
import ThumbUpAltIcon from "@mui/icons-material/ThumbUpAlt";
import ThumbDownAltIcon from "@mui/icons-material/ThumbDownAlt";
import PersonIcon from "@mui/icons-material/Person";
import { useNavigate, useParams } from "react-router-dom";

export default function BookDetail() {

    const nav = useNavigate();
    const { id } = useParams(); // URL의 /book/:id 가져옴

    // 📌 임시 도서데이터 (백엔드 연결 전까지)
    const book = {
        id,
        title:"책 먹는 여우",
        author:"프란치스카 비어만",
        category:"유아도서",
        content:"...",
        img:"https://image.aladin.co.kr/product/8/47/cover/s9788937864472.jpg",
        likes:4,
        writer:"에이블스쿨08",
        updated:"2025-12-04 16:11",
    };

    // 수정 페이지 이동
    const goUpdate = () => nav(`/book/update/${id}`);

    // 삭제 클릭
    const handleDelete = () => {
        if(confirm("정말 삭제할까요?")){
            alert("삭제 완료! (백엔드 연결 후 적용)");
            nav("/books");
        }
    };

    return(
        <Box sx={{ width:"100%", maxWidth:"1100px", mx:"auto", mt:3 }}>

            <Typography fontSize={22} fontWeight="bold" color="#666" mb={4}>
                메인페이지 > 상세페이지
            </Typography>

            <Box sx={{ display:"flex", gap:5 }}>

                {/* ===== 이미지 ===== */}
                <Box>
                    <img
                        src={book.img}
                        alt={book.title}
                        style={{ width:"300px", height:"420px", borderRadius:"6px" }}
                    />
                </Box>

                {/* ===== 책 정보 ===== */}
                <Box sx={{ flex:1 }}>

                    <Typography fontSize={22} fontWeight="700" mt={1}>
                        카테고리: <span style={{fontWeight:"400"}}>{book.category}</span>
                    </Typography>

                    <Typography fontSize={22} fontWeight="700" mt={2}>
                        제목: <span style={{fontWeight:"400"}}>{book.title}</span>
                    </Typography>

                    <Typography fontSize={22} fontWeight="700" mt={2}>
                        저자: <span style={{fontWeight:"400"}}>{book.author}</span>
                    </Typography>

                    <Typography fontSize={22} fontWeight="700" mt={2} mb={2}>
                        내용: <span style={{fontWeight:"400"}}>{book.content}</span>
                    </Typography>

                    <Box sx={{ opacity:0.6, mt:10 }}>
                        <Typography fontSize={14}>
                            마지막 수정: {book.updated}
                        </Typography>
                    </Box>

                    {/* 좋아요/싫어요 + 작성자 */}
                    <Box sx={{ display:"flex", alignItems:"center", gap:1, mt:1 }}>
                        <ThumbUpAltIcon /> {book.likes}
                        <ThumbDownAltIcon sx={{ml:2}} />
                        <PersonIcon sx={{ml:2, opacity:0.7}} /> {book.writer}
                    </Box>

                </Box>
            </Box>

            <Divider sx={{mt:3, mb:4}}/>

            {/* ===== 버튼 구역 ===== */}
            <Box sx={{ display:"flex", justifyContent:"center", gap:3, mt:2 }}>
                <Button
                    variant="outlined"
                    sx={{width:200, py:1.4, fontSize:"18px", borderColor:"#1a9bff"}}
                    onClick={goUpdate}
                >
                    수정하기
                </Button>

                <Button
                    variant="outlined"
                    sx={{width:200, py:1.4, fontSize:"18px", borderColor:"#ff4b4b", color:"#ff4b4b"}}
                    onClick={handleDelete}
                >
                    삭제하기
                </Button>
            </Box>
        </Box>
    );
}


