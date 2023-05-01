package com.travelbud.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.User;

@Repository
public interface UserDao extends JpaRepository<User, Long>{
	public User findByUsername(String username);//findByUsernameOrEmail
	public User findByEmail(String email);
	public boolean existsByUsername(String username);
	public boolean existsByEmail(String email);
	public List<User> findByUsernameOrFullnameContainingIgnoreCase(String username, String name);
	
	@Query(nativeQuery = true, value = "select s.id from travelbud.spy s where s.id in (select p.user_id from travelbud.plan p left join travelbud.plan_requested_by pr on pr.plan_id = p.id group by p.id order by count(pr.plan_id) desc) limit 30")
	public List<Long> getTrendingUsers();
	
	@Query(nativeQuery = true, value = "select count(*)>0 from travelbud.follow where follow.user_id = ?1 and follow.followed_by_id = ?2")
	public boolean checkFollowing(long userId, long currentUserId);
	
	@Query(nativeQuery = true, value = "select avg(r.stars) as stars from travelbud.rating r WHERE r.post_id IN (select id from travelbud.post where post.user_id = ?1)")
	public Double getPostRatingOf(long userId);
	
	@Query(nativeQuery = true, value = "select avg(r.stars) as stars from travelbud.review r WHERE r.plan_id IN (select id from travelbud.plan where plan.user_id = ?1)")
	public Double getPlanRatingOf(long userId);
	
	@Modifying
	@Query(nativeQuery = true, value="update travelbud.spy set password = ?2 where email = ?1")
	public void updatePassword(String email, String password);
}

