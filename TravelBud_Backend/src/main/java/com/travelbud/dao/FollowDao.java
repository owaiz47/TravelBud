package com.travelbud.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.Follow;

@Repository
public interface FollowDao extends JpaRepository<Follow, Long>{
	public Follow findByUserIdAndFollowedById(long userId, long followedById);
	
	@Query(nativeQuery = true, value = "select count(*) from travelbud.follow f where f.followed_by_id = ?1")
	public int getFollowingCount(long userId);
	
	@Query(nativeQuery = true, value = "select count(*) from travelbud.follow f where f.user_id = ?1")
	public int getFollowerCount(long userId);
}
