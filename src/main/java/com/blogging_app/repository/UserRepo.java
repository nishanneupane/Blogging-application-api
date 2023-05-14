package com.blogging_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogging_app.entity.User;

public interface UserRepo extends JpaRepository<User, Integer>{
	
	Optional<User> findByEmail(String email);

}
