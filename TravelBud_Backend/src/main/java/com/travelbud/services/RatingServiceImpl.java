package com.travelbud.services;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelbud.dao.RatingDao;
import com.travelbud.entities.Post;
import com.travelbud.entities.Rating;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;

@Service
public class RatingServiceImpl implements RatingService {

	@Autowired
	private RatingDao ratingDao;

	@Autowired
	private UserService userService;

	@Override
	public boolean saveRating(Rating rating) throws NotReadablePropertyException, AccessDeniedException {
		//checkRatingAccessibility(rating);
		if (rating == null) {
			throw new NotReadablePropertyException(Post.class, CommonErrorMessages.NULL_ERROR);
		}
		User currentUser = userService.getAuthenticatedUser();
		Rating rate = ratingDao.findByUserIdAndPostId(rating.getUserId(), rating.getPostId());
		if(rate != null) {
			rate.setStars(rating.getStars());
			ratingDao.save(rate);
			return true;
		}
		rating.setUserId(currentUser.getId());
		ratingDao.save(rating);
		return true;
	}

	private void checkRatingAccessibility(Rating rating) throws AccessDeniedException, NotReadablePropertyException {
		User currentUser = userService.getAuthenticatedUser();
		if (rating == null) {
			throw new NotReadablePropertyException(Post.class, CommonErrorMessages.NULL_ERROR);
		} else if (rating.getUserId() != currentUser.getId()) {
			throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
		}
	}
}
