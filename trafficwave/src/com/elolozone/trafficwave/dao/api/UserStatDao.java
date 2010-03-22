package com.elolozone.trafficwave.dao.api;

import java.util.List;

import com.elolozone.trafficwave.model.UserStat;

/**
 * @author brasseld@gmail.com
 * @since 22-03-2010
 */
public interface UserStatDao extends GenericDao<UserStat, String> {

	int deleteAll();
	
	List<UserStat> findBySessionAndUser(int sessionId, String userId);
	
	List<UserStat> findUserInTraffic(int activeUserSec, int mockTime);
	
	List<UserStat> findAll(String orderedProperty, boolean asc);
}
