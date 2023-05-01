package com.travelbud.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelbud.dao.PlaceDao;
import com.travelbud.entities.Place;

@Service
public class PlaceServiceImpl implements PlaceService {
	
	@Autowired
	private PlaceDao placeDao;

	@Override
	public List<Place> savePlaces(List<Place> places) {
		return placeDao.saveAllAndFlush(places);
	}

}
