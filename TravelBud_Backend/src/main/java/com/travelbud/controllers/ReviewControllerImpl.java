package com.travelbud.controllers;

import java.util.List;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelbud.entities.Review;
import com.travelbud.services.ReviewService;

@RestController
@RequestMapping("/review")
public class ReviewControllerImpl implements ReviewController {
	
	@Autowired
	private ReviewService reviewService;

	@GetMapping("/rpu/{userId}/{planId}")
	@Override
	public Review getPlanReviewByUser(@PathVariable long userId,@PathVariable long planId) throws java.nio.file.AccessDeniedException {
		return reviewService.getPlanReviewByUser(userId, planId);
	}

	@PostMapping("")
	@Override
	public Review saveReview(@RequestBody Review review) throws AccessDeniedException, NotReadablePropertyException, java.nio.file.AccessDeniedException {
		return reviewService.saveReview(review);
	}
	
	@GetMapping("/ur/{userId}/{page}")
	@Override
	public List<Review> getUserReviews(@PathVariable long userId, @PathVariable int page) {
		return reviewService.getUserReviews(userId, page);
	}

	@GetMapping("/pr/{planId}/{page}")
	@Override
	public List<Review> getPlanReviews(@PathVariable long planId, @PathVariable int page) {
		return reviewService.getPlanReviews(planId, page);
	}

	@DeleteMapping("")
	@Override
	public boolean deleteReview(@RequestBody Review review)
			throws NotReadablePropertyException, java.nio.file.AccessDeniedException {
		return reviewService.deleteReview(review);
	}

}
