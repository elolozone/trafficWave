package com.elolozone.trafficServlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.elolozone.constants.Geo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.elolozone.trafficstore.GlobalTrace;
import com.elolozone.trafficstore.ToStore;
import com.elolozone.trafficstore.Traffic;
import com.elolozone.trafficstore.UserStat;
import com.elolozone.trafficstore.UserTrace;
import com.elolozone.trafficwave.constants.IConstants;


//pour les queues : 

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.*;


@SuppressWarnings("serial")
public class TrafficServletServlet extends HttpServlet {
	


	//List<Location> locations_old;
	HashMap<String, Location> locations = new HashMap<String, Location>();

	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		resp.setContentType("text/plain");
		
		
		if (req.getParameter("getNews")!=null)
		{ 
			String idUser = req.getParameter("id");
		
			 UserTrace userTrace = new UserTrace(
					    idUser	, 
						null, //Integer lastIdSession, 
						null, //Integer totalTime, 
						null, //Integer runningMeter, 
						null, //Date lastConnectionTime, 
						null, //String email, 
						null, //String firstName, 
						null, //String lastName, 
						null, //String city, 
						null //String postalCode	
						);


				userTrace = ToStore.storeOne(userTrace);
				
				//Location location = new Location();

				//location.idUser = idUser;
				//location.idSession = userTrace.getLastIdSession();
				
				
			/*	Location previousL = locations.get(location.idUser);
				if (previousL!=null) {
					location.inTraffic = previousL.inTraffic;
					location.idSession = previousL.idSession;
				}
				else*/
				//locations.put(location.idUser, location);
			
			resp.getWriter().print(
					//"\nDerniere connection : "+userTrace.getLastConnectionTime().toString()+
					//"\n\nNews du 01/03/2010:Un correctif cote serveur vient d'etre effectue, permettant de corriger le probleme des bouchons en ville\n"+
					"\n\nActuellement : "+String.valueOf(locations.size()) + " utilisateurs connectes."+
					"\n\nVous avez utilise "+userTrace.getLastIdSession().toString() +" fois l'application."+ 
					"\n\n"+
					//""? Connectes\n? bouchons en cours\n? km connus\n\n"+
					//"\nBienvenue dans la version beta test 1.0 de trafficWave.\nServeur API version 4.0\n"+
					//"Cette premiere version permet de tester la fonctionnalite -bouchon-\n\n" +
					"--> L'application est en beta: utilisez la quand vous pouvez pour permettre d'alimenter les bases de donnees route.Merci!$"
					+userTrace.getLastIdSession().toString() +"&");
		return;
		}
			
		
		
		if (req.getParameter("trafficAlertAnswer")!=null) { 
			boolean traffic = Boolean.getBoolean(req.getParameter("traffic"));
			String idUser = req.getParameter("idUser");
			resp.getWriter().print("idUser : "+idUser + "\n");
			resp.getWriter().print("traffic : "+traffic + "\n");
		}
		
		if (req.getParameter("getPaths")!=null) { 
			String idUser = req.getParameter("idUser");
			
			Integer idSession =  Integer.valueOf(req.getParameter("idSession"));
			 
			
			 
			//Collection<UserStat> userStats = ToStore.listPathTrace (idUser,idSession);
			Collection<UserStat>  userStats = ToStore.listUserStatAsc(idSession, idUser); 
			
			
			if (userStats != null) 
			{
				for (Iterator iter = userStats.iterator(); iter.hasNext();) 
				{ 
					UserStat userStat = (UserStat) iter.next();
					
					
					Date now = new Date();
						
					resp.getWriter().print(
							userStat.getPosLa().toString()+","+
							userStat.getPosLo().toString()
							);
					
				
					resp.getWriter().print("\n");
				}
			}
			else
			{resp.getWriter().print("0,0\n");}
			return;
		}
		

