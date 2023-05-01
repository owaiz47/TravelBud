package com.travelbud.websocket.handlers;

import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.NotReadablePropertyException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.travelbud.apis.PlanAPI;
import com.travelbud.dto.NotificationType;
import com.travelbud.dto.WSCarrier;
import com.travelbud.entities.Notification;
import com.travelbud.entities.Plan;
import com.travelbud.services.PlanService;
import com.travelbud.services.UserService;
import com.travelbud.websocket.helper.WSHelper;
import com.travelbud.websocket.helper.WebSocketData;

public class PlanServiceHandler extends AbstractServiceHandler implements HandlerAPI, PlanAPI {

	private Logger log = LoggerFactory.getLogger(PlanServiceHandler.class);

	private PlanService planService;

	public PlanServiceHandler() {
	}

	@Override
	public AbstractServiceHandler construct(UserService userService, WSCarrier wsCarrier, Object... services) throws Exception {
		this.planService = (PlanService) services[0];
		setUserService(userService);
		setWsCarrier(wsCarrier);
		log.info("Initialized PlanServiceHandler ");
		continueProcessing(wsCarrier);
		return this;
	}

	@Override
	public void continueProcessing(WSCarrier wsCarrier) throws Exception {
		log.info("Processing the request " + wsCarrier.getName());
		String name = wsCarrier.getName();
		if (name.equalsIgnoreCase(WebSocketData.PLAN_ACCEPT_COWAN_METHOD)) {
			WSHelper wsPlan = getPlanHelper(wsCarrier.getContent());
			acceptCowan(wsPlan.getReferenceId());
		} else if (name.equalsIgnoreCase(WebSocketData.PLAN_REJECT_COWAN_METHOD)) {
			WSHelper wsPlan = getPlanHelper(wsCarrier.getContent());
			rejectCowan(wsPlan.getReferenceId());
		} else if (name.equalsIgnoreCase(WebSocketData.PLAN_JOIN_REQ_METHOD)) {
			WSHelper wsPlan = getPlanHelper(wsCarrier.getContent());
			requestToJoin(wsPlan.getReferenceId());
		} else if (name.equalsIgnoreCase(WebSocketData.PLAN_ACCEPT_JOIN_REQ_METHOD)) {
			WSHelper wsPlan = getPlanHelper(wsCarrier.getContent());
			acceptJoinPlan(wsPlan.getReferenceId(), wsPlan.getUserId());
		} else if (name.equalsIgnoreCase(WebSocketData.PLAN_REJECT_JOIN_REQ_METHOD)) {
			WSHelper wsPlan = getPlanHelper(wsCarrier.getContent());
			rejectJoinPlan(wsPlan.getReferenceId(), wsPlan.getUserId());
		} else if (name.equalsIgnoreCase(WebSocketData.PLAN_ADD_EXIT_REQ_METHOD)) {
			WSHelper wsPlan = getPlanHelper(wsCarrier.getContent());
			exitPlan(wsPlan.getReferenceId());
		} else if (name.equalsIgnoreCase(WebSocketData.PLAN_REMOVE_USER_METHOD)) {
			WSHelper wsPlan = getPlanHelper(wsCarrier.getContent());
			removeJoinedUser(wsPlan.getReferenceId(), wsPlan.getUserId());
		} else if (name.equalsIgnoreCase(WebSocketData.PLAN_REJECT_EXIT_REQ_METHOD)) {
			WSHelper wsPlan = getPlanHelper(wsCarrier.getContent());
			rejectExitPlan(wsPlan.getReferenceId(), wsPlan.getUserId());
		} else if (name.equalsIgnoreCase(WebSocketData.PLAN_CONFIRM_JOIN_METHOD)) {
			Plan plan = getPlanFromJson(wsCarrier.getContent());
			sendConfirmNotifications(plan);
		} else if (name.equalsIgnoreCase(WebSocketData.PLAN_WITHDRAW_REQUEST)) {
			WSHelper wsPlan = getPlanHelper(wsCarrier.getContent());
			withdrawRequest(wsPlan.getReferenceId());
		}

	}

	private void sendConfirmNotifications(Plan plan) {
		

	}

	private WSHelper getPlanHelper(String content) throws JsonMappingException, JsonProcessingException {
		return getObjectMapper().readValue(content, WSHelper.class);
	}

	private Plan getPlanFromJson(String content) throws JsonMappingException, JsonProcessingException {
		return getObjectMapper().readValue(content, Plan.class);
	}

	private String planToJson(Plan plan) throws JsonProcessingException {
		return getObjectMapper().writeValueAsString(plan);
	}

