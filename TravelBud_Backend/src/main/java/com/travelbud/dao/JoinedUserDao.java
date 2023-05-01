package com.travelbud.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.JoinedUser;

@Repository
public interface JoinedUserDao extends JpaRepository<JoinedUser, Long>{
	public JoinedUser findByUserIdAndPlanId(long userId, long planId);
	public List<JoinedUser> findAllByUserIdAndPlanConfirmed(long userId, boolean planConfirmed);
	
	@Query(nativeQuery = true, value = "select plan_id from travelbud.plan_joined_by where user_id = ?1 and plan_confirmed = false group by plan_id")
	public List<Long> getRequestPlanIdOfUser(long userId);
}
