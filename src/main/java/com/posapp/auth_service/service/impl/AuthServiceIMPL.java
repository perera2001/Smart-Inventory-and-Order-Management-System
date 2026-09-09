package com.posapp.auth_service.service.impl;

import com.posapp.auth_service.dto.req.LoginRequestDTO;
import com.posapp.auth_service.dto.req.RegisterRequestDTO;
import com.posapp.auth_service.dto.res.AuthResponseDTO;
import com.posapp.auth_service.dto.res.UserResponseDTO;
import com.posapp.auth_service.entity.User;
import com.posapp.auth_service.enums.UserRole;
import com.posapp.auth_service.exception.EmailAlreadyExistsException;
import com.posapp.auth_service.exception.InvalidCredentialsException;
import com.posapp.auth_service.repo.UserRepo;
import com.posapp.auth_service.security.JwtService;
import com.posapp.auth_service.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceIMPL implements AuthService {

	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final JwtService jwtService;

	public AuthServiceIMPL(
			UserRepo userRepo,
			PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager,
			UserDetailsService userDetailsService,
			JwtService jwtService) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.jwtService = jwtService;
	}

	@Override
	public AuthResponseDTO register(RegisterRequestDTO requestDTO) {
		if (userRepo.existsByEmail(requestDTO.getEmail())) {
			throw new EmailAlreadyExistsException("Email already exists");
		}

		User user = new User();
		user.setName(requestDTO.getName());
		user.setEmail(requestDTO.getEmail());
		user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
		user.setRole(requestDTO.getRole() == null ? UserRole.USER : requestDTO.getRole());

		User savedUser = userRepo.save(user);
		UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
		return new AuthResponseDTO(jwtService.generateToken(userDetails), mapToResponse(savedUser));
	}

	@Override
	public AuthResponseDTO login(LoginRequestDTO requestDTO) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword()));
		} catch (BadCredentialsException ex) {
			throw new InvalidCredentialsException("Invalid email or password");
		}

		User user = userRepo.findByEmail(requestDTO.getEmail())
				.orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		return new AuthResponseDTO(jwtService.generateToken(userDetails), mapToResponse(user));
	}

	private UserResponseDTO mapToResponse(User user) {
		return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
	}
}
