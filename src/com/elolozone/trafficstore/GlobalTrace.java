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


public class GlobalTrace { 
	  
	

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	 public enum Day {
		    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, 
		    THURSDAY, FRIDAY, SATURDAY 
		}
	
	@Persistent
	private Integer hour;
	
	@Persistent
	private Day  day;
	
	@Unique
	@Persistent
	private Double posLo;
	
	@Unique
	@Persistent
	private Double posLa;
	
	@Persistent
	private Double sumSpeed;
	@Persistent
	private Double maxSpeed;
	
	 @Persistent 
	 private Integer secondesStay;
	 @Persistent 
	 private Date lastStayDate;
	 
	  
	 
	 @Unique
	 @Persistent
	 private String poleDirection;
	  
	 @Persistent
	 private Integer nbUser;
	 



	@Persistent
	private Date lastLocationDate;

	public GlobalTrace (Double posLo, Double posLa , Double sumSpeed, Double maxSpeed,Geo.Direction direction, Date lastLocationDate) {
		this.posLo = posLo;
		this.posLa = posLa;
		this.sumSpeed = sumSpeed;
		this.maxSpeed = maxSpeed;
		this.poleDirection = direction.toString();
		this.lastLocationDate = lastLocationDate;
		this.day = Day.SUNDAY;
		
	}

	public Double getPosLo() { return this.posLo;}
	public Double getPosLa() { return this.posLa;}
	public Double getSumSpeed() {return this.sumSpeed;}
	public Double getMaxSpeed() {return this.maxSpeed;}
	public Integer getNbUser() {return this.nbUser;}
	public String getPoleDirection() {return this.poleDirection;}
	public Date getLastLocationDate() {return this.lastLocationDate;}
	public void setSumSpeed(Double sumSpeed) {this.sumSpeed = sumSpeed;}
	public void setMaxSpeed(Double maxSpeed) {this.maxSpeed = maxSpeed;}
	public void setnbUser(Integer nbUser) {this.nbUser = nbUser;}
	public void setposLa(Double posLa) {this.posLa= posLa;}
	public void setposLo(Double posLo) {this.posLo = posLo;}
	public void setLastLocationDate(Date lastLocationDate) {this.lastLocationDate = lastLocationDate;}
	
	
	public Key getKey() {
		return key;
	}

	

}
