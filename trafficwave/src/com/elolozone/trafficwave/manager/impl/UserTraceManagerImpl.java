package com.elolozone.trafficwave.manager.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elolozone.trafficwave.dao.api.UserStatDao;
import com.elolozone.trafficwave.dao.api.UserTraceDao;
import com.elolozone.trafficwave.manager.api.UserTraceManager;
import com.elolozone.trafficwave.model.UserTrace;

/**
 * Implementation of {@link UserTraceManager} interface.
 *
 * @author brasseld@gmail.com
 */
@Service(value = "userTraceManager")
@Transactional
public class UserTraceManagerImpl extends GenericManagerImpl<UserTrace, String> implements UserTraceManager {

	/**
	 * Link with {@link UserStatDao}.
	 */
	private UserTraceDao userTraceDao;

	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(UserTraceManagerImpl.class);

	@Autowired
	public UserTraceManagerImpl(final UserTraceDao dao) {
		super(dao);
		this.userTraceDao = dao;
	}

	/* (non-Javadoc)
	 * @see com.elolozone.trafficwave.manager.impl.GenericManagerImpl#save(java.lang.Object)
	 */
	@Override
	public void save(UserTrace userTrace) {
		// Be careful: the userTrace object must be initialized with values before
		// the call of this method !
		UserTrace previousUserTrace = this.userTraceDao.getLastTraceByUser(userTrace.getIdUser());

		if (previousUserTrace != null) {
			userTrace.setLastIdSession(previousUserTrace.getLastIdSession() + 1);
		} else {
			userTrace.setLastIdSession(1);
		}
		
		userTrace.setLastConnectionTime(new Date());
		
		this.userTraceDao.save(userTrace);
	}
}