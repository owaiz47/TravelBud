package com.travelbud.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.ResetPassword;

@Repository
public interface ResetPasswordDao extends JpaRepository<ResetPassword, Long>{
	public List<ResetPassword> findByEmailAndCode(String email, String code);
	
	@Query(nativeQuery = true, value = "SELECT  *\n"
			+ "FROM    travelbud.reset_password\n"
			+ "WHERE   created_on >= NOW() - INTERVAL '1 DAY' and email = ?1")
	public List<ResetPassword> getLastRecord(String email);
}
