package com.kbs.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kbs.blog.dto.ReplySaveRequestDto;
import com.kbs.blog.model.Board;
import com.kbs.blog.model.KakaoProfile.Properties;
import com.kbs.blog.model.Reply;
import com.kbs.blog.model.User;
import com.kbs.blog.repository.BoardRepository;
import com.kbs.blog.repository.ReplyRepository;
import com.kbs.blog.repository.UserRepository;

import lombok.Builder;

@Service
public class BoardService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private ReplyRepository replyRepository;

	@Transactional
	public void 글쓰기(Board board, User user) {
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);
	}

	@Transactional(readOnly = true)
	public Page<Board> 글목록(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Board 글상세보기(int id) {
		return boardRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("글 상세보기 실패:아이디를 찾을 수 없습니다.");
		});
	}

	@Transactional
	public void 글수정(int id, Board reqBoard) {
		// 영속화 시키기위해서 셀렉트
		Board board = boardRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("글 찾기 실패:아이디를 찾을 수 없습니다.");
		});
		board.setTitle(reqBoard.getTitle());
		board.setContent(reqBoard.getContent());
		// 해당함수 종료시 트랜젝션이 종료되면서 더디체킹이 발동되서 자동 업데이트됨 플러쉬
	}

	@Transactional
	public void 글삭제(int id) {
		System.out.println(id);
		boardRepository.deleteById(id);
	}

	@Transactional
	public void 댓글쓰기(int boardId, Reply reply, User user) {

		// 게시글 테이블 정보 취득
		Board board = boardRepository.findById(boardId).orElseThrow(() -> {
			return new IllegalArgumentException("게시글 데이터가 없습니다. board ;" + boardId);
		});

		// 댓글 테이블에 등록
		reply.setBoard(board);
		reply.setContent(reply.getContent());
		reply.setUser(user);
		replyRepository.save(reply);
	}

	@Transactional
	public void 댓글쓰기2(ReplySaveRequestDto replySaveRequestDto) {

		// 방법1
		
//		// 게시글 테이블 정보 취득
//		User user = userRepository.findById(replySaveRequestDto.getUserId()).orElseThrow(()->{
//			return new IllegalArgumentException("유저 데이터가 없습니다. userId ;" + replySaveRequestDto.getUserId());
//		});
//		
//		// 게시글 테이블 정보 취득
//		Board board = boardRepository.findById(replySaveRequestDto.getBoardId()).orElseThrow(()->{
//			return new IllegalArgumentException("게시글 데이터가 없습니다. boardId ;" + replySaveRequestDto.getBoardId());
//		});
//		
//		Reply reply = Reply.builder()
//				.user(user)
//				.board(board)
//				.content(replySaveRequestDto.getContent())
//				.build();
//		
//		// 댓글 테이블에 등록
//		replyRepository.save(reply);
		
		// 방법2
		int result = replyRepository.mSave(
				replySaveRequestDto.getUserId(), 
				replySaveRequestDto.getBoardId(), 
				replySaveRequestDto.getContent()
		);
	}
	
	@Transactional
	public void 댓글삭제(int replyId) {
		replyRepository.deleteById(replyId);
	}

}
