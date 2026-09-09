package com.posapp.auth_service.service;

import com.posapp.auth_service.dto.res.UserResponseDTO;

public interface UserService {

	UserResponseDTO getUserById(Long id);
}
