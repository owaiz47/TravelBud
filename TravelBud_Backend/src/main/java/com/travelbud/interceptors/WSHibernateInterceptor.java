package com.travelbud.interceptors;

import java.io.Serializable;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.travelbud.configs.CloudinaryConfig;
import com.travelbud.dao.DonationDao;
import com.travelbud.dao.LikeDao;
import com.travelbud.dao.RatingDao;
import com.travelbud.dao.UserDao;
import com.travelbud.entities.Like;
import com.travelbud.entities.Post;
import com.travelbud.entities.PostImage;
import com.travelbud.entities.Rating;
import com.travelbud.entities.User;
import com.travelbud.services.FollowService;
import com.travelbud.services.UserService;
import com.travelbud.utils.SpringContextUtii;

public class WSHibernateInterceptor extends EmptyInterceptor {
	private static final long serialVersionUID = 1L;

	Logger logger = LoggerFactory.getLogger(WSHibernateInterceptor.class);
	
	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (SecurityContextHolder.getContext() != null  && SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getDetails() != null) {
			UserService userService = SpringContextUtii.getApplicationContext().getBean(UserService.class);
			FollowService followService = SpringContextUtii.getApplicationContext().getBean(FollowService.class);
			User currentUser;
			try {
				currentUser = userService.getAuthenticatedUser();
				if (currentUser != null) {
					if (entity instanceof User) {
						UserDao userDao = SpringContextUtii.getApplicationContext().getBean(UserDao.class);
						User user = (User) entity;
						user.setFollowingUser(userDao.checkFollowing(user.getId(), currentUser.getId()));
						user.setPostRating(userDao.getPostRatingOf(user.getId()));
						user.setPlanRating(userDao.getPlanRatingOf(user.getId()));
						user.setFollowerCount(followService.getFollowerCount(user.getId()));
						user.setFollowingCount(followService.getFollowingCount(user.getId()));
					}else if(entity instanceof Post) {
						LikeDao likeDao = SpringContextUtii.getApplicationContext().getBean(LikeDao.class);
						DonationDao donationDao = SpringContextUtii.getApplicationContext().getBean(DonationDao.class);
						RatingDao ratingDao = SpringContextUtii.getApplicationContext().getBean(RatingDao.class);

						Post post = (Post) entity;
						Like like = likeDao.findByPostIdAndUserId(post.getId(), currentUser.getId());
						if(like != null) {
							post.setUserLiked(true);
						}
						List<String> states = Arrays.asList(propertyNames);
						String postedBy = "postedBy";
						if(states.contains(postedBy)){
							int index = states.indexOf(postedBy);
							User ps = (User) state[index];
							if(ps != null)post.setDonationDetails(donationDao.findByUserId(ps.getId()));
						}
						
						Rating rating = ratingDao.findByUserIdAndPostId(currentUser.getId(), post.getId());
						if(rating != null) {
							post.setUserRating(rating.getStars());
						}else {
							post.setUserRating(0.0);
						}
					}
				}
			} catch (AccessDeniedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return super.onLoad(entity, id, state, propertyNames, types);
	}
	
	/*
	 * @Override public String onPrepareStatement(String sql) { logger.info(sql);
	 * boolean replaced = false; if(SecurityContextHolder.getContext() != null &&
	 * SecurityContextHolder.getContext().getAuthentication() != null &&
	 * SecurityContextHolder.getContext().getAuthentication().getDetails() != null)
	 * { userDao= SpringContextUtii.getApplicationContext().getBean(UserDao.class);
	 * WSWebAuthenticationDetailsSource details = (WSWebAuthenticationDetailsSource)
	 * SecurityContextHolder.getContext().getAuthentication().getDetails(); if
	 * (details != null && details.getAuthenticatedUser() != null) { sql =
	 * sql.replaceAll("user0_.CURRENT_USER_ID",
	 * details.getAuthenticatedUser().getId().toString()); replaced = true; } }
	 * if(!replaced)sql = sql.replaceAll("user0_.CURRENT_USER_ID", "0");
	 * logger.info(sql); return super.onPrepareStatement(sql); }
	 */
}
