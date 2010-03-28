package com.elolozone.trafficwave.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.elolozone.trafficwave.util.Geo;

@Entity
@Table(name = "tw_global_trace")
public class GlobalTrace { 

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private Double longitude;
	private Double latitude;
	private Double sumSpeed;
	private Double maxSpeed;
	private Integer direction;
	private Integer nbPoints;
	private Date lastLocationDate;

	public GlobalTrace (Double posLo, Double posLa, Double sumSpeed, Double maxSpeed, Geo.Direction direction, Date lastLocationDate) {
		this.longitude = posLo;
		this.latitude = posLa;
		this.sumSpeed = sumSpeed;
		this.maxSpeed = maxSpeed;
		this.direction = direction.getValue();
		this.lastLocationDate = lastLocationDate;
	}
	
	@Id
	@GeneratedValue(generator = "strategy-uuid")
	@GenericGenerator(name = "strategy-uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	@Column(name = "direction")
	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	@Basic
	@Column(name = "nb_points")
	public Integer getNbPoints() {
		return nbPoints;
	}

	public void setNbPoints(Integer nbPoints) {
		this.nbPoints = nbPoints;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "last_location_date")
	public Date getLastLocationDate() {
		return lastLocationDate;
	}

	public void setLastLocationDate(Date lastLocationDate) {
		this.lastLocationDate = lastLocationDate;
	}
}
