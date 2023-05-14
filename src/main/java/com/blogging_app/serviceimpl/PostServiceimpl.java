package com.blogging_app.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.blogging_app.entity.Category;
import com.blogging_app.entity.Post;
import com.blogging_app.entity.User;
import com.blogging_app.exception.ResourceNotFoundException;
import com.blogging_app.payloads.PostDto;
import com.blogging_app.payloads.PostResponse;
import com.blogging_app.payloads.UserDto;
import com.blogging_app.repository.CategoryRepo;
import com.blogging_app.repository.PostRepo;
import com.blogging_app.repository.UserRepo;
import com.blogging_app.service.PostService;

@Service
public class PostServiceimpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		User users = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));

		Post posts = modelMapper.map(postDto, Post.class);

		posts.setContent(postDto.getContent());
		posts.setTitle(postDto.getTitle());
		posts.setImageName("default.png");
		posts.setAddedDate(new Date());
		posts.setUser(users);
		posts.setCategory(category);

		Post save = postRepo.save(posts);

		return modelMapper.map(save, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post posts = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post ", "Post Id ", postId));
		
		posts.setTitle(postDto.getTitle());
		posts.setContent(postDto.getContent());
		posts.setImageName(postDto.getImageName());
		
		Post savedPost = postRepo.save(posts);		
		return modelMapper.map(savedPost, PostDto.class);
	}

	@Override
	public PostDto getByPostId(Integer postId) {
		Post posts = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "Post Id", postId));
		
		return modelMapper.map(posts, PostDto.class);
	}

	@Override
	public PostResponse getAllPosts(Integer pageNumber,Integer pageSize,String sortBy,String sortDir) {
		
		Sort sort=null;
		if(sortDir.equalsIgnoreCase("asc")) {
			sort=Sort.by(sortBy).ascending();
			
		}
		else {
			sort=Sort.by(sortBy).descending();
		}
		Pageable p=PageRequest.of(pageNumber, pageSize, sort);
		
		
		Page<Post> pagePost = postRepo.findAll(p);
		List<Post> posts = pagePost.getContent();
		
		List<PostDto> collect = posts.stream().map(post->modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		PostResponse postResponse=new PostResponse();
		postResponse.setContent(collect);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());
		
		return postResponse;
	}

	@Override
	public void deletePost(Integer postId) {
		Post posts = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "Post Id", postId));
		
		postRepo.delete(posts);
	}

	@Override
	public List<PostDto> getAllPostByCategory(Integer categoryId) {
		Category category = categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "Category Id", categoryId));
		
		List<Post> posts = postRepo.findByCategory(category);
		
		List<PostDto> collect = posts.stream().map(cat->modelMapper.map(cat, PostDto.class)).collect(Collectors.toList());
		return collect;
	}

	@Override
	public List<PostDto> getAllPostByUser(Integer userId) {
		User users = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User ", "User Id", userId));
		
		List<Post> posts = postRepo.findByUser(users);
		
		List<PostDto> collect = posts.stream().map(post->modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
		return collect;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		List<Post> posts = postRepo.findByTitleContaining(keyword);
		List<PostDto> postDtos = posts.stream().map(post->modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		return postDtos;
	}

}
