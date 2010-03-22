package com.elolozone.trafficstore;

import java.math.BigDecimal;
import com.elolozone.constants.IConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.elolozone.trafficServlet.Location;
import com.elolozone.trafficstore.UserTrace;
import com.elolozone.trafficstore.GlobalTrace;
import com.elolozone.trafficstore.UserStat;
import com.elolozone.trafficstore.PMF;
import java.util.List;
import com.elolozone.constants.Geo;

public class ToStore {

	static public Double gridConvertion(Double pos) {
		BigDecimal bdPos;
		bdPos = BigDecimal.valueOf(pos);
		bdPos = bdPos.setScale(4, BigDecimal.ROUND_DOWN);
		return Double.valueOf(bdPos.doubleValue());
	}


	static public Double roundDown(Double pos,int n) {
		BigDecimal bdPos;
		bdPos = BigDecimal.valueOf(pos);
		bdPos = bdPos.setScale(n, BigDecimal.ROUND_DOWN);
		return Double.valueOf(bdPos.doubleValue());
	}
	

	static public Geo.Direction getDirection(Double course) {

		if (course >= 45 && course < 135)
			return Geo.Direction.EAST;
		if (course >= 135 && course < 225)
			return Geo.Direction.SOUTH;
		if (course >= 225 && course < 315)
			return Geo.Direction.WEST;
		if (course >= 315 || course < 45)
			return Geo.Direction.NORTH;
		return Geo.Direction.NULL;
	}

	static public void deleteUserStat() {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		String query = "select from " + UserStat.class.getName();
		List<UserStat> userStats = (List<UserStat>) pm.newQuery(query)
		.execute();

		for (Iterator iter = userStats.iterator(); iter.hasNext();) {
			UserStat userStat = (UserStat) iter.next();

			pm.deletePersistent(userStat);
		}

		pm.close();
	}

	static public void deleteAll() {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		String query = "select from " + GlobalTrace.class.getName();
		List<GlobalTrace> globalTraces = (List<GlobalTrace>) pm.newQuery(query)
		.execute();

		for (Iterator iter = globalTraces.iterator(); iter.hasNext();) {
			GlobalTrace globalTrace = (GlobalTrace) iter.next();

			pm.deletePersistent(globalTrace);
		}
		query = "select from " + UserTrace.class.getName();
		List<UserTrace> userTraces = (List<UserTrace>) pm.newQuery(query)
		.execute();

		for (Iterator iter = userTraces.iterator(); iter.hasNext();) {
			UserTrace userTrace = (UserTrace) iter.next();

			pm.deletePersistent(userTrace);
		}
		pm.close();
	}

	static public GlobalTrace getAverageSpot(Double posLo, Double posLa,
			Double course) {
		posLo = gridConvertion(posLo);
		posLa = gridConvertion(posLa);
		Geo.Direction direction = getDirection(course);

		PersistenceManager pm = PMF.get().getPersistenceManager();

		String query = "select from " + GlobalTrace.class.getName()
		+ " where  posLo==" + posLo.toString() + " && posLa=="
		+ posLa.toString() + " && poleDirection=='"
		+ direction.toString() + "'";

		List<GlobalTrace> globalTraces = (List<GlobalTrace>) pm.newQuery(query)
		.execute();

		// pm.close();
		if (globalTraces.size() > 0)
			return globalTraces.get(0);
		return null;
	}

	public static List<GlobalTrace> listGlobalTrace() {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		String query = "select from " + GlobalTrace.class.getName();
		List<GlobalTrace> globalTraces = (List<GlobalTrace>) pm.newQuery(query)
		.execute();
		return globalTraces;

	}

	public static List<UserStat> listUserStat() {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		String query = "select from " + UserStat.class.getName() + " order by lastLocationDate desc";
		List<UserStat> userStats = (List<UserStat>) pm.newQuery(query)
		.execute();
		return userStats;

	}

