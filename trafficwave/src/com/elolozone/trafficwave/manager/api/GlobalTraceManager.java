package com.elolozone.trafficwave.manager.api;

import com.elolozone.trafficwave.model.GlobalTrace;

/**
 * @author brasseld@gmail.com
 * @since 22-03-2010
 */
public interface GlobalTraceManager extends GenericManager<GlobalTrace, String> {

	GlobalTrace findBy(Double latitude, Double longitude, Double angle);
}