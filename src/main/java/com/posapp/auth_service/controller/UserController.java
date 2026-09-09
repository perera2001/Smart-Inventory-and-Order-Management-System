package com.posapp.auth_service.controller;

import com.posapp.auth_service.service.UserService;
import com.posapp.auth_service.util.StandardResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<StandardResponse> getUser(@PathVariable Long id) {
		return ResponseEntity.ok(new StandardResponse(200, "User found", userService.getUserById(id)));
	}
}
