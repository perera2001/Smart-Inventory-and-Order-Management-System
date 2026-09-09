package com.posapp.auth_service.security;

import com.posapp.auth_service.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.expiration}")
	private long expiration;

	public String generateToken(UserDetails userDetails, UserRole role, Long userId) {
		Date now = new Date();
		var builder = Jwts.builder()
				.subject(userDetails.getUsername())
				.claim("role", role.name())
				.issuedAt(now)
				.expiration(new Date(now.getTime() + expiration));

		if (userId != null) {
			builder.claim("userId", userId);
		}

		return builder.signWith(getSigningKey()).compact();
	}

	public String extractUsername(String token) {
		Claims claims = extractAllClaims(token);
		return claims == null ? null : claims.getSubject();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	public long getExpiration() {
		return expiration;
	}

	private boolean isTokenExpired(String token) {
		Claims claims = extractAllClaims(token);
		return claims == null || claims.getExpiration().before(new Date());
	}

	private Claims extractAllClaims(String token) {
		try {
			return Jwts.parser()
					.verifyWith(getSigningKey())
					.build()
					.parseSignedClaims(token)
					.getPayload();
		} catch (JwtException | IllegalArgumentException ex) {
			return null;
		}
	}

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}
}
