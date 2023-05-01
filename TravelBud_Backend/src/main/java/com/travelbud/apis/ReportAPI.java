package com.travelbud.apis;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.NotReadablePropertyException;

import com.travelbud.entities.Report;

public interface ReportAPI {
	public Report saveReport(Report report) throws NotReadablePropertyException, AccessDeniedException;
}
