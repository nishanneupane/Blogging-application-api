package com.blogging_app.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogging_app.config.AppConstants;
import com.blogging_app.entity.Role;
import com.blogging_app.entity.User;
import com.blogging_app.exception.ResourceNotFoundException;
import com.blogging_app.payloads.UserDto;
import com.blogging_app.repository.RoleRepo;
import com.blogging_app.repository.UserRepo;
import com.blogging_app.service.UserService;

@Service
public class UserServiceimpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;
	
	

	@Override
	public UserDto createUser(UserDto userDto) {
		User user=dtoToUser(userDto);
		
		User saveduser = userRepo.save(user);

		return userToDto(saveduser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User user= userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User ", "id", userId));
		
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setAbout(userDto.getAbout());
		user.setPassword(userDto.getPassword());
		
		User save = userRepo.save(user);
		
		UserDto userToDto = userToDto(save);
		return userToDto;
	}

	@Override
	public UserDto getUserById(Integer userId) {
		
		User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", " id", userId));
		
		return userToDto(user);
	}

	@Override
	public List<UserDto> getAllUser() {
		List<User> users = userRepo.findAll();
		
		List<UserDto> userDto = users.stream().map(user->userToDto(user)).collect(Collectors.toList());  
		
		return userDto;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User ", "id", userId));
		
		userRepo.delete(user);

	}

	public User dtoToUser(UserDto userDto) {

		User user = modelMapper.map(userDto, User.class);

		return user;

	}

	public UserDto userToDto(User user) {

		UserDto userDto = modelMapper.map(user, UserDto.class);

		
		
		return userDto;

	}

	@Override
	public UserDto registerNewUser(UserDto userDto) {
		
		User user = modelMapper.map(userDto, User.class);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Role role = roleRepo.findById(AppConstants.NORMAL_USER).get();
		
		user.getRoles().add(role);
		
		User newUser = userRepo.save(user);
		
		return modelMapper.map(newUser, UserDto.class);
	}

}
