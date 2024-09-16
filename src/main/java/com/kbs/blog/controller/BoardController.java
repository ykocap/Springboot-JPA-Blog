package com.kbs.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.kbs.blog.model.Board;
import com.kbs.blog.service.BoardService;


@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;

	@GetMapping({ "", "/" })
	public String index(Model model,
			@PageableDefault(size = 3, sort = "id", direction = Direction.DESC) Pageable pageable) {
//	public String index(@AuthenticationPrincipal PrincipalDetail principal) { // 컨트롤러에서 세션을 어떻게 찾는지?
		// WEB-INF/views/index.jsp
//		System.out.println("로그인 사용자 아이디 : " + principal.getUsername());
		Page<Board> pagingBoards = boardService.글목록(pageable);
		model.addAttribute("boards", pagingBoards);
		return "index";
	}

	@GetMapping("/board/{id}")
	public String findId(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.글상세보기(id));
		return "board/detail";

	}

	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";
	}
	
	@GetMapping("/board/{id}/updateForm")
	public String update(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.글상세보기(id));
		return "board/updateForm";
	}

}
