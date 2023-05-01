package com.travelbud.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.Report;

@Repository
public interface ReportDao extends JpaRepository<Report, Long> {

}