	public static List<UserStat> listUserStatAsc(int idSession, String idUsera) {
		PersistenceManager pm = PMF.get().getPersistenceManager();  
		/* 
		String query = "select from " + UserStat.class.getName() + " order by lastLocationDate asc";
		List<UserStat> userStats = (List<UserStat>) pm.newQuery(query)
				.execute();
		return userStats;
		 */
		
		// count
		/*Query query = pm.newQuery("javax.jdo.query.SQL", "SELECT count(*) FROM MYTABLE");
		List results = (List) query.execute();
		Integer tableSize = (Integer) result.iterator().next();*/
		//
		
		
		Date nowDate = new Date();
		// TODO : récupération du graphique (ici 24heure)
		Date selectDate = new Date(nowDate.getTime() - 1000 * 60 * 60*24*365);


		/*Query query2 = pm.newQuery(UserStat.class,
				"lastLocationDate > selectDate && idUser==\""
						+ userStat.getIdUser() + "\"");*/
		String sqlReq;
		if (idSession==-1)
			 sqlReq ="lastLocationDate > selectDate && idUser==\""+ idUsera + "\"";	
		else
		 sqlReq ="lastLocationDate > selectDate && idSession=="+String.valueOf(idSession) + " &&  idUser==\""+ idUsera + "\"";
		Query query2 = pm.newQuery(UserStat.class,sqlReq );
		query2.declareImports("import java.util.Date");
		query2.declareParameters("Date selectDate");
		query2.setOrdering("lastLocationDate ascending");
		
		

		List<UserStat> userStats = (List<UserStat>) pm.newQuery(query2)
		.execute(selectDate);
		
		// on refait la requete en selectionnant
		long min = 0;
		long max = Long.MAX_VALUE;
	
		if (userStats.size()>IConstants.NBRE_POINT_TOGRAPH) min=userStats.size()-IConstants.NBRE_POINT_TOGRAPH;
		query2.setRange(min,max);
		
		userStats = (List<UserStat>) pm.newQuery(query2)
		.execute(selectDate);
		
		return userStats;

	}

	public static Collection<UserStat> listUserInTraffic(int actifUserSec, int dureeBouchonSec) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Date nowDate = new Date();
		
		Date selectDateBouchon = new Date(nowDate.getTime() - 1000 * dureeBouchonSec);
		Date selectDateActifUser = new Date(nowDate.getTime() - 1000 * actifUserSec);



 
		Query query2 = pm.newQuery(UserStat.class,
				"inTrafficDeclaredTime <= selectDateBouchon && inTraffic==true");// && lastLocation==true");

		query2.declareImports("import java.util.Date");
		query2.declareParameters("Date selectDateBouchon");
		query2.setOrdering("inTrafficDeclaredTime descending");

		List<UserStat> userStats = (List<UserStat>) pm.newQuery(query2)
		.execute(selectDateBouchon);

		if (userStats.size()>0){

			//ArrayList<UserStat> userStatsResult = new ArrayList<UserStat>(); 
			HashMap<String, UserStat> userStatsResult = new HashMap<String, UserStat>();


			for (Iterator iter = userStats.iterator(); iter.hasNext();) 
			{ 
				UserStat userStat = (UserStat) iter.next();
				if (!userStatsResult.containsKey(userStat.getIdUser()) && userStat.getLastLocationDate().after(selectDateActifUser)  )
				{
					//userStatsResult.add(userStat);
					userStatsResult.put(userStat.getIdUser(), userStat);
				}

				// TODO : virer les userStat non actifs, dans un batch , moins de deux minutes ou les lastlocationtrue ancien
			}	
			return userStatsResult.values();
		}
		else return null;

	}

	public static Collection<UserStat> getIdSessionFromPos ( int depuisSec, String idUser, Double la, Double lo) {

		
		Double nLa = gridConvertion(la);
		Double nLo = gridConvertion(lo);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Date nowDate = new Date();
		
		Date selectDate = new Date(nowDate.getTime() - 1000 * depuisSec);
		
		
		//première requete pour récupérer les sessions
		Query query2 = pm.newQuery(UserStat.class,
				"posLa =="+ nLa.toString() +" && posLo == " +nLo.toString()+ " && lastLocationDate > selectDate && idUser==\"" + idUser + "\"");
		query2.declareImports("import java.util.Date");
		query2.declareParameters("Date selectDate");
		
		List<UserStat> userStats = (List<UserStat>) pm.newQuery(query2).execute(selectDate);
		

		if (userStats.size()>0){

			//ArrayList<UserStat> userStatsResult = new ArrayList<UserStat>(); 
			HashMap<Integer, UserStat> userStatsResult = new HashMap<Integer, UserStat>();


			for (Iterator iter = userStats.iterator(); iter.hasNext();) 
			{ 
				UserStat userStat = (UserStat) iter.next();
				
					userStatsResult.put(userStat.getIdSession(), userStat);
				

				
			}	
			//return userStatsResult.values();
			return userStats;
		}
		else return null;
		
		
	}
	
