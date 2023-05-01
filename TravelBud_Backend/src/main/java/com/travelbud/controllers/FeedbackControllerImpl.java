package com.travelbud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelbud.entities.Feedback;
import com.travelbud.services.FeedbackService;

@RestController
@RequestMapping("/feedback")
public class FeedbackControllerImpl implements FeedbackController {

	@Autowired
	private FeedbackService feedbackService;
	
	@PostMapping("")
	@Override
	public void saveFeedback(@RequestBody Feedback feedback) {
		feedbackService.saveFeedback(feedback);

	}

}
