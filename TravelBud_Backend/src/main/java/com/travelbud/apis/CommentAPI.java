package com.travelbud.apis;

import java.nio.file.AccessDeniedException;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.NotReadablePropertyException;

import com.travelbud.entities.Comment;

public interface CommentAPI {
	public Comment saveComment(Comment comment) throws NotReadablePropertyException, AccessDeniedException;
	public List<Comment> getComments(long postId, int page);
	public Comment getComment(long id) throws EntityNotFoundException;
}
