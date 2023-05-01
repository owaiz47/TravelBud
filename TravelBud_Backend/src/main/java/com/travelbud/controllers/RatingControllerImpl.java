package com.travelbud.controllers;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelbud.entities.Post;
import com.travelbud.entities.Rating;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;
import com.travelbud.services.RatingService;
import com.travelbud.services.UserService;

@RestController
@RequestMapping("/rating")
public class RatingControllerImpl implements RatingController {

	@Autowired
	private RatingService ratingService;
	
	
	@Override
	public boolean saveRating(Rating rating) throws NotReadablePropertyException, AccessDeniedException {
		return ratingService.saveRating(rating);
	}
}
