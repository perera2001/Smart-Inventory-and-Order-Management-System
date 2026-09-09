package com.posapp.auth_service.controller;

import com.posapp.auth_service.dto.req.LoginRequestDTO;
import com.posapp.auth_service.dto.req.RegisterRequestDTO;
import com.posapp.auth_service.service.AuthService;
import com.posapp.auth_service.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<StandardResponse> register(@RequestBody RegisterRequestDTO requestDTO) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new StandardResponse(201, "User registered successfully", authService.register(requestDTO)));
	}

	@PostMapping("/login")
	public ResponseEntity<StandardResponse> login(@RequestBody LoginRequestDTO requestDTO) {
		return ResponseEntity.ok(new StandardResponse(200, "Login successful", authService.login(requestDTO)));
	}
}
