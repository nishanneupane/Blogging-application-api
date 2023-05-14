package com.blogging_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogging_app.entity.Category;
import com.blogging_app.entity.Post;
import com.blogging_app.entity.User;

public interface PostRepo extends JpaRepository<Post, Integer>{
	List<Post> findByUser(User user);
	List<Post> findByCategory(Category category);
	
	List<Post> findByTitleContaining(String title);

}
