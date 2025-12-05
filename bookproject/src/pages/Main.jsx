import { Box, Grid, Card, CardActionArea, CardContent, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";
import EditIcon from "@mui/icons-material/Edit";
import MenuBookIcon from "@mui/icons-material/MenuBook";

export default function Main() {
    const nav = useNavigate();

    return (
        <Box
            sx={{
                width:"100%",
                height:"calc(100vh - 64px)",
                display:"flex",
                justifyContent:"center",
                alignItems:"center",
                bgcolor:"#fff",
            }}
        >
            <Grid
                container
                spacing={8}              // 🔥 카드 사이 간격 넓게
                justifyContent="center"
                alignItems="center"
            >

                <Grid item>
                    <Card sx={{ width:300, height:320, borderRadius:5 }}>   {/* PC에서도 크게 */}
                        <CardActionArea sx={{height:"100%"}} onClick={()=>nav("/book/create")}>
                            <CardContent sx={{ textAlign:"center", mt:5 }}>
                                <EditIcon sx={{fontSize:100}}/>                    {/* 아이콘 100px */}
                                <Typography variant="h5" sx={{mt:3}}>책 등록하기</Typography>
                            </CardContent>
                        </CardActionArea>
                    </Card>
                </Grid>

                <Grid item>
                    <Card sx={{ width:300, height:320, borderRadius:5 }}>
                        <CardActionArea sx={{height:"100%"}} onClick={()=>nav("/books")}>
                            <CardContent sx={{ textAlign:"center", mt:5 }}>
                                <MenuBookIcon sx={{fontSize:100}}/>
                                <Typography variant="h5" sx={{mt:3}}>책 목록 보기</Typography>
                            </CardContent>
                        </CardActionArea>
                    </Card>
                </Grid>

            </Grid>
        </Box>
    );
}

