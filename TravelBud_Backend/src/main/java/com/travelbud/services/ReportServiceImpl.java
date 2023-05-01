package com.travelbud.services;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelbud.dao.ReportDao;
import com.travelbud.entities.Post;
import com.travelbud.entities.Report;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportDao reportDao;
	
	@Autowired
	private UserService userService;
	
	@Override
	public Report saveReport(Report report) throws NotReadablePropertyException, AccessDeniedException {
		User authUser = userService.getAuthenticatedUser();
		report.setReportedBy(authUser);
		checkReportAccessibility(report);
		return reportDao.saveAndFlush(report);
	}
	
	private void checkReportAccessibility(Report report) throws AccessDeniedException, NotReadablePropertyException {
		User currentUser = userService.getAuthenticatedUser();
		if (report == null) {
			throw new NotReadablePropertyException(Post.class, CommonErrorMessages.NULL_ERROR);
		} else if (report.getReportedBy().getId() == null || report.getReportedBy().getId() != currentUser.getId()) {
			throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
		}
	}

}
