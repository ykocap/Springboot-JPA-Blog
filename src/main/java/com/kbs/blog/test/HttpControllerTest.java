package com.kbs.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpControllerTest {

	@GetMapping("/http/lombok")
	public String lombokTest() {
		Member m = Member.builder().username("kbs").password("1234").email("test@nate.com").build();
		System.out.println(m.getId());
		m.setId(5000);
		System.out.println(m.getId());
		System.out.println(m.getUsername());
		return "lombok test complete";
		
	}
	//messageConverter json -> bean parsing
	// http://localhost:8080/http/get (select)
	@GetMapping("/http/get")
	public String getTest(Member m) {
		return "get req : " + m.getId() + "," + m.getUsername() + "," + m.getPassword() + "," + m.getEmail();
		
	}

	// http://localhost:8080/http/post (insert)
	@PostMapping("/http/post")
	public String postTest(@RequestBody Member m) {
		return "post req : " + m.getId() + "," + m.getUsername() + "," + m.getPassword() + "," + m.getEmail();
	}

	// http://localhost:8080/http/put (update)
	@PutMapping("/http/put")
	public String putTest(@RequestBody Member m) {
		return "put req : " + m.getId() + "," + m.getUsername() + "," + m.getPassword() + "," + m.getEmail();
	}

	// http://localhost:8080/http/delete (delete)
	@DeleteMapping("/http/delete")
	public String deleteTest() {
		return "delete req";
	}
}
