package com.travelbud.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.travelbud.configs.FirebaseConfig;
import com.travelbud.dto.ItemType;

@Service
public class FirebaseUtil {

	@Autowired
	private FirebaseConfig firebaseConfig;
	
	public String createDynamicLink(long id, ItemType itemType) throws UniformInterfaceException, ClientHandlerException, UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
		String link = firebaseConfig.getLink();
		if(itemType == ItemType.USER) {
			link = link + "?user=" + id; 
		}else if(itemType == ItemType.POST) {
			link = link + "?post=" + id; 
		}else if(itemType == ItemType.PLAN) {
			link = link + "?plan=" + id; 
		}
		
		Client client = Client.create();

        WebResource webResource = client
           .resource(firebaseConfig.getServiceUrl());

        ClientResponse response = webResource.header("Content-Type", "application/json").header("Accept", "application/json")
           .post(ClientResponse.class, generateJson(link));

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = response.getEntity(String.class);
        Map<String, String> obj = mapper.readValue(json , Map .class);
        return obj.get("shortLink");
	}
	
	private String generateJson(String link) throws UnsupportedEncodingException {
		String json = "{\n"
				+ "  \"dynamicLinkInfo\": {\n"
				+ "    \"domainUriPrefix\": \"" + firebaseConfig.getUriPrefix() + "\",\n"
				+ "    \"link\": \""+ link + "\",\n"
				+ "    \"androidInfo\": {\n"
				+ "      \"androidPackageName\": \"" + firebaseConfig.getApppackage() + "\"\n"
				+ "    },\n"
				+ "    \"iosInfo\": {\n"
				+ "      \"iosBundleId\": \"" + firebaseConfig.getApppackage() + "\"\n"
				+ "    }\n"
				+ "  }\n"
				+ "}";
		return json;
	}
}
