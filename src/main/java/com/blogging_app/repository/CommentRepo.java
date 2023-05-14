package com.blogging_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogging_app.entity.Comment;

public interface CommentRepo extends JpaRepository<Comment, Integer>{

}
