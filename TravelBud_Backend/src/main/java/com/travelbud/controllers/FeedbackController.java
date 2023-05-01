package com.travelbud.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.travelbud.apis.FeedbackAPI;

@RestController("/feedback")
public interface FeedbackController extends FeedbackAPI{

}
