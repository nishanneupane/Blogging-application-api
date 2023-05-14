package com.blogging_app.controller;

import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogging_app.entity.User;
import com.blogging_app.exception.ApiException;
import com.blogging_app.payloads.JwtAuthRequest;
import com.blogging_app.payloads.JwtAuthResponse;
import com.blogging_app.payloads.UserDto;
import com.blogging_app.security.JwtTokenHelper;
import com.blogging_app.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("/login")
//	@CrossOrigin("*")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {
		authenticate(request.getUsername(), request.getPassword());

		UserDetails loadUserByUsername = userDetailsService.loadUserByUsername(request.getUsername());

		String token = jwtTokenHelper.generateToken(loadUserByUsername);

		JwtAuthResponse response = new JwtAuthResponse(); 

		response.setToken(token);
		response.setUser(modelMapper.map((User)loadUserByUsername, UserDto.class));
		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
	}
	

	private void authenticate(String username, String password) throws Exception {

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				username, password);

		try {
			authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		} catch (BadCredentialsException e) {
			throw new ApiException("Invalid username or password");
			
		}

	}
	
	
	//register new user api
	
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto){
		UserDto registeredUser = userService.registerNewUser(userDto);
	
		return new ResponseEntity<UserDto>(registeredUser,HttpStatus.CREATED);
	}
}
