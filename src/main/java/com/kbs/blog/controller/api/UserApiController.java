package com.kbs.blog.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbs.blog.dto.ResponseDto;
import com.kbs.blog.dto.UserSaveRequestDto;
import com.kbs.blog.model.User;
import com.kbs.blog.service.UserService;

import jakarta.validation.Valid;

@RestController
public class UserApiController {

	@Autowired
	private UserService userService;

//	@Autowired
//	HttpSession session;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/auth/joinProc")
	public ResponseDto<Integer> save(@Valid @RequestBody UserSaveRequestDto userSaveRequestDto, BindingResult bindingResult) {

		System.out.println("UserApiController : save 호출");
		
		User user = new User();
		BeanUtils.copyProperties(userSaveRequestDto, user);
		
		userService.회원가입(user);

		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

//	@PostMapping("/api/user/login")
//	public ResponseDto<Integer> login(@RequestBody User user, HttpSession session) {
//		
//		System.out.println("UserApiController : login 호출");
//		User principal = userService.로그인(user); //   접근주체
//		
//		// Session작성
//		if (principal != null ) {
//			session.setAttribute("principal", principal);
//		}
//		
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//	}

	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user) {

		userService.회원수정(user);
		
		// 세션값을 갱신해 준다.
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

}
