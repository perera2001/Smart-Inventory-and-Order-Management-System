package com.posapp.auth_service.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

	private String accessToken;
	private String tokenType;
	private long expiresIn;
	private UserResponseDTO user;
}
