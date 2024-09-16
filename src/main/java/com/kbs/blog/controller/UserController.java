package com.kbs.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbs.blog.config.auth.PrincipalDetailService;
import com.kbs.blog.model.KakaoProfile;
import com.kbs.blog.model.OAuthToken;
import com.kbs.blog.model.User;
import com.kbs.blog.service.UserService;

import jakarta.servlet.http.HttpSession;


/*
 * 인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/** 허용
 * 그냥 주소가 / 이면 index.jsp 허용
 * static이하에 있는 /js/**, /css/**, /image/** 허용
*/

@Controller
public class UserController {

	@Value("${kbs.key}")
	private String kbsKey;
	
	@Autowired
	UserService userService;
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private PrincipalDetailService principalDetailService;
	
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}

	@GetMapping("/auth/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}

	@GetMapping("/user/updateForm")
	public String updateForm() {
		return "user/updateForm";
	}

	@GetMapping("/auth/kakao/callback")
	public String kakaoCallback(String code, HttpSession session) {

		String kakaoToken = getKakaoToken(code);
		
		getKakaoProfile(kakaoToken, session);
		
		return "redirect:/";
	}

	private String getKakaoToken(String code) {

		String requestUrl = "https://kauth.kakao.com/oauth/token";
		String clientId = "b55f69560154b966e8fdb26e5845aa9e"; // 카카오 REST API 키
		String redirectUri = "http://localhost:8080/auth/kakao/callback"; // 설정한 리다이렉트 URI

		RestTemplate rt = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

		// HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", clientId);
		params.add("redirect_uri", redirectUri);
		params.add("code", code);

		// HttpHeader 와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity(params, headers);

		// Http 요청하기
		ResponseEntity<String> response = rt.exchange(
				requestUrl, 
				HttpMethod.POST, 
				kakaoTokenRequest, 
				String.class,
				params
		);
		
		// Gson, Json Simple, ObjectMapper등의 라이브러리가 있다
		ObjectMapper objectMapper = new ObjectMapper();

		OAuthToken oathToken = null;
		try {
			oathToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println("카카오 토큰 응답정보: " + response.getBody());
		System.out.println("카카오 토큰 응답정보 객체: " + oathToken.getAccess_token());
		
		return oathToken.getAccess_token();

	}


	private void getKakaoProfile(String kakaoToken, HttpSession session) {

		String requestUrl = "https://kapi.kakao.com/v2/user/me";

		RestTemplate rt = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + kakaoToken);
		headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

		// HttpHeader 와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity(headers);

		// Http 요청하기
		ResponseEntity<String> response = rt.exchange(
				requestUrl, 
				HttpMethod.POST, 
				kakaoTokenRequest, 
				String.class
		);
		
		// Gson, Json Simple, ObjectMapper등의 라이브러리가 있다
		ObjectMapper objectMapper = new ObjectMapper();

		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = objectMapper.readValue(response.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		User kakaoUser = User.builder()
				.username("k" + kakaoProfile.getId())
				.password(kbsKey)
				.email(kakaoProfile.getId() + "@email.com")
				.oauth("kakao")
				.build();
		
		// 가입자 혹은 비가입자 체크 해서 처리
		User originUser = userService.회원찾기(kakaoUser.getUsername());
		
		if (originUser == null) {
			System.out.println("기존 회원이 아닙니다");
			userService.회원가입(kakaoUser);
		} 

		System.out.println("자동로그인을 진행합니다.");
		
		// 로그인 처리
//		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), kbsKey));
//  		SecurityContextHolder.getContext().setAuthentication(authentication);
  		
  		UserDetails userDetails = principalDetailService.loadUserByUsername(kakaoUser.getUsername());
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);
		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

		

	}
}
