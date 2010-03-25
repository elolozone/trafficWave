package com.elolozone.trafficwave.dao.api;

import com.elolozone.trafficwave.model.User;

/**
 * @author brasseld@gmail.com
 * @since 22-03-2010
 */
public interface UserDao extends GenericDao<User, String> {

	User getLastTraceByUser(String userId);
	
	int deleteAll();
}
