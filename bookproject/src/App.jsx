import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Header from "./components/Header";

// Pages
import Login from "./pages/Login";
import Register from "./pages/Register";
import Main from "./pages/Main";
import BookList from "./pages/BookList";
import BookDetail from "./pages/BookDetail";
import BookCreate from "./pages/BookCreate";
import BookUpdate from "./pages/BookUpdate";

function App() {
    return (
        <BrowserRouter>
            <div style={{width:"100%", minHeight:"100vh", display:"flex", flexDirection:"column"}}>

                <Header />

                <div style={{flexGrow:1}}>  {/* ← 페이지가 Header 아래 전체영역을 차지 */}
                    <Routes>
                        <Route path="/" element={<Navigate to="/login" />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />
                        <Route path="/main" element={<Main />} />
                        <Route path="/books" element={<BookList />} />
                        <Route path="/book/:id" element={<BookDetail />} />
                        <Route path="/book/create" element={<BookCreate />} />
                        <Route path="/book/update/:id" element={<BookUpdate />} />
                    </Routes>
                </div>

            </div>
        </BrowserRouter>
    );
}

export default App;
