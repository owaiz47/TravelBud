package com.travelbud.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.RequestedUser;

@Repository
public interface RequestedUserDao extends JpaRepository<RequestedUser, Long>{
	public RequestedUser findByUserIdAndPlanId(long userId, long planId);

}
