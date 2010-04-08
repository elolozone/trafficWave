package com.elolozone.trafficwave.ws.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elolozone.trafficwave.constants.IConstants;
import com.elolozone.trafficwave.manager.api.GlobalTraceManager;
import com.elolozone.trafficwave.manager.api.UserTraceManager;
import com.elolozone.trafficwave.manager.api.UserManager;
import com.elolozone.trafficwave.model.GlobalTrace;
import com.elolozone.trafficwave.model.Location;
import com.elolozone.trafficwave.model.User;
import com.elolozone.trafficwave.model.UserTrace;
import com.elolozone.trafficwave.util.Geo;
import com.elolozone.trafficwave.util.Math;
import com.elolozone.trafficwave.ws.api.TrafficService;

/**
 * The implementation of {@link TrafficService}.
 * 
 * @author brasseld@gmail.com
 * @since 19-03-2010 
 */
@Path("/traffic")
@Component
@Scope("singleton")
public class TrafficServiceImpl implements TrafficService {

	private GlobalTraceManager globalTraceManager;
	private UserManager userManager;
	private UserTraceManager userTraceManager;
	
	/**
	 * Hold current users location.
	 */
	private Map<String, Location> locations = new HashMap<String, Location>();
	
	/**
	 * {@inheritDoc}
	 */
	public String getNews(String userId) {
		User currentUser = this.getUserManager().connect(userId);
		
		return 
				"\n\nActuellement : "+String.valueOf(locations.size()) + " utilisateurs connectes."+
				"\n\nVous avez utilise "+currentUser.getLastIdSession().toString() +" fois l'application."+ 
				"\n\n"+
				"--> L'application est en beta: utilisez la quand vous pouvez pour permettre d'alimenter les bases de donnees route.Merci!$"
				+currentUser.getLastIdSession().toString() + "&";
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPaths(String userId, int sessionId) {
		List<UserTrace> userStats = this.getUserTraceManager().findBySessionAndUser(sessionId, userId); 
		
		if (userStats != null)  {
			StringBuilder sb = new StringBuilder();
			
			for (UserTrace userStat : userStats) {
				sb.append(userStat.getLatitude()).append(',').append(userStat.getLongitude()).append('\n');
			}
			
			return sb.toString();
		}
		
		return "0,0";
	}

	/**
	 * {@inheritDoc}
	 */
	public String identifyTrafficJob() {
		List<UserTrace> userStats = this.getUserTraceManager().findUserInTraffic(IConstants.ACTIF_USER_MAX_SEC, IConstants.DUREE_TODECLARE_BOUCHON_SEC);

		if (userStats != null) {
			
			StringBuilder sb = new StringBuilder();
			
			for (UserTrace userStat : userStats) {
				Location location =  locations.get(userStat.getIdUser());
				
				sb.append(userStat.getIdUser()).append(',').
					append(userStat.getLastLocationDate()).append(',').
					append(userStat.getInTrafficDeclaredTime()).append(',').
					append(userStat.getPoleDirection()).append(',').
					append(userStat.getLatitude()).append(',').
					append(userStat.getLongitude()).append(',').
					append(userStat.getSpeed()).append(',').
					append((new Date().getTime() - userStat.getInTrafficDeclaredTime().getTime()) / 1000);
				
				if (location!=null)	 {
					if (location.isInTraffic()) 
						sb.append(",inTraffic"); 
					else 
						sb.append(",notInTraffic");
				}
				
				sb.append('&').append("\n");
			}
			
			return sb.toString();
		}
		else 
			return  "NO TRAFFICJAM";
	}

	/**
	 * {@inheritDoc}
	 */
	public String imagineUserDestination(String userId) {
		Location actualLocation =  locations.get(userId);
		
		if (actualLocation == null) 
			return "";
		
		//TODO : nombre de jours historique des itinÃ©raires
		Collection<UserTrace> userTraces = this.userTraceManager.findUserDestinations(60*60*24*30, userId, actualLocation.getLatitude(), actualLocation.getLongitude());

		if (userTraces != null) {
			StringBuilder sb = new StringBuilder();
			
			for (UserTrace userTrace : userTraces) { 
				// iti 1, iti 2
				sb.append(userTrace.getIdUser()).append(',').
					append(userTrace.getLastLocationDate()).append(',').
					append("0,").
					append("0,").
					append(userTrace.getLatitude()).append(',').
					append(userTrace.getLongitude()).append(',').
					append((int)((userTrace.getLastLocationDate().getTime() - userTrace.getStartLocationDate().getTime())/1000)*1.15).append(',').
					append(60).append(',').
					append(userTrace.getIdSession()).
					append(",null&\n");
			}
			
			return sb.toString();
		}
		
		return "NO ITI";
	}

	/**
	 * {@inheritDoc}
	 */
	public String listGlobalTrace() {
		StringBuilder sb = new StringBuilder();
		
		List<GlobalTrace> globalTraces = this.getGlobalTraceManager().findAll();
		
		for (GlobalTrace globalTrace : globalTraces) {

			sb.append(globalTrace.getLatitude()).append(',').
				append(globalTrace.getLongitude()).append(',').
				append(globalTrace.getSumSpeed()).append(',').
				append(globalTrace.getNbPoints()).append(',').
				append(globalTrace.getDirection()).append(',').
				append(globalTrace.getMaxSpeed()).append(",&\n");
		}
		
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public String listUserStat() {
		StringBuilder sb = new StringBuilder("Latitude,Longitude,maxSpeed,Ratio,RatioPond,AvgSpeed,time,surfDiff,surfVmoy,idUser,speed,umSurfVmoy\n");
		
		List<UserTrace> userStats = this.getUserTraceManager().findAllAndOrderBy("lastLocationDate", Boolean.FALSE);
		
		for (UserTrace userStat : userStats) {
			sb.append(userStat.getLatitude()).append(',').
				append(userStat.getLongitude()).append(',').
				append(userStat.getMaxSpeed()).append(',').
				append(userStat.getRatio()).append(',').
				append(userStat.getRatioPond()).append(',').
				append(userStat.getAvgSpeed()).append(',').
				append(userStat.getLastLocationDate().getTime()).append(',').
				append(userStat.getSurfDiff()).append(',').
				append(userStat.getSurfVmoy()).append(',').
				append(userStat.getIdUser()).append(',').
				append(userStat.getSpeed()).append(',').
				append(userStat.getSumSurfVmoy()).append(',').
				append("\n");
		}
		
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public String averageSpot(double longitude, double latitude, double course) {
		GlobalTrace globalTrace = this.getGlobalTraceManager().findBy(latitude, longitude, course);
		
		if (globalTrace != null) {
			// TODO: Some explanation about this calc, what is 3.6f ?
			Double vitMoy = globalTrace.getSumSpeed() / globalTrace.getNbPoints() * 3.6f;
			
			if (globalTrace.getMaxSpeed() != null) 
			{
				Double vitMax = globalTrace.getMaxSpeed() * 3.6f;
				
				return vitMoy.toString() + ',' + vitMax.toString() + '&';
			}
			else 
				return "max inconnu&";
		}
		
		return "Zone inconnue&";
	}

	/**
	 * {@inheritDoc}
	 */
	public String trafficQuestion() {
		StringBuilder sb = new StringBuilder();
		
		for (Entry<String, Location> entry : locations.entrySet()) {
			Location loc = entry.getValue();

			GlobalTrace globalTrace = this.getGlobalTraceManager().findBy(loc.getLatitude(), loc.getLongitude(), loc.getCourse());
			
			if (globalTrace != null) {
				Double vitMax = globalTrace.getMaxSpeed() * 3.6f;
				Double vit = Double.valueOf(loc.getSpeed()) * 3.6f ;
				
				if (vit < vitMax) {
					Double ratioV = vit / vitMax * 100.0f;
					
					sb.append(loc.getLatitude()).append(',').append(loc.getLongitude()).append(',').append(ratioV).append('&');
				} 
			} 
		}
		
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public String sendPositionOnly(String id, int idSession, double longitude,
			double latitude, double speed, double course) {
		Location location = new Location();

		location.setIdUser(id);
		location.setLongitude(Math.gridConvertion(longitude));
		location.setLatitude(Math.gridConvertion(latitude));
		location.setSpeed(speed);
		location.setCourse(course);
		location.setIdSession(idSession);

		UserTrace userTrace = null;
		
		if (location.getIdUser() != null && location.getLatitude() != null && location.getLongitude() != null && location.getSpeed() != null && location.getCourse() != null)     
		{
			Location previousL = locations.get(location.getIdUser());
		    
			if (previousL!=null) {
				location.setInTraffic(previousL.isInTraffic());
			}

			locations.put(location.getIdUser(), location);

			Geo.Direction direction = Geo.getDirection(location.getCourse());

			//  on rÃ©cupÃ©re la vitesse moyenne et max et on enregistre la position
			GlobalTrace globalTraceAvgSport = this.getGlobalTraceManager().findBy(location.getLatitude(), location.getLongitude(), location.getCourse());
			Double maxSpeed = null;
			Double moySpeed = null;
			if (globalTraceAvgSport != null) {
				maxSpeed = globalTraceAvgSport.getMaxSpeed();
				moySpeed = globalTraceAvgSport.getSumSpeed() / globalTraceAvgSport.getNbPoints();	
			}
			else
			{
				maxSpeed = location.getSpeed();
				moySpeed = location.getSpeed();
			}
				boolean inTrafficUser=false;
				
				if (location.getInTrafficUser() != null && location.getInTrafficUser().equals("TRUE")) 
						inTrafficUser = true;
				if (location.getInTrafficUser() == null || location.getInTrafficUser().equals("FALSE"))
						inTrafficUser = false;
						
				userTrace = new UserTrace(
						location.getIdUser(),
						location.getIdSession(),
						location.getLongitude(),
						location.getLatitude(),
						moySpeed,
						maxSpeed,
						location.getSpeed(),
						direction,
						new Date(), false, null, inTrafficUser);

				this.userTraceManager.save(userTrace);
			

			if (location.getSpeed() > -1 && location.getCourse() > -1) {
				GlobalTrace globalTrace = new GlobalTrace(
						location.getLongitude(),
						location.getLatitude(),
						location.getSpeed(),
						location.getSpeed(),
						direction,
						new Date());
				
				this.globalTraceManager.save(globalTrace);
			}
		}

		if (userTrace != null) {
			if (userTrace.getInTraffic()) {
				return "TRAFFIC%";
			} else {
				return "NOTRAFFIC%"; // NO TRAFFIC
			}
		} else
			return "NOTRAFFIC%"; // NO TRAFFIC
	}
	
	public String listDots() {
		List<UserTrace> lstUserTrace = this.userTraceManager.findAllAndOrderBy("lastLocationDate", true);
		
		StringBuffer x = new StringBuffer();
		StringBuffer y = new StringBuffer();
		
		for (UserTrace userTrace : lstUserTrace) {
			x.append(userTrace.getLastLocationDate().getTime()).append(',');
			y.append(userTrace.getRatioPond()).append(',');
		}
		 
		if (x.length() != 0)
			x.deleteCharAt(x.length() - 1);
		if (y.length() != 0) {
			y.deleteCharAt(y.length() - 1);
		}
		
		return x.toString() + '|' + y.toString();
	}
	
	public String getPositions(String userId) {
		StringBuilder sb = new StringBuilder();
		
		for (Entry<String, Location> entry : locations.entrySet()) {
			Location loc = entry.getValue();

					sb.append(loc.getLatitude()).append(',').
					append(loc.getLongitude()).append(',').
					append(loc.getSpeed()).append(',').
					append(loc.getCourse()).append(',').
					append(loc.getIdUser()).append(',').
					append(loc.isInTraffic()).append('&');
				 
			  
		}
		
		return sb.toString();
		
	}

	public UserManager getUserManager() {
		return userManager;
	}

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public UserTraceManager getUserTraceManager() {
		return userTraceManager;
	}

	@Autowired
	public void setUserTraceManager(UserTraceManager userTraceManager) {
		this.userTraceManager = userTraceManager;
	}

	public GlobalTraceManager getGlobalTraceManager() {
		return globalTraceManager;
	}

	@Autowired
	public void setGlobalTraceManager(GlobalTraceManager globalTraceManager) {
		this.globalTraceManager = globalTraceManager;
	}
}
