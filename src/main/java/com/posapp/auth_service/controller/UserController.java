package com.posapp.auth_service.controller;

import com.posapp.auth_service.service.UserService;
import com.posapp.auth_service.util.StandardResponse;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/me")
	public ResponseEntity<StandardResponse> getCurrentUser(Principal principal) {
		return ResponseEntity.ok(new StandardResponse(
				200,
				"Current account fetched successfully",
				userService.getCurrentUser(principal.getName())));
	}

	@GetMapping("/admin/all")
	public ResponseEntity<StandardResponse> getAllUsers() {
		return ResponseEntity.ok(new StandardResponse(200, "Users fetched successfully", userService.getAllUsers()));
	}
}
