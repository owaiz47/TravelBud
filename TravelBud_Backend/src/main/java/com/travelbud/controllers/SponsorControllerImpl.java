package com.travelbud.controllers;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.travelbud.entities.Sponsor;
import com.travelbud.services.SponsorService;

@RestController
@RequestMapping("/sponsor")
public class SponsorControllerImpl implements SponsorController {

	@Autowired
	private SponsorService sponsorService;
	
	@PostMapping("")
	@Override
	public Sponsor saveSponsor(@RequestBody Sponsor sponsor) throws NotReadablePropertyException, AccessDeniedException {
		return sponsorService.saveSponsor(sponsor);
	}

	@GetMapping("/page/{page}")
	@Override
	public List<Sponsor> getSponsors(@PathVariable int page) throws AccessDeniedException {
		return sponsorService.getSponsors(page);
	}

	@GetMapping("/{id}")
	@Override
	public Sponsor getSponsor(@PathVariable long id) {
		return sponsorService.getSponsor(id);
	}

}
