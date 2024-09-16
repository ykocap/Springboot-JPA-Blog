package com.kbs.blog.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbs.blog.config.auth.PrincipalDetail;
import com.kbs.blog.dto.ReplySaveRequestDto;
import com.kbs.blog.dto.ResponseDto;
import com.kbs.blog.model.Board;
import com.kbs.blog.model.Reply;
import com.kbs.blog.model.RoleType;
import com.kbs.blog.model.User;
import com.kbs.blog.repository.BoardRepository;
import com.kbs.blog.service.BoardService;
import com.kbs.blog.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@RestController
public class BoardApiController {

	@Autowired
	private BoardService boardService;
	
	@PostMapping("/api/board")
	public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principalDetail) {
		
		boardService.글쓰기(board, principalDetail.getUser());
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

	@PutMapping("/api/board/{id}")
	public ResponseDto<Integer> update(@PathVariable int id,@RequestBody Board board) {
		
		boardService.글수정(id, board);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("/api/board/{id}")
	public ResponseDto<Integer> delete(@PathVariable int id) {
		
		boardService.글삭제(id);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

	// 실무에서는 DTO를 정의후 받아오는게 좋다
	@PostMapping("/api/board/{boardId}/reply")
	
	// 방법1
//	public ResponseDto<Integer> replySave(@PathVariable int boardId, @RequestBody Reply reply, @AuthenticationPrincipal PrincipalDetail principalDetail) {
//		boardService.댓글쓰기(boardId, reply, principalDetail.getUser());
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//	}

	// 방법2
	public ResponseDto<Integer> replySave(@RequestBody ReplySaveRequestDto replySaveRequestDto) {
		boardService.댓글쓰기2(replySaveRequestDto);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("/api/board/{boardId}/reply/{replyId}")
	public ResponseDto<Integer> replyDelete (@PathVariable int replyId) {

		boardService.댓글삭제(replyId);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
}
