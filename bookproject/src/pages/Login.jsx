import { Box, TextField, Button, Typography, Paper } from "@mui/material";
import { useNavigate } from "react-router-dom";

export default function Login() {
    const nav = useNavigate();

    const login = () => {
        alert("로그인 시도 (백엔드 연결 예정)");
        nav("/main"); // 로그인 성공 시 메인 이동
    };

    return (
        <Box
            sx={{
                width:"100%", height:"100vh",
                display:"flex", justifyContent:"center", alignItems:"center",
                bgcolor:"#f8f8f8"
            }}
        >
            <Paper
                elevation={6}
                sx={{
                    p:5, width:"400px",
                    borderRadius:"14px",
                    textAlign:"center"
                }}
            >

                <Typography fontSize={28} fontWeight="bold" mb={1}>
                    📚 BOOK LOGIN
                </Typography>
                <Typography fontSize={14} mb={4} color="#666">
                    도서 관리 시스템에 로그인하세요
                </Typography>

                {/* 이메일 입력 */}
                <TextField
                    fullWidth label="이메일" variant="outlined"
                    sx={{mb:2}}
                />

                {/* 비밀번호 입력 */}
                <TextField
                    fullWidth label="비밀번호" type="password" variant="outlined"
                    sx={{mb:4}}
                />

                {/* 로그인 버튼 */}
                <Button
                    fullWidth variant="contained"
                    sx={{ py:1.5, fontSize:18, bgcolor:"#00b6b8"}}
                    onClick={login}
                >
                    로그인
                </Button>

                {/* 회원가입 이동 */}
                <Button
                    fullWidth variant="text" sx={{mt:2, fontSize:16, color:"#333"}}
                    onClick={() => nav("/register")}
                >
                    회원가입 →
                </Button>

            </Paper>
        </Box>
    );
}

