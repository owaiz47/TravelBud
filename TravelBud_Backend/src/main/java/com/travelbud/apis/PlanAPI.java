package com.travelbud.apis;

import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.NotReadablePropertyException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.travelbud.entities.Plan;

public interface PlanAPI {
	public Plan savePlan(Plan plan) throws AccessDeniedException, NotReadablePropertyException;
	public Plan getPlan(long id);
	public List<Plan> getPlansOfUser(long userId, int page);//get initial plans of user if id is null
	public List<Plan> getPlans(int page);//Same as above get initail plans if id is null
	public String getPlanUrl(long id) throws JsonMappingException, JsonProcessingException, UniformInterfaceException, ClientHandlerException, UnsupportedEncodingException;
	public Plan requestToJoin(long id) throws Exception;
	public Plan withdrawRequest(long id) throws Exception;
	public Plan removeJoinedUser(long id, long userId) throws Exception;	
	public Plan exitPlan(long id) throws Exception;
	public Plan rejectExitPlan(long id, long userId) throws Exception;
	public Plan acceptJoinPlan(long id, long userId) throws Exception;
	public Plan rejectJoinPlan(long id, long userId) throws Exception;
	public List<Plan> getTrendingPlans();
	public List<Plan> getPlansByName(String name);
	public List<Plan> getMyPlansByName(String name, Boolean completed) throws AccessDeniedException;
	public Plan acceptCowan(long id) throws Exception;
	public Plan rejectCowan(long id) throws Exception;
	public boolean deletePlan(Plan plan) throws Exception;
	public List<Plan> getRequestedPlans() throws Exception;
}
