import { Box, TextField, Button, Typography, Paper } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useState } from "react";

export default function Register() {
    const nav = useNavigate();

    const [form, setForm] = useState({
        email: "",
        pw: "",
        pwCheck: "",
        nickname: ""
    });

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const register = () => {
        if (!form.email || !form.pw || !form.pwCheck || !form.nickname) {
            alert("모든 항목을 입력해주세요!");
            return;
        }
        if (form.pw !== form.pwCheck) {
            alert("비밀번호가 일치하지 않습니다!");
            return;
        }

        alert("회원가입 완료! (API 연결 예정)");
        nav("/login");
    };

    return (
        <Box
            sx={{
                width: "100%",
                height: "100vh",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                bgcolor: "#f8f8f8"
            }}
        >
            <Paper elevation={6} sx={{ p: 5, width: "450px", borderRadius: "14px" }}>

                <Typography fontSize={28} fontWeight="bold" textAlign="center" mb={1}>
                    📝 회원가입
                </Typography>

                <Typography fontSize={14} textAlign="center" mb={4} color="#666">
                    아래 정보를 입력하여 계정을 생성하세요
                </Typography>

                <TextField
                    fullWidth label="이메일" name="email"
                    sx={{ mb: 2 }} value={form.email} onChange={handleChange}
                />

                <TextField
                    fullWidth label="닉네임" name="nickname"
                    sx={{ mb: 2 }} value={form.nickname} onChange={handleChange}
                />

                <TextField
                    fullWidth label="비밀번호" name="pw" type="password"
                    sx={{ mb: 2 }} value={form.pw} onChange={handleChange}
                />

                <TextField
                    fullWidth label="비밀번호 확인" name="pwCheck" type="password"
                    sx={{ mb: 4 }} value={form.pwCheck} onChange={handleChange}
                />

                <Button
                    fullWidth variant="contained"
                    sx={{ py: 1.5, fontSize: 17, bgcolor:"#00b6b8" }}
                    onClick={register}
                >
                    가입완료
                </Button>

                <Button
                    fullWidth variant="text" sx={{ mt: 2, fontSize: 16 }}
                    onClick={() => nav("/login")}
                >
                    로그인으로 돌아가기 →
                </Button>

            </Paper>
        </Box>
    );
}
