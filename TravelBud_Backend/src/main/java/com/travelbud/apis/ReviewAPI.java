package com.travelbud.apis;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.NotReadablePropertyException;

import com.travelbud.entities.Review;

public interface ReviewAPI {
	public Review saveReview(Review review) throws NotReadablePropertyException, AccessDeniedException;
	public Review getPlanReviewByUser(long userId, long planId) throws AccessDeniedException;
	public List<Review> getUserReviews(long userId, int page);
	public List<Review> getPlanReviews(long planId, int page);
	public boolean deleteReview(Review review) throws NotReadablePropertyException, AccessDeniedException;
}
