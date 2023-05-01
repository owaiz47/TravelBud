package com.travelbud.apis;

import java.nio.file.AccessDeniedException;

import com.travelbud.entities.LastOnline;
import com.travelbud.entities.User;

public interface LastOnlineAPI {
	public void updateLastOnline(User user) throws AccessDeniedException;
	public LastOnline getLastOnline(long userId);
}
