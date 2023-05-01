package com.travelbud.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("notify")
public class OneSignalConfig {

	private String app_id;
	private String channel_for_external_user_ids;
	private String channel;
	private String url;
	private String auth;
	
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getChannel_for_external_user_ids() {
		return channel_for_external_user_ids;
	}
	public void setChannel_for_external_user_ids(String channel_for_external_user_ids) {
		this.channel_for_external_user_ids = channel_for_external_user_ids;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
}
