package com.blogging_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogging_app.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Integer>{

}
