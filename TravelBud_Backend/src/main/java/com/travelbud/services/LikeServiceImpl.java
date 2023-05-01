package com.travelbud.services;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelbud.dao.LikeDao;
import com.travelbud.entities.Like;
import com.travelbud.entities.Post;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;

@Service
public class LikeServiceImpl implements LikeService {

	@Autowired
	private LikeDao likeDao;

	@Autowired
	private UserService userService;

	@Override
	public boolean saveLike(Like like) throws NotReadablePropertyException, AccessDeniedException {
		//checkLikeAccessibility(like);
		if (like == null) {
			throw new NotReadablePropertyException(Post.class, CommonErrorMessages.NULL_ERROR);
		}
		User authUser = userService.getAuthenticatedUser();
		Like l = likeDao.findByPostIdAndUserId(like.getPostId(), authUser.getId());
		if(l != null) {
			return true;
		}
		like.setUserId(authUser.getId());
		likeDao.save(like);
		return true;
	}

	@Override
	public boolean deleteLike(Like like) throws NotReadablePropertyException, AccessDeniedException {
		User authUser = userService.getAuthenticatedUser();

		if(like.getId() == null) {
			like = likeDao.findByPostIdAndUserId(like.getPostId(), authUser.getId());
		}
		checkLikeAccessibility(like);
		if(like == null)return false;
		likeDao.delete(like);
		return true;
	}

	private void checkLikeAccessibility(Like like) throws AccessDeniedException, NotReadablePropertyException {
		User authUser = userService.getAuthenticatedUser();
		if (like == null) {
			throw new NotReadablePropertyException(Post.class, CommonErrorMessages.NULL_ERROR);
		} else if (like.getUserId() != authUser.getId()) {
			throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
		}
	}

}
