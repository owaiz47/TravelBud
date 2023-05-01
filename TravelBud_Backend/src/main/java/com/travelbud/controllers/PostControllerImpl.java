package com.travelbud.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.travelbud.configs.CloudinaryConfig;
import com.travelbud.configs.WSWebAuthenticationDetailsSource;
import com.travelbud.entities.Post;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;
import com.travelbud.services.PostService;
import com.travelbud.services.UserService;

@RestController
@RequestMapping("/posts")
public class PostControllerImpl implements PostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private CloudinaryConfig cloudinaryConfig;

	@Override
	public Post savePost(@RequestBody Post post, MultipartFile coverImage, @RequestParam("files") List<MultipartFile> images) throws Exception {
		//return postService.savePost(post, images);
		return null;
	}

	@GetMapping("/page/{page}")
	@Override
	public List<Post> getPosts(@PathVariable int page) {
		return postService.getPosts(page);

	}

	@GetMapping("/user/{userId}/page/{page}")
	@Override
	public List<Post> getPostsOfUser(@PathVariable long userId, @PathVariable int page) {
		return postService.getPostsOfUser(userId, page);
	}

	@GetMapping("/{id}")
	@Override
	public Post getPost(@PathVariable long id) {
		return postService.getPost(id);
	}

	@GetMapping("/trend")
	@Override
	public List<Post> getRandom() {
		return postService.getRandom();
	}

	@DeleteMapping("/{id}")
	@Override
	public boolean deletePost(@PathVariable long id) throws AccessDeniedException, NotReadablePropertyException {
		return postService.deletePost(id);
	}

	@GetMapping("/share/{id}")
	@Override
	public String getPostUrl(@PathVariable long id) throws JsonMappingException, JsonProcessingException, UniformInterfaceException, ClientHandlerException, UnsupportedEncodingException {
		return postService.getPostUrl(id);
	}
	
	@GetMapping("/plan/{planId}")
	@Override
	public List<Post> getPostForPlan(@PathVariable long planId) {
		return postService.getPostForPlan(planId);
	}
	
	@PostMapping("/upload")
	public boolean upload(@RequestParam("images") List<MultipartFile> images) throws IOException {
		Map<String, Object> uploadMap = new HashMap<String, Object>();
		uploadMap.put("folder", "dp/");
		uploadMap.put("overwrite", true);
		uploadMap.put("resourct_type", "image");
		for(MultipartFile m : images) {
			uploadMap.put("public_id",  "image" + images.indexOf(m));
			Object ret = cloudinaryConfig.getCloudinary().uploader().upload(m.getBytes(), uploadMap);
			System.out.println(ret.toString());
		}
		return false;
	}
	
	@PostMapping(value = "/save", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public Post savePostt(@RequestPart("cover") MultipartFile coverImage, @RequestPart("files") List<MultipartFile> images, @RequestPart("post") String post) throws Exception {
		ObjectMapper map = new ObjectMapper();
		Post ps = map.readValue(post, Post.class);
		return postService.savePost(ps, coverImage, images);
	}

}
