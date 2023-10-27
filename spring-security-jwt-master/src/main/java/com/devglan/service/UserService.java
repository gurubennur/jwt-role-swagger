package com.devglan.service;

import java.util.List;

import com.devglan.model.UpdateUserDto;
import com.devglan.model.User;
import com.devglan.model.UserDto;

public interface UserService {

	User save(UserDto user);

	List<User> findAll();

	String delete(long id);

	User findOne(String username);

	User findById(Long id);

	User updateUser(UpdateUserDto updateUser, Long id);
}
