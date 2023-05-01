package com.travelbud.services;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelbud.dao.FollowDao;
import com.travelbud.entities.Follow;
import com.travelbud.entities.User;

@Service
public class FollowServiceImpl implements FollowService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private FollowDao followDao;
	
	@Override
	public boolean follow(long userId) throws AccessDeniedException {
		User authUser = userService.getAuthenticatedUser();
		//Follow follow = new Follow(userService.getUserById(userId), authUser);
		User user = userService.getUserById(userId);//checking to see if use exists
		if(user == null || user.getId() != userId)throw new AccessDeniedException(null);
		Follow follow = new Follow(userId, authUser.getId());
		followDao.save(follow);
		return true;
	}

	@Override
	public boolean unfollow(long userId) throws AccessDeniedException {
		User authUser = userService.getAuthenticatedUser();
		Follow follow = followDao.findByUserIdAndFollowedById(userId, authUser.getId());
		if(follow != null && follow.getId() != null) {
			followDao.delete(follow);
		}
		return true;
	}

	@Override
	public int getFollowingCount(long userId) throws AccessDeniedException {
		return followDao.getFollowingCount(userId);
	}

	@Override
	public int getFollowerCount(long userId) throws AccessDeniedException {
		return followDao.getFollowerCount(userId);
	}

}
