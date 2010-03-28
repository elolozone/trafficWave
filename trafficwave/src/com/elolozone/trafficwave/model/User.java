package com.elolozone.trafficwave.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tw_user")
public class User {
	
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private Integer lastIdSession;
	private Integer totalTime;
	private Integer runningMeter;
	private Date lastConnectionTime;
	
	public User() {}
	
	public User (String id) {
		this.id = id;
	}

	@Id
	@Column(name = "id", nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "last_connection_time")
	public Date getLastConnectionTime() {
		return lastConnectionTime;
	}

	public void setLastConnectionTime(Date lastConnectionTime) {
		this.lastConnectionTime = lastConnectionTime;
	}
}
