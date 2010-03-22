package com.elolozone.trafficwave.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "user_trace")
public class UserTrace extends BaseEntity {
	
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -7346883922402081777L;
	
	private String idUser; 
	private Integer lastIdSession; 
	private Integer totalTime; 
	private Integer runningMeter;
	private Date lastConnectionTime; 
	private String email; 
	private String firstName; 
	private String lastName; 
	private String city;
	private String postalCode;

	public UserTrace (
			String idUser, 
			Integer lastIdSession, 
			Integer totalTime, 
			Integer runningMeter, 
			Date lastConnectionTime, 
			String email, 
			String firstName,  
			String lastName, 
			String city, 
			String postalCode) {
		this.idUser = idUser;
		this.lastIdSession = lastIdSession; 
		this.totalTime = totalTime;
		this.runningMeter=runningMeter ; 
		this.lastConnectionTime=lastConnectionTime;
		this.email= email;
		this.firstName= firstName; 
		this.lastName=lastName;
		this.city=city; 
		this.postalCode = postalCode;
	}
	
	public UserTrace (String idUser) {
		this.idUser = idUser;
	}

	@Basic
	@Column(name = "id_user")
	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	@Basic
	@Column(name = "last_id_session")
	public Integer getLastIdSession() {
		return lastIdSession;
	}

	public void setLastIdSession(Integer lastIdSession) {
		this.lastIdSession = lastIdSession;
	}

	@Basic
	@Column(name = "total_time")
	public Integer getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Integer totalTime) {
		this.totalTime = totalTime;
	}

	@Basic
	@Column(name = "running_meter")
	public Integer getRunningMeter() {
		return runningMeter;
	}

	public void setRunningMeter(Integer runningMeter) {
		this.runningMeter = runningMeter;
	}

	@Temporal(value = TemporalType.DATE)
	@Column(name = "last_connection_time")
	public Date getLastConnectionTime() {
		return lastConnectionTime;
	}

	public void setLastConnectionTime(Date lastConnectionTime) {
		this.lastConnectionTime = lastConnectionTime;
	}

	@Basic
	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Basic
	@Column(name = "first_name")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Basic
	@Column(name = "last_name")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Basic
	@Column(name = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Basic
	@Column(name = "postal_code")
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
}
