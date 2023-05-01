package com.travelbud.websocket;

import java.nio.file.AccessDeniedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelbud.dto.WSCarrier;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;
import com.travelbud.websocket.config.WebSocketConfig;
import com.travelbud.websocket.helper.WebSocketData;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {
	
	private WebSocketConfig webSocketConfig;
	private User authenticatedUser;
	
	HttpServerHandler(WebSocketConfig webSocketConfig) {
		this.webSocketConfig = webSocketConfig;
	}

	WebSocketServerHandshaker handshaker;

	String clientId;
	private static Log log = LogFactory.getLog(HttpServerHandler.class);

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		webSocketConfig.getWebSocketUtil().getActiveUserMap().remove(authenticatedUser.getId());
		ctx.close();
		super.exceptionCaught(ctx, cause);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
			if (msg instanceof HttpRequest) {

				HttpRequest httpRequest = (HttpRequest) msg;

				log.info("Http Request Received");

				HttpHeaders headers = httpRequest.headers();

				authenticatedUser = authenticate(headers);
				if (authenticatedUser == null || authenticatedUser.getId() < 1) {
					sendUnauthenticated(ctx);
					return;
				}
				log.info("Authentication Successful. User " + authenticatedUser.getUsername() + " was authenticated");
				if ("Upgrade".equalsIgnoreCase(headers.get(HttpHeaderNames.CONNECTION))
						&& "WebSocket".equalsIgnoreCase(headers.get(HttpHeaderNames.UPGRADE))) {
					ctx.pipeline().replace(this, "websocketHandler",
							new WebSocketHandler(webSocketConfig, authenticatedUser));
					// Do the Handshake to upgrade connection from HTTP to
					// WebSocket
					// protocol
					handleHandshake(ctx, httpRequest);
					log.info("Handshake completed " + ctx.name());
					webSocketConfig.getWebSocketUtil().getActiveUserMap().put(this.authenticatedUser.getId(), ctx);
					log.info("Added user to the active members list " + this.authenticatedUser.getUsername());
					ObjectMapper obj = new ObjectMapper();  
					WSCarrier ws = new WSCarrier();
					ws.setName(WebSocketData.AUTHENTICATED_USER);
					ws.setContent(obj.writeValueAsString(this.authenticatedUser));
			        String jsonStr = obj.writeValueAsString(ws);
					ctx.channel().writeAndFlush(new TextWebSocketFrame(jsonStr));
					//ctx.fireChannelActive();
				}
			} else {
				log.error("Incoming request is unknown");
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
		}

	}
	
/*	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		WSCarrier ws = new WSCarrier();
		ws.setService("user_service");
		ws.setName(WebSocketData.AUTHENTICATED_USER);
		ObjectMapper Obj = new ObjectMapper();  
		//this.user = Obj.readValue(wsCarrier.getContent(), User.class);
        String jsonStr = Obj.writeValueAsString(this.authenticatedUser);
		ws.setContent(jsonStr);
		webSocketConfig.getWebSocketUtil().getActiveUserMap().put(this.authenticatedUser, ctx);
		webSocketConfig.getWebSocketUtil().send(ctx, ws);
	}*/

	private void sendUnauthenticated(ChannelHandlerContext ctx) {
		HttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED,
				ctx.channel().alloc().buffer(0));
		HttpUtil.setContentLength(res, 0);
		ctx.channel().writeAndFlush(res, ctx.channel().newPromise());
		ctx.close();
	}

	protected void handleHandshake(ChannelHandlerContext ctx, HttpRequest req) {
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketURL(req), null,
				true);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
			log.info("Handshake Failed");
		} else {
			handshaker.handshake(ctx.channel(), req);
			log.info("Handshake Completed");
		}
	}

	private User authenticate(HttpHeaders headers) {
		try {
			log.info("Authenticating......");
			/*if (!headers.contains(HttpHeaderNames.AUTHORIZATION)) {
				log.info("Websocket connection denied. No Authorization header provided");
				return null;
			}*/
			String header = headers.get("Authorization");
			//String header = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvd2FpejQ3IiwiZXhwIjoxNjIzODAxMDgzLCJpYXQiOjE2MjM3NjUwODN9.JxKQNIawCYjTOmdsziJBMZTPhB2CmBbuUHS-07EU-tE";
			webSocketConfig.getJwtFilter().authorize(header, null);
			User authenticatedUser = webSocketConfig.getUserService().getAuthenticatedUser();
			if(authenticatedUser != null && authenticatedUser.getId() > 0)return authenticatedUser;
			throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
		}
		catch (Exception e) {
			log.info("Error while authenticating " +e.getMessage());
			return null;
		}
	}

	protected String getWebSocketURL(HttpRequest req) {
		String url = "ws://" + req.headers().get("Host") + req.getUri();
		return url;
	}
}
