package com.example.demo.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.user.dto.request.UpdateUserinfoRequest;
import com.example.demo.domain.user.dto.response.GetUserInfoResponse;
import com.example.demo.domain.user.dto.response.UpdateUserResponse;
import com.example.demo.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/me")
public class UserController {

	private final UserService userService;

	@GetMapping("/{userId}")
	public ResponseEntity<GetUserInfoResponse> getUserInfo(@PathVariable Long userId){
		return ResponseEntity.status(HttpStatus.OK).body(userService.userInfo(userId));
	}

	@PatchMapping("/{userId}")
	public ResponseEntity<UpdateUserResponse> updateUserInfo(@PathVariable Long userId, @Valid @RequestBody UpdateUserinfoRequest request){
		return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserInfo(request, userId));
	}
}
