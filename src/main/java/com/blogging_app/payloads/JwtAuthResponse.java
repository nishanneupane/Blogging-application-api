package com.blogging_app.payloads;

import com.blogging_app.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthResponse {
	
	private String token;
	private UserDto user;
}
