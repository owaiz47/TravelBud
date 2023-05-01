package com.travelbud.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.travelbud.configs.CloudinaryConfig;
@Service
public class UploadUtil {

	@Autowired
	private CloudinaryConfig cloudinaryConfig;
	
	public String uploadImage(MultipartFile file, String folder) throws IOException {
		Map<String, Object> uploadMap = new HashMap<String, Object>();
		uploadMap.put("overwrite", true);
		uploadMap.put("resourct_type", "image");
		uploadMap.put("folder", folder);
		String tag = generateRandom().toString();
		uploadMap.put("public_id", tag);
		cloudinaryConfig.getCloudinary().uploader().upload(file.getBytes(), uploadMap);
		return tag;	
	}
	
	private Long generateRandom() {
		Random generator = new Random(System.currentTimeMillis());
		return generator.nextLong() % 1000000000000L;
	}
}
