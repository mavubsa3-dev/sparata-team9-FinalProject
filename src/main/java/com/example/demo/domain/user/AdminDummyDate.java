package com.example.demo.domain.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.enums.UserRole;
import com.example.demo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminDummyDate implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void run(String... args) throws Exception{
		if(userRepository.findByEmail("admin@admin.com").isEmpty()){

			String encodedPassword = passwordEncoder.encode("admin1234!");

			User admin = new User(
				"admin@admin.com",
				encodedPassword,
				"총괄관리자",
				"010-9999-9999",
				UserRole.ADMIN
			);

			userRepository.save(admin);
		}

	}
}
