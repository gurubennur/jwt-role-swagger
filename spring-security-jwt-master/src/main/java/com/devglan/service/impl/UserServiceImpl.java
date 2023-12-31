package com.devglan.service.impl;

import com.devglan.Exception.InvalidRequestException;
import com.devglan.Exception.ResourceNotFoundException;
import com.devglan.Exception.ServiceUnavailableException;
import com.devglan.dao.UserDao;
import com.devglan.model.Role;
import com.devglan.model.RoleDto;
import com.devglan.model.UpdateUserDto;
import com.devglan.model.User;
import com.devglan.model.UserDto;
import com.devglan.service.UserService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private ModelMapper modelmapper;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				getAuthority(user));
	}

	private Set<SimpleGrantedAuthority> getAuthority(User user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		user.getRoles().forEach(role -> {
			// authorities.add(new SimpleGrantedAuthority(role.getName()));
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		});
		return authorities;
		// return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	public List<User> findAll() {
		List<User> list = new ArrayList<>();
		userDao.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public String delete(long id) {
		try {
			Optional<User> user = userDao.findById(id);
			if (user.isPresent()) {
				userDao.deleteById(id);
				return id + "deleted Successfully";
			} else {
				throw new ResourceNotFoundException("User not found for given id:" + id);
			}
		} catch (Exception ex) {
			throw new ServiceUnavailableException("Delete Operation is not working");
		}

	}

	@Override
	public User findOne(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	public User findById(Long id) {
		return userDao.findById(id).get();
	}

	@Override
	public User save(UserDto user) {
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setEmail(user.getEmail());
		newUser.setPhone(user.getPhone());

		Set<Role> roles = new HashSet<>();
		for (RoleDto roledto : user.getRoles()) {
			Role role = modelmapper.map(roledto, Role.class);
			roles.add(role);

		}

		newUser.setRoles(roles);

		return userDao.save(newUser);
	}

	@Override
	public User updateUser(UpdateUserDto updateUser, Long id) {
		// TODO Auto-generated method stub
		Optional<User> user = userDao.findById(id);
		if (!user.isPresent()) {
			throw new InvalidRequestException("Invalid UserId: " + id);
		} else {
			User userobj = user.get();

			if (updateUser.getEmail() != null) {
				userobj.setEmail(updateUser.getEmail());
			}

			if (updateUser.getPassword() != null) {
				userobj.setPassword(bcryptEncoder.encode(updateUser.getPassword()));
			}

			if (updateUser.getPhone() != null) {
				userobj.setPhone(updateUser.getPhone());
			}

			if (updateUser.getUsername() != null) {
				userobj.setUsername(updateUser.getUsername());
			}

			return userDao.save(userobj);

		}
	}
}
