package com.elolozone.trafficwave.manager.api;

import java.util.Collection;
import java.util.List;

import com.elolozone.trafficwave.model.UserTrace;

/**
 * @author brasseld@gmail.com
 * @since 22-03-2010
 */
public interface UserTraceManager extends GenericManager<UserTrace, String> {

	List<UserTrace> findBySessionAndUser(int sessionId, String userId);
	
	List<UserTrace> findUserInTraffic(int activeUserSec, int mockTime);
	
	List<UserTrace> findAllAndOrderBy(String property, boolean asc);
	
	Collection<UserTrace> findUserDestinations(int destinationDepuisSec, String idUser, double latitude, double longitude);
}