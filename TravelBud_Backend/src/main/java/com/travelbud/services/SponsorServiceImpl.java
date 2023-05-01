package com.travelbud.services;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.travelbud.dao.SponsorDao;
import com.travelbud.entities.Post;
import com.travelbud.entities.Sponsor;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;

@Service
public class SponsorServiceImpl implements SponsorService {

	@Autowired
	private SponsorDao sponsorDao;
	
	@Autowired
	private UserService userService;
	
	private final int MAX_SPONSOR_LENGTH = 50;
	
	@Override
	public Sponsor saveSponsor(Sponsor sponsor) throws NotReadablePropertyException, AccessDeniedException {
		checkSponsorAccessibility(sponsor);
		return sponsorDao.save(sponsor);
	}

	@Override
	public List<Sponsor> getSponsors(int page) throws AccessDeniedException {
		User authUser = userService.getAuthenticatedUser();
		return sponsorDao.findAllBySponsoredToId(authUser.getId(), PageRequest.of(page, MAX_SPONSOR_LENGTH, Sort.by("id").descending()));
	}

	@Override
	public Sponsor getSponsor(long id) {
		Optional<Sponsor> sponsor = sponsorDao.findById(id);
		if(sponsor == null || !sponsor.isPresent()) {
			throw new EntityNotFoundException(CommonErrorMessages.NO_RECORD_WITH_ID);
		}
		return sponsor.get();
	}

	private void checkSponsorAccessibility(Sponsor sponsor) throws AccessDeniedException, NotReadablePropertyException {
		User currentUser = userService.getAuthenticatedUser();
		if (sponsor == null) {
			throw new NotReadablePropertyException(Post.class, CommonErrorMessages.NULL_ERROR);
		} else if (sponsor.getSponsoredBy().getId() == null || sponsor.getSponsoredBy().getId() != currentUser.getId()) {
			throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
		}
	}
}
