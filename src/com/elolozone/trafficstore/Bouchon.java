package com.elolozone.trafficstore;


import com.elolozone.constants.Geo;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

import org.datanucleus.jpa.annotations.Extension;

@PersistenceCapable(identityType = IdentityType.APPLICATION)


public class Bouchon {
	  
	
	@Persistent 
	@Extension(vendorName = "datanucleus", key = "gae.pk-id", value = "true") 
	private String key;

	@Unique
	@Persistent
	 private String idUser;
	
	
	
	@Unique
	@Persistent
	 private String prevKey;

	@Unique
	@Persistent
	private Double posLo;
	
	@Unique
	@Persistent
	private Double posLa;
	
	
	@Persistent
	private Date declaredTime;
	
	 @Unique
	 @Persistent
	 private String poleDirection;

	public Bouchon (Double posLo, Double posLa ,  String idUser, String poleDirection, Date declaredTime) {
		this.posLo = posLo;
		this.posLa = posLa;
		this.idUser = idUser;
		this.poleDirection = poleDirection;
		this.declaredTime = declaredTime;
		
	}

	public String getKey() {
		return key;
	}
	
	
}
