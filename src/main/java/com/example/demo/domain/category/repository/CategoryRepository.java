package com.example.demo.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
