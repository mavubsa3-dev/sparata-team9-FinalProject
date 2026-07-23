package com.example.demo.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
