package com.travelbud.dto;

import java.util.ArrayList;
import java.util.List;

import com.travelbud.entities.Notification;

public class OneSignal {
	private String app_id;
	private List<String> included_segments;
	private List<String> include_external_user_ids;
	private String channel_for_external_user_ids;
	private String android_channel_id;
	private String huawei_channel_id;
	private OneSignalHeadNContent headings;
	private OneSignalData data;
	private String big_picture;
	private OneSignalHeadNContent contents;
	
	public OneSignal(Long user_id, String heading, String content, OneSignalData data, String picture) {
		List<String> externalUserIds = new ArrayList<String>();
		externalUserIds.add(user_id.toString());
		this.include_external_user_ids = externalUserIds;
		this.headings = new OneSignalHeadNContent(heading == null ? "travelbud" : heading);
		this.contents = new OneSignalHeadNContent(content == null ? "New Notification" : content);
		this.data = data;
		this.big_picture = picture;
		
	}
	
	public static OneSignal from(Notification notification, String data, String itemType) {
		OneSignalData oneSignalData = new OneSignalData();
		oneSignalData.setData(data);
		oneSignalData.setType(itemType);
		return new OneSignal(notification.getNotificationTo().getId(), notification.getHeading(), notification.getNotificationFrom().getFullname() + " " + notification.getMessage(), oneSignalData, notification.getPicture());
	}
	
	public OneSignal() {
		this.include_external_user_ids = new ArrayList<String>();
		this.included_segments = new ArrayList<String>();
	}
	
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public List<String> getIncluded_segments() {
		return included_segments;
	}
	public void setIncluded_segments(List<String> included_segments) {
		this.included_segments = included_segments;
	}
	public List<String> getInclude_external_user_ids() {
		return include_external_user_ids;
	}
	public void setInclude_external_user_ids(List<String> include_external_user_ids) {
		this.include_external_user_ids = include_external_user_ids;
	}
	public String getChannel_for_external_user_ids() {
		return channel_for_external_user_ids;
	}
	public void setChannel_for_external_user_ids(String channel_for_external_user_ids) {
		this.channel_for_external_user_ids = channel_for_external_user_ids;
	}
	public String getAndroid_channel_id() {
		return android_channel_id;
	}
	public void setAndroid_channel_id(String android_channel_id) {
		this.android_channel_id = android_channel_id;
	}
	public String getHuawei_channel_id() {
		return huawei_channel_id;
	}
	public void setHuawei_channel_id(String huawei_channel_id) {
		this.huawei_channel_id = huawei_channel_id;
	}
	public OneSignalHeadNContent getHeadings() {
		return headings;
	}
	public void setHeadings(OneSignalHeadNContent headings) {
		this.headings = headings;
	}
	public OneSignalData getData() {
		return data;
	}
	public void setData(OneSignalData data) {
		this.data = data;
	}
	public OneSignalHeadNContent getContents() {
		return contents;
	}
	public void setContents(OneSignalHeadNContent contents) {
		this.contents = contents;
	}

	public String getBig_picture() {
		return big_picture;
	}

	public void setBig_picture(String big_picture) {
		this.big_picture = big_picture;
	}
	
}
