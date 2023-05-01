package com.travelbud.controllers;

import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.travelbud.entities.Plan;
import com.travelbud.services.PlanService;

@RestController
@RequestMapping("/plans")
public class PlanControllerImpl implements PlanController {

	@Autowired
	private PlanService planService;
	
	@PostMapping("")
	@Override
	public Plan savePlan(@RequestBody Plan plan) throws NotReadablePropertyException, AccessDeniedException {
		return planService.savePlan(plan);
	}
	
	@GetMapping("/{id}")
	@Override
	public Plan getPlan(@PathVariable long id) {
		return planService.getPlan(id);
	}

	@GetMapping("/user/{userId}/page/{page}")
	@Override
	public List<Plan> getPlansOfUser(@PathVariable long userId, @PathVariable int page) {
		return planService.getPlansOfUser(userId, page);
	}

	@GetMapping("/page/{page}")
	@Override
	public List<Plan> getPlans(@PathVariable int page) {
		return planService.getPlans(page);
	}

	@GetMapping("/share/{id}")
	@Override
	public String getPlanUrl(@PathVariable long id) throws JsonMappingException, JsonProcessingException, UniformInterfaceException, ClientHandlerException, UnsupportedEncodingException {
		return planService.getPlanUrl(id);
	}

	@GetMapping("/join/{id}")
	@Override
	public Plan requestToJoin(@PathVariable long id) throws Exception{
		return planService.requestToJoin(id);
	}

	@GetMapping("/withdraw/{id}")
	@Override
	public Plan withdrawRequest(@PathVariable long id) throws Exception {
		return planService.withdrawRequest(id);
	}

	@GetMapping("/{id}/remove/{userId}")
	@Override
	public Plan removeJoinedUser(@PathVariable long id, @PathVariable long userId) throws Exception {
		return planService.removeJoinedUser(id, userId);
	}

	@GetMapping("/exit/{id}")
	@Override
	public Plan exitPlan(@PathVariable long id) throws Exception {
		return planService.exitPlan(id);
	}

	@GetMapping("/{id}/reject_exit/{userId}")
	@Override
	public Plan rejectExitPlan(@PathVariable long id, @PathVariable long userId) throws Exception {
		return planService.rejectExitPlan(id, userId);
	}

	@GetMapping("/{id}/accept_join/{userId}")
	@Override
	public Plan acceptJoinPlan(@PathVariable long id, @PathVariable long userId) throws Exception {
		return planService.acceptJoinPlan(id, userId);
	}

	@GetMapping("/{id}/reject_join/{userId}")
	@Override
	public Plan rejectJoinPlan(@PathVariable long id, @PathVariable long userId) throws Exception {
		return planService.rejectJoinPlan(id, userId);
	}

	@GetMapping("/trend")
	@Override
	public List<Plan> getTrendingPlans() {
		return planService.getTrendingPlans();
	}

	@GetMapping("/match/{name}")
	@Override
	public List<Plan> getPlansByName(@PathVariable String name) {
		return planService.getPlansByName(name);
	}

	@GetMapping("/my_plans/match/{name}/{completed}")
	@Override
	public List<Plan> getMyPlansByName(@PathVariable String name, @PathVariable Boolean completed) throws AccessDeniedException {
		return planService.getMyPlansByName(name, completed);
	}

	@GetMapping("/accept_co/{id}")
	@Override
	public Plan acceptCowan(@PathVariable long id) throws Exception {
		return planService.acceptCowan(id);
	}

	@GetMapping("/reject_co/{id}")
	@Override
	public Plan rejectCowan(@PathVariable long id) throws Exception {
		return planService.rejectCowan(id);
	}

	@DeleteMapping("/")
	@Override
	public boolean deletePlan(@RequestBody Plan plan) throws Exception {
		return planService.deletePlan(plan);
	}

	@GetMapping("/requests")
	@Override
	public List<Plan> getRequestedPlans() throws Exception {
		return planService.getRequestedPlans();
	}

}
