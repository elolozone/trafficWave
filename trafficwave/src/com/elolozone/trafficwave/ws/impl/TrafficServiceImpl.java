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

import com.elolozone.constants.Geo;
import com.elolozone.trafficstore.GlobalTrace;
import com.elolozone.trafficstore.ToStore;
import com.elolozone.trafficstore.UserStat;
import com.elolozone.trafficwave.constants.IConstants;
import com.elolozone.trafficwave.manager.api.GlobalTraceManager;
import com.elolozone.trafficwave.manager.api.UserStatManager;
import com.elolozone.trafficwave.manager.api.UserTraceManager;
import com.elolozone.trafficwave.model.Location;
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
	private UserTraceManager userTraceManager;
	private UserStatManager userStatManager;
	
	/**
	 * Hold current users location.
	 */
	private Map<String, Location> locations = new HashMap<String, Location>();
	
	/**
	 * {@inheritDoc}
	 */
	public String getNews(String userId) {
		com.elolozone.trafficwave.model.UserTrace userTrace = new com.elolozone.trafficwave.model.UserTrace(userId);
		this.getUserTraceManager().save(userTrace);
		
		return 
				"\n\nActuellement : "+String.valueOf(locations.size()) + " utilisateurs connectes."+
				"\n\nVous avez utilise "+userTrace.getLastIdSession().toString() +" fois l'application."+ 
				"\n\n"+
				"--> L'application est en beta: utilisez la quand vous pouvez pour permettre d'alimenter les bases de donnees route.Merci!$"
				+userTrace.getLastIdSession().toString() + "&";
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPaths(String userId, int sessionId) {
		List<com.elolozone.trafficwave.model.UserStat> userStats = this.getUserStatManager().findBySessionAndUser(sessionId, userId); 
		
		if (userStats != null)  {
			StringBuilder sb = new StringBuilder();
			
			for (com.elolozone.trafficwave.model.UserStat userStat : userStats) {
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
		List<com.elolozone.trafficwave.model.UserStat> userStats = this.getUserStatManager().findUserInTraffic(IConstants.ACTIF_USER_MAX_SEC, IConstants.DUREE_TODECLARE_BOUCHON_SEC);

		if (userStats != null) {
			
			StringBuilder sb = new StringBuilder();
			
			for (com.elolozone.trafficwave.model.UserStat userStat : userStats) {
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
		
		//TODO : nombre de jours historique des itinéraires
		Collection<UserStat> userStats = ToStore.listUserDestination(60*60*24*30, userId, actualLocation.getLatitude(), actualLocation.getLongitude());

		if (userStats != null) 
		{
			StringBuilder sb = new StringBuilder();
			
			for (UserStat userStat : userStats) 
			{ 
				// iti 1, iti 2
				sb.append(userStat.getIdUser()).append(',').
					append(userStat.getLastLocationDate()).append(',').
					append("0,").
					append("0,").
					append(userStat.getPosLa()).append(',').
					append(userStat.getPosLo()).append(',').
					append((int)((userStat.getLastLocationDate().getTime() - userStat.getStartLocationDate().getTime())/1000)*1.15).append(',').
					append(60).append(',').
					append(userStat.getIdSession()).
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
		
		List<com.elolozone.trafficwave.model.GlobalTrace> globalTraces = this.getGlobalTraceManager().findAll();
		
		for (com.elolozone.trafficwave.model.GlobalTrace globalTrace : globalTraces) {

			sb.append(globalTrace.getLatitude()).append(',').
				append(globalTrace.getLongitude()).append(',').
				append(globalTrace.getSumSpeed()).append(',').
				append(globalTrace.getNbUser()).append(',').
				append(globalTrace.getPoleDirection()).append(',').
				append(globalTrace.getMaxSpeed()).append(",&\n");
		}
		
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public String listUserStat() {
		StringBuilder sb = new StringBuilder("Latitude,Longitude,maxSpeed,Ratio,RatioPond,AvgSpeed,time,surfDiff,surfVmoy,idUser,speed,umSurfVmoy\n");
		
		List<com.elolozone.trafficwave.model.UserStat> userStats = this.getUserStatManager().findAllAndOrderBy("lastLocationDate", Boolean.FALSE);
		
		for (com.elolozone.trafficwave.model.UserStat userStat : userStats) {
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
		GlobalTrace globalTrace = ToStore.getAverageSpot(longitude, latitude, course);
		
		if (globalTrace != null) {
			// TODO: Some explanation about this calc, what is 3.6f ?
			Double vitMoy = globalTrace.getSumSpeed() / globalTrace.getNbUser() * 3.6f;
			
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

			GlobalTrace globalTrace = ToStore.getAverageSpot (loc.getLongitude(), loc.getLatitude(), loc.getCourse());
			
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
	public String sendPositionOnly(String id, double longitude, double latitude, double speed, double course, String street,
			int idSession, String postalCode, String traffic) {
		Location location = new Location();

		location.setIdUser(id);
		location.setLongitude(longitude);
		location.setLatitude(latitude);
		location.setSpeed(speed);
		location.setCourse(course);
		location.setStreet(street) ;
		location.setIdSession(idSession);
		location.setPostalCode(postalCode);
		location.setInTrafficUser(traffic);

		UserStat userStat = null;
		
		if (location.getIdUser() != null && location.getLatitude() != null && location.getLongitude() != null && location.getSpeed() != null && location.getCourse() != null)     
		{
			Location previousL = locations.get(location.getIdUser());
		    
			if (previousL!=null) {
				location.setInTraffic(previousL.isInTraffic());
			}

			locations.put(location.getIdUser(), location);

			Geo.Direction direction =  ToStore.getDirection(location.getCourse());

			//  on récupére la vitesse moyenne et max et on enregistre la position
			GlobalTrace globalTraceAvgSport = ToStore.getAverageSpot (location.getLatitude(), location.getLongitude(), location.getCourse());
			
			if (globalTraceAvgSport != null) {
				Double maxSpeed = globalTraceAvgSport.getMaxSpeed();
				Double moySpeed = globalTraceAvgSport.getSumSpeed() / globalTraceAvgSport.getNbUser();
				boolean inTrafficUser;
				
				if (location.getInTrafficUser().equals("TRUE")) 
					inTrafficUser = true;
				else 
					inTrafficUser = false;
						
				userStat = new UserStat(
						location.getIdUser(),
						location.getIdSession(),
						location.getLongitude(),
						location.getLatitude(),
						moySpeed,
						maxSpeed,
						location.getSpeed(),
						direction,
						new Date(), false, null, inTrafficUser);

				userStat = ToStore.storeOne(userStat);
			}

			GlobalTrace globalTrace = new GlobalTrace(
					location.getLongitude(),
					location.getLatitude(),
					location.getSpeed(),
					location.getSpeed(),
					direction,
					new Date());

			if (location.getSpeed() > -1 && location.getCourse() > -1) 
				ToStore.storeOne(globalTrace);
		}

		if (userStat != null) {
			if (userStat.getInTraffic()) {
				return "TRAFFIC%";
			} else {
				return "NOTRAFFIC%"; // NO TRAFFIC
			}
		} else
			return "NOTRAFFIC%"; // NO TRAFFIC
	}

	public UserTraceManager getUserTraceManager() {
		return userTraceManager;
	}

	@Autowired
	public void setUserTraceManager(UserTraceManager userTraceManager) {
		this.userTraceManager = userTraceManager;
	}

	public UserStatManager getUserStatManager() {
		return userStatManager;
	}

	@Autowired
	public void setUserStatManager(UserStatManager userStatManager) {
		this.userStatManager = userStatManager;
	}

	public GlobalTraceManager getGlobalTraceManager() {
		return globalTraceManager;
	}

	public void setGlobalTraceManager(GlobalTraceManager globalTraceManager) {
		this.globalTraceManager = globalTraceManager;
	}
}
