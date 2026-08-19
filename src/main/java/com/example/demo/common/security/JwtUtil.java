package com.example.demo.common.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;

@Component
public class JwtUtil {

	@Value("${jwt.secret-key}")
	private String secret;

	@Value("${jwt.access-token-expire}")
	private long accessTokenExpire;

	@Getter
	@Value("${jwt.refresh-token-expire}")
	private long refreshTokenExpire;

	private SecretKey key;

	// 한 번만 실행, 암호화 키 객체 생성
	@PostConstruct
	public void init(){
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	// 토큰 발급
	public String createToken(Long userId, String email, String role){
		Date now = new Date();
		Date expiry = new Date(now.getTime() + accessTokenExpire);

		return Jwts.builder()
			.subject(String.valueOf(userId))
			.claim("email", email)
			.claim("role", role)
			.claim("type", "access")
			.issuedAt(now)
			.expiration(expiry)
			.signWith(key)
			.compact();
	}

	// Refresh Token 발급
	public String createRefreshToken(Long userId){

		Date now = new Date();
		Date expiry = new Date(now.getTime() + refreshTokenExpire);

		return Jwts.builder()
			.subject(String.valueOf(userId))
			.claim("type", "refresh")
			.issuedAt(now)
			.expiration(expiry)
			.signWith(key)
			.compact();
	}

	// 토큰 유효성 검증
	public boolean validateToken(String token){
		try{
			parseClaims(token);
			return true;
		} catch (Exception e){
			return false;
		}
	}

	// 토큰에서 userId 추출
	public Long getUserId(String token){
		return Long.parseLong(parseClaims(token).getSubject());
	}

	// 토큰에서 role 추출
	public String getRole(String token){
		return parseClaims(token).get("role", String.class);
	}

	// 공통 파싱 로직
	public Claims parseClaims(String token){
		return Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

}
