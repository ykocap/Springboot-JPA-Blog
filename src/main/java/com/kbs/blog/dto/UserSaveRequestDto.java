package com.kbs.blog.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSaveRequestDto {

	@NotNull(message = "유저네임을 입력하세요.")
	@NotBlank(message = "유저네임을 입력하세요.")
	@Size(max = 10, message = "유저네임 길이를 초과하였습니다.")
	private String username;

	@NotNull(message = "비밀번호를 입력하세요.")
	@NotBlank(message = "비밀번호를 입력하세요.")
	private String password;
	
	@Email(message = "이메일 양식이 바람직 하지 않습니다.")
	private String email;
	
}
