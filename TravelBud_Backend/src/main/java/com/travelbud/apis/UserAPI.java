package com.travelbud.apis;

import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.travelbud.entities.User;

import javassist.bytecode.DuplicateMemberException;

public interface UserAPI {
	public ResponseEntity<Object> saveUser(User user, MultipartFile dpImage) throws Exception;
	public User getUserById(long userId);
	public User getUserByUsername(String username);
	public User getUserByEmail(String email);
	public List<User> getUsers();
	public User deleteUser(long id);
	//public List<User> saveUsers(List<User> users);
	public List<User> getUserByName(String name);
	public List<User> getFollowingUsers(long userId);
	public List<User> getTrendingUsers();
	public String getUserLink(long userId) throws JsonMappingException, JsonProcessingException, UniformInterfaceException, ClientHandlerException, UnsupportedEncodingException;
	public User updateUser(User user, MultipartFile dpImage) throws Exception;
	public boolean sendVerificationCode(String email) throws UnsupportedEncodingException, MessagingException;
	public void verifyAndSendCode(String email) throws UnsupportedEncodingException, MessagingException;
	public boolean validateVerificationCode(String code, String email) throws Exception;
	public User getAuthenticatedUser() throws AccessDeniedException;
	public boolean doesUsernameExists(String username);
	public boolean doesEmailExists(String email);
	public boolean resetPassword(String code, String email, String password) throws Exception;
}
