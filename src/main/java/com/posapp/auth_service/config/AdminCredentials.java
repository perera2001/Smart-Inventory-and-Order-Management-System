package com.posapp.auth_service.config;

public final class AdminCredentials {

	// Hardcoded credentials are only for this learning project.
	// Production credentials should come from environment variables or AWS Secrets Manager.
	public static final String ADMIN_EMAIL = "admin@inventory.com";
	public static final String ADMIN_PASSWORD = "Admin@123";
	public static final String ADMIN_NAME = "System Admin";

	private AdminCredentials() {
	}
}
