package com.blogging_app.payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.blogging_app.entity.Category;
import com.blogging_app.entity.Comment;
import com.blogging_app.entity.User;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
	
	private Integer postId;
	
	@NotEmpty
	@Size(min=7)
	private String title;
	
	@NotEmpty
	@Size(min=10,max=10000)
	private String content;

	private String imageName;
	private Date addedDate;
	private UserDto user;
	private CategoryDto category;
	
	private Set<CommentDto> comments=new HashSet<>();
	
	

}
