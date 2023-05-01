package com.travelbud.websocket;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelbud.dto.MessageStatus;
import com.travelbud.dto.NotificationType;
import com.travelbud.dto.OneSignal;
import com.travelbud.dto.WSCarrier;
import com.travelbud.entities.LastOnline;
import com.travelbud.entities.Message;
import com.travelbud.entities.Notification;
import com.travelbud.entities.User;
import com.travelbud.services.NotificationService;
import com.travelbud.services.UserService;
import com.travelbud.websocket.config.WebSocketConfig;
import com.travelbud.websocket.handlers.AbstractServiceHandler;
import com.travelbud.websocket.handlers.CommentServiceHandler;
import com.travelbud.websocket.handlers.FollowServiceHandler;
import com.travelbud.websocket.handlers.HandlerAPI;
import com.travelbud.websocket.handlers.LikeServiceHandler;
import com.travelbud.websocket.handlers.MessageServiceHandler;
import com.travelbud.websocket.handlers.PlanServiceHandler;
import com.travelbud.websocket.handlers.PostServiceHanlder;
import com.travelbud.websocket.handlers.RatingServiceHandler;
import com.travelbud.websocket.handlers.ReviewServiceHandler;
import com.travelbud.websocket.handlers.SponsorServiceHandler;
import com.travelbud.websocket.handlers.UserServiceHandler;
import com.travelbud.websocket.helper.WebSocketData;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebSocketHandler extends ChannelInboundHandlerAdapter {

	Calendar cal = Calendar.getInstance();

	private Logger log = LoggerFactory.getLogger(WebSocketHandler.class);

	private WebSocketConfig webSocketConfig;
	private User authenticatedUser;
	private Map<String, Object> serviceMap = new HashMap<String, Object>();
	private Map<String, HandlerAPI> handlerMap = new HashMap<String, HandlerAPI>();

	WebSocketHandler(WebSocketConfig webSocketConfig, User authenticatedUser) {
		this.webSocketConfig = webSocketConfig;
		this.authenticatedUser = authenticatedUser;
		fillServiceMap();
		fillHandlerMap();
	}

	/*
	 * @Override public void channelActive(ChannelHandlerContext ctx) throws
	 * Exception { super.channelActive(ctx); WSCarrier ws = new WSCarrier();
	 * ws.setService("user_service"); ws.setName(WebSocketData.AUTHENTICATED_USER);
	 * ObjectMapper Obj = new ObjectMapper(); //this.user =
	 * Obj.readValue(wsCarrier.getContent(), User.class); String jsonStr =
	 * Obj.writeValueAsString(this.authenticatedUser); ws.setContent(jsonStr);
	 * webSocketConfig.getWebSocketUtil().getActiveUserMap().put(this.
	 * authenticatedUser, ctx); webSocketConfig.getWebSocketUtil().send(ctx, ws);
	 * handleMessage(ws, ctx); }
	 */

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		webSocketConfig.getWebSocketUtil().getActiveUserMap().remove(authenticatedUser.getId());
		webSocketConfig.getLastOnlineService().updateLastOnline(this.authenticatedUser);
		ctx.channel().close();
		webSocketConfig.getJwtFilter().revoke();
		super.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		
		webSocketConfig.getJwtFilter().authenticate(authenticatedUser.getUsername(), authenticatedUser.getPassword());
		
		try {
			if (msg instanceof WebSocketFrame) {
				if (msg instanceof TextWebSocketFrame) {
					WSCarrier wsCarrier = getWSCarrier(((TextWebSocketFrame) msg).text());
					// ctx.channel().writeAndFlush(new TextWebSocketFrame("Message recieved : "));
					handleMessage(wsCarrier, ctx);
				}
			}
		} catch (JsonMappingException e) {
			// ctx.channel().writeAndFlush(new TextWebSocketFrame("Json Error. Verify json
			// structure"));
			sendUnsupported(ctx);
			log.error("JsonMappingException in websockets" + e.getMessage());
		} catch (JsonProcessingException e) {
			// ctx.channel().writeAndFlush(new TextWebSocketFrame("Json Error. Verify json
			// structure"));
			sendUnsupported(ctx);
			log.error("JsonProcessingException in websockets" + e.getMessage());
		} catch (Exception e) {
			// ctx.channel().writeAndFlush(new TextWebSocketFrame("500 Internal Server
			// Error."));
			log.error("Exception in websockets" + e.getMessage());
		} finally {
			webSocketConfig.getJwtFilter().revoke();
		}
	}

	private void handleMessage(WSCarrier wsCarrier, ChannelHandlerContext ctx) throws ParseException, Exception {

		if (wsCarrier.getName().equalsIgnoreCase(WebSocketData.LAST_ONLINE_CHECK_METHOD)) {
			long userId = Long.valueOf(wsCarrier.getContent());
			LastOnline lastOnline;
			if (webSocketConfig.getWebSocketUtil().getActiveUserMap().containsKey(userId)) {
				lastOnline = new LastOnline(true, webSocketConfig.getLastOnlineService().getLastOnline(userId).getUser());

			} else {
				lastOnline = webSocketConfig.getLastOnlineService().getLastOnline(userId);
			}
			if (lastOnline != null) {
				ObjectMapper obj = new ObjectMapper();
				WSCarrier ws = new WSCarrier();
				ws.setContent(obj.writeValueAsString(lastOnline));
				ws.setItem(WebSocketData.LAST_ONLINE_CHECK_METHOD);
				webSocketConfig.getWebSocketUtil().send(ctx, ws);
			}
		} else {
			AbstractServiceHandler handler = handlerMap.get(wsCarrier.getService()).construct(
					(UserService) serviceMap.get(WebSocketData.USER_SERVICE),
					wsCarrier, serviceMap.get(wsCarrier.getService()), serviceMap.get(WebSocketData.POST_SERVICE));//The Post Service at the end is added specifically for Rating Service
			NotificationService notifyServic = (NotificationService) serviceMap.get(WebSocketData.NOTIFY_SERVICE);
			if (handler.getReturningObject() != null) {
				
				WSCarrier wsMsg = handler.getReturningObject();
				webSocketConfig.getWebSocketUtil().send(ctx, wsMsg);
				
				if(wsMsg.getAlsoSendToUserId() != null) {
					WSCarrier alsoWs = wsMsg.getAlsoUsersWs() == null ? wsMsg : wsMsg.getAlsoUsersWs();
					if(webSocketConfig.getWebSocketUtil().getActiveUserMap().containsKey(wsMsg.getAlsoSendToUserId())) {
						webSocketConfig.getWebSocketUtil().send(webSocketConfig.getWebSocketUtil().getActiveUserMap().get(wsMsg.getAlsoSendToUserId()), alsoWs);
					}else if(wsMsg.getItem().equals(WebSocketData.ITEM_MESSAGE)){
						if(handler.getNotifications() != null && handler.getNotifications().size() > 0 && handler.getNotifications().get(0).getNotificationType() == NotificationType.MESSAGE) {
							notifyServic.saveNotifications(handler.getNotifications());
						}
					}
					
					if(wsMsg.getItem().equals(WebSocketData.ITEM_UPDATE_MESSAGE)) {
						Message msg = handler.getObjectMapper().readValue(wsMsg.getContent(), Message.class);
						msg.setMsgStatus(MessageStatus.RECEIVED);
						webSocketConfig.getMessageService().saveMessage(msg);
						WSCarrier wsCar = new WSCarrier();
						wsCar.setContent(handler.getObjectMapper().writeValueAsString(msg));
						wsCar.setItem(WebSocketData.ITEM_UPDATE_MESSAGE);
						webSocketConfig.getWebSocketUtil().send(ctx, wsCar);
					}
				}
				
				/*if(handler.getMessage() != null) {
					Message msg = handler.getMessage();
					if(webSocketConfig.getWebSocketUtil().getActiveUserMap().containsKey(msg.getMessageTo().getId())) {
						webSocketConfig.getWebSocketUtil().send(webSocketConfig.getWebSocketUtil().getActiveUserMap().get(msg.getMessageTo().getId()), handler.getReturningObject());
					}else {
						if(handler.getNotifications() != null && handler.getNotifications().size() > 0 && handler.getNotifications().get(0).getNotificationType() == NotificationType.MESSAGE) {
							notifyServic.saveNotifications(handler.getNotifications());
						}
					}
					
					
					
					msg = null;
					handler.sendMessage(null);
				}*/
			}

			if (handler.getNotifications() != null && handler.getNotifications().size() > 0) {
				List<String> extIds = new ArrayList<String>();
				List<Notification> notifications = handler.getNotifications();
				for (Notification notif : notifications) {
					extIds.add(notif.getNotificationTo().getId().toString());
				}
				if (extIds.size() > 0 && handler.getReturningObject() != null) {
					OneSignal oneSignal = OneSignal.from(notifications.get(0),
							handler.getReturningObject().getContent(), handler.getReturningObject().getItem());
					oneSignal.setInclude_external_user_ids(extIds);
					boolean important = (handler.getReturningObject().getItem()
							.equalsIgnoreCase(WebSocketData.ITEM_MESSAGE));
					webSocketConfig.getOneSignalUtil().sendNotificaton(oneSignal, important);
				}
				if(notifications != null && notifications.size() > 0 && notifications.get(0).getNotificationType() != NotificationType.MESSAGE && notifications.get(0).getNotificationTo().getId() != authenticatedUser.getId()) {
					notifications = notifyServic.saveNotifications(notifications);
				}
				for (Notification notification : notifications) {
					if(notification.getNotificationType() == NotificationType.MESSAGE)continue;
					webSocketConfig.getWebSocketUtil().sendNotification(notification.getNotificationTo().getId(),
							notification);
				}
			}
		}
		/*
		 * String service = wsCarrier.getService(); if
		 * (service.equalsIgnoreCase(WebSocketData.USER_SERVICE)) { UserServiceHandler
		 * userHandler = new UserServiceHandler(webSocketConfig.getUserService(),
		 * wsCarrier); if (userHandler.getReturningObject() != null) {
		 * webSocketConfig.getWebSocketUtil().send(ctx,
		 * userHandler.getReturningObject()); } }
		 */
	}

	private void sendUnsupported(ChannelHandlerContext ctx) {
		HttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE,
				ctx.channel().alloc().buffer(0));
		HttpUtil.setContentLength(res, 0);
		ctx.channel().writeAndFlush(res, ctx.channel().newPromise());
	}

	private WSCarrier getWSCarrier(String json) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		WSCarrier wsCarrier = mapper.readValue(json, WSCarrier.class);
		return wsCarrier;
	}

	private void fillServiceMap() {
		serviceMap.put(WebSocketData.USER_SERVICE, webSocketConfig.getUserService());
		serviceMap.put(WebSocketData.POST_SERVICE, webSocketConfig.getPostService());
		serviceMap.put(WebSocketData.PLAN_SERVICE, webSocketConfig.getPlanService());
		serviceMap.put(WebSocketData.COMMENT_SERVICE, webSocketConfig.getCommentService());
		serviceMap.put(WebSocketData.FOLLOW_SERVICE, webSocketConfig.getFollowService());
		serviceMap.put(WebSocketData.LIKE_SERVICE, webSocketConfig.getLikeService());
		serviceMap.put(WebSocketData.MSG_SERVICE, webSocketConfig.getMessageService());
		serviceMap.put(WebSocketData.NOTIFY_SERVICE, webSocketConfig.getNotificationService());
		serviceMap.put(WebSocketData.RATE_SERVICE, webSocketConfig.getRatingService());
		serviceMap.put(WebSocketData.REVIEW_SERVICE, webSocketConfig.getReviewService());
		serviceMap.put(WebSocketData.SPONSOR_SERVICE, webSocketConfig.getSponsorService());

	}

	private void fillHandlerMap() {
		handlerMap.put(WebSocketData.USER_SERVICE, new UserServiceHandler());
		handlerMap.put(WebSocketData.POST_SERVICE, new PostServiceHanlder());
		handlerMap.put(WebSocketData.PLAN_SERVICE, new PlanServiceHandler());
		handlerMap.put(WebSocketData.COMMENT_SERVICE, new CommentServiceHandler());
		handlerMap.put(WebSocketData.FOLLOW_SERVICE, new FollowServiceHandler());
		handlerMap.put(WebSocketData.LIKE_SERVICE, new LikeServiceHandler());
		handlerMap.put(WebSocketData.MSG_SERVICE, new MessageServiceHandler());
		//handlerMap.put(WebSocketData.NOTIFY_SERVICE, new NotificationS());
		handlerMap.put(WebSocketData.RATE_SERVICE, new RatingServiceHandler());
		handlerMap.put(WebSocketData.REVIEW_SERVICE, new ReviewServiceHandler());
		handlerMap.put(WebSocketData.SPONSOR_SERVICE, new SponsorServiceHandler());
	}

}
