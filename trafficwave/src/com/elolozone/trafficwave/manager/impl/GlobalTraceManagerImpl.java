package com.elolozone.trafficwave.manager.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elolozone.trafficwave.dao.api.GlobalTraceDao;
import com.elolozone.trafficwave.manager.api.GlobalTraceManager;
import com.elolozone.trafficwave.model.GlobalTrace;
import com.elolozone.trafficwave.util.Geo;
import com.elolozone.trafficwave.util.Math;

/**
 * Implementation of {@link GlobalTraceManager} interface.
 *
 * @author brasseld@gmail.com
 */
@Service(value = "globalTraceManager")
@Transactional
public class GlobalTraceManagerImpl extends GenericManagerImpl<GlobalTrace, String> implements GlobalTraceManager {

	/**
	 * Link with {@link GlobalTraceDao}.
	 */
	private GlobalTraceDao globalTraceDao;

	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(GlobalTraceManagerImpl.class);

	@Autowired
	public GlobalTraceManagerImpl(final GlobalTraceDao dao) {
		super(dao);
		this.globalTraceDao = dao;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void save(GlobalTrace globalTrace) {
		globalTrace.setLatitude(Math.gridConvertion(globalTrace.getLatitude()));
		globalTrace.setLongitude(Math.gridConvertion(globalTrace.getLongitude()));
		
		List<GlobalTrace> lstGlobalTraces = this.globalTraceDao.findBy(
				globalTrace.getLatitude(), globalTrace.getLongitude(), globalTrace.getDirection());
		
		if (lstGlobalTraces.size() == 1) {
			GlobalTrace globalTracePersisted = lstGlobalTraces.get(0);

			globalTracePersisted.setSumSpeed(globalTracePersisted.getSumSpeed()
					+ globalTrace.getSumSpeed());

			if (globalTracePersisted.getMaxSpeed() < globalTrace.getMaxSpeed()) {
				globalTracePersisted.setMaxSpeed(globalTrace.getMaxSpeed());
			}

			globalTracePersisted.setLastLocationDate(globalTrace
					.getLastLocationDate());
			
			globalTracePersisted.setNbPoints(globalTracePersisted.getNbPoints() + 1);

			this.globalTraceDao.update(globalTracePersisted);
		} else {
			globalTrace.setNbPoints(1);
			this.globalTraceDao.save(globalTrace);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public GlobalTrace findAverageSpot(Double latitude, Double longitude, Double angle) {
		return this.globalTraceDao.findAverageSpot(latitude, longitude, Geo.getDirection(angle).getValue());
	}
}