		if (req.getParameter("identifyTrafficJob")!=null) { 


			       // Queue queue = QueueFactory.getDefaultQueue();
			       //  queue.add(url("/imagineUserDestination=1").param("key", "clef" ));
			        

		
			Collection<UserStat> userStats = ToStore.listUserInTraffic(IConstants.ACTIF_USER_MAX_SEC,IConstants.DUREE_TODECLARE_BOUCHON_SEC);
			//resp.getWriter().print("biloutetest,date1,date2,NORTH,50,3,12,123&\n");
			//resp.getWriter().print("biloutetest,date1,date2,NORTH,51,2,8,250&\n");
			if (userStats != null) 
			{
				for (Iterator iter = userStats.iterator(); iter.hasNext();) 
				{ 
					UserStat userStat = (UserStat) iter.next();
					
					Location location =  locations.get(userStat.getIdUser());
					Date now = new Date();
						
					resp.getWriter().print(
							userStat.getIdUser() + ","+
							userStat.getLastLocationDate().toString() + "," + 
							userStat.getInTrafficDeclaredTime().toString()+","+
							userStat.getPoleDirection()+","+ 
							userStat.getPosLa().toString()+","+
							userStat.getPosLo().toString()+","+
							userStat.getSpeed().toString()+","+
							String.valueOf((now.getTime() - userStat.getInTrafficDeclaredTime().getTime())/1000)
							);
					
					if (location!=null)	if (location.inTraffic) resp.getWriter().print(",inTraffic"); else resp.getWriter().print(",notInTraffic");
					resp.getWriter().print("&\n");
				} 
			}
			else resp.getWriter().print("NO TRAFFICJAM");


			return;
		}

		if (req.getParameter("imagineUserDestination")!=null) { 

			String idUser = req.getParameter("idUser");
			// TODO virer l'idUser
			
			
			
			//idUser = "c4c52259d75d64e2c9b8a1c4de26532d22daff0c";
			//idUser = "c68263c0f221c0886e7590c72331c68b5e5f7aa3"; //gilles
			
			Location actualLocation =  locations.get(idUser);
			
				if (actualLocation == null) return;
				//TODO : nombre de jours historique des itinŽraires
			Collection<UserStat> userStats = ToStore.listUserDestination(60*60*24*30, idUser,Double.valueOf(actualLocation.posLa),Double.valueOf(actualLocation.posLo));
			//int tempsTrajetSec = (int)(originUserStatsResult.get(userStat.getIdSession()).getLastLocationDate().getTime()/1000);
			
			if (userStats != null) 
			{
				for (Iterator iter = userStats.iterator(); iter.hasNext();) 
				{ 
					UserStat userStat = (UserStat) iter.next();
					
					//Location location =  locations.get(userStat.getIdUser());
					Date now = new Date();
						
					
					// iti 1, iti 2 
					resp.getWriter().print(
							userStat.getIdUser() + ","+
							userStat.getLastLocationDate().toString() + "," + 
							"0,"+
							"0,"+ 
							userStat.getPosLa().toString()+","+
							userStat.getPosLo().toString()+","+
							//TODO : constante de temps ˆ ajouter , ici 15%
							String.valueOf((int)((userStat.getLastLocationDate().getTime() - userStat.getStartLocationDate().getTime())/1000)*1.15)+","+
							String.valueOf(60)+","+
							userStat.getIdSession().toString()
							);
					
					//if (location!=null)	if (location.inTraffic) resp.getWriter().print(",inTraffic"); else resp.getWriter().print(",notInTraffic");
					resp.getWriter().print(",null&\n");
				}
			}
			else resp.getWriter().print("NO ITI");


			return;
		}

		


		if (req.getParameter("deleteAll")!=null) {ToStore.deleteAll(); return;}
		if (req.getParameter("deleteUserStat")!=null) {ToStore.deleteUserStat(); return;}

		if (req.getParameter("listGlobalTrace")!=null){
			List<GlobalTrace> globalTraces = ToStore.listGlobalTrace();
			for (Iterator iter = globalTraces.iterator(); iter.hasNext();) 
			{ 
				GlobalTrace globalTrace = (GlobalTrace) iter.next();

				resp.getWriter().print(
						globalTrace.getPosLa()+","+
						globalTrace.getPosLo()+","+
						globalTrace.getSumSpeed()+","+
						globalTrace.getNbUser()+","+
						globalTrace.getPoleDirection ()+ ","+
						globalTrace.getMaxSpeed() +  ",&\n"); //","+globalTrace.getMaxSpeed()+  "&\n");

			}

			return;
		}

		
		
		if (req.getParameter("listUserStat")!=null){
			List<UserStat> userStats = ToStore.listUserStat();
			resp.getWriter().print("Latitude,Longitude,maxSpeed,Ratio,RatioPond,AvgSpeed,time,surfDiff,surfVmoy,idUser,speed,umSurfVmoy\n");
			for (Iterator iter = userStats.iterator(); iter.hasNext();) 
			{ 
				UserStat userStat = (UserStat) iter.next();

				resp.getWriter().print(
						userStat.getPosLa() +","+
						userStat.getPosLo() +","+
						userStat.getMaxSpeed() +","+
						userStat.getRatio() +","+
						userStat.getRatioPond() +","+
						userStat.getAvgSpeed() +","+ 
						userStat.getLastLocationDate().getTime() + "," +
						userStat.getSurDiff() + "," +
						userStat.getSurfVmoy() + "," +
						userStat.getIdUser() + "," +
						userStat.getSpeed()+ "," +
						userStat.getSumSurfVmoy()+
				"\n"); 

			}

			return;
		}


