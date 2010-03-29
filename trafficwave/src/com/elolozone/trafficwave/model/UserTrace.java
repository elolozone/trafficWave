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
public class UserTrace {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String idUser;
	private Integer idSession;
	private Boolean inTraffic;
	private Boolean inTrafficUser;
	private Boolean inTrafficAlert;
	private Date inTrafficDeclaredTime;
	private UserTrace previous;
	private Double longitude;
	private Double latitude;
	private Double maxSpeed;
	private Double avgSpeed;
	private Double speed;
	private Double sumSurfDif;
	private Double sumSurfVmoy;
	private Double surfDiff;
	private Double minAvgSpeedAndSpeed;
	private Double surfVmoy;
	private Double ratio;
	private Double ratioPond;
	private String poleDirection;
	private Date lastLocationDate;
	private Date startLocationDate;
	private boolean lastLocation;
	
	public UserTrace (String idUser, 
			Integer idSession,
			Double longitude, 
			Double latitude , 
			Double avgSpeed, 
			Double maxSpeed,
			Double speed,
			Geo.Direction direction, 
			Date lastLocationDate,
			boolean inTraffic,
			Date inTrafficDeclaredTime,
			boolean inTrafficUser) {
		this.idUser = idUser;
		this.idSession = idSession;
		this.longitude = longitude;
		this.latitude = latitude;
		this.avgSpeed = avgSpeed;
		this.maxSpeed = maxSpeed;
		this.speed = speed;
		this.poleDirection = direction.toString();
		this.lastLocationDate = lastLocationDate;
		this.inTraffic = inTraffic;
		this.inTrafficUser = inTrafficUser;
		this.inTrafficDeclaredTime = inTrafficDeclaredTime;
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

	@Basic
	@Column(name = "in_traffic")
	public Boolean getInTraffic() {
		return inTraffic;
	}

	public void setInTraffic(Boolean inTraffic) {
		this.inTraffic = inTraffic;
	}

	@Basic
	@Column(name = "in_traffic_user")
	public Boolean getInTrafficUser() {
		return inTrafficUser;
	}

	public void setInTrafficUser(Boolean inTrafficUser) {
		this.inTrafficUser = inTrafficUser;
	}

	@Basic
	@Column(name = "in_traffic_alert")
	public Boolean getInTrafficAlert() {
		return inTrafficAlert;
	}

	public void setInTrafficAlert(Boolean inTrafficAlert) {
		this.inTrafficAlert = inTrafficAlert;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "in_traffic_decl_time")
	public Date getInTrafficDeclaredTime() {
		return inTrafficDeclaredTime;
	}

	public void setInTrafficDeclaredTime(Date inTrafficDeclaredTime) {
		this.inTrafficDeclaredTime = inTrafficDeclaredTime;
	}

	@ManyToOne
    @JoinColumn(name = "previous_trace_id")
	public UserTrace getPrevious() {
		return previous;
	}

	public void setPrevious(UserTrace previous) {
		this.previous = previous;
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
	@Column(name = "max_speed")
	public Double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(Double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	@Basic
	@Column(name = "avg_speed")
	public Double getAvgSpeed() {
		return avgSpeed;
	}

	public void setAvgSpeed(Double avgSpeed) {
		this.avgSpeed = avgSpeed;
	}

	@Basic
	@Column(name = "speed")
	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	@Basic
	@Column(name = "sum_sur_dif")
	public Double getSumSurfDif() {
		return sumSurfDif;
	}

	public void setSumSurfDif(Double sumSurfDif) {
		this.sumSurfDif = sumSurfDif;
	}

	@Basic
	@Column(name = "sum_sur_vmoy")
	public Double getSumSurfVmoy() {
		return sumSurfVmoy;
	}

	public void setSumSurfVmoy(Double sumSurfVmoy) {
		this.sumSurfVmoy = sumSurfVmoy;
	}

	@Basic
	@Column(name = "surf_diff")
	public Double getSurfDiff() {
		return surfDiff;
	}

	public void setSurfDiff(Double surfDiff) {
		this.surfDiff = surfDiff;
	}

	@Basic
	@Column(name = "min_avg_speed_and_speed")
	public Double getMinAvgSpeedAndSpeed() {
		return minAvgSpeedAndSpeed;
	}

	public void setMinAvgSpeedAndSpeed(Double minAvgSpeedAndSpeed) {
		this.minAvgSpeedAndSpeed = minAvgSpeedAndSpeed;
	}

	@Basic
	@Column(name = "surf_vmoy")
	public Double getSurfVmoy() {
		return surfVmoy;
	}

	public void setSurfVmoy(Double surfVmoy) {
		this.surfVmoy = surfVmoy;
	}

	@Basic
	@Column(name = "ratio")
	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

	@Basic
	@Column(name = "ratio_pond")
	public Double getRatioPond() {
		return ratioPond;
	}

	public void setRatioPond(Double ratioPond) {
		this.ratioPond = ratioPond;
	}

	@Basic
	@Column(name = "pole_direction")
	public String getPoleDirection() {
		return poleDirection;
	}

	public void setPoleDirection(String poleDirection) {
		this.poleDirection = poleDirection;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "last_location_date")
	public Date getLastLocationDate() {
		return lastLocationDate;
	}

	public void setLastLocationDate(Date lastLocationDate) {
		this.lastLocationDate = lastLocationDate;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "start_location_date")
	public Date getStartLocationDate() {
		return startLocationDate;
	}

	public void setStartLocationDate(Date startLocationDate) {
		this.startLocationDate = startLocationDate;
	}

	@Basic
	@Column(name = "last_location")
	public boolean isLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(boolean lastLocation) {
		this.lastLocation = lastLocation;
	}
}
