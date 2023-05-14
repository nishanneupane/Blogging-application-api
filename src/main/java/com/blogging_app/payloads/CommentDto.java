package com.blogging_app.payloads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
	private Integer id;
	private String content;
}
