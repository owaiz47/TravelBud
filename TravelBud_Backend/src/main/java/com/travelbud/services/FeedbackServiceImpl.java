package com.travelbud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelbud.dao.FeedbackDao;
import com.travelbud.entities.Feedback;

@Service
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	private FeedbackDao feedbackDao;
	
	@Override
	public void saveFeedback(Feedback feedback) {
		feedbackDao.save(feedback);

	}

}
