package com.aivle.backend.board.controller;

import com.aivle.backend.board.domain.Board;
import com.aivle.backend.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
    public final BoardService boardService;

    @PostMapping
    public Board createBoard(@RequestBody Board board) {
        return boardService.insertBoard(board);
    }

    @PutMapping
    public Board updateBoard(@RequestBody Board board) {
        return boardService.updateBoard(board);
    }

    @DeleteMapping("/{boardId}")
    public String removeBoard(@PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(boardId);
        return "삭제 완료";
    }

    @GetMapping("/{boardId}")
    public Board getBoard(@PathVariable("boardId") Long boardId) {
        return boardService.findBoard(boardId);
    }

    @GetMapping
    public List<Board> getBoardList() {
        return boardService.getBoardsAll();
    }
}
