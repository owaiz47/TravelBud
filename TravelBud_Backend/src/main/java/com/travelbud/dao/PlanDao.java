package com.travelbud.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.Plan;

@Repository
public interface PlanDao extends JpaRepository<Plan, Long>{
	public List<Plan> findAllByPlanById(long userId, Pageable pageable);
	
	@Query(nativeQuery = true, value = "SELECT * FROM travelbud.plan p WHERE (p.user_id = ?1 OR p.id IN (SELECT pj.plan_id FROM travelbud.plan_joined_by pj WHERE pj.user_id = ?1 AND plan_confirmed = true)) AND deleted = false")
	public List<Plan> getPlansOfUser(long userId, Pageable pageable);
	
	@Query(nativeQuery = true, value = "select plan.*, count(plan_requested_by.plan_id) as views from travelbud.plan left join travelbud.plan_requested_by on plan_requested_by.plan_id = plan.id where plan.deleted = false group by plan.id order by views desc limit 30")
	public List<Plan> getTrendingPlans();
	
	@Query(nativeQuery = true, value = "select * from travelbud.plan p1 where p1.id  in (select p.plan_id from  travelbud.place p where p.name ilike %?1% or p.place_des ilike %?1%) and p1.deleted=false limit 10")
	public List<Plan> getPlanMatching(String name);
	
	@Query(nativeQuery = true, value = "SELECT * FROM travelbud.plan pl WHERE (pl.user_id = ?1 OR pl.id IN (SELECT pj.plan_id FROM travelbud.plan_joined_by pj WHERE pj.user_id = ?1 AND pj.plan_confirmed = true)) AND (pl.id IN (SELECT p.plan_id FROM travelbud.place p WHERE pl.deleted = false AND (p.name ILIKE %?2% OR p.place_des ILIKE %?2%))) AND pl.deleted = false AND pl.start_date < CURRENT_TIMESTAMP LIMIT 10")
	public List<Plan> geMyCompletedPlansMatching(long userId, String name);
	
	@Query(nativeQuery = true, value = "SELECT * FROM travelbud.plan pl WHERE (pl.user_id = ?1 OR pl.id IN (SELECT pj.plan_id FROM travelbud.plan_joined_by pj WHERE pj.user_id = ?1 AND pj.plan_confirmed = true)) AND (pl.id IN (SELECT p.plan_id FROM travelbud.place p WHERE pl.deleted = false AND (p.name ILIKE %?2% OR p.place_des ILIKE %?2%))) AND pl.deleted = false LIMIT 10")
	public List<Plan> geMyPlansMatching(long userId, String name);
}
