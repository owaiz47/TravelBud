package com.travelbud.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.travelbud.filters.JwtFilter;
import com.travelbud.services.CommentService;
import com.travelbud.services.FollowService;
import com.travelbud.services.LastOnlineService;
import com.travelbud.services.LikeService;
import com.travelbud.services.MessageService;
import com.travelbud.services.NotificationService;
import com.travelbud.services.PlanService;
import com.travelbud.services.PostService;
import com.travelbud.services.RatingService;
import com.travelbud.services.ReportService;
import com.travelbud.services.ReviewService;
import com.travelbud.services.SponsorService;
import com.travelbud.services.UserService;
import com.travelbud.utils.OneSignalUtil;
import com.travelbud.websocket.util.WebSocketUtil;

@Configuration
public class WebSocketConfig {

	@Autowired
	private JwtFilter jwtFilter;
	
	@Autowired
	private WebSocketUtil webSocketUtil;

	@Autowired
	private UserService userService;

	@Autowired
	private PostService postService;

	@Autowired
	private PlanService planService;

	@Autowired
	private RatingService ratingService;

	@Autowired
	private LikeService likeService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private SponsorService sponsorService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private FollowService followService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private ReportService reportService;
	
	@Autowired
	private LastOnlineService lastOnlineService;
	
	@Autowired
	private OneSignalUtil oneSignalUtil;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public PostService getPostService() {
		return postService;
	}

	public void setPostService(PostService postService) {
		this.postService = postService;
	}

	public PlanService getPlanService() {
		return planService;
	}

	public void setPlanService(PlanService planService) {
		this.planService = planService;
	}

	public RatingService getRatingService() {
		return ratingService;
	}

	public void setRatingService(RatingService ratingService) {
		this.ratingService = ratingService;
	}

	public LikeService getLikeService() {
		return likeService;
	}

	public void setLikeService(LikeService likeService) {
		this.likeService = likeService;
	}

	public ReviewService getReviewService() {
		return reviewService;
	}

	public void setReviewService(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	public SponsorService getSponsorService() {
		return sponsorService;
	}

	public void setSponsorService(SponsorService sponsorService) {
		this.sponsorService = sponsorService;
	}

	public CommentService getCommentService() {
		return commentService;
	}

	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public WebSocketUtil getWebSocketUtil() {
		return webSocketUtil;
	}

	public void setWebSocketUtil(WebSocketUtil webSocketUtil) {
		this.webSocketUtil = webSocketUtil;
	}

	public JwtFilter getJwtFilter() {
		return jwtFilter;
	}

	public void setJwtFilter(JwtFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}

	public FollowService getFollowService() {
		return followService;
	}

	public void setFollowService(FollowService followService) {
		this.followService = followService;
	}

	public NotificationService getNotificationService() {
		return notificationService;
	}

	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	public OneSignalUtil getOneSignalUtil() {
		return oneSignalUtil;
	}

	public void setOneSignalUtil(OneSignalUtil oneSignalUtil) {
		this.oneSignalUtil = oneSignalUtil;
	}

	public LastOnlineService getLastOnlineService() {
		return lastOnlineService;
	}

	public void setLastOnlineService(LastOnlineService lastOnlineService) {
		this.lastOnlineService = lastOnlineService;
	}
	
}
