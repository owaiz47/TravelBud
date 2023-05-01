package com.travelbud.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.travelbud.entities.Place;

@Service
public interface PlaceService {
	List<Place> savePlaces(List<Place> places);
}
