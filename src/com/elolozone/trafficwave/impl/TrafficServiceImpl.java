package com.elolozone.trafficwave.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Path;

import com.elolozone.constants.Geo;
import com.elolozone.constants.IConstants;
import com.elolozone.trafficstore.GlobalTrace;
import com.elolozone.trafficstore.ToStore;
import com.elolozone.trafficstore.UserStat;
import com.elolozone.trafficstore.UserTrace;
import com.elolozone.trafficwave.api.TrafficService;
import com.elolozone.trafficwave.domain.Location;

/**
 * The implementation of {@link TrafficService}.
 * 
 * @author brasseld@gmail.com
 * @since 19-03-2010 
 */
@Path("/traffic")
public class TrafficServiceImpl implements TrafficService {

	/**
	 * Hold current users location.
	 */
	private Map<String, Location> locations = new HashMap<String, Location>();
	
	/**
	 * {@inheritDoc}
	 */
	public String getNews(String userId) {
		UserTrace userTrace = ToStore.storeOne(new UserTrace(userId));
		
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
		Collection<UserStat>  userStats = ToStore.listUserStatAsc(sessionId, userId); 
		
		if (userStats != null)  {
			StringBuilder sb = new StringBuilder();
			
			for (UserStat userStat : userStats) {
				sb.append(userStat.getPosLa()).append(',').append(userStat.getPosLo()).append('\n');
			}
			
			return sb.toString();
		}
		
		return "0,0";
	}

	/**
	 * {@inheritDoc}
	 */
	public String identifyTrafficJob() {
		Collection<UserStat> userStats = ToStore.listUserInTraffic(IConstants.ACTIF_USER_MAX_SEC, IConstants.DUREE_TODECLARE_BOUCHON_SEC);

		if (userStats != null) {
			
			StringBuilder sb = new StringBuilder();
			
			for (UserStat userStat : userStats) {
				Location location =  locations.get(userStat.getIdUser());
				
				sb.append(userStat.getIdUser()).append(',').
					append(userStat.getLastLocationDate()).append(',').
					append(userStat.getInTrafficDeclaredTime()).append(',').
					append(userStat.getPoleDirection()).append(',').
					append(userStat.getPosLa()).append(',').
					append(userStat.getPosLo()).append(',').
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
		Collection<UserStat> userStats = ToStore.listUserDestination(60*60*24*30, userId,Double.valueOf(actualLocation.getPosLa()),Double.valueOf(actualLocation.getPosLo()));

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
		
		List<GlobalTrace> globalTraces = ToStore.listGlobalTrace();
		
		for (GlobalTrace globalTrace : globalTraces) {

			sb.append(globalTrace.getPosLa()).append(',').
				append(globalTrace.getPosLo()).append(',').
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
		
		List<UserStat> userStats = ToStore.listUserStat();
		
		for (UserStat userStat : userStats) {
			sb.append(userStat.getPosLa()).append(',').
				append(userStat.getPosLo()).append(',').
				append(userStat.getMaxSpeed()).append(',').
				append(userStat.getRatio()).append(',').
				append(userStat.getRatioPond()).append(',').
				append(userStat.getAvgSpeed()).append(',').
				append(userStat.getLastLocationDate().getTime()).append(',').
				append(userStat.getSurDiff()).append(',').
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

			Double dposLai = Double.parseDouble(loc.getPosLa()) ; 
			Double dposLoi = Double.parseDouble(loc.getPosLo()) ;

			GlobalTrace globalTrace = ToStore.getAverageSpot (dposLoi, dposLai, Double.valueOf(loc.getCourse()));
			
			if (globalTrace != null) {
				Double vitMax = globalTrace.getMaxSpeed() * 3.6f;
				Double vit = Double.valueOf(loc.getSpeed()) * 3.6f ;
				
				if (vit < vitMax) {
					Double ratioV = vit / vitMax * 100.0f;
					
					sb.append(loc.getPosLa()).append(',').append(loc.getPosLo()).append(',').append(ratioV).append('&');
				} 
			} 
		}
		
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public String sendPositionOnly(String id, String longitude, String latitude, String speed, String course, String street,
			int idSession, String postalCode, String traffic) {
		Location location = new Location();

		location.setIdUser(id);
		location.setPosLo(longitude);
		location.setPosLa(latitude);
		location.setSpeed(speed);
		location.setCourse(course);
		location.setStreet(street) ;
		location.setIdSession(idSession);
		location.setPostalCode(postalCode);
		location.setInTrafficUser(traffic);

		UserStat userStat = null;
		
		if (location.getIdUser() != null && location.getPosLa() != null && location.getPosLo() != null && location.getSpeed() != null && location.getCourse() != null)     
		{
			Location previousL = locations.get(location.getIdUser());
		    
			if (previousL!=null) {
				location.setInTraffic(previousL.isInTraffic());
			}

			locations.put(location.getIdUser(), location);

			Geo.Direction direction =  ToStore.getDirection(Double.valueOf(location.getCourse()));

			//  on récupére la vitesse moyenne et max et on enregistre la position
			GlobalTrace globalTraceAvgSport = ToStore.getAverageSpot (Double.valueOf(location.getPosLo()), Double.valueOf(location.getPosLa()), Double.valueOf(location.getCourse()));
			
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
						Double.valueOf(location.getPosLo()),
						Double.valueOf(location.getPosLa()),
						moySpeed,
						maxSpeed,
						Double.valueOf(location.getSpeed()),
						direction,
						new Date(), false, null, inTrafficUser);

				userStat = ToStore.storeOne(userStat);
			}

			GlobalTrace globalTrace = new GlobalTrace(
					Double.valueOf(location.getPosLo()),
					Double.valueOf(location.getPosLa()),
					Double.valueOf(location.getSpeed()),
					Double.valueOf(location.getSpeed()),
					direction,
					new Date());

			if (Double.valueOf(location.getSpeed()) > -1 && Double.valueOf(location.getCourse()) > -1) 
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
}
