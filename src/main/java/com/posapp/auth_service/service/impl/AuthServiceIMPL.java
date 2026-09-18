package com.posapp.auth_service.service.impl;

import com.posapp.auth_service.config.AdminCredentials;
import com.posapp.auth_service.dto.req.LoginRequestDTO;
import com.posapp.auth_service.dto.req.RegisterRequestDTO;
import com.posapp.auth_service.dto.res.AuthResponseDTO;
import com.posapp.auth_service.dto.res.UserResponseDTO;
import com.posapp.auth_service.entity.User;
import com.posapp.auth_service.enums.UserRole;
import com.posapp.auth_service.exception.EmailAlreadyExistsException;
import com.posapp.auth_service.exception.InvalidCredentialsException;
import com.posapp.auth_service.exception.PasswordMismatchException;
import com.posapp.auth_service.repo.UserRepo;
import com.posapp.auth_service.security.JwtService;
import com.posapp.auth_service.service.AuthService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceIMPL implements AuthService {

	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;
	private final UserDetailsService userDetailsService;
	private final JwtService jwtService;

	public AuthServiceIMPL(
			UserRepo userRepo,
			PasswordEncoder passwordEncoder,
			UserDetailsService userDetailsService,
			JwtService jwtService) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
		this.jwtService = jwtService;
	}

	@Override
	public UserResponseDTO registerUser(RegisterRequestDTO requestDTO) {
		if (!requestDTO.getPassword().equals(requestDTO.getConfirmPassword())) {
			throw new PasswordMismatchException("Password and confirm password do not match");
		}

		String fullName = requestDTO.getFullName().trim();
		String email = normalizeEmail(requestDTO.getEmail());

		if (AdminCredentials.ADMIN_EMAIL.equals(email)) {
			throw new EmailAlreadyExistsException("This email address is reserved for the administrator");
		}
		if (userRepo.existsByEmail(email)) {
			throw new EmailAlreadyExistsException("Email already exists");
		}

		User user = new User();
		user.setFullName(fullName);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
		user.setRole(UserRole.USER);
		user.setEnabled(true);

		User savedUser = userRepo.save(user);
		return mapToResponse(savedUser);
	}

	@Override
	public AuthResponseDTO login(LoginRequestDTO requestDTO) {
		String email = normalizeEmail(requestDTO.getEmail());

		if (AdminCredentials.ADMIN_EMAIL.equals(email)) {
			if (!AdminCredentials.ADMIN_PASSWORD.equals(requestDTO.getPassword())) {
				throw new InvalidCredentialsException("Invalid email or password");
			}
			UserDetails adminDetails = userDetailsService.loadUserByUsername(AdminCredentials.ADMIN_EMAIL);
			String accessToken = jwtService.generateToken(adminDetails, UserRole.ADMIN, null);
			return new AuthResponseDTO(accessToken, "Bearer", jwtService.getExpiration(), adminResponse());
		}

		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
		if (!user.isEnabled() || !passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("Invalid email or password");
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		String accessToken = jwtService.generateToken(userDetails, user.getRole(), user.getUserId());
		return new AuthResponseDTO(accessToken, "Bearer", jwtService.getExpiration(), mapToResponse(user));
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

	private UserResponseDTO adminResponse() {
		return new UserResponseDTO(
				null,
				AdminCredentials.ADMIN_NAME,
				AdminCredentials.ADMIN_EMAIL,
				UserRole.ADMIN,
				true,
				null);
	}

	private String normalizeEmail(String email) {
		return email.trim().toLowerCase();
	}
}
