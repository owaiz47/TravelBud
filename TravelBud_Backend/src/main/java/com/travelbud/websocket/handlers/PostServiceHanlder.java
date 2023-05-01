package com.travelbud.websocket.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelbud.dto.WSCarrier;
import com.travelbud.services.PostService;
import com.travelbud.services.UserService;

public class PostServiceHanlder extends AbstractServiceHandler implements HandlerAPI {

	private Logger log = LoggerFactory.getLogger(PostServiceHanlder.class);

	private PostService postService;

	public PostServiceHanlder() {
	}

	@Override
	public AbstractServiceHandler construct(UserService userService, WSCarrier wsCarrier, Object... services) throws Exception {
		this.postService = (PostService) services[0];
		setUserService(userService);
		setWsCarrier(wsCarrier);
		log.info("Initialized PostServiceHandler");
		continueProcessing(wsCarrier);
		return this;
	}

	@Override
	public void continueProcessing(WSCarrier wsCarrier) {
		// No methods for now
	}
}
