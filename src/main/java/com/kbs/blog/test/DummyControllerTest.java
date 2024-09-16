package com.kbs.blog.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbs.blog.model.RoleType;
import com.kbs.blog.model.User;
import com.kbs.blog.repository.UserRepository;

import jakarta.transaction.Transactional;

@RestController
public class DummyControllerTest {

	@Autowired // DI 의존성 주입
	private UserRepository userRepository;
	

	/* 
	 *delete
	*/	
	// http://localhost:8080/blog/dummy/user/1
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (Exception e) {
			return "삭제를 실패했습니다.";
		}
		return "삭제를 완료했습니다. id : " + id;
	}
	
	
	/* 
	 * update
	*/	
	@Transactional
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User reqUser) {
		System.out.println("id : " + id);
		System.out.println("password : " + reqUser.getPassword());
		System.out.println("email : " + reqUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하였습니다.");
		});
		user.setPassword(reqUser.getPassword());
		user.setEmail(reqUser.getEmail());
		
		// 방법1 save
		// save는 DB에 키데이터가 존재할 경우 update처리한다.
//		userRepository.save(user);
		
		// 방법2
		// @Transactional 추가
		// 더티체킹 : JPA에서 데이터 변경을 감지하는 것
		
		return user;
	}
	
	
	/* 
	 * select
	*/	
	
	@GetMapping("/dummy/users")
	public List<User> list() {
		return userRepository.findAll(); 
	}
	
	// http://localhost:8080/blog/dummy/user?page=1
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size = 2, sort = "id", direction = Direction.DESC) Pageable pageable){
		Page<User> pasingUser = userRepository.findAll(pageable);
		List<User> users = pasingUser.getContent();
		return users;
	}
	
	// http://localhost:8080/blog/dummy/user/1
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		
		// 방법1 없으면 에러
//		User user = userRepository.findById(id).get();

		// 방법2 없으면 에러안나고 공백데이터 돌려
//		User user = userRepository.findById(id).orElseGet(new Supplier<User>() {
//			// Supplier는 추상 클래스이기 때문에 구현해야한다.
//			@Override
//			public User get() {
//				// TODO Auto-generated method stub
//				return new User();
//			}
//		});

		// 방법3 없으면 에러나고 설정한 메세지 돌려줌, 보통aop로 에러핸들링 해서 페이지 전환이나 에러데이터로 돌려주겠급한다.
//		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
//			@Override
//			public IllegalArgumentException get() {
//				// TODO Auto-generated method stub
//				return new IllegalArgumentException("해당 유저는 없습니다. id : " + id);
//			}
//		});
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("해당 유저는 없습니다. id : " + id);
		});

		// user객체 = 자바 오브젝트
		// 웹브라우저가 이해할 수 있는 데이터를 json변경 (예전엔 gson 라이브러리 사용)
		// 스프링브트는 MessageConverter라는 애가 응답시에 자동 작동
		// jackson으로 자동 변경해서 리턴한다.
		return user;
	}

	/* 
	 * insert
	*/	
	@PostMapping("/dummy/join")
	public String join(@RequestBody User user) {

		System.out.println("username : " + user.getUsername());
		System.out.println("password : " + user.getPassword());
		System.out.println("email : " + user.getEmail());
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료 되었습니다.";
	}
}
