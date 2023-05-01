package com.travelbud.services;

import java.nio.file.AccessDeniedException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelbud.dao.LastOnlineDao;
import com.travelbud.entities.LastOnline;
import com.travelbud.entities.User;

@Service
public class LastOnlineServiceImpl implements LastOnlineService {

	@Autowired
	private LastOnlineDao lastOnlineDao;
	
	@Override
	public void updateLastOnline(User user) throws AccessDeniedException {
		if(user == null || user.getId() == 0)return;
		LastOnline lastOnline = getLastOnline(user.getId());
		if(lastOnline == null || lastOnline.getId() < 1) {
			lastOnline = new LastOnline();
			lastOnline.setUser(user);
		}
		lastOnline.setLastOnline(new Date());
		lastOnlineDao.save(lastOnline);
	}

	@Override
	public LastOnline getLastOnline(long userId) {
		return lastOnlineDao.findByUserId(userId);
	}

}
