package com.example.demo.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.user.entity.User;

public interface AuthRepository extends JpaRepository<User, Long> {
}
