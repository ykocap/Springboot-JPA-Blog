package com.kbs.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kbs.blog.model.RoleType;
import com.kbs.blog.model.User;
import com.kbs.blog.repository.UserRepository;

// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌.IOC를해준다.
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Transactional
	public User 회원찾기(String username) {
		return userRepository.findByUsername(username).orElseGet(()->{
			return null;
		});
	}
	
	@Transactional
	public void 회원가입(User user) {
		String rawPassword = user.getPassword(); // 1234원문
		String encPassword = encoder.encode(rawPassword); // 해쉬
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);
		userRepository.save(user);
	}

//	@Transactional(readOnly = true) // Select 할때 트랙잭션 시작, 서비스 종료시에 트랜잭션 종료(정합성 유지)
//	public User 로그인(User user) {
//		return userRep.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//	}

	@Transactional
	public void 회원수정(User reqUser) {
		User user = userRepository.findById(reqUser.getId()).orElseThrow(()->{
			return new IllegalArgumentException("");
		});
		
		// Validate체크
		if (user.getOauth() == null || user.getOauth().equals("")) {
			String rawPassword = reqUser.getPassword();
			String encPassword = encoder.encode(rawPassword);
			user.setPassword(encPassword);
			user.setEmail(reqUser.getEmail());
		}
		

	}
}
