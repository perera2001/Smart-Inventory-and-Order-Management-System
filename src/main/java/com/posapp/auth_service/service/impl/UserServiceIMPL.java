package com.posapp.auth_service.service.impl;

import com.posapp.auth_service.dto.res.UserResponseDTO;
import com.posapp.auth_service.entity.User;
import com.posapp.auth_service.exception.UserNotFoundException;
import com.posapp.auth_service.repo.UserRepo;
import com.posapp.auth_service.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceIMPL implements UserService {

	private final UserRepo userRepo;

	public UserServiceIMPL(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserResponseDTO getUserById(Long id) {
		User user = userRepo.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
	}
}
