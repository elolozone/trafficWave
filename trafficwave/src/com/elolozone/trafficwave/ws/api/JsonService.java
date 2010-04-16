package com.elolozone.trafficwave.ws.api;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.elolozone.trafficwave.ws.Person;
import com.sun.jersey.api.json.JSONJAXBContext;

/**
 * The main interface which define all Traffic engine services.
 * 
 * 
 * @author brasseld@gmail.com 
 * @since 16-04-2010 
 */
public interface JsonService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	// Pour tester, appeler l'url suivante : http://localhost:8080/trafficwave/ws/json
	Person get();
}
