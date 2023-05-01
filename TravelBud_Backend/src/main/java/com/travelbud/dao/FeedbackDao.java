package com.travelbud.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.Feedback;

@Repository
public interface FeedbackDao extends JpaRepository<Feedback, Long> {

}
