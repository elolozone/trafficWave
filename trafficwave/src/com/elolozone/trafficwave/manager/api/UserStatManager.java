package com.elolozone.trafficwave.manager.api;

import java.util.List;

import com.elolozone.trafficwave.model.UserStat;

/**
 * @author brasseld@gmail.com
 * @since 22-03-2010
 */
public interface UserStatManager extends GenericManager<UserStat, String> {

	List<UserStat> findBySessionAndUser(int sessionId, String userId);
	
	List<UserStat> findUserInTraffic(int activeUserSec, int mockTime);
	
	List<UserStat> findAllAndOrderBy(String property, boolean asc);
}