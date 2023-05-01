package com.travelbud.apis;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.NotReadablePropertyException;

import com.travelbud.entities.Rating;

public interface RatingAPI {
	public boolean saveRating(Rating rating) throws NotReadablePropertyException, AccessDeniedException;
}
