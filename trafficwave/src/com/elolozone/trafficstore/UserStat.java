package com.elolozone.trafficstore;

import com.google.appengine.api.datastore.Key;
import com.elolozone.constants.Geo;
import java.util.Date;
import java.util.List;


import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

@PersistenceCapable(identityType = IdentityType.APPLICATION)

	//TODO : supprimer l'historique utilisateur lorsqu'il se reconnecte

public class UserStat {
	 
	      

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
  
	@Unique   
	@Persistent
	 private String idUser;  
	
	@Persistent
	 private Integer idSession;  
	 
	 @Persistent
	 private Boolean inTraffic;
	 
	 @Persistent
	 private Boolean inTrafficUser;
	 
	 @Persistent
	 private Boolean inTrafficAlert;
	
	 @Persistent
	 private Date inTrafficDeclaredTime;
	 
	
	@Unique
	@Persistent
	 private Key prevKey;

	@Unique
	@Persistent
	private Double posLo;
	
	@Unique
	@Persistent
	private Double posLa;
	@Persistent
	private Double maxSpeed;
	
	@Persistent
	private Double avgSpeed;
	public Double getAvgSpeed() {return this.avgSpeed;}
	
	@Persistent
	private Double speed;
	public Double getSpeed() {return this.speed;}
	
	@Persistent
	private Double sumSurfDif;
	@Persistent
	private Double sumSurfVmoy;
	
	private Double minAvgSpeedAndSpeed;
	public Double calcMinAvgSpeedAndSpeed() {
		
		return Math.min(getAvgSpeed(),getSpeed());
		}
	
	
	
	private Double dif;
	public Double calcDif ()
	{
	return getAvgSpeed() - calcMinAvgSpeedAndSpeed();
		
	}
	@Persistent
	private Double surfDiff; //=(A5-A4)*MIN(G5;G4)+((MAX(G5;G4)-MIN(G5;G4))*(A5-A4))/2
	
	
	public Double calcSurfDiff (UserStat previousUserStat){
		//=(A5-A4)*MIN(G5;G4)+((MAX(G5;G4)-MIN(G5;G4))*(A5-A4))/2
		//a=(A5-A4)
	
		
		long l1 =this.getLastLocationDate().getTime();
		long l2 = previousUserStat.getLastLocationDate().getTime();
		Double a = new Double(l1-l2); 
		
		//b=MIN(G5;G4)
		Double b = Math.min(this.calcDif(), previousUserStat.calcDif());
		
		//c=MAX(G5;G4)
		Double c = Math.max(this.calcDif(), previousUserStat.calcDif());
		
		return a*b+((c-b)*a)/2;
		
		
	}
	@Persistent
	private Double surfVmoy;
	public Double calcSurfVmoy (UserStat previousUserStat){
		//=(A5-A4)*MIN(G5;G4)+((MAX(G5;G4)-MIN(G5;G4))*(A5-A4))/2
		//a=(A5-A4)
	
		
		long l1 =this.getLastLocationDate().getTime();
		long l2 = previousUserStat.getLastLocationDate().getTime();
		Double a = new Double(l1-l2); 
		
		//b=MIN(G5;G4)
		Double b = Math.min(this.getAvgSpeed(), previousUserStat.getAvgSpeed());
		
		//c=MAX(G5;G4)
		Double c = Math.max(this.getAvgSpeed(), previousUserStat.getAvgSpeed());
		
		return a*b+((c-b)*a)/2;
		
		
	}
	
	@Persistent
	private Double ratio;
	@Persistent
	private Double ratioPond;
	 
	 
	 @Unique
	 @Persistent
	 private String poleDirection;
	  

	 
	@Persistent
	private Date lastLocationDate;
	@Persistent
	private Date startLocationDate;
	   
	 @Persistent
	 private boolean lastLocation;

	public UserStat (String idUser, 
			Integer idSession,
			Double posLo, 
			Double posLa , 
			Double avgSpeed, 
			Double maxSpeed,
			Double speed,
			Geo.Direction direction, 
			Date lastLocationDate,
			boolean inTraffic,
			Date inTrafficDeclaredTime,
			boolean inTrafficUser
			) {
		this.idUser = idUser;
		this.idSession = idSession;
		this.posLo = posLo;
		this.posLa = posLa;
		this.avgSpeed = avgSpeed;
		this.maxSpeed = maxSpeed;
		this.speed = speed;
		this.poleDirection = direction.toString();
		this.lastLocationDate = lastLocationDate;
		this.inTraffic = inTraffic;
		this.inTrafficUser = inTrafficUser;
		this.inTrafficDeclaredTime = inTrafficDeclaredTime;
		
		
	}

	public Double getPosLo() { return this.posLo;}
	public Double getPosLa() { return this.posLa;}
	
	public Double getSurDiff() { return this.surfDiff;}
	public Double getSurfVmoy() { return this.surfVmoy;}
	
	public String getIdUser() { return this.idUser;}
	public Double getRatio () { return this.ratio;}
	public Double getRatioPond () { return this.ratioPond;}
	
	public Double getMaxSpeed() {return this.maxSpeed;}
	public Double getSumSurfVmoy () {return this.sumSurfVmoy;}
	
	public String getPoleDirection() {return this.poleDirection;}
	public Integer getIdSession() {return this.idSession;}
	
	public Date getLastLocationDate() {return this.lastLocationDate;}
	public Date getStartLocationDate() {return this.startLocationDate;}
	
	public Date getInTrafficDeclaredTime() {return this.inTrafficDeclaredTime;}
	public boolean getInTraffic() {return this.inTraffic;}
	public boolean getInTrafficUser() {return this.inTrafficUser;}
	public boolean getInTrafficAlert() {return this.inTrafficAlert;}
	public boolean getLastLocation() {return this.lastLocation;}
	
	
	
	
	public void setMaxSpeed(Double maxSpeed) {this.maxSpeed = maxSpeed;}
	public void setIdSession(Integer idSession) {this.idSession = idSession;}

	public void setposLa(Double posLa) {this.posLa= posLa;}
	public void setposLo(Double posLo) {this.posLo = posLo;}
	public void setPrevKey (Key prevKey) {this.prevKey = prevKey;}
	public void setInTraffic(boolean inTraffic) {this.inTraffic= inTraffic;}
	public void setInTrafficUser(boolean inTrafficUser) {this.inTrafficUser= inTrafficUser;}
	public void setInTrafficAlert(boolean inTrafficAlert) {this.inTrafficAlert= inTrafficAlert;}
	public void setLastLocation(boolean lastLocation) {this.lastLocation= lastLocation;}
	
	
	public void setSumSurfDif(Double sumSurfDif) {this.sumSurfDif = sumSurfDif;}
	public void setSumSurfVmoy(Double sumSurfVmoy) {this.sumSurfVmoy = sumSurfVmoy;}
	
	public void setSurfDiff(Double surfDiff) {this.surfDiff = surfDiff;}
	public void setSurfVmoy(Double surfVmoy) {this.surfVmoy = surfVmoy;}
	public void setRatio (Double ratio){this.ratio = ratio;}
	public void setRatioPond (Double ratioPond){this.ratioPond = ratioPond;}
	
	
	
	public void setLastLocationDate(Date lastLocationDate) {this.lastLocationDate = lastLocationDate;}
	public void setStartLocationDate(Date startLocationDate) {this.startLocationDate = startLocationDate;}
	
	public void setInTrafficDeclaredTime(Date inTrafficDeclaredTime) {this.inTrafficDeclaredTime = inTrafficDeclaredTime;}
	
	
	public Key getKey() {
		return key;
	}

	

}
