package com.travelbud.websocket;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelbud.websocket.config.WebSocketConfig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Service
public class WebSocketService {
	private Logger log = LoggerFactory.getLogger(WebSocketService.class);
	private static final int PORT = 8888;
	private EventLoopGroup masterGroup;
	private EventLoopGroup slaveGroup;
	private Thread thread;	
	
	@Autowired
	private WebSocketConfig webSocketConfig;
	
	
	@PostConstruct
	public void init() {
		Runnable runnable = new Runnable() {
			public void run() {
				masterGroup = new NioEventLoopGroup(1);
				slaveGroup = new NioEventLoopGroup();
				try {
					ServerBootstrap b = new ServerBootstrap();
					b.option(ChannelOption.SO_BACKLOG, 1024);
					b.group(masterGroup, slaveGroup).channel(NioServerSocketChannel.class)
							.handler(new LoggingHandler(LogLevel.INFO)).childHandler(
									new HTTPInitializer(webSocketConfig));

					Channel ch = b.bind(PORT).sync().channel();
					log.info("Starting Web Sokcet");
					ch.closeFuture().sync();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					masterGroup.shutdownGracefully();
					slaveGroup.shutdownGracefully();
				}
			}
		};
		thread = new Thread(runnable);
		thread.start();
	}

	@PreDestroy
	public void destroy(){
		if(masterGroup != null){
			masterGroup.shutdownGracefully();
		}
		if(slaveGroup != null){
			slaveGroup.shutdownGracefully();
		}
		if(thread != null){
			thread.interrupt();
		}
	}
}
