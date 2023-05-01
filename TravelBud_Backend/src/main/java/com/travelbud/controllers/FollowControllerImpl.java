package com.travelbud.controllers;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelbud.services.FollowService;

@RestController
@RequestMapping("/follow")
public class FollowControllerImpl implements FollowController {

	@Autowired
	private FollowService followService;
	
	@GetMapping("/{userId}")
	@Override
	public boolean follow(@PathVariable long userId) throws AccessDeniedException {
		return followService.follow(userId);
	}

	@DeleteMapping("/{userId}")
	@Override
	public boolean unfollow(@PathVariable long userId) throws AccessDeniedException {
		return followService.unfollow(userId);
	}

	@GetMapping("/following_count/{userId}")
	@Override
	public int getFollowingCount(@PathVariable long userId) throws AccessDeniedException {
		return followService.getFollowingCount(userId);
	}

	@GetMapping("/followers_count/{userId}")
	@Override
	public int getFollowerCount(@PathVariable long userId) throws AccessDeniedException {
		return followService.getFollowerCount(userId);
	}

}
