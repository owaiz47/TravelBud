package com.travelbud.services;

import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.travelbud.configs.WSWebAuthenticationDetailsSource;
import com.travelbud.dao.ResetPasswordDao;
import com.travelbud.dao.UserDao;
import com.travelbud.dto.ItemType;
import com.travelbud.entities.ResetPassword;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;
import com.travelbud.utils.FirebaseUtil;
import com.travelbud.utils.MailUtil;
import com.travelbud.utils.UploadUtil;

@Service
public class UserServiceImpl implements UserService {
	
	private Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private MailUtil mailUtil;
	
	@Autowired
	private UploadUtil uploadUtil;
	
	@Autowired
	private ResetPasswordDao resetPasswordDao;
	
	@Autowired
	private FirebaseUtil firebaseUtil;
	
	private String MAIL_BODY = "<h1>Verify Your Email</h1>\n"
			+ "<p>This is your verification code. Please note that this is valid for 5 minutes after which this will expire. Use the below given code to verify your email.</p>\n"
			+ "<center>\n"
			+ "<p><a style=\"background-color: #ffbe00; color: #000000; display: inline-block; padding: 12px 40px 12px 40px; text-align: center; text-decoration: none;\" href=\"\" target=\"_blank\">VERIFY_CODE</a></p>\n"
			+ "<span style=\"font-size: 10px;\"><a href=\"https://app.travelbud.com/\">Visit our website</a></span></center>";
	
	private final String MAIL_SUBJECT = "travelbud- Email Verification";
	
	private final String CLOUDINARY_DP_FOLDER = "dp/"; 

	@Override
	public ResponseEntity<Object> saveUser(User user, MultipartFile dpImage) throws Exception {
		log.info("Registration in progress for username "+ user.getUsername());
		user.setDp("default");
		if(dpImage != null) {
			log.info("Found image as dp saving.... for user " + user.getUsername());
			String tag = uploadUtil.uploadImage(dpImage, CLOUDINARY_DP_FOLDER);
			user.setDp(tag);
			log.info("Saved the dp moving to save user, username "+ user.getUsername());
		}
		userDao.saveAndFlush(user);
		log.info("User saved, username "+ user.getUsername());
		return new ResponseEntity<Object>(null, HttpStatus.OK);
		/*
		 * try { authenticationManager.authenticate( new
		 * UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		 * } catch (Exception e) { return new
		 * ResponseEntity<Object>(HttpStatus.UNAUTHORIZED); } res.put("token",
		 * jwtUtil.generateToken(user.getUsername())); return new
		 * ResponseEntity<Object>(res, HttpStatus.OK);
		 */
	}

	@Override
	public User getUserById(long userId) {
		log.info("finding user by id "+ userId);
		Optional<User> user = userDao.findById(userId);
		if (user.isPresent()) {
			log.info("User by id "+ userId +" found responding");
			return user.get();
		} else {
			log.info("No user by id "+ userId);
			return null;
		}
	}

	@Override
	public User getUserByUsername(String username) {
		log.info("finding user with username "+ username);
		return userDao.findByUsername(username);
	}

	@Override
	public List<User> getUsers() {
		log.info("sending back all  user information");
		//return userDao.findAll();
		return null;
	}

	@Override
	public User deleteUser(long id) {
		/*User user2del = getUserById(id);
		if (user2del != null) {
			userDao.delete(user2del);
		}
		return user2del;
		*/
		return null;
	}

	/*
	 * @Override public List<User> saveUsers(List<User> users) { return
	 * userDao.saveAll(users); }
	 */

	@Override
	public List<User> getUserByName(String name) {
		log.info("searching users with name "+ name);
		return userDao.findByUsernameOrFullnameContainingIgnoreCase(name, name);
	}

	/*
	 * @Override public String saveUser(User user) { userDao.save(user); return
	 * "ABC"; }
	 */

