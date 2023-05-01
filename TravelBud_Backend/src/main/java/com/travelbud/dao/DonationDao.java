package com.travelbud.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.DonationDetails;

@Repository
public interface DonationDao extends JpaRepository<DonationDetails, Long>{
	public List<DonationDetails> findByUserId(long userId);
}
