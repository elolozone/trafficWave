package com.elolozone.trafficwave.dao.api;

import java.util.List;

import com.elolozone.trafficwave.model.GlobalTrace;

/**
 * @author brasseld@gmail.com
 * @since 22-03-2010
 */
public interface GlobalTraceDao extends GenericDao<GlobalTrace, String> {

	List<GlobalTrace> findBy(Double latitude, Double longitude, Integer direction);
	
	int deleteAll();
	
	GlobalTrace findAverageSpot(Double latitude, Double longitude, Integer direction);
}
