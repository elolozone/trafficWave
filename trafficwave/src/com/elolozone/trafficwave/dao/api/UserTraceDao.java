package com.elolozone.trafficwave.dao.api;

import java.util.Date;
import java.util.List;

import com.elolozone.trafficwave.model.UserTrace;

/**
 * @author brasseld@gmail.com
 * @since 22-03-2010
 */
public interface UserTraceDao extends GenericDao<UserTrace, String> {

	int deleteAll();
	
	List<UserTrace> findBySessionAndUser(int sessionId, String userId);
	
	List<UserTrace> findUserInTraffic(int activeUserSec, int mockTime);
	
	List<UserTrace> findAll(String orderedProperty, boolean asc);
	
	List<UserTrace> findBy(String idUser, Integer idSession, Date selectDate);
}
