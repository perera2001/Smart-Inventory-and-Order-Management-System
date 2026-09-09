package com.posapp.auth_service.advisor;

import com.posapp.auth_service.exception.EmailAlreadyExistsException;
import com.posapp.auth_service.exception.InvalidCredentialsException;
import com.posapp.auth_service.exception.UnauthorizedException;
import com.posapp.auth_service.exception.UserNotFoundException;
import com.posapp.auth_service.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWideExceptionHandler {

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<StandardResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
		return build(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler({InvalidCredentialsException.class, UnauthorizedException.class})
	public ResponseEntity<StandardResponse> handleUnauthorized(RuntimeException ex) {
		return build(HttpStatus.UNAUTHORIZED, ex.getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<StandardResponse> handleUserNotFound(UserNotFoundException ex) {
		return build(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<StandardResponse> handleException(Exception ex) {
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error");
	}

	private ResponseEntity<StandardResponse> build(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(new StandardResponse(status.value(), message, null));
	}
}
