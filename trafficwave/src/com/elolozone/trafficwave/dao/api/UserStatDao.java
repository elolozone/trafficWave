package com.elolozone.trafficwave.dao.api;

import com.elolozone.trafficwave.model.UserStat;

/**
 * @author brasseld@gmail.com
 * @since 22-03-2010
 */
public interface UserStatDao extends GenericDao<UserStat, String> {

	int deleteAll();
}
