package com.posapp.auth_service.dto.res;

import com.posapp.auth_service.enums.UserRole;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

	private Long userId;
	private String fullName;
	private String email;
	private UserRole role;
	private boolean enabled;
	private LocalDateTime createdAt;
}
