package com.travelbud.apis;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.NotReadablePropertyException;

import com.travelbud.entities.Sponsor;

public interface SponsorAPI {
	public Sponsor saveSponsor(Sponsor sponsor) throws NotReadablePropertyException, AccessDeniedException;
	public List<Sponsor> getSponsors(int page) throws AccessDeniedException;
	public Sponsor getSponsor(long id);
}