		if (req.getParameter("averageSpot")!=null)
		{
			Location location = new Location();
			location.posLo = req.getParameter("posLo");
			location.posLa = req.getParameter("posLa");
			location.course = req.getParameter("course");

			GlobalTrace globalTrace = ToStore.getAverageSpot (Double.valueOf(location.posLo), Double.valueOf(location.posLa), Double.valueOf(location.course));
			if (globalTrace!=null) 
			{
				Double vitMoy = globalTrace.getSumSpeed()/globalTrace.getNbUser()*3.6f;
				Double vitMax ;
				if (globalTrace.getMaxSpeed() != null) 
				{	 vitMax = globalTrace.getMaxSpeed()*3.6f;
				resp.getWriter().print(vitMoy.toString()+","+vitMax.toString()+"&");
				}
				else resp.getWriter().print("max inconnu&");
			} else resp.getWriter().print("Zone inconnue&");
			return;

		}


		if (req.getParameter("trafficQuestion")!=null)
		{

			Double dist;
			Double minDist; 
			dist = 0.0;
			minDist = -1.0;

			Location l = null;
			for ( Iterator iter = locations.entrySet().iterator(); iter.hasNext(); )
			{ 
				Map.Entry enti = (Map.Entry) iter.next();
				Location li = (Location) enti.getValue();
				dist = 0.0;

				Double dposLai = Double.parseDouble(li.posLa) ; 
				Double dposLoi = Double.parseDouble(li.posLo) ;

				//
				GlobalTrace globalTrace = ToStore.getAverageSpot (dposLoi, dposLai, Double.valueOf(li.course));
				if (globalTrace!=null) 
				{ 

					Double vitMax = globalTrace.getMaxSpeed()*3.6f;
					Double vit = Double.valueOf(li.speed)*3.6f ;
					if (  vit  < vitMax)

					{Double ratioV = new Double(0.0f);
					ratioV = vit/vitMax  *100.0f;
					resp.getWriter().print(li.posLa+","
							+li.posLo+","
							+ratioV.toString()+"&");
					} 
				} 


				//


				/*
				for ( Iterator jter = locations.entrySet().iterator(); jter.hasNext(); )
				{ 
					Map.Entry entj = (Map.Entry) jter.next();
					Location lj = (Location) entj.getValue();


					Double dposLaj = Double.parseDouble(lj.posLa) ;
					Double dposLoj = Double.parseDouble(lj.posLo) ;
					dist = dist +Math.sqrt( Math.pow(dposLai-dposLaj,2) + Math.pow(dposLoi-dposLoj,2) );



				}
				if (minDist==-1.0) {minDist = dist; l=li;} else if (minDist>dist) {minDist=dist; l = li;} 
				 */
			}


			/*
			 * if (l != null)	resp.getWriter().print(l.posLa+","+l.posLo+"&"); else resp.getWriter().print("problem!");
			 */

			return;	
		}
		if (req.getParameter("reset")!=null){ locations.clear(); return;}

