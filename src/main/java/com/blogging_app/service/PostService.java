package com.blogging_app.service;

import java.util.List;

import com.blogging_app.payloads.PostDto;
import com.blogging_app.payloads.PostResponse;

public interface PostService {
	PostDto createPost(PostDto postDto,Integer userId,Integer categoryId);
	
	PostDto updatePost(PostDto postDto,Integer postId);
	
	PostDto getByPostId(Integer postId);
	
	PostResponse getAllPosts(Integer pageNumber,Integer pageSize,String sortBy,String sortDir);
	
	void deletePost(Integer postId);
	
	List<PostDto> getAllPostByCategory(Integer categoryId);
	
	List<PostDto> getAllPostByUser(Integer userId);
	
	List<PostDto> searchPosts(String keyword);
}