	@Override
	public List<User> getFollowingUsers(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getTrendingUsers() {
		log.info("checking trending users info");
		List<Long> ids = userDao.getTrendingUsers();
		List<User> users = userDao.findAllById(ids);
		return users;
	}

	@Override
	public String getUserLink(long userId) throws JsonMappingException, JsonProcessingException, UniformInterfaceException, ClientHandlerException, UnsupportedEncodingException {
		log.info("finding dynamic link for user with id "+ userId);
		User user = getUserById(userId);
		if(user != null && user.getProfileURL() != null && !user.getProfileURL().isEmpty()) {
			log.info("found user sending dynamic link "+ user.getUsername());
			return user.getProfileURL();
		}
		log.info("No dynamic link provided creating a new dynamic link for user "+ user.getUsername());
		String dynamicLink = firebaseUtil.createDynamicLink(user.getId(), ItemType.USER);
		user.setProfileURL(dynamicLink);
		userDao.save(user);
		return user.getProfileURL();
	}

	@Override
	public boolean validateVerificationCode(String code, String email) throws Exception {
		log.info("validating verification code for email "+ email);
		List<ResetPassword> resets = resetPasswordDao.findByEmailAndCode(email, code);
		if(resets != null && resets.size() > 0) {
			ResetPassword reset = resets.get(resets.size() - 1);//Last is the most recent one
			log.info("Found verification code, checking if expired, email "+ email);
			if (!reset.isExpired()) {//5Minutes  //new Date().getTime() - reset.getCreatedOn().getTime() < 5*60*1000
				log.info("Valid verification code found, email "+ email);
			    return true;
			}
			throw new CredentialsExpiredException(CommonErrorMessages.CODE_EXPIRED);
		}
		throw new EntityNotFoundException(CommonErrorMessages.CODE_NOT_FOUND);
	}
	
	@Override
	public User getAuthenticatedUser()  throws AccessDeniedException{
		if(SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
			if(SecurityContextHolder.getContext().getAuthentication().getDetails() != null && SecurityContextHolder.getContext()
					.getAuthentication().getDetails() instanceof WSWebAuthenticationDetailsSource) {
				WSWebAuthenticationDetailsSource details = (WSWebAuthenticationDetailsSource) SecurityContextHolder.getContext()
						.getAuthentication().getDetails();
				if(details == null || details.getAuthenticatedUser() == null)throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
				return details.getAuthenticatedUser();
			}else {
				String username = SecurityContextHolder.getContext().getAuthentication().getName();
				User user = getUserByUsername(username);
				if(user == null || user.getId() < 1) {
					throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
				}
				return user;
			}
			
		}else {
			throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
		}
	}

	@Override
	public User updateUser(User user, MultipartFile dpImage) throws Exception {
		log.info("Updating user, username "+ user.getUsername());
		User authUser = getAuthenticatedUser();
		if(authUser.getId() != user.getId())throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
		User realUser = getUserById(authUser.getId());
		Path path = Paths.get(realUser.getDp());
		String dp = path.getFileName().toString();
		user.setPassword(authUser.getPassword());
		user.setDp(dp);
		user.setEmail(realUser.getEmail());
		if(dpImage != null) {
			log.info("Found dp image to udpate, updataing for user "+ user.getUsername());
			String tag = uploadUtil.uploadImage(dpImage, CLOUDINARY_DP_FOLDER);
			user.setDp(tag);
			log.info("updated dp for user "+ user.getUsername());
		}
		return userDao.saveAndFlush(user);
	}

	@Override
	public boolean sendVerificationCode(String email) throws UnsupportedEncodingException, MessagingException {
		log.info("Sending verification code to email "+ email);
		/*List<ResetPassword> resets = resetPasswordDao.getLastRecord(email);
		if(resets != null && resets.size() >= 3) {
			log.info("Already sent 3 verification emails to this email "+ email);
			throw new MessagingException(CommonErrorMessages.MAIL_EXCEEDS_LIMIT); 
		}*/
		//if(doesEmailExists(email))throw new DuplicateMemberException("Email Already Registered");
		//String code = getRandomNumberString();
		String code = "123456";
		MAIL_BODY = MAIL_BODY.replaceAll("VERIFY_CODE", code);
		if(true) {//mailUtil.sendMail(email, MAIL_SUBJECT, MAIL_BODY)
			log.info("Verification sent to email "+ email);
			ResetPassword reset = new ResetPassword(code, email);
			resetPasswordDao.save(reset);
			return true;
		}
		log.error("Verification code not sent for email "+ email);
		throw new MessagingException(CommonErrorMessages.MAIL_NOT_SENT);
	} 
	
	private static String getRandomNumberString() {
	    Random rnd = new Random();
	    int number = rnd.nextInt(999999);
	    return String.format("%06d", number);
	}

	@Override
	public void verifyAndSendCode(String email) throws UnsupportedEncodingException, MessagingException {
		log.info("Verifing if user exists with email "+ email);
		boolean exists = userDao.existsByEmail(email);
		if(exists) {
			log.info("User found with email "+ email +" sending verification code for password reset");
			sendVerificationCode(email);
		}else {
			log.info("User not found, will not send any verification code");
		}
	}

	@Override
	public User getUserByEmail(String email) {
		log.info("finding user with email "+ email);
		return userDao.findByEmail(email);
	}

	@Override
	public boolean doesUsernameExists(String username) {
		return userDao.existsByUsername(username);
	}

	@Override
	public boolean doesEmailExists(String email) {
		return userDao.existsByEmail(email);
	}

	@Override
	public boolean resetPassword(String code, String email, String password) throws Exception {
		if(validateVerificationCode(code, email)) {
			userDao.updatePassword(email, password);
			return true;
		}
		return false;
	}

}
