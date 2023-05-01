package com.travelbud.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
public class Place extends AbstractPersistentObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)   
	@JoinColumn(name = "plan_id")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Plan plan;
	private String placeRefId;
	private String placeDes;
	private String name;
	private Double lat;
	private Double lng;
	
	
	public Plan getPlan() {
		return plan;
	}
	public void setPlan(Plan plan) {
		this.plan = plan;
	}
	public String getPlaceRefId() {
		return placeRefId;
	}
	public void setPlaceRefId(String placeRefId) {
		this.placeRefId = placeRefId;
	}
	public String getPlaceDes() {
		return placeDes;
	}
	public void setPlaceDes(String placeDes) {
		this.placeDes = placeDes;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public Place(Plan plan, String placeRefId, String placeDes, String name, Double lat, Double lng) {
		super();
		this.plan = plan;
		this.placeRefId = placeRefId;
		this.placeDes = placeDes;
		this.name = name;
		this.lat = lat;
		this.lng = lng;
	}
	public Place() {
	}
	
	public Place(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}
	
	
	
}
