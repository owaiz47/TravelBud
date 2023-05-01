package com.travelbud.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.Sponsor;

@Repository
public interface SponsorDao extends JpaRepository<Sponsor, Long> {
	public List<Sponsor> findAllBySponsoredToId(long sponsorToId, Pageable pageable);
}
