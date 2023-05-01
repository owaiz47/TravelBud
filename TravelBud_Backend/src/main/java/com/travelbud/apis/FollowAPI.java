package com.travelbud.apis;

import java.nio.file.AccessDeniedException;

public interface FollowAPI {
	public boolean follow(long userId) throws AccessDeniedException;
	public boolean unfollow(long userId) throws AccessDeniedException;
	public int getFollowingCount(long userId) throws AccessDeniedException;
	public int getFollowerCount(long userId) throws AccessDeniedException;
}
