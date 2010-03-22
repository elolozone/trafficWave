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
	
	@Override
	public void save(GlobalTrace globalTrace) {
		globalTrace.setLatitude(gridConvertion(globalTrace.getLatitude()));
		globalTrace.setLongitude(gridConvertion(globalTrace.getLongitude()));
		
		List<GlobalTrace> lstGlobalTraces = this.globalTraceDao.findBy(
				globalTrace.getLatitude(), globalTrace.getLongitude(), globalTrace.getPoleDirection());
		
		if (lstGlobalTraces.size() == 1) {
			GlobalTrace globalTracePerstited = lstGlobalTraces.get(0);

			globalTracePerstited.setSumSpeed(globalTracePerstited.getSumSpeed()
					+ globalTrace.getSumSpeed());

			if (globalTracePerstited.getMaxSpeed() < globalTrace.getMaxSpeed()) {
				globalTracePerstited.setMaxSpeed(globalTrace.getMaxSpeed());
			}

			globalTracePerstited.setLastLocationDate(globalTrace
					.getLastLocationDate());
			
			globalTracePerstited.setNbUser(globalTracePerstited.getNbUser() + 1);

			this.globalTraceDao.update(globalTracePerstited);
		} else {
			globalTrace.setNbUser(1);
			this.globalTraceDao.save(globalTrace);
		}
	}
	
	static private Double gridConvertion(Double pos) {
		BigDecimal bdPos;
		bdPos = BigDecimal.valueOf(pos);
		bdPos = bdPos.setScale(4, BigDecimal.ROUND_DOWN);
		return Double.valueOf(bdPos.doubleValue());
	}
}