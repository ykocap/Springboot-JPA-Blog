package com.kbs.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kbs.blog.model.User;

// DAO
// 자동으로 bean등록이 된다
// @Repository // 생략가능하다.
public interface UserRepository extends JpaRepository<User, Integer>{

	Optional<User> findByUsername(String username);
	
//	// 방법 1
//	// JPA Naming 쿼리전략
//	// SELECT * FROM user WHERE username= ? AND password = ? ;
//	User findByUsernameAndPassword(String username, String password);

//	// 방법 2
//	@Query(value = "SELECT * FROM user WHERE username= ? AND password = ?", nativeQuery = true)
//	User login(String username, String password);
		
}
