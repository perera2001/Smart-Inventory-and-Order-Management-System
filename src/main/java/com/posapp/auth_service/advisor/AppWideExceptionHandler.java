package com.posapp.auth_service.advisor;

import com.posapp.auth_service.exception.EmailAlreadyExistsException;
import com.posapp.auth_service.exception.InvalidCredentialsException;
import com.posapp.auth_service.exception.PasswordMismatchException;
import com.posapp.auth_service.exception.UnauthorizedException;
import com.posapp.auth_service.exception.UserNotFoundException;
import com.posapp.auth_service.util.StandardResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWideExceptionHandler {

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<StandardResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
		return build(HttpStatus.CONFLICT, ex.getMessage(), ex.getMessage());
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<StandardResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
		return build(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
	}

	@ExceptionHandler(PasswordMismatchException.class)
	public ResponseEntity<StandardResponse> handlePasswordMismatch(PasswordMismatchException ex) {
		return build(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<StandardResponse> handleUnauthorized(UnauthorizedException ex) {
		return build(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<StandardResponse> handleUserNotFound(UserNotFoundException ex) {
		return build(HttpStatus.NOT_FOUND, ex.getMessage(), null);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardResponse> handleValidation(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error ->
				errors.put(error.getField(), error.getDefaultMessage()));
		return build(HttpStatus.BAD_REQUEST, "Validation failed", errors);
	}

	@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
	public ResponseEntity<StandardResponse> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
		return build(HttpStatus.FORBIDDEN, "Access denied", null);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<StandardResponse> handleException(Exception ex) {
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", null);
	}

	private ResponseEntity<StandardResponse> build(HttpStatus status, String message, Object data) {
		return ResponseEntity.status(status).body(new StandardResponse(status.value(), message, data));
	}
}
