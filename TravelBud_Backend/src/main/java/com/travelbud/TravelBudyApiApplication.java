package com.travelbud;

import java.io.UnsupportedEncodingException;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.travelbud.dao.UserDao;
import com.travelbud.dto.Gender;
import com.travelbud.entities.User;
import com.travelbud.utils.OneSignalUtil;

@EnableConfigurationProperties
@EnableTransactionManagement
@EnableAspectJAutoProxy
@SpringBootApplication
public class TravelBudyApiApplication {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private OneSignalUtil oneSignalUtil;
	
	@PostConstruct
	public void fillUser() throws UnsupportedEncodingException, MessagingException, JsonMappingException, JsonProcessingException, UniformInterfaceException, ClientHandlerException {
		/*User user = new User();
		user.setId(1L);
		user.setUsername("owaiz47");
		user.setPassword("7095851859");
		user.setEmail("owaiz47@gmail.com");
		user.setFullname("Mohammed Owaiz");
		user.setGender(Gender.MALE);
		user.setDp("default");
		user.setProfileURL("profileURL");
		user = userDao.save(user);*/
		
		/*Message message = new Message();
		message.setMessageFrom(user);
		message.setMessageTo(new User());
		message.setMessage("Hello");
		message.setCreatedOn(new Date());
		
		ObjectMapper map = new ObjectMapper();
		String js = map.writeValueAsString(message);
		
		
		
		OneSignal on = new OneSignal();
		on.getData().setData(js);
		on.getData().setType("message");
		OneSignalHeadNContent onConte = new OneSignalHeadNContent();
		onConte.setEn("Heres the content");
		on.setContents(onConte);
		onConte.setEn("Heres the heading");
		on.setHeadings(onConte);
		on.getInclude_external_user_ids().add("9");
		oneSignalUtil.sendNotificaton(on, true);*/
	}

	public static void main(String[] args) {
		SpringApplication.run(TravelBudyApiApplication.class, args);
	}

}
