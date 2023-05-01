package com.travelbud.errors;

public class PlanErrorMessages {
	public static String ALREADY_REJECTED = "You request have already been rejected by the owner.";
	public static String ALREADY_COWANDERER= CommonErrorMessages.DUPLICATE_ERROR + "You are already a part of the plan";
	public static String ALREADY_CONFIRMED_COWANDERER= CommonErrorMessages.DUPLICATE_ERROR + "You have already confirmed as a cowanderer";
	public static String DUPLICATE_JOIN_REQUEST = CommonErrorMessages.DUPLICATE_ERROR + "recieved. A request is already in place";
	public static String CANNOT_WITHDRAW = CommonErrorMessages.DUPLICATE_ERROR + "Cowanderes cannot withdraw. Try exit plan";
	public static String NO_COWAN_RECORD = "User not found in cowanderers.";
	public static String NO_JOIN_REQUEST = "No join request was found.";

}
