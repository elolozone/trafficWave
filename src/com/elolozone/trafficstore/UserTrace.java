package com.elolozone.trafficstore;



import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.datanucleus.jpa.annotations.Extension;

@PersistenceCapable(identityType = IdentityType.APPLICATION)


public class UserTrace {

	@Persistent 
	@Extension(vendorName = "datanucleus", key = "gae.pk-id", value = "true") 
	private String key;
 
	 
	 @Persistent
	 private String idUser; 
	 
	 @Persistent 
	 private Integer lastIdSession; 
	 
	 @Persistent
	 private Integer totalTime; 
	
	 @Persistent
	 private Integer runningMeter;
	 
	 @Persistent
	 private Date lastConnectionTime; 
	 @Persistent
	 private String email; 
	 @Persistent
	 private String firstName; 
	 @Persistent
	 private String lastName; 
	 @Persistent
	 private String city;
	 @Persistent
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

	public String getIdUser() { return this.idUser;}
	public Integer getLastIdSession() { return this.lastIdSession;}
	public Date getLastConnectionTime() { return this.lastConnectionTime;}
	public void setLastIdSession(Integer lastIdSession) {  this.lastIdSession=lastIdSession;}
	public void setLastConnectionTime(Date lastConnectionTime) {  this.lastConnectionTime=lastConnectionTime;}
	
	public String getKey() {
		return key;
	}

	

}
