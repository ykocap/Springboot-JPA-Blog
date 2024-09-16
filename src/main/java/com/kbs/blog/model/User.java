package com.kbs.blog.model;

import java.sql.Timestamp;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity // User 클래스가 mariadb 에 테이블이 생성이 된다.
//@DynamicInsert // insert값이null인 경우, insert 항목에서 제외시킨다. 컬럼의 default 값 적용하고 싶을때 사

public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//프로젝트에서 연결된 DB의 넘버링 전략을 다라간다.
	private int id; //seq , auto-increment
	
	@Column(nullable = false, length = 100, unique = true)
	private String username;

	@Column(nullable = false, length = 100) // 123456 => 해쉬(비밀번호 암호)
	private String password;
	
	@Column(nullable = false, length = 50)
	private String email;
	
//	@ColumnDefault("'user'") //''로 줘야 sql error 가 발생안함
//	private String role; // Enum을 쓰는게 좋다 // domain(어떤 범위)정의가능 admin,user, manager; 성별, 직업등
	
	// DB는 RoleType이없다.
	@Enumerated(EnumType.STRING)
	private RoleType role;
	
	private String oauth; // kakao, google로그인 플러그
	
	@CreationTimestamp // insert 시 시간이 자동 입력
	private Timestamp createDate;

}
