package com.travelbud.controllers;

import java.nio.file.AccessDeniedException;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.travelbud.entities.Comment;
import com.travelbud.entities.Post;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;
import com.travelbud.services.CommentService;
import com.travelbud.services.UserService;

@RestController
@RequestMapping("/comments")
public class CommentControllerImpl implements CommentController {

	@Autowired
	private CommentService commentService;
	
	@PostMapping("")
	@Override
	public Comment saveComment(@RequestParam Comment comment) throws NotReadablePropertyException, AccessDeniedException {
		return commentService.saveComment(comment);
	}

	@GetMapping("/{postId}/page/{page}")
	@Override
	public List<Comment> getComments(@PathVariable long postId, @PathVariable int page) {
		return commentService.getComments(postId, page);
	}

	@GetMapping("/{id}")
	@Override
	public Comment getComment(@PathVariable long id) throws EntityNotFoundException {
		return commentService.getComment(id);
	}

}
