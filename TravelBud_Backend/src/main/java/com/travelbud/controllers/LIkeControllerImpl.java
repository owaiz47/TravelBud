package com.travelbud.controllers;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.travelbud.entities.Like;
import com.travelbud.entities.Post;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;
import com.travelbud.services.LikeService;
import com.travelbud.services.UserService;

@RestController
@RequestMapping("/likes")
public class LIkeControllerImpl implements LikeController {

	@Autowired
	private LikeService likeService;
	
	@PostMapping("")
	@Override
	public boolean saveLike(@RequestParam Like like) throws AccessDeniedException {
		return likeService.saveLike(like);
	}

	@DeleteMapping("")
	@Override
	public boolean deleteLike(@RequestParam Like like) throws NotReadablePropertyException, AccessDeniedException {
		return likeService.deleteLike(like);
	}

}
