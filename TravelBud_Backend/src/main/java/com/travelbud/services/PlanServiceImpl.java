
package com.travelbud.services;

import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.travelbud.dao.JoinedUserDao;
import com.travelbud.dao.PlanDao;
import com.travelbud.dao.RequestedUserDao;
import com.travelbud.dto.ItemType;
import com.travelbud.entities.JoinedUser;
import com.travelbud.entities.Place;
import com.travelbud.entities.Plan;
import com.travelbud.entities.Post;
import com.travelbud.entities.RequestedUser;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;
import com.travelbud.errors.PlanErrorMessages;
import com.travelbud.utils.FirebaseUtil;

import javassist.bytecode.DuplicateMemberException;

@Service
public class PlanServiceImpl implements PlanService {

	@Autowired
	private PlanDao planDao;

	@Autowired
	private JoinedUserDao joinedUserDao;

	@Autowired
	private RequestedUserDao requestedUserDao;

	@Autowired
	private UserService userService;

	@Autowired
	private FirebaseUtil firebaseUtil;

	@Autowired
	private PlaceService placeService;
	
	private final static int MAX_PLANS_LENGHT = 20;

	@Override
	public Plan savePlan(Plan plan) throws NotReadablePropertyException, AccessDeniedException {
		checkPlanAccessibility(plan);
		 
		Plan savedPlan = planDao.save(plan);
		for(Place place : plan.getVisitingPlaces()) {
			place.setPlan(savedPlan);
		}
		List<Place> visiting = placeService.savePlaces(plan.getVisitingPlaces());
		savedPlan.setVisitingPlaces(visiting);
		
		if (plan.getJoinedBy() != null) {
			for (JoinedUser j : plan.getJoinedBy()) {
				j.setPlan(savedPlan);
			}
			List<JoinedUser> joined = joinedUserDao.saveAllAndFlush(plan.getJoinedBy());
			savedPlan.setJoinedBy(new HashSet<JoinedUser>(joined));
		}
		if (plan.getJoinRequests() != null) {
			for (RequestedUser r : plan.getJoinRequests()) {
				r.setPlan(savedPlan);
			}
			List<RequestedUser> joined = requestedUserDao.saveAllAndFlush(plan.getJoinRequests());
			savedPlan.setJoinRequests(new HashSet<RequestedUser>(joined));
		}
		return savedPlan;
	}

	@Override
	public Plan getPlan(long id) {
		Optional<Plan> plan = planDao.findById(id);
		if (plan.isPresent()) {
			return plan.get();
		} else {
			throw new EntityNotFoundException(CommonErrorMessages.NO_RECORD_WITH_ID + id);
		}
	}

	@Override
	public List<Plan> getPlansOfUser(long userId, int page) {
		return planDao.getPlansOfUser(userId, PageRequest.of(page, MAX_PLANS_LENGHT, Sort.by("id").descending()));
	}

	@Override
	public List<Plan> getPlans(int page) {
		Page<Plan> plans = planDao.findAll(PageRequest.of(page, MAX_PLANS_LENGHT, Sort.by("id").descending()));
		return plans.getContent();
	}

	@Override
	public String getPlanUrl(long id) throws JsonMappingException, JsonProcessingException, UniformInterfaceException,
			ClientHandlerException, UnsupportedEncodingException {
		Plan plan = getPlan(id);
		if (plan != null && plan.getPlanURL() != null && !plan.getPlanURL().isEmpty()) {
			return "{\"url\":\""+ plan.getPlanURL() + "\"}";
		}
		String link = firebaseUtil.createDynamicLink(plan.getId(), ItemType.PLAN);
		plan.setPlanURL(link);
		planDao.save(plan);
		return "{\"url\":\""+ link + "\"}";
	}

