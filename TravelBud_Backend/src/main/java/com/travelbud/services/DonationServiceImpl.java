package com.travelbud.services;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelbud.dao.DonationDao;
import com.travelbud.dto.DontaionAccountType;
import com.travelbud.entities.DonationDetails;
import com.travelbud.entities.Post;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;

@Service
public class DonationServiceImpl implements DonationService {

	@Autowired
	private DonationDao donationDao;

	@Autowired
	private UserService userService;

	@Override
	public boolean saveDontaion(List<DonationDetails> donations) throws NotReadablePropertyException, AccessDeniedException {
		
		for(DonationDetails d : donations) {
			if(d != null && d.getId() !=null && d.getId() > 0)continue;
			checkDonationAccessibility(d);
			donationDao.save(d);
			
		}
		return true;
	}

	@Override
	public List<DonationDetails> getDontaionDetails(long userId) {
		if(userId == 0) {
			return supportedTypes();
		}
		return donationDao.findByUserId(userId);
	}
	
	private List<DonationDetails> supportedTypes(){
		List<DonationDetails> supported = new ArrayList<>();
		
		for(DontaionAccountType type : DontaionAccountType.values()) {
			DonationDetails d = new DonationDetails();
			d.setDontaionAccountType(type);
			d.setImageUrl(d.getDontaionAccountType().getImage());
			supported.add(d);
		}
		return supported;
	}

	private void checkDonationAccessibility(DonationDetails donationDetails)
			throws AccessDeniedException, NotReadablePropertyException {
		User authUser = userService.getAuthenticatedUser();
		if (donationDetails == null) {
			throw new NotReadablePropertyException(Post.class, CommonErrorMessages.NULL_ERROR);
		} else if (donationDetails.getUser().getId() == null || donationDetails.getUser().getId() != authUser.getId()) {
			throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
		}
	}

	@Override
	public boolean deleteDonationDetail(long id) throws NotReadablePropertyException, AccessDeniedException {
		Optional<DonationDetails> donation = donationDao.findById(id);
		if (donation == null || !donation.isPresent()) {
			throw new EntityNotFoundException(CommonErrorMessages.NO_RECORD_WITH_ID);
		}
		checkDonationAccessibility(donation.get());
		donationDao.delete(donation.get());
		return true;
	}

}
