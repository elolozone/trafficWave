package com.elolozone.trafficwave.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.elolozone.constants.Geo;

public class GlobalTrace extends BaseEntity { 

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 222852616628718200L;
	
	private Double longitude;
	private Double latitude;
	private Double sumSpeed;
	private Double maxSpeed;
	private Integer secondesStay;
	private Date lastStayDate;
	private String poleDirection;
	private Integer nbUser;
	private Date lastLocationDate;

	public GlobalTrace (Double posLo, Double posLa, Double sumSpeed, Double maxSpeed, Geo.Direction direction, Date lastLocationDate) {
		this.longitude = posLo;
		this.latitude = posLa;
		this.sumSpeed = sumSpeed;
		this.maxSpeed = maxSpeed;
		this.poleDirection = direction.toString();
		this.lastLocationDate = lastLocationDate;
	}

	@Basic
	@Column(name = "longitude")
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Basic
	@Column(name = "latitude")
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Basic
	@Column(name = "sum_speed")
	public Double getSumSpeed() {
		return sumSpeed;
	}

	public void setSumSpeed(Double sumSpeed) {
		this.sumSpeed = sumSpeed;
	}

	@Basic
	@Column(name = "max_speed")
	public Double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(Double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	@Basic
	@Column(name = "secondes_stay")
	public Integer getSecondesStay() {
		return secondesStay;
	}

	public void setSecondesStay(Integer secondesStay) {
		this.secondesStay = secondesStay;
	}

	@Temporal(value = TemporalType.DATE)
	@Column(name = "last_stay_date")
	public Date getLastStayDate() {
		return lastStayDate;
	}

	public void setLastStayDate(Date lastStayDate) {
		this.lastStayDate = lastStayDate;
	}

	@Basic
	@Column(name = "pole_direction")
	public String getPoleDirection() {
		return poleDirection;
	}

	public void setPoleDirection(String poleDirection) {
		this.poleDirection = poleDirection;
	}

	@Basic
	@Column(name = "nb_user")
	public Integer getNbUser() {
		return nbUser;
	}

	public void setNbUser(Integer nbUser) {
		this.nbUser = nbUser;
	}

	@Temporal(value = TemporalType.DATE)
	@Column(name = "last_location_date")
	public Date getLastLocationDate() {
		return lastLocationDate;
	}

	public void setLastLocationDate(Date lastLocationDate) {
		this.lastLocationDate = lastLocationDate;
	}
}
