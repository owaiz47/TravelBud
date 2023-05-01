package com.travelbud.apis;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.NotReadablePropertyException;

import com.travelbud.entities.Like;

public interface LikeAPI {
	public boolean saveLike(Like like) throws AccessDeniedException;
	public boolean deleteLike(Like like) throws NotReadablePropertyException, AccessDeniedException;
}
