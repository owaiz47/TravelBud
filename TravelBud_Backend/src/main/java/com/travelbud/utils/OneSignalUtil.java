package com.travelbud.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.travelbud.configs.OneSignalConfig;
import com.travelbud.dto.OneSignal;

@Service
public class OneSignalUtil {

	@Autowired
	private OneSignalConfig oneSignalConfig;
	
	public void sendNotificaton(OneSignal oneSignal, boolean important) throws JsonProcessingException {
		oneSignal.setApp_id(oneSignalConfig.getApp_id());
		oneSignal.setChannel_for_external_user_ids(oneSignalConfig.getChannel_for_external_user_ids());
		if(important) {
			oneSignal.setAndroid_channel_id(oneSignalConfig.getChannel());
			oneSignal.setHuawei_channel_id(oneSignalConfig.getChannel());
		}
		
		Client client = Client.create();

        WebResource webResource = client
           .resource(oneSignalConfig.getUrl());
        
        ObjectMapper map = new ObjectMapper();
        String json = map.writeValueAsString(oneSignal);

        ClientResponse response = webResource.header("Content-Type", "application/json").header("Authorization", oneSignalConfig.getAuth())
           .post(ClientResponse.class, json);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
	}
}
