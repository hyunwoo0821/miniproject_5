package com.aivle.backend.book.controller;

import com.aivle.backend.book.domain.Book;
import com.aivle.backend.book.dto.BookRequestDto;
import com.aivle.backend.book.repository.BookRepository;
import com.aivle.backend.book.service.BookService;
import com.aivle.backend.security.CustomUserDetails;
import com.aivle.backend.user.entity.User;
import com.aivle.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    // 책 등록 - POST
    @PostMapping
    public Book insertBook(
            @RequestBody BookRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUser().getId(); // JWT에서 userId 추출
        return bookService.insertBook(userId, dto);
    }

//    public Book insertBook(Long userId, BookRequestDto dto) {
//
//        //  1. userId로 유저 조회
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//
//        //  2. Book 생성 시 닉네임 저장
//        Book book = Book.builder()
//                .bookTitle(dto.getBookTitle())
//                .author(dto.getAuthor())
//                .category(dto.getCategory())
//                .content(dto.getContent())
//                .bookImageUrl(dto.getBookImageUrl())
//                .writer(user.getNickname()) // 작성자 닉네임 저장
//                .user(user)                 // 연관관계 설정
//                .build();
//
//        //  3. 저장 후 반환
//        return bookRepository.save(book);
//    }

//    // 책 수정(PUT)
//    @PutMapping("/{bookId}")
//    public Book updateBook(@PathVariable Long bookId, @RequestBody BookRequestDto dto) {
//        return bookService.updateBook(bookId, dto);
//    }
//
//    // 책 삭제 - DELETE
//    @DeleteMapping("/{bookId}")
//    public void deleteBook(@PathVariable Long bookId) {
//        bookService.deleteBook(bookId);
//    }
    @PutMapping("/{bookId}")
    public Book updateBook(
            @PathVariable Long bookId,
            @RequestBody BookRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUser().getId(); // 로그인된 사용자 ID
        return bookService.updateBook(bookId, dto, userId);
    }

    @DeleteMapping("/{bookId}")
    public void deleteBook(
            @PathVariable Long bookId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUser().getId(); // 로그인된 사용자 ID
        bookService.deleteBook(bookId, userId);
    }


    // 책 조회(단건) - GET
    @GetMapping("/{bookId}")
    public Book getBook(@PathVariable("bookId") Long bookId) {
        return bookService.getBook(bookId);
    }

    // 책 조회 (다건) - GET
    @GetMapping
    public Page<Book> getAllBooks(Pageable pageable) {
        try {
            return bookService.getAllBooks(pageable);
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔 확인용
            throw new RuntimeException("책 목록 조회 실패", e);
        }
    }

    @GetMapping("/")
    public String home() {
        System.out.println("=== 🚀 진짜 진짜 최종 버전 V6 🚀 ===");  // <--- 이 줄을 추가!
        return "Backend V6 is running!";
    }

    

//    @PostMapping("/{bookId}/like")
//    public ResponseEntity<Book> toggleLike(
//            @PathVariable Long bookId,
//            @RequestParam Long userId,
//            @RequestParam boolean liked) {
//        Long userId = userDetails.getUser().getId();  // JWT 토큰에서 사용자 ID 추출
//        Book updated = bookService.toggleLike(bookId, userId, liked);
//        return ResponseEntity.ok(updated);
//    }
    @PostMapping("/{bookId}/like")
    public ResponseEntity<Book> toggleLike(
            @PathVariable Long bookId,
            @RequestParam boolean liked,
            @AuthenticationPrincipal CustomUserDetails userDetails) { // 로그인된 사용자 정보 주입
        Long userId = userDetails.getUser().getId();  // JWT에서 가져온 사용자 ID
        Book updated = bookService.toggleLike(bookId, userId, liked);
        return ResponseEntity.ok(updated);
    }

}
