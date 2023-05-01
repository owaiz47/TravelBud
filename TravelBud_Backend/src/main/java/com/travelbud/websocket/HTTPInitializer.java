package com.travelbud.websocket;

import com.travelbud.websocket.config.WebSocketConfig;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HTTPInitializer extends ChannelInitializer<SocketChannel>  {
	
	private WebSocketConfig webSocketConfig;
	
	public HTTPInitializer(WebSocketConfig webSocketConfig) {
		this.webSocketConfig = webSocketConfig;
	}
	
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline pipeline = socketChannel.pipeline();
		pipeline.addLast("httpServerCodec", new HttpServerCodec());
		pipeline.addLast("httpHandler", new HttpServerHandler(webSocketConfig));
		pipeline.addLast(new HttpObjectAggregator(65536));
	}
}
