package com.elolozone.trafficwave.manager.impl;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elolozone.trafficwave.dao.api.UserTargetDao;
import com.elolozone.trafficwave.dao.api.UserTraceDao;
import com.elolozone.trafficwave.manager.api.UserTargetManager;
import com.elolozone.trafficwave.manager.api.UserTraceManager;
import com.elolozone.trafficwave.model.UserTarget;
import com.elolozone.trafficwave.model.UserTrace;
import com.elolozone.trafficwave.util.Math;




@Service(value = "userTargetManager")
@Transactional
public class UserTargetManagerImpl   extends GenericManagerImpl<UserTarget, String> implements UserTargetManager {
	/**
	 * Link with {@link UserTraceDao}.
	 */
	private UserTargetDao userTargetDao;
	private UserTraceDao userTraceDao;
	//private UserTraceManager userTraceManager;

	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(UserTraceManagerImpl.class);

	@Autowired
	public UserTargetManagerImpl(final UserTargetDao dao,final UserTraceDao dao2 ) {
		super(dao);
		this.userTargetDao = dao;
		this.userTraceDao = dao2;
	}
	
/*
	@Autowired
	public void setUserTraceManager(UserTraceManager userTraceManager) {
		this.userTraceManager = userTraceManager;
	}
	*/

	
	/**
	 * {@inheritDoc}
	 */
	public Collection<UserTarget> findUserDestinations(int destinationDepuisSec,
			String idUser, double latitude, double longitude) {
		
		double precision = 0.005d;
		Date now = new Date();
		Date selectDate = new Date(now.getTime() + 1000 * destinationDepuisSec);
		
		// on recherche tous les trajets pour un user (depuis une certaine date)
		
		List<UserTrace> availableUserTraces = this.userTraceDao.findByUserAndLowerThanLastLocationDate(idUser, selectDate);
		Map<Integer, UserTrace> selectedUserTracesResult = null;
		
		if (! availableUserTraces.isEmpty()) {
			selectedUserTracesResult = new HashMap<Integer, UserTrace>();
			
			for (UserTrace userTrace : availableUserTraces)  { 
				// Calcul
				double dLa = latitude - userTrace.getLatitude();
				double dLo = longitude - userTrace.getLongitude();
				double d = sqrt(pow(dLa, 2) + pow(dLo, 2));
				// On retient les trajets qui passe à proximité de la position actuelle du user
				if (d <= precision)
					selectedUserTracesResult.put(userTrace.getIdSession(), userTrace);
			}
		}
	
		
		
		
		
		if (selectedUserTracesResult == null)
			return null;
		
		availableUserTraces = this.userTraceDao.findByUserAndLastLocationAndLowerThanLastLocationDate(idUser, Boolean.TRUE, selectDate);
		
		Map<String, UserTrace> userTraceResult = new HashMap<String, UserTrace>();
		Map<String, UserTarget> userTargetResult = new HashMap<String, UserTarget>();
		
		if (! availableUserTraces.isEmpty()) {
			
			for (UserTrace userTrace : availableUserTraces) { 
				if (abs(userTrace.getLatitude() - latitude) > 0.001 && abs(userTrace.getLongitude() - longitude) > 0.001) {
					UserTrace originUserTrace  = selectedUserTracesResult.get(userTrace.getIdSession());
						if (originUserTrace != null) {
							userTrace.setStartLocationDate(originUserTrace.getLastLocationDate());
							
							Double nLa = Math.roundDown(userTrace.getLatitude(), 2);
							Double nLo = Math.roundDown(userTrace.getLongitude(), 2);
									
							userTraceResult.put(nLa.toString()+nLo.toString(), userTrace);
							//
							UserTarget userTarget = userTargetResult.get(nLa.toString()+nLo.toString());
							if (userTarget==null)
							{
								userTarget = new UserTarget();
								userTarget.setOriLatitude(originUserTrace.getLatitude());
								userTarget.setOriLongitude(originUserTrace.getLongitude());
								userTarget.setDestLatitude(nLa);
								userTarget.setDestLongitude(nLo);
								userTarget.setWantedTarget(false);
								userTarget.setMinTrackTimeSec (Integer.MAX_VALUE);
								userTarget.setMaxTrackTimeSec (Integer.MIN_VALUE);
								
								userTarget.setNbTrackPossible(0);
								
								
							}
							userTarget.setNbTrackPossible(userTarget.getNbTrackPossible()+1);
							if (userTarget.getMinTrackTimeSec() > userTrace.calcTrackTimeInSec() ) 
							{	
								userTarget.setMinTrackTimeSec( userTrace.calcTrackTimeInSec());
								userTarget.setIdSessionMinTime(userTrace.getIdSession());
							}
							if (userTarget.getMaxTrackTimeSec() < userTrace.calcTrackTimeInSec() ) 
							{	
								userTarget.setMaxTrackTimeSec( userTrace.calcTrackTimeInSec());
								userTarget.setIdSessionMaxTime(userTrace.getIdSession());
							}
							
							userTargetResult.put(nLa.toString()+nLo.toString(), userTarget);
						}
						else 
						{
							
							// on récupère les trajets dont la destination est souvent la même
							// TODO
						}
					
				}
			}
			return userTargetResult.values();
		}
		
		return null;
	}

	
}

