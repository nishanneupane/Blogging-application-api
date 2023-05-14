package com.blogging_app.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogging_app.entity.Comment;
import com.blogging_app.entity.Post;
import com.blogging_app.exception.ResourceNotFoundException;
import com.blogging_app.payloads.CommentDto;
import com.blogging_app.repository.CommentRepo;
import com.blogging_app.repository.PostRepo;
import com.blogging_app.service.CommentService;

@Service
public class CommentServiceimpl implements CommentService{
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {
		Post post = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "Post id", postId));
		
		Comment comment = modelMapper.map(commentDto, Comment.class);
		
		comment.setPost(post);
		
		Comment save = commentRepo.save(comment);
		
		return modelMapper.map(save, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		Comment comment = commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment", "Comment id", commentId));
		
		commentRepo.delete(comment);
	}

}
