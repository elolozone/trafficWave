package com.elolozone.trafficwave.manager.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elolozone.trafficwave.dao.api.UserStatDao;
import com.elolozone.trafficwave.manager.api.UserStatManager;
import com.elolozone.trafficwave.model.UserStat;

/**
 * Implementation of {@link UserStatManager} interface.
 *
 * @author brasseld@gmail.com
 */
@Service(value = "userStatManager")
@Transactional
public class UserStatManagerImpl extends GenericManagerImpl<UserStat, String> implements UserStatManager {

	/**
	 * Link with {@link UserStatDao}.
	 */
	private UserStatDao userStatDao;

	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(UserStatManagerImpl.class);

	@Autowired
	public UserStatManagerImpl(final UserStatDao dao) {
		super(dao);
		this.userStatDao = dao;
	}

	/* (non-Javadoc)
	 * @see com.elolozone.trafficwave.manager.api.UserStatManager#findBySessionAndUser(int, java.lang.String)
	 */
	public List<UserStat> findBySessionAndUser(int sessionId, String userId) {
		return this.userStatDao.findBySessionAndUser(sessionId, userId);
	}

	/* (non-Javadoc)
	 * @see com.elolozone.trafficwave.manager.api.UserStatManager#findUserInTraffic(int, int)
	 */
	public List<UserStat> findUserInTraffic(int activeUserSec, int mockTime) {
		return this.userStatDao.findUserInTraffic(activeUserSec, mockTime);
	}

	@Override
	public List<UserStat> findAllAndOrderBy(String property, boolean asc) {
		return this.userStatDao.findAll(property, asc);
	}
}