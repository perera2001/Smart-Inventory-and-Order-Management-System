package com.posapp.auth_service.service;

import com.posapp.auth_service.dto.res.UserResponseDTO;
import java.util.List;

public interface UserService {

	UserResponseDTO getCurrentUser(String email);

	List<UserResponseDTO> getAllUsers();
}
