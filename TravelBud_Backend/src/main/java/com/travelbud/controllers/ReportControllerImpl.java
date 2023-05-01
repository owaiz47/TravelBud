package com.travelbud.controllers;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelbud.entities.Report;
import com.travelbud.services.ReportService;

@RestController
@RequestMapping("/report")
public class ReportControllerImpl implements ReportController {
	
	@Autowired
	private ReportService reportService;

	@PostMapping("")
	@Override
	public Report saveReport(@RequestBody Report report) throws NotReadablePropertyException, AccessDeniedException {
		return reportService.saveReport(report);
	}

}
