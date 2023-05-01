package com.travelbud.services;

import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.travelbud.dao.PostDao;
import com.travelbud.dto.ItemType;
import com.travelbud.entities.Post;
import com.travelbud.entities.PostImage;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;
import com.travelbud.utils.FirebaseUtil;
import com.travelbud.utils.UploadUtil;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostDao postDao;

	@Autowired
	private UserService userService;

	@Autowired
	private UploadUtil uploadUtil;

	@Autowired
	private FirebaseUtil firebaseUtil;

	private final String CLOUDINARY_COVER_FOLDER = "post/";
	private final String CLOUDINARY_POST_IMAGES_FOLDER = "post_image/";

	private final int MAX_POST_LENGTH = 20;

	@Override
	public Post savePost(Post post, MultipartFile coverImage, List<MultipartFile> images) throws Exception {
		checkPostAccessibility(post);
		post.setCoverImageURL(uploadUtil.uploadImage(coverImage, CLOUDINARY_COVER_FOLDER));
		for (int i = 0; i < images.size(); i++) {
			post.getPostImages().get(i)
					.setPostImageURL(uploadUtil.uploadImage(images.get(i), CLOUDINARY_POST_IMAGES_FOLDER));
		}
		postDao.save(post);
		post.addPhoneToImages();
		postDao.save(post);
		/*for(PostImage pi : post.getPostImages()) {
			pi.setPost(post);
			
		}*/
		return post;
	}

	@Override
	public List<Post> getPosts(int page) {
		Page<Post> posts = postDao.findAll(PageRequest.of(page, MAX_POST_LENGTH, Sort.by("id").descending()));
		return posts.getContent();
	}

	@Override
	public List<Post> getPostsOfUser(long userId, int page) {
		List<Post> posts = postDao.findAllByPostedById(userId,
				PageRequest.of(page, MAX_POST_LENGTH, Sort.by("id").descending()));
		return posts;
	}

	@Override
	public Post getPost(long id) {
		Optional<Post> post = postDao.findById(id);
		if (post.isPresent()) {
			return post.get();
		} else {
			throw new EntityNotFoundException(CommonErrorMessages.NO_RECORD_WITH_ID + id);
		}
	}

	@Override
	public List<Post> getRandom() {
		return postDao.findAll();
	}

	@Override
	public boolean deletePost(long id) throws NotReadablePropertyException, AccessDeniedException {
		Post post = getPost(id);
		checkPostAccessibility(post);
		post.setDeleted(true);
		postDao.save(post);
		//postDao.deleteById(post.getId());
		return true;
	}

	@Override
	public String getPostUrl(long id) throws JsonMappingException, JsonProcessingException, UniformInterfaceException,
			ClientHandlerException, UnsupportedEncodingException {
		Post post = getPost(id);
		if (post != null && post.getMediaURL() != null && !post.getMediaURL().isEmpty()) {
			return "{\"url\":\""+ post.getMediaURL() +"\"}";
		}
		String link = firebaseUtil.createDynamicLink(post.getId(), ItemType.POST);
		post.setMediaURL(link);
		postDao.save(post);
		return "{\"url\":\""+ link +"\"}";
	}

	@Override
	public List<Post> getPostForPlan(long planId) {
		return postDao.findAllByPlanId(planId);
	}

	private void checkPostAccessibility(Post post) throws AccessDeniedException, NotReadablePropertyException {
		User currentUser = userService.getAuthenticatedUser();
		if (post == null) {
			throw new NotReadablePropertyException(Post.class, CommonErrorMessages.NULL_ERROR);
		} else if (post.getPostedBy().getId() == null || post.getPostedBy().getId() != currentUser.getId()) {
			throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
		}
	}
}
