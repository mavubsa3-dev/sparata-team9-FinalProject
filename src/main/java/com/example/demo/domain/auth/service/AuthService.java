package com.example.demo.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.common.security.JwtUtil;
import com.example.demo.common.security.RefreshTokenService;
import com.example.demo.domain.auth.dto.request.LoginRequest;
import com.example.demo.domain.auth.dto.request.SignupRequest;
import com.example.demo.domain.auth.dto.response.LoginResponse;
import com.example.demo.domain.auth.dto.response.SignupResponse;
import com.example.demo.domain.cart.entity.Cart;
import com.example.demo.domain.cart.repository.CartRepository;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.enums.UserRole;
import com.example.demo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final RefreshTokenService refreshTokenService;

	@Transactional
	public SignupResponse signUp(SignupRequest request){
		if(userRepository.existsByEmail(request.email())){
			throw new CustomException(ErrorCode.EMAIL_DUPLICATE);
		}

		String encodedPassword = passwordEncoder.encode(request.password());

		User user = new User(request.email(), encodedPassword, request.name(), request.phoneNumber(), UserRole.USER);

		User savedUser = userRepository.save(user);

		Cart cart = new Cart(savedUser);
		cartRepository.save(cart);

		return SignupResponse.from(savedUser);
	}

	@Transactional(readOnly = true)
	public LoginResponse login(LoginRequest request){
		User user = userRepository.findByEmail(request.email()).orElseThrow(
				() -> new CustomException(ErrorCode.INVALID_CREDENTIALS)
		);

		if (!passwordEncoder.matches(request.password(), user.getPassword())){
			throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
		}

		String token = jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole().name());
		String refreshToken = jwtUtil.createRefreshToken(user.getId());

		refreshTokenService.save(user.getId(), refreshToken, jwtUtil.getRefreshTokenExpire());

		return new LoginResponse(token, refreshToken);
	}

	@Transactional(readOnly = true)
	public LoginResponse reissue(String refreshToken) {

		if (!jwtUtil.validateToken(refreshToken)) {
			throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
		}

		String type = jwtUtil.parseClaims(refreshToken).get("type", String.class);
		if (!"refresh".equals(type)) {
			throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
		}

		Long userId = jwtUtil.getUserId(refreshToken);

		String savedRefreshToken = refreshTokenService.findByUserId(userId);
		if (savedRefreshToken == null || ! savedRefreshToken.equals(refreshToken)) {
			throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
		}

		User user = userRepository.findById(userId).orElseThrow(
			() -> new CustomException(ErrorCode.USER_NOT_FOUND)
		);


		String newAccessToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole().name());
		String newRefreshToken = jwtUtil.createRefreshToken(user.getId());

		refreshTokenService.save(user.getId(), newRefreshToken, jwtUtil.getRefreshTokenExpire());

		return new LoginResponse(newAccessToken, newRefreshToken);
	}

	@Transactional
	public void logout(Long userId) {
		refreshTokenService.deleteByUserId(userId);
	}
}
