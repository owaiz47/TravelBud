package com.travelbud.apis;

import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.travelbud.entities.Post;

public interface PostAPI {
	public Post savePost(Post post, MultipartFile coverImage, List<MultipartFile> files) throws Exception;
	public List<Post> getPosts(int page);//method to get Posts and also posts after id, consider first requset when id is null
	public List<Post> getPostsOfUser(long userId, int page);//same as above
	public Post getPost(long id);
	public List<Post> getRandom();
	public boolean deletePost(long id) throws AccessDeniedException, NotReadablePropertyException;
	public String getPostUrl(long id) throws JsonMappingException, JsonProcessingException, UniformInterfaceException, ClientHandlerException, UnsupportedEncodingException;
	public List<Post> getPostForPlan(long planId);
}
