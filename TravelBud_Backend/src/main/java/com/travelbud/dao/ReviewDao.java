package com.travelbud.dao;

import java.util.List;

import javax.persistence.Entity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.Review;

@Repository
public interface ReviewDao extends JpaRepository<Review, Long> {
	public Review findByReviewedByIdAndPlanId(long reviewedById, long planId);
	public List<Review> findAllByPlanId(long planId, Pageable pageable);
	
	@Query(nativeQuery = true, value = "SELECT * FROM travelbud.review WHERE plan_id IN (SELECT id FROM travelbud.plan WHERE user_id = ?1)")
	public List<Review> getUserReviews(long userId, Pageable pageable);
}
