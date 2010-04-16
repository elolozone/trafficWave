package com.elolozone.trafficwave.ws.impl;

import javax.ws.rs.Path;

import com.elolozone.trafficwave.ws.Person;
import com.elolozone.trafficwave.ws.api.JsonService;

/**
 * The implementation of {@link JsonService}.
 * 
 * @author brasseld@gmail.com
 * @since 19-03-2010 
 */
@Path("/json")
public class JsonServiceImpl implements JsonService {

	public Person get() {
		Person p = new Person();
		
		p.setId(1234);
		p.setNom("Biloute");
		p.setPrenom("toto");
		
		return p;
	}

}
