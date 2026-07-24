package com.example.demo.domain.auth.service;

import com.example.demo.domain.cart.entity.Cart;
import com.example.demo.domain.cart.repository.CartRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.exception.CustomException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.common.security.JwtUtil;
import com.example.demo.domain.auth.dto.request.LoginRequest;
import com.example.demo.domain.auth.dto.request.SignupRequest;
import com.example.demo.domain.auth.dto.response.LoginResponse;
import com.example.demo.domain.auth.dto.response.SignupResponse;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Transactional
	public SignupResponse signUp(SignupRequest request){
		if(userRepository.existsByEmail(request.email())){
			throw new CustomException(ErrorCode.EMAIL_DUPLICATE);
		}
		String encodedPassword = passwordEncoder.encode(request.password());
		User user = new User(request.email(), encodedPassword, request.name(), request.phoneNumber());
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
		return new LoginResponse(token);
	}
}