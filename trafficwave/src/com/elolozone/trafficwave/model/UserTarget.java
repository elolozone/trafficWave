package com.elolozone.trafficwave.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.elolozone.trafficwave.util.Geo;

@Entity
@Table(name = "user_trace")
public class UserTarget {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String idUser;
	private Integer idSession;
	private Double destLongitude;
	private Double destLatitude;
	private Double oriLongitude;
	private Double oriLatitude;
	private boolean wantedTarget;
	private boolean favoriteTarget;
	private int minTrackTimeSec;
	private int maxTrackTimeSec;
	private int avgTrackTimeSec;
	private int nbTrackPossible;
	private int idSessionMinTime;
	private int idSessionMaxTime;
	



	private String poleDirection;

	
	public UserTarget () {};
	public UserTarget (String idUser, 
			Integer idSession,
			Double longitude, 
			Double latitude , 
			
			Geo.Direction direction
			) {
		this.idUser = idUser;
		this.idSession = idSession;
		this.destLongitude = longitude;
		this.destLatitude = latitude;
		
		this.poleDirection = direction.toString();
		
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
	@Column(name = "id_user")
	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	@Basic
	@Column(name = "id_session")
	public Integer getIdSession() {
		return idSession;
	}

	public void setIdSession(Integer idSession) {
		this.idSession = idSession;
	}



 

	
	//
	
	@Basic
	@Column(name = "dest_longitude")
	public Double getDestLongitude() {
		return destLongitude;
	}
	public void setDestLongitude(Double destLongitude) {
		this.destLongitude = destLongitude;
	}
	
	@Basic
	@Column(name = "dest_latitude")
	public Double getDestLatitude() {
		return destLatitude;
	}
	public void setDestLatitude(Double destLatitude) {
		this.destLatitude = destLatitude;
	}
	
	@Basic
	@Column(name = "ori_longitude")
	public Double getOriLongitude() {
		return oriLongitude;
	}
	public void setOriLongitude(Double oriLongitude) {
		this.oriLongitude = oriLongitude;
	}
	
	@Basic
	@Column(name = "ori_latitude")
	public Double getOriLatitude() {
		return oriLatitude;
	}
	public void setOriLatitude(Double oriLatitude) {
		this.oriLatitude = oriLatitude;
	}
	
	@Basic
	@Column(name = "wanted_target")
	public boolean getWantedTarget() {
		return wantedTarget;
	}
	public void setWantedTarget(boolean wantedTarget) {
		this.wantedTarget = wantedTarget;
	}
	
	@Basic
	@Column(name = "favorite_target")
	public boolean getFavoriteTarget() {
		return favoriteTarget;
	}
	public void setFavoriteTarget(boolean favoriteTarget) {
		this.favoriteTarget = favoriteTarget;
	}
	
	@Basic
	@Column(name = "min_track_time_sec")
	public int getMinTrackTimeSec() {
		return minTrackTimeSec;
	}
	public void setMinTrackTimeSec(int minTrackTimeSec) {
		this.minTrackTimeSec = minTrackTimeSec;
	}
	
	@Basic
	@Column(name = "max_track_time_sec")
	public int getMaxTrackTimeSec() {
		return maxTrackTimeSec;
	}
	public void setMaxTrackTimeSec(int maxTrackTimeSec) {
		this.maxTrackTimeSec = maxTrackTimeSec;
	}
	
	@Basic
	@Column(name = "avg_track_time_sec")
	public int getAvgTrackTimeSec() {
		return avgTrackTimeSec;
	}
	public void setAvgTrackTimeSec(int avgTrackTimeSec) {
		this.avgTrackTimeSec = avgTrackTimeSec;
	}
	
	@Basic
	@Column(name = "nb_track_possible")
	public int getNbTrackPossible() {
		return nbTrackPossible;
	}
	public void setNbTrackPossible(int nbTrackPossible) {
		this.nbTrackPossible = nbTrackPossible;
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
	@Column(name = "id_session_min_time")
	public int getIdSessionMinTime() {
		return idSessionMinTime;
	}
	public void setIdSessionMinTime(int idSessionMinTime) {
		this.idSessionMinTime = idSessionMinTime;
	}
	
	@Basic
	@Column(name = "id_session_max_time")
	public int getIdSessionMaxTime() {
		return idSessionMaxTime;
	}
	public void setIdSessionMaxTime(int idSessionMaxTime) {
		this.idSessionMaxTime = idSessionMaxTime;
	}

	


}
