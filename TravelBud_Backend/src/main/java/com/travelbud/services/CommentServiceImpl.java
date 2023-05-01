package com.travelbud.services;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.travelbud.dao.CommentDao;
import com.travelbud.entities.Comment;
import com.travelbud.entities.Post;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentDao commentDao;
	
	@Autowired
	private UserService userService;
	
	private final int MAX_COMMENT_LENGTH = 25;
	
	@Override
	public Comment saveComment(Comment comment) throws NotReadablePropertyException, AccessDeniedException {
		//checkCommentAccessibility(comment);
		if (comment == null) {
			throw new NotReadablePropertyException(Post.class, CommonErrorMessages.NULL_ERROR);
		}
		User authUser = userService.getAuthenticatedUser();
		comment.setCommentedBy(authUser);
		return commentDao.saveAndFlush(comment);
	}

	@Override
	public List<Comment> getComments(long postId, int page) {
		return commentDao.findAllByPostId(postId, PageRequest.of(page, MAX_COMMENT_LENGTH, Sort.by("id").descending()));
	}

	@Override
	public Comment getComment(long id) throws EntityNotFoundException{
		Optional<Comment> comment = commentDao.findById(id);
		if(comment != null && comment.isPresent()) {
			return comment.get();
		}
		throw new EntityNotFoundException(CommonErrorMessages.NO_RECORD_WITH_ID + id);
	}
	
	private void checkCommentAccessibility(Comment comment) throws AccessDeniedException, NotReadablePropertyException {
		User authUser = userService.getAuthenticatedUser();
		if (comment == null) {
			throw new NotReadablePropertyException(Post.class, CommonErrorMessages.NULL_ERROR);
		} else if (comment.getCommentedBy().getId() == null || comment.getCommentedBy().getId() != authUser.getId()) {
			throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
		}
	}

}
