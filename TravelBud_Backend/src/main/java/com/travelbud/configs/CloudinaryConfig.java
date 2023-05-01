package com.travelbud.configs;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.cloudinary.*;

@Configuration
public class CloudinaryConfig {
	
	private Cloudinary cloudinary;
	
	@Value("${cloudinary.name}")
	private String name;

	@Value("${cloudinary.key}")
	private String key;
	@Value("${cloudinary.secret}")
	private String secret;
	@Value("${cloudinary.secure}")
	private boolean secure;
	
	@PostConstruct
	private void init() {
		Map<String, Object> configMap = new HashMap<String, Object>();
		configMap.put("cloud_name", name);
		configMap.put("api_key" , key);
		configMap.put("api_secret" , secret);
		configMap.put("secure", secure);
		cloudinary = new Cloudinary(configMap);
	}
	
	@PreDestroy
	private void destroy() {
		
	}

	public Cloudinary getCloudinary() {
		return cloudinary;
	}

	public void setCloudinary(Cloudinary cloudinary) {
		this.cloudinary = cloudinary;
	}

	
}