	@Override
	public Plan requestToJoin(long id) throws Exception {
		User currentUser = userService.getAuthenticatedUser();
		JoinedUser joinedUser = joinedUserDao.findByUserIdAndPlanId(currentUser.getId(), id);
		if (joinedUser != null) {
			throw new DuplicateMemberException(PlanErrorMessages.ALREADY_COWANDERER);
		}
		RequestedUser reqUser = requestedUserDao.findByUserIdAndPlanId(currentUser.getId(), id);
		if (reqUser != null) {
			if (reqUser.isRejected()) {
				throw new Exception(PlanErrorMessages.ALREADY_REJECTED);
			}
			throw new DuplicateMemberException(PlanErrorMessages.DUPLICATE_JOIN_REQUEST);
		}
		RequestedUser requestedUser = new RequestedUser();
		requestedUser.setUser(currentUser);
		requestedUser.setPlan(new Plan(id, null, null));
		requestedUserDao.save(requestedUser);
		return getPlan(id);
	}

	@Override
	public Plan withdrawRequest(long id) throws Exception {
		User currentUser = userService.getAuthenticatedUser();
		JoinedUser joinedUser = joinedUserDao.findByUserIdAndPlanId(currentUser.getId(), id);
		if (joinedUser != null) {
			throw new DuplicateMemberException(PlanErrorMessages.CANNOT_WITHDRAW);
		}
		RequestedUser reqUser = requestedUserDao.findByUserIdAndPlanId(currentUser.getId(), id);
		if (reqUser != null) {
			if (reqUser.isRejected()) {
				throw new Exception(PlanErrorMessages.ALREADY_REJECTED);
			}
			requestedUserDao.delete(reqUser);
		} else {
			throw new EntityNotFoundException(PlanErrorMessages.NO_JOIN_REQUEST);
		}
		return getPlan(id);
	}

	@Override
	public Plan removeJoinedUser(long id, long userId) throws Exception {
		Plan plan = getPlan(id);
		checkPlanAccessibility(plan);
		JoinedUser joinedUser = joinedUserDao.findByUserIdAndPlanId(userId, id);
		if (joinedUser == null) {
			throw new EntityNotFoundException(PlanErrorMessages.NO_COWAN_RECORD);
		}
		joinedUserDao.delete(joinedUser);
		plan.getJoinedBy().remove(joinedUser);
		return plan;
	}

	@Override
	public Plan exitPlan(long id) throws Exception {
		User currentUser = userService.getAuthenticatedUser();
		JoinedUser joinedUser = joinedUserDao.findByUserIdAndPlanId(currentUser.getId(), id);
		if (joinedUser == null) {
			throw new EntityNotFoundException(PlanErrorMessages.NO_COWAN_RECORD);
		}
		joinedUser.setPlanExitReq(true);
		joinedUserDao.saveAndFlush(joinedUser);
		return getPlan(id);
	}

	@Override
	public Plan rejectExitPlan(long id, long userId) throws NotReadablePropertyException, AccessDeniedException {
		checkPlanAccessibility(getPlan(id));
		JoinedUser joinedUser = joinedUserDao.findByUserIdAndPlanId(userId, id);
		if (joinedUser == null) {
			throw new EntityNotFoundException(PlanErrorMessages.NO_COWAN_RECORD);
		}
		joinedUser.setPlanExitReq(false);
		joinedUserDao.saveAndFlush(joinedUser);
		return getPlan(id);
	}

	@Override
	public Plan acceptJoinPlan(long id, long userId) throws Exception {
		Plan plan = getPlan(id);
		checkPlanAccessibility(plan);
		JoinedUser joinedUser = joinedUserDao.findByUserIdAndPlanId(userId, id);
		if (joinedUser != null) {
			throw new DuplicateMemberException(PlanErrorMessages.ALREADY_COWANDERER);
		}
		RequestedUser reqUser = requestedUserDao.findByUserIdAndPlanId(userId, id);
		if (reqUser == null) {
			throw new EntityNotFoundException(PlanErrorMessages.NO_JOIN_REQUEST);
		}
		JoinedUser join = new JoinedUser();
		join.setUser(reqUser.getUser());
		join.setPlan(reqUser.getPlan());
		join.setPlanConfirmed(true);

		joinedUserDao.saveAndFlush(join);
		
		reqUser.setAccepted(true);
		requestedUserDao.save(reqUser);
		return getPlan(id);
	}

