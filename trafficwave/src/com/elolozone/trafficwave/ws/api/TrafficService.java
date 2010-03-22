package com.elolozone.trafficwave.ws.api;

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

	@Path("/news/{user}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	String getNews(@PathParam ("user") String userId);
	
	@Path("/paths/{user}/{session}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	String getPaths(@PathParam ("user") String userId, @PathParam ("session") int sessionId);
	
	@Path("/identifyTrafficJob")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	String identifyTrafficJob();
	
	@Path("/imagine/{user}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	String imagineUserDestination(@PathParam ("user") String userId);
	
	@Path("/listGlobalTrace")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	String listGlobalTrace();
	
	@Path("/listUserStat")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	String listUserStat();
	
	@Path("/averageSpot")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	String averageSpot(@QueryParam("posLo") double longitude, @QueryParam("posLa") double latitude, @QueryParam("course") double course);
	
	@Path("/trafficQuestion")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	String trafficQuestion();
	
	@Path("/sendposition/{user}/{session}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	String sendPositionOnly(@PathParam ("user") String id, @QueryParam("posLo") String longitude, @QueryParam("posLa") String latitude, 
			@QueryParam("speed") String speed, @QueryParam("course") String course, @QueryParam("street") String street,
			@PathParam ("session") int idSession, @QueryParam("postalCode") String postalCode, @QueryParam("traffic") String traffic);
}
