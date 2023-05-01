package com.travelbud.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("firebase")
public class FirebaseConfig {
	private String link;
	private String serviceUrl;
	private String uriPrefix;
	private String apppackage;
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getServiceUrl() {
		return serviceUrl;
	}
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	public String getUriPrefix() {
		return uriPrefix;
	}
	public void setUriPrefix(String uriPrefix) {
		this.uriPrefix = uriPrefix;
	}
	public String getApppackage() {
		return apppackage;
	}
	public void setApppackage(String apppackage) {
		this.apppackage = apppackage;
	}
	
}
