package com.travelbud.websocket.handlers;

import com.travelbud.dto.WSCarrier;
import com.travelbud.services.UserService;

public interface HandlerAPI {
	public AbstractServiceHandler construct(UserService userService, WSCarrier wsCarrier, Object... services) throws Exception;
	public void continueProcessing(WSCarrier wsCarrier) throws Exception;
}
