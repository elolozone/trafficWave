package com.elolozone.trafficstore;


import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

import org.datanucleus.jpa.annotations.Extension;

@PersistenceCapable(identityType = IdentityType.APPLICATION)


public class Traffic {


	@Persistent 
	@Extension(vendorName = "datanucleus", key = "gae.pk-id", value = "true") 
	private String key;


	
	
	 @Persistent
	 private String idUser;
	 
	 @Persistent
	 private Date inTrafficDeclaredTime;
	 
		
		@Persistent
		private Double posLo;

		@Persistent
		private Double posLa;
		
	
		@Persistent
		private Double ratioPond;
		 
		 @Unique
		 @Persistent
		 private String poleDirection;
		 
		 
		
	 public Traffic (String idUser, boolean inTraffic, Date inTrafficDeclaredTime) {
			
			this.idUser = idUser;
			
			this.inTrafficDeclaredTime = inTrafficDeclaredTime;
			
		}

		public String getKey() {
			return key;
		}
	 
	
}