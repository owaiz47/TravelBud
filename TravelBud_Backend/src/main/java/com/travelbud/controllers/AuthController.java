package com.travelbud.controllers;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelbud.dto.AuthRequest;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;
import com.travelbud.services.UserService;
import com.travelbud.utils.JwtUtil;

@RestController("/")
public class AuthController {
	
	private Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@PostMapping("/authenticate")
	public ResponseEntity<Object> generateToken(@RequestBody AuthRequest authRequest, HttpServletRequest request) throws Exception {
		log.info("Authenticate request incoming.., from username" + authRequest.getUsername());
		Map<String, String> res = new HashMap<String, String>();
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
			log.info("User authenticated.." + authRequest.getUsername());
		} catch (Exception ex) {
			log.error("Error while authenticating, username "+ authRequest.getUsername() + "Error "+ ex.getMessage());
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}
		log.info("Authetication succesfull sending back response");
		res.put("token", jwtUtil.generateToken(authRequest.getUsername()));
		log.info("Authetication succesfull sending back response");
		return new ResponseEntity<Object>(res, HttpStatus.OK);
	}
	
	@GetMapping("/verify/{email}")
	public boolean sendVerificationCode(@PathVariable String email) throws UnsupportedEncodingException, MessagingException {
		log.info("request to send verification code to email "+ email);
		return userService.sendVerificationCode(email);
	}

	//@PostMapping("/register")
	public ResponseEntity<Object> saveUser(@RequestParam("code") String code, @RequestBody User user,
			@RequestParam(required = false, name = "dpImage") MultipartFile dpImage) throws Exception {
		log.info("Request to register a new user, user with username "+ user.getUsername());
		if (user == null)
			throw new NotReadablePropertyException(User.class, CommonErrorMessages.NULL_ERROR);
		userService.validateVerificationCode(code, user.getEmail());
		ResponseEntity<Object> response = userService.saveUser(user, dpImage);
		if (response.getStatusCode() == HttpStatus.OK) {
			log.info("New user save with username "+ user.getUsername());
			Map<String, String> res = new HashMap<String, String>();
			try {
				authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
				log.info("User authenticated after registration, username "+ user.getUsername());
			} catch (Exception e) {
				log.error("Registration failed, username "+ user.getUsername() + " Error " + e.getMessage());
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}
			res.put("token", jwtUtil.generateToken(user.getUsername()));
			log.info("User registration successfull, username "+ user.getUsername() + " sending back response");
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		}
		throw new Exception(CommonErrorMessages.COMMON_ERROR);
	}
	
	@GetMapping("/forgot/{email}")
	public void verifyAndSendCode(@PathVariable String email) throws UnsupportedEncodingException, MessagingException {
		log.info("verification code request for forgot password, email "+ email);
		userService.verifyAndSendCode(email);
	}
	
	@PostMapping("/reset_password")
	public boolean resetPassword(@RequestPart("code") String code, @RequestPart("email") String email, @RequestPart("password") String password) throws Exception {
		log.info("Request to reset password, email "+ email);
		return userService.resetPassword(code, email, password);
	}

	@GetMapping("/authenticated_user")
	public User getAuthenticatedUser() throws Exception {
		log.info("Requesting authenticated user details..");
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		}
		if (username == null) {
			log.info("Username is null, Authenticated user not found");
			return null;
		}
		User user = userService.getUserByUsername(username);
		log.info("Sending authenticated user details");
		return user;
	}
	
	@GetMapping("/vu/{username}")
	public Map<String, Boolean> validateUsername(@PathVariable String username) {
		Map<String, Boolean> map = new HashMap<>();
		boolean exist = userService.doesUsernameExists(username);
		map.put("taken", exist);
		return map;
	}
	
	@GetMapping("/ve/{email}")
	public Map<String, Boolean> validateEmail(@PathVariable String email) {
		Map<String, Boolean> map = new HashMap<>();
		boolean exist = userService.doesEmailExists(email);
		map.put("taken", exist);
		return map;
	}
	
	@PostMapping("/register")
	public ResponseEntity<Object> registerUser(@RequestPart("code") String code,@RequestPart("user") String us, @RequestPart(required = false, name = "dpImage") MultipartFile dpImage) throws Exception {
		if (us == null || us.isEmpty())
			throw new NotReadablePropertyException(User.class, CommonErrorMessages.NULL_ERROR);
		ObjectMapper map = new ObjectMapper();
		User user = map.readValue(us, User.class);
		log.info("Request to register a new user, user with username "+ user.getUsername());
		userService.validateVerificationCode(code, user.getEmail());
		ResponseEntity<Object> response = userService.saveUser(user, dpImage);
		if (response.getStatusCode() == HttpStatus.OK) {
			log.info("New user save with username "+ user.getUsername());
			Map<String, String> res = new HashMap<String, String>();
			try {
				authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
				log.info("User authenticated after registration, username "+ user.getUsername());
			} catch (Exception e) {
				log.error("Registration failed, username "+ user.getUsername() + " Error " + e.getMessage());
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}
			res.put("token", jwtUtil.generateToken(user.getUsername()));
			log.info("User registration successfull, username "+ user.getUsername() + " sending back response");
			return new ResponseEntity<Object>(res, HttpStatus.OK);
		}
		throw new Exception(CommonErrorMessages.COMMON_ERROR);
	}
}
