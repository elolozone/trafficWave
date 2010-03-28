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

import com.elolozone.trafficwave.constants.IConstants;
import com.elolozone.trafficwave.dao.api.UserTraceDao;
import com.elolozone.trafficwave.manager.api.UserTraceManager;
import com.elolozone.trafficwave.model.UserTrace;
import com.elolozone.trafficwave.util.Math;

/**
 * Implementation of {@link UserTraceManager} interface.
 *
 * @author brasseld@gmail.com
 */
@Service(value = "userTraceManager")
@Transactional
public class UserTraceManagerImpl extends GenericManagerImpl<UserTrace, String> implements UserTraceManager {

	/**
	 * Link with {@link UserTraceDao}.
	 */
	private UserTraceDao userTraceDao;

	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(UserTraceManagerImpl.class);

	@Autowired
	public UserTraceManagerImpl(final UserTraceDao dao) {
		super(dao);
		this.userTraceDao = dao;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(UserTrace userTrace) {
		Date now = new Date();
		Date selectDate = new Date(now.getTime() - 1000 * IConstants.TEMPS_PONDERATION_RATIO_SEC);
		
		List<UserTrace> lstUserTrace = this.userTraceDao.findBy(userTrace.getIdUser(), userTrace.getIdSession(), selectDate);
		
		if (! lstUserTrace.isEmpty()) {
			UserTrace previous = lstUserTrace.get(0);
			
			previous.setLastLocation(false);
			userTrace.setLastLocation(true);

			double surfDiff = Math.calcSurfDiff(userTrace, previous);
			double surfVMoy = Math.calcSurfVmoy(userTrace, previous);
			
			userTrace.setSurfDiff(surfDiff);
			userTrace.setSurfVmoy(surfVMoy);

			if (surfVMoy > 0)
				userTrace.setRatio(surfDiff / surfVMoy); 
			else 
				userTrace.setRatio(0d);
			
			if (userTrace.getAvgSpeed() < (IConstants.VITESSE_MIN_ANNULATION_RATIO / 3.6d))
				userTrace.setRatio(0d);
			
			userTrace.setPrevious(previous);

			// Calcul du ratio pondéré
			double cumulRatio = 0d;
			
			for (UserTrace oneUserTrace : lstUserTrace) {
				cumulRatio = cumulRatio + oneUserTrace.getRatio() ; 
			}
			
			double ratioPond = cumulRatio / lstUserTrace.size();
			userTrace.setRatioPond(ratioPond);

			// vitesse incorecte
			if (ratioPond > IConstants.RATIO_DECLENCHEMENT_BOUCHON && 
					userTrace.getSpeed() <= IConstants.VITESSE_MAX_BOUCHON_KMH / 3.6d)
			{
				if (! previous.getInTraffic()) {
					// c'est nouveau 
					userTrace.setInTraffic(true);
					userTrace.setInTrafficDeclaredTime(new Date());
				}
				else {
					userTrace.setInTraffic(true);
					userTrace.setInTrafficDeclaredTime(previous.getInTrafficDeclaredTime());
				}
			}
			else
			{
				userTrace.setInTraffic(false);
				userTrace.setInTrafficDeclaredTime(null);
			}

			this.userTraceDao.update(previous);
			this.userTraceDao.save(userTrace);
		} else {
			userTrace.setMaxSpeed(0d);
			userTrace.setSurfDiff(0d);
			userTrace.setSurfVmoy(0d);
			userTrace.setRatio(0d);
			userTrace.setLastLocation(true);
			userTrace.setRatioPond(0d);
			userTrace.setLastLocationDate(now);
			userTrace.setInTraffic(false);
			userTrace.setSumSurfDif(0d);
			userTrace.setSumSurfVmoy(0d);
			
			this.userTraceDao.save(userTrace);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<UserTrace> findUserDestinations(int destinationDepuisSec,
			String idUser, double latitude, double longitude) {
		
		double precision = 0.005d;
		Date now = new Date();
		Date selectDate = new Date(now.getTime() - 1000 * destinationDepuisSec);
		
		List<UserTrace> availableUserTraces = this.userTraceDao.findByUserAndLowerThanLastLocationDate(idUser, selectDate);
		Map<Integer, UserTrace> selectedUserTracesResult = null;
		
		if (! availableUserTraces.isEmpty()) {
			selectedUserTracesResult = new HashMap<Integer, UserTrace>();
			
			for (UserTrace userTrace : availableUserTraces)  { 
				// Calcul
				double dLa = latitude - userTrace.getLatitude();
				double dLo = longitude - userTrace.getLongitude();
				double d = sqrt(pow(dLa, 2) + pow(dLo, 2));
				
				if (d <= precision)
					selectedUserTracesResult.put(userTrace.getIdSession(), userTrace);
			}
		}
		
		if (selectedUserTracesResult == null)
			return null;
		
		availableUserTraces = this.userTraceDao.findByUserAndLastLocationAndLowerThanLastLocationDate(idUser, Boolean.TRUE, selectDate);
		
		Map<String, UserTrace> userTraceResult = new HashMap<String, UserTrace>();
		
		if (! availableUserTraces.isEmpty()) {
			
			for (UserTrace userTrace : availableUserTraces) { 
				if (abs(userTrace.getLatitude() - latitude) > 0.001 && abs(userTrace.getLongitude() - longitude) > 0.001) {
					UserTrace originUserTrace  = selectedUserTracesResult.get(userTrace.getIdSession());
					if (originUserTrace != null) {
						userTrace.setStartLocationDate(originUserTrace.getLastLocationDate());
						
						Double nLa = Math.roundDown(userTrace.getLatitude(), 2);
						Double nLo = Math.roundDown(userTrace.getLongitude(), 2);
								
						// uniquement les idSession selectionnées
						userTraceResult.put(nLa.toString()+nLo.toString(), userTrace);
					}
				}
			}
			return userTraceResult.values();
		}
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<UserTrace> findBySessionAndUser(int sessionId, String userId) {
		return this.userTraceDao.findBySessionAndUser(sessionId, userId);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<UserTrace> findUserInTraffic(int activeUserSec, int mockTime) {
		return this.userTraceDao.findUserInTraffic(activeUserSec, mockTime);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<UserTrace> findAllAndOrderBy(String property, boolean asc) {
		return this.userTraceDao.findAll(property, asc);
	}
}