package com.travelbud.apis;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.NotReadablePropertyException;

import com.travelbud.entities.DonationDetails;

public interface DonationAPI {
	public boolean saveDontaion(List<DonationDetails> donations) throws NotReadablePropertyException, AccessDeniedException;
	public List<DonationDetails> getDontaionDetails(long userId);
	public boolean deleteDonationDetail(long id) throws NotReadablePropertyException, AccessDeniedException;
}
