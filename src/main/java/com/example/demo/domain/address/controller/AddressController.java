package com.example.demo.domain.address.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.address.dto.request.CreateAddressRequest;
import com.example.demo.domain.address.dto.request.UpdateAddressRequest;
import com.example.demo.domain.address.dto.response.CreateAddressResponse;
import com.example.demo.domain.address.dto.response.GetAddressInfoResponse;
import com.example.demo.domain.address.dto.response.UpdateAddressResponse;
import com.example.demo.domain.address.service.AddressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {

	private final AddressService addressService;

	@GetMapping
	public ResponseEntity<List<GetAddressInfoResponse>> getAddress(@AuthenticationPrincipal Long userId){
		return ResponseEntity.status(HttpStatus.OK).body(addressService.getAddressInfo(userId));
	}

	@PatchMapping("/{addressId}")
	public ResponseEntity<UpdateAddressResponse> updateAddress(@Valid @RequestBody UpdateAddressRequest request, @AuthenticationPrincipal Long userId
	, @PathVariable Long addressId){
		return ResponseEntity.status(HttpStatus.OK).body(addressService.updateAddress(request, userId, addressId));
	}

	@PostMapping
	public ResponseEntity<CreateAddressResponse> createAddress(@Valid @RequestBody CreateAddressRequest request, @AuthenticationPrincipal Long userId){

		return ResponseEntity.status(HttpStatus.CREATED).body(addressService.createAddress(request, userId));
	}
}
