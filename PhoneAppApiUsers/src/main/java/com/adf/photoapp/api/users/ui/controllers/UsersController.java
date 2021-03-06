package com.adf.photoapp.api.users.ui.controllers;

import javax.validation.Valid;
import javax.ws.rs.core.MediaType;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adf.photoapp.api.users.service.UsersService;
import com.adf.photoapp.api.users.shared.UserDto;
import com.adf.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.adf.photoapp.api.users.ui.model.CreateUserResponseModel;

@RestController()
@RequestMapping("/users")
public class UsersController {
	@Autowired()
	UsersService usersService;
	
	@Autowired()
	private Environment env;
	@GetMapping("/status/check")
	public String status()
	{
		return "Working on port "+env.getProperty("local.server.port")+" with token = "+env.getProperty("token.secret");
	}
	
	@PostMapping(consumes= {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON},
			produces= {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails)
	{
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		UserDto createdUser = usersService.createUser(userDto);
		
		CreateUserResponseModel createdUserResponseModel = modelMapper.map(createdUser, CreateUserResponseModel.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUserResponseModel);
	}
}
