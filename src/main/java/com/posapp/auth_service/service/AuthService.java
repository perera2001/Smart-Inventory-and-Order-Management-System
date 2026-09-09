package com.posapp.auth_service.service;

import com.posapp.auth_service.dto.req.LoginRequestDTO;
import com.posapp.auth_service.dto.req.RegisterRequestDTO;
import com.posapp.auth_service.dto.res.AuthResponseDTO;

public interface AuthService {

	AuthResponseDTO register(RegisterRequestDTO requestDTO);

	AuthResponseDTO login(LoginRequestDTO requestDTO);
}
