package com.travelbud.controllers;

import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.travelbud.configs.WSWebAuthenticationDetailsSource;
import com.travelbud.entities.Post;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;
import com.travelbud.services.UserService;

@RestController
@RequestMapping("/users")
public class UserControllerImpl implements UserController{
	@Autowired
	private UserService userService;

	@Override
	@PostMapping("")
	public ResponseEntity<Object> saveUser(@RequestBody User user, @RequestParam(required = false, name = "dpImage") MultipartFile dpImage) throws Exception {
		if(user == null)throw new NotReadablePropertyException(User.class, CommonErrorMessages.NULL_ERROR);
		return userService.saveUser(user, dpImage);
	}

	@GetMapping("/id/{userId}")
	@Override
	public User getUserById(@PathVariable long userId) {
		return userService.getUserById(userId);
	}

	@GetMapping("/{username}")
	@Override
	public User getUserByUsername(@PathVariable String username) {
		return userService.getUserByUsername(username);
	}

	@Override
	@GetMapping("")
	public List<User> getUsers() {
		return userService.getUsers();
	}

	@Override
	public User deleteUser(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@GetMapping("/match/{name}")
	@Override
	public List<User> getUserByName(@PathVariable String name) {
		return userService.getUserByName(name);
	}

	@GetMapping("/followers/{userId}")
	@Override
	public List<User> getFollowingUsers(@PathVariable long userId) {
		return userService.getFollowingUsers(userId);
	}

	@GetMapping("/trend")
	@Override
	public List<User> getTrendingUsers() {
		return userService.getTrendingUsers();
	}
	
	@GetMapping("/share/{userId}")
	@Override
	public String getUserLink(@PathVariable long userId) throws JsonMappingException, JsonProcessingException, UniformInterfaceException, ClientHandlerException, UnsupportedEncodingException {
		return userService.getUserLink(userId);
	}

	@PostMapping("/reset_password")
	@Override
	public boolean validateVerificationCode(@RequestParam("code") String code, @RequestParam("email")  String email) {
		/*
		 * if(user == null)throw new NotReadablePropertyException(User.class,
		 * CommonErrorMessages.NULL_ERROR); return
		 * userService.validateVerificationCode(code, user);
		 */
		return false;
	}

	@PutMapping("")
	@Override
	public User updateUser(User user, @RequestParam(required = false, name = "dpImage") MultipartFile dpImage) throws Exception {
		if(user == null)throw new NotReadablePropertyException(User.class, CommonErrorMessages.NULL_ERROR);
		checkUserAccessibility(user);
		return updateUser(user, dpImage);
	}
	
	@GetMapping("/authenticated")
	@Override
	public User getAuthenticatedUser() throws AccessDeniedException {
		return userService.getAuthenticatedUser();
	}
	
	private void checkUserAccessibility(User user) throws AccessDeniedException, NotReadablePropertyException {
		WSWebAuthenticationDetailsSource details = (WSWebAuthenticationDetailsSource) SecurityContextHolder.getContext()
				.getAuthentication().getDetails();
		User currentUser = details.getAuthenticatedUser();
		if (user == null) {
			throw new NotReadablePropertyException(Post.class, CommonErrorMessages.NULL_ERROR);
		} else if (user.getId() == null || user.getId() != currentUser.getId()) {
			throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
		}
	}

	@Override
	public boolean sendVerificationCode(String email) throws UnsupportedEncodingException, MessagingException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void verifyAndSendCode(String email) throws UnsupportedEncodingException, MessagingException {
		// TODO Auto-generated method stub
		
	}
	
	@PostMapping("/update")
	public User updateUser(@RequestPart String user, @RequestPart(required = false, name = "dpImage") MultipartFile dpImage) throws Exception {
		ObjectMapper map = new ObjectMapper();
		User u = map.readValue(user, User.class);
		if(u == null)throw new NotReadablePropertyException(User.class, CommonErrorMessages.NULL_ERROR);
		checkUserAccessibility(u);
		return userService.updateUser(u, dpImage);
	}

	@Override
	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean doesUsernameExists(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doesEmailExists(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean resetPassword(String code, String email, String password) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
}
