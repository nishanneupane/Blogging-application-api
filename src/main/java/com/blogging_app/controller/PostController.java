package com.blogging_app.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blogging_app.config.AppConstants;
import com.blogging_app.entity.Post;
import com.blogging_app.payloads.ApiResponse;
import com.blogging_app.payloads.PostDto;
import com.blogging_app.payloads.PostResponse;
import com.blogging_app.service.FileService;
import com.blogging_app.service.PostService;
import com.blogging_app.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class PostController {
	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;

	@Autowired
	private ModelMapper modelMapper;

	@Value("${project.image}")
	private String path;

	@PostMapping("/user/{userId}/categories/{categoryId}/posts")
	private ResponseEntity<PostDto> createPosts(@Valid @RequestBody PostDto post, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {
		return new ResponseEntity<PostDto>(postService.createPost(post, userId, categoryId), HttpStatus.CREATED);

	}

	@GetMapping("/post")
	private ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		PostResponse allPosts = postService.getAllPosts(pageNumber, pageSize, sortBy, sortDir);

		return new ResponseEntity<PostResponse>(allPosts, HttpStatus.OK);
	}

	@PutMapping("/post/{postId}")
	private ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable Integer postId) {
		return new ResponseEntity<PostDto>(postService.updatePost(postDto, postId), HttpStatus.OK);
	}

	@DeleteMapping("/post/{postId}")
	private ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId) {
		postService.deletePost(postId);

		return new ResponseEntity<ApiResponse>(new ApiResponse("Post Deleted Sucessfully ", true), HttpStatus.OK);
	}

	@GetMapping("/post/{postId}")
	private ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
		PostDto posts = postService.getByPostId(postId);
		PostDto getPost = modelMapper.map(posts, PostDto.class);
		return new ResponseEntity<PostDto>(getPost, HttpStatus.OK);
	}

	@GetMapping("/category/{categoryId}/post")
	private ResponseEntity<List<PostDto>> getAllPostByCategoryId(@PathVariable Integer categoryId) {
		List<PostDto> allpost = postService.getAllPostByCategory(categoryId);

		List<PostDto> collects = allpost.stream().map(post -> modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		return new ResponseEntity<List<PostDto>>(collects, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}/post")
	private ResponseEntity<List<PostDto>> getAllPostByUser(@PathVariable Integer userId) {
		List<PostDto> allposts = postService.getAllPostByUser(userId);

		List<PostDto> collect = allposts.stream().map(post -> modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		return new ResponseEntity<List<PostDto>>(collect, HttpStatus.OK);
	}

	@GetMapping("/posts/search/{keywords}")
	public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable(name = "keywords") String keywords) {
		List<PostDto> result = postService.searchPosts(keywords);

		return new ResponseEntity<List<PostDto>>(result, HttpStatus.OK);
	}

	// Image Upload

	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(@RequestParam(name = "image") MultipartFile multipartFile,
			@PathVariable Integer postId) throws IOException {

		PostDto postDto = postService.getByPostId(postId);
		String filename = fileService.uploadImage(path, multipartFile);

		postDto.setImageName(filename);
		PostDto updatePost = postService.updatePost(postDto, postId);

		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
	}

	// method to serve files
	@GetMapping(value = "/post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable("imageName") String imageName, jakarta.servlet.http.HttpServletResponse response)
			throws IOException {

		InputStream resource = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		org.springframework.util.StreamUtils.copy(resource, response.getOutputStream());

	}

}