	@Override
	public Plan savePlan(Plan plan) throws AccessDeniedException, NotReadablePropertyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plan getPlan(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Plan> getPlansOfUser(long userId, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Plan> getPlans(int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlanUrl(long id) throws JsonMappingException, JsonProcessingException, UniformInterfaceException,
			ClientHandlerException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plan requestToJoin(long id) throws Exception {
		Plan plan = planService.requestToJoin(id);
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_PLAN);
		ws.setContent(planToJson(plan));
		ws.setAlsoSendToUserId(plan.getPlanBy().getId());
		
		Notification notification = new Notification();
		notification.setNotificationFrom(getUserService().getAuthenticatedUser());
		notification.setNotificationTo(plan.getPlanBy());
		notification.setReferenceId(plan.getId());
		notification.setNotificationType(NotificationType.PLAN_JOIN_REQ);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.add(notification);
		
		setReturns(ws, notifications);
		
		//setReturningObject(ws);
		return null;
	}

	@Override
	public Plan withdrawRequest(long id) throws Exception {
		Plan plan = planService.withdrawRequest(id);
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_PLAN);
		ws.setContent(planToJson(plan));
		ws.setAlsoSendToUserId(plan.getPlanBy().getId());
		
		setReturns(ws, null);
		return null;
	}

	@Override
	public Plan removeJoinedUser(long id, long userId) throws Exception {
		Plan plan = planService.removeJoinedUser(id, userId);
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_PLAN);
		ws.setContent(planToJson(plan));
		ws.setAlsoSendToUserId(userId);
		
		Notification notification = new Notification();
		notification.setNotificationFrom(getUserService().getAuthenticatedUser());
		notification.setNotificationTo(getUserService().getUserById(userId));
		notification.setReferenceId(plan.getId());
		notification.setNotificationType(NotificationType.PLAN_REMOVED_USER);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.add(notification);
		
		setReturns(ws, notifications);
		
		//setReturningObject(ws);
		return null;
	}

	@Override
	public Plan exitPlan(long id) throws Exception {
		Plan plan = planService.exitPlan(id);
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_PLAN);
		ws.setContent(planToJson(plan));
		ws.setAlsoSendToUserId(plan.getPlanBy().getId());

		Notification notification = new Notification();
		notification.setNotificationFrom(getUserService().getAuthenticatedUser());
		notification.setNotificationTo(plan.getPlanBy());
		notification.setReferenceId(plan.getId());
		notification.setNotificationType(NotificationType.PLAN_EXIT_REQ);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.add(notification);
		
		setReturns(ws, notifications);
		
		//setReturningObject(ws);
		return null;
	}

	@Override
	public Plan rejectExitPlan(long id, long userId) throws Exception {
		Plan plan = planService.rejectExitPlan(id, userId);
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_PLAN);
		ws.setContent(planToJson(plan));
		ws.setAlsoSendToUserId(userId);

		Notification notification = new Notification();
		notification.setNotificationFrom(getUserService().getAuthenticatedUser());
		notification.setNotificationTo(getUserService().getUserById(userId));
		notification.setReferenceId(plan.getId());
		notification.setNotificationType(NotificationType.PLAN_EXIT_RJCT);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.add(notification);
		
		setReturns(ws, notifications);
		
		//setReturningObject(ws);
		return null;
	}

	@Override
	public Plan acceptJoinPlan(long id, long userId) throws Exception {
		Plan plan = planService.acceptJoinPlan(id, userId);
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_PLAN);
		ws.setContent(planToJson(plan));
		ws.setAlsoSendToUserId(userId);

		Notification notification = new Notification();
		notification.setNotificationFrom(getUserService().getAuthenticatedUser());
		notification.setNotificationTo(getUserService().getUserById(userId));
		notification.setReferenceId(plan.getId());
		notification.setNotificationType(NotificationType.PLAN_JOIN_ACPT);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.add(notification);
		
		setReturns(ws, notifications);
		
		//setReturningObject(ws);
		return null;
	}

	@Override
	public Plan rejectJoinPlan(long id, long userId) throws Exception {
		Plan plan = planService.rejectJoinPlan(id, userId);
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_PLAN);
		ws.setContent(planToJson(plan));
		ws.setAlsoSendToUserId(userId);

		Notification notification = new Notification();
		notification.setNotificationFrom(getUserService().getAuthenticatedUser());
		notification.setNotificationTo(getUserService().getUserById(userId));
		notification.setReferenceId(plan.getId());
		notification.setNotificationType(NotificationType.PLAN_JOIN_RJCT);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.add(notification);
		
		setReturns(ws, notifications);
		
		//setReturningObject(ws);
		return null;
	}

	@Override
	public List<Plan> getTrendingPlans() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Plan> getPlansByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Plan> getMyPlansByName(String name, Boolean completed) throws AccessDeniedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plan acceptCowan(long id) throws Exception {
		planService.acceptCowan(id);
		Plan plan = planService.getPlan(id);
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_PLAN);
		ws.setContent(planToJson(plan));
		ws.setAlsoSendToUserId(plan.getPlanBy().getId());

		Notification notification = new Notification();
		notification.setNotificationFrom(getUserService().getAuthenticatedUser());
		notification.setNotificationTo(plan.getPlanBy());
		notification.setReferenceId(plan.getId());
		notification.setNotificationType(NotificationType.PLAN_ACCEPT_COWNA);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.add(notification);
		
		setReturns(ws, notifications);
		
		//setReturningObject(ws);
		return null;
	}

	@Override
	public Plan rejectCowan(long id) throws Exception {
		Plan plan = planService.rejectCowan(id);
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_PLAN);
		ws.setContent(planToJson(plan));
		ws.setAlsoSendToUserId(plan.getPlanBy().getId());

		Notification notification = new Notification();
		notification.setNotificationFrom(getUserService().getAuthenticatedUser());
		notification.setNotificationTo(plan.getPlanBy());
		notification.setReferenceId(plan.getId());
		notification.setNotificationType(NotificationType.PLAN_REJECT_COWNA);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.add(notification);
		
		setReturns(ws, notifications);
		
		//setReturningObject(ws);
		return null;
	}

	@Override
	public boolean deletePlan(Plan plan) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Plan> getRequestedPlans() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
