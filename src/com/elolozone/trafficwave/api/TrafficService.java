package com.elolozone.trafficwave.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * The main interface which define all Traffic engine services.
 * 
 * @author brasseld@gmail.com
 * @since 19-03-2010 
 */
public interface TrafficService {

	@Path("/{user}")
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	String getNews(@PathParam ("user") String userId);
	
	@Path("/{user}/{session}")
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	String getPaths(@PathParam ("user") String userId, @PathParam ("session") int sessionId);
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	String identifyTrafficJob();
	
	@Path("/{user}")
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	String imagineUserDestination(@PathParam ("user") String userId);
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	String listGlobalTrace();
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	String listUserStat();
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	String averageSpot(@QueryParam("posLo") double longitude, @QueryParam("posLa") double latitude, @QueryParam("course") double course);
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	String trafficQuestion();
	
	@Path("/{user}/{session}")
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	String sendPositionOnly(@PathParam ("user") String id, @QueryParam("posLo") String longitude, @QueryParam("posLa") String latitude, 
			@QueryParam("speed") String speed, @QueryParam("course") String course, @QueryParam("street") String street,
			@PathParam ("session") int idSession, @QueryParam("postalCode") String postalCode, @QueryParam("traffic") String traffic);
}
