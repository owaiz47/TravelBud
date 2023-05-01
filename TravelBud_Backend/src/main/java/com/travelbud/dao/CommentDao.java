package com.travelbud.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.Comment;

@Repository
public interface CommentDao extends JpaRepository<Comment, Long> {
	public List<Comment> findAllByPostId(long postId, Pageable pageable);
}
