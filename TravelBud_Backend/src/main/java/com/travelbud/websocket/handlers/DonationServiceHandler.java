package com.travelbud.websocket.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelbud.dto.WSCarrier;
import com.travelbud.services.DonationService;
import com.travelbud.services.UserService;

public class DonationServiceHandler extends AbstractServiceHandler implements HandlerAPI{

	private Logger log = LoggerFactory.getLogger(DonationServiceHandler.class);

	private DonationService donationService;
	
	@Override
	public AbstractServiceHandler construct(UserService userService, WSCarrier wsCarrier, Object...services)
			throws Exception {
		setUserService(userService);
		this.donationService = (DonationService) services[0];
		setWsCarrier(wsCarrier);
		log.info("Initialized DonationServiceHandler");
		continueProcessing(wsCarrier);
		return this;
	}

	@Override
	public void continueProcessing(WSCarrier wsCarrier) throws Exception {
		String name = wsCarrier.getName();
		
	}

}
