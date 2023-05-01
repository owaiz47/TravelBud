package com.travelbud.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.Place;

@Repository
public interface PlaceDao extends JpaRepository<Place, Long>{
}
