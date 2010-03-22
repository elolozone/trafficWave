package com.elolozone.trafficwave.dao.api;

import com.elolozone.trafficwave.model.UserTrace;

/**
 * @author brasseld@gmail.com
 * @since 22-03-2010
 */
public interface UserTraceDao extends GenericDao<UserTrace, String> {

	UserTrace getLastTraceByUser(String userId);
	
	int deleteAll();
}