public static Collection<UserStat> getIdSessionFromPosArround ( int depuisSec, String idUser, Double la, Double lo, Double precision) {

		
		//Double nLa = gridConvertion(la);
		//Double nLo = gridConvertion(lo);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Date nowDate = new Date();
		
		Date selectDate = new Date(nowDate.getTime() - 1000 * depuisSec);
		
		 
		//première requete pour récupérer les sessions
		Query query2 = pm.newQuery(UserStat.class,
				"lastLocationDate > selectDate && idUser==\"" + idUser + "\"");
		query2.declareImports("import java.util.Date");
		query2.declareParameters("Date selectDate");
		
		List<UserStat> userStats = (List<UserStat>) pm.newQuery(query2).execute(selectDate);
		
		HashMap<Integer, UserStat> userStatsResult = new HashMap<Integer, UserStat>();
		if (userStats.size()>0){

			//ArrayList<UserStat> userStatsResult = new ArrayList<UserStat>(); 
			


			for (Iterator iter = userStats.iterator(); iter.hasNext();) 
			{ 
				UserStat userStat = (UserStat) iter.next();
				
				// calcul
				Double dLa = la-userStat.getPosLa();
				Double dLo = lo-userStat.getPosLo();
				Double d = Math.sqrt(Math.pow(dLa,2)+Math.pow(dLo,2));
				if (d<=precision)
					userStatsResult.put(userStat.getIdSession(), userStat);
				

				
			}	
			//return userStatsResult.values();
			return  userStatsResult.values();
		}
		else return null;
		
		
	}
	
	
	public static Collection<UserStat> listUserDestination( int destinationDepuisSec, String idUser, Double la, Double lo) 
	{
		Logger theLogger =	 Logger.getLogger(ToStore.class.getName());
		
		
		
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		//TODO : scope recheche itinéraire 
		Collection<UserStat> selectedUserStats = ToStore.getIdSessionFromPosArround (destinationDepuisSec,idUser,la,lo,0.005); //default 0.001
		if (selectedUserStats==null) return null;
		
		HashMap<Integer, UserStat> selectedUserStatsResult = new HashMap<Integer, UserStat>();
			
	
		
		//String sessionSelection = "(";
		
		for (Iterator iter = selectedUserStats.iterator(); iter.hasNext();) 
			{ 
				UserStat userStat = (UserStat) iter.next();
			//	sessionSelection = sessionSelection + " idSession == "+userStat.getIdSession().toString();
			//	if (iter.hasNext())  sessionSelection = sessionSelection + " || ";
				selectedUserStatsResult.put(userStat.getIdSession(), userStat);
		}	
		//sessionSelection = sessionSelection+  ") && ";
		
		//
		 
		Date nowDate = new Date();
		
		Date selectDate = new Date(nowDate.getTime() - 1000 * destinationDepuisSec);
		//Date selectDateActifUser = new Date(nowDate.getTime() - 1000 * actifUserSec);


		Query query2 = pm.newQuery(UserStat.class,
				" lastLocationDate > selectDate && lastLocation==true && idUser==\"" + idUser + "\"");
		//sessionSelection+" lastLocationDate > selectDate && lastLocation==true && idUser==\"" + idUser + "\"");
		query2.declareImports("import java.util.Date");
		query2.declareParameters("Date selectDate");
		query2.setOrdering("lastLocationDate ascending");
		
		List<UserStat> userStats = (List<UserStat>) pm.newQuery(query2).execute(selectDate);
		
		/*
		//on cherche les points de départ
		Query query3 = pm.newQuery(UserStat.class,
				" lastLocationDate > selectDate && prevKey == null && idUser==\"" + idUser + "\"");
		//sessionSelection+" lastLocationDate > selectDate && prevKey == null && idUser==\"" + idUser + "\"");
		query3.declareImports("import java.util.Date");
		query3.declareParameters("Date selectDate");
		query2.setOrdering("lastLocationDate ascending");
	
		List<UserStat> originUserStats = (List<UserStat>) pm.newQuery(query3).execute(selectDate);
		
		HashMap<Integer, UserStat> originUserStatsResult = new HashMap<Integer, UserStat>();
		
		if (originUserStats.size()>0){
		
		for (Iterator iter = originUserStats.iterator(); iter.hasNext();) 
		{ 
			UserStat userStat = (UserStat) iter.next();
			originUserStatsResult.put(userStat.getIdSession(), userStat);
		}
		}
		
		*/
		HashMap<String, UserStat> userStatsResult = new HashMap<String, UserStat>();
		
		if (userStats.size()>0){
			//
			
			
				for (Iterator iter = userStats.iterator(); iter.hasNext();) 
				{ 
					UserStat userStat = (UserStat) iter.next();
					
			
					if (Math.abs(userStat.getPosLa()-la) > 0.001 && Math.abs(userStat.getPosLo()-lo) > 0.001  )
					{
						//UserStat originUserStat  = originUserStatsResult.get(userStat.getIdSession());
						UserStat originUserStat  = selectedUserStatsResult.get(userStat.getIdSession());
				
						if (originUserStat != null) 
							{
						
							userStat.setStartLocationDate(originUserStat.getLastLocationDate());
							//if (userStat.getLastLocationDate().getTime() > userStat.getStartLocationDate().getTime())
								{
								Double nLa = roundDown(userStat.getPosLa(),2);
								Double nLo = roundDown(userStat.getPosLo(),2);
						
								theLogger.info("Clef unique:"+nLa.toString()+nLo.toString());
								// uniquement les idSession selectionnées
								//if (selectedUserStatsResult.get(userStat.getIdSession()) != null)
									userStatsResult.put(nLa.toString()+nLo.toString(), userStat);
								}
							}
			
					}
					

					
				}	
				//return userStatsResult.values();
				return  userStatsResult.values();
	
		}
		else return null;

	}




	public static String getDot() {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		String query = "select from " + UserStat.class.getName()
		+ "  order by lastLocationDate ASC";

		String x = "";
		String y = "";

		List<UserStat> userStats = (List<UserStat>) pm.newQuery(query)
		.execute();
		if (userStats.size() > 0) {
			for (Iterator iter = userStats.iterator(); iter.hasNext();) {
				UserStat userStat = (UserStat) iter.next();
				x = x
				+ String.valueOf(userStat.getLastLocationDate()
						.getTime()) ;
				y = y + String.valueOf(userStat.getRatioPond() ) ;


				if ( iter.hasNext()) {x=x+","; y=y+",";}

			}
 
		}
		return x + "|" + y;
	}

	public static UserStat storeOne(UserStat userStat) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Date nowDate = new Date();

	 

		Date selectDate = new Date(nowDate.getTime() - 1000 * IConstants.TEMPS_PONDERATION_RATIO_SEC); 
		//TODO : faire un batch qui delete avant les 15 dernière minutes


		Query query2 = pm.newQuery(UserStat.class,
				"idSession =="+ userStat.getIdSession().toString() +" && lastLocationDate > selectDate && idUser==\""
				+ userStat.getIdUser() + "\"");

		query2.declareImports("import java.util.Date");
		query2.declareParameters("Date selectDate");
		query2.setOrdering("lastLocationDate descending");

		List<UserStat> userStats = (List<UserStat>) pm.newQuery(query2)
		.execute(selectDate);

		Integer nbStat = new Integer(userStats.size());
		if (nbStat > 0) {

			UserStat previousUserStat = userStats.get(0);
			previousUserStat.setLastLocation(false);
			userStat.setLastLocation(true);
			boolean previousUserStatInTraffic = previousUserStat.getInTraffic();
			Date previousUserStatInTrafficDeclaredTime = previousUserStat.getInTrafficDeclaredTime();

			// userStat.setSumSurfDif( userStat.getSurfDiff(previousUserStat));
			// userStat.setSumSurfVmoy( userStat.getSurfVmoy(previousUserStat));
			Double a = userStat.calcSurfDiff(previousUserStat);
			Double b = userStat.calcSurfVmoy(previousUserStat);
			userStat.setSurfDiff(a);
			userStat.setSurfVmoy(b);

			if (b>0) userStat.setRatio(a / b); else userStat.setRatio(0.0d); 
			if (userStat.getAvgSpeed()<(IConstants.VITESSE_MIN_ANNULATION_RATIO/3.6d) )  userStat.setRatio(0.0d); 
			userStat.setPrevKey(previousUserStat.getKey());

			Date initDate = previousUserStat.getLastLocationDate();

			// Calcul du ratio pondéré


			Double cumulRatio = new Double(0.0d); 
			for (Iterator iter = userStats.iterator(); iter.hasNext();) {
				UserStat curUserStat = (UserStat) iter.next();
				cumulRatio = cumulRatio + curUserStat.getRatio() ; 


				//if (iter.hasNext()) { x=x+ ","; y=y+",";}

			}
			Double ratioPond = cumulRatio / nbStat;
			userStat.setRatioPond (ratioPond);



			// vitesse incorecte
		
				if (ratioPond>IConstants.RATIO_DECLENCHEMENT_BOUCHON && userStat.getSpeed()<=IConstants.VITESSE_MAX_BOUCHON_KMH/3.6d )
			{
				if (!previousUserStatInTraffic) // c'est nouveau 
				{userStat.setInTraffic(true);
				userStat.setInTrafficDeclaredTime(new Date());
				}
				else {
					userStat.setInTraffic(true);
					userStat.setInTrafficDeclaredTime(previousUserStat.getInTrafficDeclaredTime());
				}
			}
			else
			{
				userStat.setInTraffic(false);
				userStat.setInTrafficDeclaredTime(null);
			}
			//
			try {
				pm.makePersistent(previousUserStat);
				pm.makePersistent(userStat); 
			} finally {
				pm.close();
			}
		} else {
			userStat.setMaxSpeed(0d);
			userStat.setSurfDiff(0d);
			userStat.setSurfVmoy(0d);
			userStat.setRatio(0d);
			userStat.setLastLocation(true);
			userStat.setRatioPond(0d);
			userStat.setLastLocationDate(nowDate);
			userStat.setInTraffic(false);
			userStat.setSumSurfDif(0d);
			userStat.setSumSurfVmoy(0d);
			
			
			
			try {
				pm.makePersistent(userStat);
			} finally {
				pm.close(); 
			}
		} 
		return userStat;
	}
	
	public static UserTrace storeOne(UserTrace userTrace) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Date nowDate = new Date();

		
		Query query2 = pm.newQuery(UserTrace.class,
				"idUser==\""
				+ userTrace.getIdUser() + "\"");

		List<UserTrace> userTraces = (List<UserTrace>) pm.newQuery(query2).execute();

		Integer nbStat = new Integer(userTraces.size());
		UserTrace previousUserTrace;
		if (nbStat > 0) {

			 previousUserTrace = userTraces.get(0);
			//
			Integer lastIdSession = previousUserTrace.getLastIdSession();
			previousUserTrace.setLastIdSession(lastIdSession+1);
			previousUserTrace.setLastConnectionTime(nowDate);
			
			userTrace=previousUserTrace;
			
		
			try {
				pm.makePersistent(previousUserTrace);
			} finally {
				pm.close();
			}
		} else {
			userTrace.setLastIdSession(1);
		
			userTrace.setLastConnectionTime(nowDate);
			
			
			try {
				pm.makePersistent(userTrace);
			} finally {
				pm.close();
			}
		} 
		return userTrace;
	}

	public static void storeOne(GlobalTrace globalTrace) {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		globalTrace.setposLa(gridConvertion(globalTrace.getPosLa()));
		globalTrace.setposLo(gridConvertion(globalTrace.getPosLo()));

		String query = "select from " + GlobalTrace.class.getName()
		+ " where  posLo==" + globalTrace.getPosLo().toString()
		+ " && posLa==" + globalTrace.getPosLa().toString()
		+ " && poleDirection==" + globalTrace.getPoleDirection();

		List<GlobalTrace> globalTraces = (List<GlobalTrace>) pm.newQuery(query)
		.execute();
		if (globalTraces.size() == 1) {
			// GlobalTrace globalTracePerstited =
			// pm.getObjectById(GlobalTrace.class,
			// globalTraces.get(0).getKey());
			GlobalTrace globalTracePerstited = globalTraces.get(0);

			globalTracePerstited.setSumSpeed(globalTracePerstited.getSumSpeed()
					+ globalTrace.getSumSpeed());

			if (globalTracePerstited.getMaxSpeed() < globalTrace.getMaxSpeed())
				globalTracePerstited.setMaxSpeed(globalTrace.getMaxSpeed());

			globalTracePerstited.setLastLocationDate(globalTrace
					.getLastLocationDate());
			globalTracePerstited
			.setnbUser(globalTracePerstited.getNbUser() + 1);

			pm.makePersistent(globalTracePerstited);

		} else

		{
			globalTrace.setnbUser(1);
			pm.makePersistent(globalTrace);
		}
		pm.close();

	}
}