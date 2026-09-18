package com.posapp.auth_service.service.impl;

import com.posapp.auth_service.config.AdminCredentials;
import com.posapp.auth_service.dto.res.UserResponseDTO;
import com.posapp.auth_service.entity.User;
import com.posapp.auth_service.enums.UserRole;
import com.posapp.auth_service.exception.UserNotFoundException;
import com.posapp.auth_service.repo.UserRepo;
import com.posapp.auth_service.service.UserService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserServiceIMPL implements UserService {

	private final UserRepo userRepo;

	public UserServiceIMPL(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserResponseDTO getCurrentUser(String email) {
		String normalizedEmail = email.trim().toLowerCase();
		if (AdminCredentials.ADMIN_EMAIL.equals(normalizedEmail)) {
			return new UserResponseDTO(null, AdminCredentials.ADMIN_NAME, AdminCredentials.ADMIN_EMAIL, UserRole.ADMIN, true, null);
		}

		User user = userRepo.findByEmail(normalizedEmail)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		return mapToResponse(user);
	}

	@Override
	public List<UserResponseDTO> getAllUsers() {
		return userRepo.findAll().stream()
				.map(this::mapToResponse)
				.toList();
	}

	private UserResponseDTO mapToResponse(User user) {
		return new UserResponseDTO(
				user.getUserId(),
				user.getFullName(),
				user.getEmail(),
				user.getRole(),
				user.isEnabled(),
				user.getCreatedAt());
	}
}
