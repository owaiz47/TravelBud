package com.travelbud.websocket.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelbud.dto.WSCarrier;
import com.travelbud.entities.Notification;
import com.travelbud.websocket.helper.WebSocketData;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@Component
@Scope("singleton")
public class WebSocketUtil {

	private Map<Long, ChannelHandlerContext> activeUserMap = new HashMap<Long, ChannelHandlerContext>();

	public Map<Long, ChannelHandlerContext> getActiveUserMap() {
		return activeUserMap;
	}

	public void setActiveUserMap(Map<Long, ChannelHandlerContext> activeUserMap) {
		this.activeUserMap = activeUserMap;
	}
	
	public void send(ChannelHandlerContext ctx, WSCarrier wsCarrier) throws JsonProcessingException {
		ObjectMapper obj = new ObjectMapper();  
        String jsonStr = obj.writeValueAsString(wsCarrier);
		ctx.channel().writeAndFlush(new TextWebSocketFrame(jsonStr));
	}

	public void sendNotification(long userId, Notification notification) throws JsonProcessingException {
		if(this.activeUserMap.containsKey(userId)) {
			ObjectMapper obj = new ObjectMapper();
			String json = obj.writeValueAsString(notification);
			WSCarrier ws = new WSCarrier();
			ws.setItem(WebSocketData.ITEM_NOTIFICATION);
			ws.setContent(json);
			send(this.activeUserMap.get(userId), ws);
		}
	}
	
}
