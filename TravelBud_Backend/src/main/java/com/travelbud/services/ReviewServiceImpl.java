package com.travelbud.services;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.travelbud.dao.ReviewDao;
import com.travelbud.entities.Post;
import com.travelbud.entities.Review;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;

@Service
public class ReviewServiceImpl implements ReviewService {
	
	@Autowired
	private ReviewDao reviewDao;
	
	@Autowired
	private UserService userService;
	
	private final int MAX_REVIEW_LENGTH = 40;

	@Override
	public Review saveReview(Review review) throws NotReadablePropertyException, AccessDeniedException {
		checkReviewAccessibility(review);
		return reviewDao.saveAndFlush(review);
	}

	@Override
	public Review getPlanReviewByUser(long userId, long planId) throws AccessDeniedException {
		if(userId == 0) {
			userId = userService.getAuthenticatedUser().getId();
		}
		Review r = reviewDao.findByReviewedByIdAndPlanId(userId, planId);
		return r == null ? null : r;
	}

	@Override
	public List<Review> getUserReviews(long userId, int page) {
		return reviewDao.getUserReviews(userId, PageRequest.of(page, MAX_REVIEW_LENGTH, Sort.by("id").descending()));
	}

	@Override
	public List<Review> getPlanReviews(long planId, int page) {
		return reviewDao.findAllByPlanId(planId, PageRequest.of(page, MAX_REVIEW_LENGTH, Sort.by("id").descending()));
	}
	
	private void checkReviewAccessibility(Review review) throws AccessDeniedException, NotReadablePropertyException {
		User currentUser = userService.getAuthenticatedUser();
		if (review == null) {
			throw new NotReadablePropertyException(Post.class, CommonErrorMessages.NULL_ERROR);
		} else if (review.getReviewedBy().getId() == null || review.getReviewedBy().getId() != currentUser.getId()) {
			throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
		}
	}

	@Override
	public  boolean deleteReview(Review review) throws NotReadablePropertyException, AccessDeniedException {
		checkReviewAccessibility(review);
		reviewDao.delete(review);
		return true;
	}
}
