package com.blogging_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogging_app.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer>{

	
}
