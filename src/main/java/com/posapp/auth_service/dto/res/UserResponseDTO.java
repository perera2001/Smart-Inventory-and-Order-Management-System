package com.posapp.auth_service.dto.res;

import com.posapp.auth_service.enums.UserRole;

public class UserResponseDTO {

	private Long id;
	private String name;
	private String email;
	private UserRole role;

	public UserResponseDTO(Long id, String name, String email, UserRole role) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
}
