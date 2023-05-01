package com.travelbud.controllers;

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

import com.travelbud.entities.DonationDetails;
import com.travelbud.services.DonationService;

@RestController
@RequestMapping("/donations")
public class DonationControllerImpl implements DonationController {

	@Autowired
	private DonationService donationService;
	
	@PostMapping("")
	@Override
	public boolean saveDontaion(@RequestBody List<DonationDetails> donations) throws NotReadablePropertyException, AccessDeniedException {
		return donationService.saveDontaion(donations);
	}

	
	@GetMapping("/{userId}")
	@Override
	public List<DonationDetails> getDontaionDetails(@PathVariable long userId) {
		return donationService.getDontaionDetails(userId);
	}

	@DeleteMapping("/{id}")
	@Override
	public boolean deleteDonationDetail(@PathVariable long id) throws NotReadablePropertyException, AccessDeniedException {
		return donationService.deleteDonationDetail(id);
	}

}
