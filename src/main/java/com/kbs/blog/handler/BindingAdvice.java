package com.kbs.blog.handler;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.kbs.blog.dto.ResponseDto;

// 메모리에 띄우기 위한 어노테이션
// @Controller, @RestConroller, @Component, @Configuration
// @Component는Controlle가 뜬이후에 뜬다

@Aspect
@Component
public class BindingAdvice {

	// 함수 : 앞뒤
	// 함수 : 앞(username이 안들어 왔으면 내가 강제로 넣어주고 실행)
	// 함수 : 뒤 (응답만 처리)
	
	// @Before
	// @After
	@Around("execution(* com.kbs.blog.controller.api.*Controller.*(..))")
	public Object validCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		String type = proceedingJoinPoint.getSignature().getDeclaringTypeName();
		String method = proceedingJoinPoint.getSignature().getName();
		
		System.out.println("AOP executed type : " + type);
		System.out.println("AOP executed method : " + method);
		
		Object[] args = proceedingJoinPoint.getArgs();
		
		for (Object arg : args) {
			if (arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult)arg;
				
				if (bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap();
					
					for (FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}
					
					return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), errorMap);
				}
			}
		}
		
		return proceedingJoinPoint.proceed(); // 함수의 뒷처리를 실행하라
		
	}
}
