package com.kbs.blog.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.kbs.blog.dto.ResponseDto;


@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    public ResponseDto<Object> handleValidationExceptions(MethodArgumentNotValidException e) {
//        List<String> errors = e.getBindingResult()
//                                .getAllErrors()
//                                .stream()
//                                .map(error -> error.getDefaultMessage())
//                                .collect(Collectors.toList());
//		return new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), errors);
//
//    }

	@ExceptionHandler(value = Exception.class)
	public ResponseDto<String> handleALLException(Exception e) {
		return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
	}
	
	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseDto<String> handleArgumentException(IllegalArgumentException e) {
		return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
	}

}