	@Override
	public Plan rejectJoinPlan(long id, long userId) throws Exception {
		checkPlanAccessibility(getPlan(id));
		JoinedUser joinedUser = joinedUserDao.findByUserIdAndPlanId(userId, id);
		if (joinedUser != null) {
			throw new DuplicateMemberException(PlanErrorMessages.ALREADY_COWANDERER);
		}
		RequestedUser reqUser = requestedUserDao.findByUserIdAndPlanId(userId, id);
		if (reqUser == null) {
			throw new EntityNotFoundException(PlanErrorMessages.NO_JOIN_REQUEST);
		}
		reqUser.setRejected(true);
		requestedUserDao.saveAndFlush(reqUser);

		return getPlan(id);
	}

	@Override
	public List<Plan> getTrendingPlans() {
		return planDao.getTrendingPlans();
	}

	@Override
	public List<Plan> getPlansByName(String name) {
		List<Plan> plans = planDao.getPlanMatching(name);
		return plans;
	}

	@Override
	public List<Plan> getMyPlansByName(String name, Boolean completed) throws AccessDeniedException {
		User user = userService.getAuthenticatedUser();
		if (completed) {
			return planDao.geMyCompletedPlansMatching(user.getId(), name);
		} else {
			return planDao.geMyPlansMatching(user.getId(), name);
		}
	}

	@Override
	public Plan acceptCowan(long id) throws Exception {
		User currentUser = userService.getAuthenticatedUser();
		JoinedUser joinedUser = joinedUserDao.findByUserIdAndPlanId(currentUser.getId(), id);
		if (joinedUser == null) {
			throw new EntityNotFoundException(PlanErrorMessages.NO_COWAN_RECORD);
		}
		if (joinedUser != null && joinedUser.isPlanConfirmed()) {
			throw new DuplicateMemberException(PlanErrorMessages.ALREADY_CONFIRMED_COWANDERER);
		}

		joinedUser.setPlanConfirmed(true);
		joinedUserDao.save(joinedUser);

		return getPlan(id);
	}

	@Override
	public Plan rejectCowan(long id) throws Exception {
		User currentUser = userService.getAuthenticatedUser();
		JoinedUser joinedUser = joinedUserDao.findByUserIdAndPlanId(currentUser.getId(), id);
		if (joinedUser == null) {
			throw new EntityNotFoundException(PlanErrorMessages.NO_COWAN_RECORD);
		}
		joinedUserDao.delete(joinedUser);

		return getPlan(id);
	}

	@Override
	public boolean deletePlan(Plan plan) throws NotReadablePropertyException, AccessDeniedException {
		checkPlanAccessibility(plan);
		plan = getPlan(plan.getId());
		if (plan != null) {
			plan.setDeleted(true);
			planDao.save(plan);
		}
		return true;
	}

	@Override
	public List<Plan> getRequestedPlans() throws Exception {
		User currentUser = userService.getAuthenticatedUser();
		List<Long> planIds = joinedUserDao.getRequestPlanIdOfUser(currentUser.getId());
		List<Plan> plans = planDao.findAllById(planIds);
		return plans;
	}

	private void checkPlanAccessibility(Plan plan) throws AccessDeniedException, NotReadablePropertyException {
		User currentUser = userService.getAuthenticatedUser();
		if (plan == null) {
			throw new NotReadablePropertyException(Post.class, CommonErrorMessages.NULL_ERROR);
		} else if (plan.getPlanBy().getId() == null || plan.getPlanBy().getId() != currentUser.getId()) {
			throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
		}
	}

}
