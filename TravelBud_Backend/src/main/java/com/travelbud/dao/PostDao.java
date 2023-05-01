package com.travelbud.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.Post;

@Repository
public interface PostDao extends JpaRepository<Post, Long>{
	public List<Post> findAllByPostedById(long userId, Pageable pageable);
	public List<Post> findAllByPlanId(long planId);
}