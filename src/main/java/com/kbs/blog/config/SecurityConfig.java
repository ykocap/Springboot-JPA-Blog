package com.kbs.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


// 빈등록:스프링 컨테이너에서 객체를 관리할 수있게 하는것
@Configuration // 빈등록(IOC관리)
//@EnableWebSecurity // 시큐리티 필터가 등록이 된다. 그설정을 filterCharin에서 해준다.
//@EnableMethodSecurity(prePostEnabled = true) // 특정주소로 접근을 하면 권한 및 인증을 미리 체크하겠다라는 뜻
public class SecurityConfig{

//	@Bean
//	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//		// CSRF TOKKEN非活性
//		http.csrf((c) -> c.disable());
//
//		// ログイン認証の設定
//		http.formLogin((login) -> login
//				// フォーム認証のログイン画面のURL
//				.loginPage("/auth/loginForm")
//				// パラメーター項目名
//				.usernameParameter("username").passwordParameter("password")
//				// ユーザー名・パスワードの送信先のURL
//				.loginProcessingUrl("/auth/loginProc")
//				// 認証成功時に遷移するURL
//				.defaultSuccessUrl("/")
//				// ログイン画面は未ログインでもアクセス可能
//				.permitAll());
//
////		// ログアウト処理の設定
////		http.logout((logout) -> logout
////				// ログアウトのURL
////				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
////				// ログアウト成功時のURL（ログイン画面に遷移）
////				.logoutSuccessUrl("/")
////				// Cookieの値を削除する
////				.deleteCookies("JSESSIONID")
////				// セッションを無効化する
////				.invalidateHttpSession(true).permitAll());
//
////		// 認証アドレス設定
////		http.authorizeHttpRequests((auth) -> auth
////				// static対象"/images/**", "/css/**", "/js/**"
////				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
////				// 認証対象外のURL指定（静的ファイル、エラーページ遷移のURL）
////				.requestMatchers("/", "/auth/**").permitAll()
////				// ログイン画面のURLも認証対象外とする
////				.requestMatchers("/auth/loginForm").permitAll()
////				// 上記以外は認証が必要
////				.anyRequest().authenticated());
//		// 認証アドレス設定
//		http.authorizeHttpRequests(
//				(auth) -> auth.requestMatchers("/board/**", "/user/**").authenticated().anyRequest().permitAll());
//
//		return http.build();
//	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// 1. csrf 비활성화
		http.csrf(c -> c.disable());

		// 2. 인증 주소 설정 (WEB-INF/** 추가해줘야 함. 아니면 인증이 필요한 주소로 무한 리다이렉션 일어남)
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/WEB-INF/**", "/", "/auth/**", "/js/**", "/css/**", "/image/**", "/dummy/**").permitAll()
				.anyRequest().authenticated());

		// 3. 로그인 처리 프로세스 설정
		http.formLogin(f -> f
				.loginPage("/auth/loginForm")
				.loginProcessingUrl("/auth/loginProc")
				.defaultSuccessUrl("/"));

		return http.build();
	}

	@Bean // IoC가 됨
	BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
	}

	// 시큐리티가 대신 로그인 해주는데 password를 가로채기를 하는데
	// 해당 password 가 멀로 해쉬가 되어 회원가입이 되었는지 알아야
	// 같은 해쉬로 암호화 해서 DB에 있는 해쉬랑 비교할수 있음
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	

}
