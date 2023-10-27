package com.devglan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devglan.error.ApiErrorResponse;
import com.devglan.model.UpdateUserDto;
import com.devglan.model.User;
import com.devglan.model.UserDto;
import com.devglan.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "CRUD Rest APIs for Post resources")
public class UserController {

	@Autowired
	private UserService userService;

	@ApiOperation(value = "geAlltCustomer", nickname = "getAllCustomer")
	@ApiResponses(value = { @ApiResponse(code = 500, response = ApiErrorResponse.class, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = User.class, responseContainer = "List") })
	// @Secured({"ROLE_ADMIN", "ROLE_USER"})
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/api/customers", method = RequestMethod.GET)
	public List<User> listUser() {
		return userService.findAll();
	}

	@ApiOperation(value = "getCustomer", nickname = "getCustomer")
	@ApiResponses(value = { @ApiResponse(code = 500, response = ApiErrorResponse.class, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful retrieval", response = User.class, responseContainer = "List") })
	// @Secured("ROLE_USER")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	//// @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@RequestMapping(value = "/api/customer/{id}", method = RequestMethod.GET)
	public User getOne(@PathVariable(value = "id") Long id) {
		return userService.findById(id);
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@RequestMapping(value = "/api/customer/{id}", method = RequestMethod.PUT)
	public User updateUser(@PathVariable(value = "id") Long id, @RequestBody UpdateUserDto user) {
		return userService.updateUser(user, id);
	}

	@ApiOperation(value = "deleteCustomer", nickname = "deleteCustomer")
	@ApiResponses(value = { @ApiResponse(code = 500, response = ApiErrorResponse.class, message = "Server error"),
			@ApiResponse(code = 200, message = "Successful delete", response = String.class, responseContainer = "List") })
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/api/customer/{id}", method = RequestMethod.DELETE)
	public String deleteUser(@PathVariable(value = "id") Long id) {
		return userService.delete(id);
	}

	@ApiOperation(value = "Create Customer")
	@ApiResponses(value = { @ApiResponse(code = 201, response = User.class, message = "Customer Response"),
			@ApiResponse(code = 404, response = ApiErrorResponse.class, message = "Not Found"),
			@ApiResponse(code = 422, response = ApiErrorResponse.class, message = "Error processing request, check the error code in the response for more detail") })

	@PostMapping("/api/v1/posts")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/api/customer", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public User createUser(@RequestBody UserDto user) {
		return userService.save(user);
	}

}
