package com.travelbud.websocket.helper;

public class WebSocketData {
	//Main heads for web socket JSON
	public final static String SERVICE = "service";
	public final static String NAME = "name";
	public final static String CONTENT = "content";
	public final static String ITEM_TYPE = "item";
	
	//ITEM NAMES
	public final static String ITEM_USER = "user";
	public final static String ITEM_POST = "post";
	public final static String ITEM_PLAN = "plan";
	public final static String ITEM_MESSAGE = "message";
	public final static String ITEM_UPDATE_MESSAGE = "update_message";
	public final static String ITEM_MESSAGE_LIST = "message_list";
	public final static String ITEM_SPONSOR = "sponsor";
	public final static String ITEM_NOTIFICATION = "notification";
	
	//Service names for web socket JSON
	public final static String USER_SERVICE = "user_service";
	public final static String POST_SERVICE = "post_service";
	public final static String PLAN_SERVICE = "plan_service";
	public final static String MSG_SERVICE = "msg_service";
	public final static String LIKE_SERVICE = "like_service";
	public final static String COMMENT_SERVICE = "comment_service";
	public final static String RATE_SERVICE = "rate_service";
	public final static String FOLLOW_SERVICE = "follow_service";
	public final static String SPONSOR_SERVICE = "sponsor_service";
	public final static String REVIEW_SERVICE = "review_service";
	public final static String NOTIFY_SERVICE = "notify_service";
	public final static String LAST_ONLINE_SERVICE = "last_online_service";
	
	
	//METHODS FOR WEBSOCKET JSON
	
	//User Service Methods
	public final static String AUTHENTICATED_USER = "authenticated_user";
	
	//Post Service Methods
	//#No methods for now
	
	//Message Service Methods
	public final static String MSG_SEND = "send_message";
	public final static String MSGS_READ = "msgs_read";
	public final static String MSG_READ = "msg_read";
	
	//Last Online Check Activity Methods
	public final static String LAST_ONLINE_CHECK_METHOD = "check_active";
	
	//Like Service Methods
	public final static String LIKE_METHOD = "liked";
	public final static String DISLIKE_METHOD = "dislike";
	
	//Comment Service Methods
	public final static String COMMENT_SAVE_METHOD = "save_comment";
	
	//Rating Service Methods
	public final static String RATE_METHOD = "save_rating";
	
	//Follow Service Methods
	public final static String FOLLOW_METHOD = "follow";
	
	//Review Service Methods 
	public final static String REVIEW_METHOD = "save_review";
	
	//Sponsor Service Methods
	public final static String SPONSOR_SAVE_METHOD = "save_sponsor";
	
	//Plan Service Methods
	public final static String PLAN_CONFIRM_JOIN_METHOD = "confirm_join";
	public final static String PLAN_ACCEPT_COWAN_METHOD = "accept_cowan";
	public final static String PLAN_REJECT_COWAN_METHOD = "reject_cowan";
	public final static String PLAN_JOIN_REQ_METHOD = "request_join";
	public final static String PLAN_ACCEPT_JOIN_REQ_METHOD = "accept_join_request";
	public final static String PLAN_REJECT_JOIN_REQ_METHOD = "reject_join_request";
	public final static String PLAN_ADD_EXIT_REQ_METHOD = "exit_plan_req";
	public final static String PLAN_ACCEPT_EXIT_REQ_METHOD = "accpet_exit_request";
	public final static String PLAN_REJECT_EXIT_REQ_METHOD = "reject_exit_request";
	public final static String PLAN_REMOVE_USER_METHOD = "plan_remove_user";
	public final static String PLAN_WITHDRAW_REQUEST = "plan_withdraw_request";
	
	
}