		//nouveau
		//		
		if (req.getParameter("sendTagWave")!=null){
			
		}
		if (req.getParameter("sendPositionOnly")!=null)
		{
		Location location = new Location();


		//(Double posLo, Double posLa , Double speed, String idUser, Double course, Date locationDate) {


		location.idUser = req.getParameter("id");
		location.posLo = req.getParameter("posLo");
		location.posLa = req.getParameter("posLa");
		location.speed = req.getParameter("speed");
		location.course = req.getParameter("course");
		location.street = req.getParameter("street"); 
		
		location.idSession =  Integer.valueOf(req.getParameter("idSession"));
		location.postalCode = req.getParameter("postalCode");
		location.inTrafficUser =  req.getParameter("traffic");
	
		UserStat userStat = null ;
		if (location.idUser != null && location.posLa != null && location.posLo != null && location.speed != null && location.course!= null)     
		{	Location previousL = locations.get(location.idUser);
		    
			if (previousL!=null) {
				location.inTraffic = previousL.inTraffic;
				//location.idSession = previousL.idSession;
			}
			//
			locations.put(location.idUser, location);

			/*
		UserTrace userTrace = new UserTrace(
				Double.valueOf(location.posLo),
				Double.valueOf(location.posLa),
				Double.valueOf(location.speed),
				location.idUser,
				Double.valueOf(location.course),
				new Date());
			 */

			Geo.Direction direction =  ToStore.getDirection(Double.valueOf(location.course));
		
			
			
			//  on rŽcup�re la vitesse moyenne et max et on enregistre la position
			GlobalTrace globalTraceAvgSport = ToStore.getAverageSpot (Double.valueOf(location.posLo), Double.valueOf(location.posLa), Double.valueOf(location.course));
			if (globalTraceAvgSport!=null) {
				Double maxSpeed = globalTraceAvgSport.getMaxSpeed();
				Double moySpeed = globalTraceAvgSport.getSumSpeed()/globalTraceAvgSport.getNbUser();
				boolean inTrafficUser;
				if (location.inTrafficUser=="TRUE") inTrafficUser=true; else inTrafficUser=false;
						
				 
				 userStat = new UserStat(
						location.idUser,
						location.idSession,
						Double.valueOf(location.posLo),
						Double.valueOf(location.posLa),
						moySpeed,
						maxSpeed,
						Double.valueOf(location.speed),
						direction,
						new Date(), false, null,inTrafficUser);


				userStat = ToStore.storeOne(userStat);
				//resp.getWriter().print(dots + "£££");

			}



			//List<String> idUsers = new ArrayList<String> ();
			//idUsers.add(location.idUser);




			GlobalTrace globalTrace = new GlobalTrace(
					Double.valueOf(location.posLo),
					Double.valueOf(location.posLa),
					Double.valueOf(location.speed),
					Double.valueOf(location.speed),
					direction,
					new Date());

			/*
		if (Double.valueOf(location.speed)>-1 && Double.valueOf(location.course)>-1) 
			ToStore.storeOne(userTrace);
			 */

			if (Double.valueOf(location.speed)>-1 && Double.valueOf(location.course)>-1) 
				ToStore.storeOne(globalTrace);
		}

		//Location ll =  locations.get(location.idUser);
		if (userStat != null)
		if (userStat.getInTraffic()) 
			{
			resp.getWriter().print("TRAFFIC%"); 
			//Traffic traffic;
			//ToStore.storeOne(traffic);
			}
			else 
			{
				resp.getWriter().print("NOTRAFFIC%"); // NO TRAFFIC
			}
		else resp.getWriter().print("NOTRAFFIC%"); // NO TRAFFIC

		
//		GlobalTrace globalTrace = ToStore.getAverageSpot (Double.valueOf(location.posLo), Double.valueOf(location.posLa), Double.valueOf(location.course));
//		
//		if (globalTrace!=null) 
//		{ resp.getWriter().print("GlobalTrace not null;"); 
//		Double vitMoy = globalTrace.getSumSpeed()/globalTrace.getNbUser()*3.6;
//		if (Double.valueOf(location.speed)     < vitMoy *0.25f)
//		{
//			resp.getWriter().print(vitMoy.toString()+"RED&");} 
//		else resp.getWriter().print("GREEN&");
//		} else resp.getWriter().print("GREEN&");
//
//
//		resp.getWriter().print("OK"); 
		
		}

		if (req.getParameter("getDots")!=null) resp.getWriter().print(ToStore.getDot()); 


		if (req.getParameter("sendPositionOnly")==null)
			for ( Iterator iter = locations.entrySet().iterator(); iter.hasNext(); )
			{
				Map.Entry ent = (Map.Entry) iter.next();
				//String  clŽ = ent.getKey();
				Location l = (Location) ent.getValue();
				//DEMO
				//resp.getWriter().print("37.33169,-122.03073,10.000000,15.000000,1 Infinite Loop,95014,00000000-0000-1000-8000-0017F2F054B9&37.33180,-122.03084,100.000000,45.000000,1 Infinite Loop,95014,00000000-0000-1000-8000-0017F2F054Bw&37.33171,-122.03075,200.000000,100.000000,1 Infinite Loop,95014,00000000-0000-1000-80000017F2F054B9&");
				//resp.getWriter().print(l.posLa+","+l.posLo+","+l.speed+","+l.course+","+l.street+","+l.postalCode+","+l.idUser+"&");
				resp.getWriter().print(l.posLa+","+l.posLo+","+l.speed+","+l.course+","+l.street+","+l.postalCode+","+l.idUser+","+l.inTraffic+"&");
				
			}
		else {

		}



	}
}
