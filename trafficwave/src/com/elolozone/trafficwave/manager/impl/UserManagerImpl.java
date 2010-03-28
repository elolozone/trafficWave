package com.elolozone.trafficwave.manager.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elolozone.trafficwave.dao.api.UserDao;
import com.elolozone.trafficwave.manager.api.UserManager;
import com.elolozone.trafficwave.model.User;

/**
 * Implementation of {@link UserManager} interface.
 *
 * @author brasseld@gmail.com
 */
@Service(value = "userManager")
@Transactional
public class UserManagerImpl extends GenericManagerImpl<User, String> implements UserManager {

	/**
	 * Link with {@link UserDao}.
	 */
	private UserDao userDao;

	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(UserManagerImpl.class);

	@Autowired
	public UserManagerImpl(final UserDao dao) {
		super(dao);
		this.userDao = dao;
	}

	/**
	 * {@inheritDoc}
	 */
	public void save(User user) {
		// Be careful: the userTrace object must be initialized with values before
		// the call of this method !
		User previousUserTrace = this.userDao.findById(user.getId());

		if (previousUserTrace != null) {
			previousUserTrace.setLastIdSession(previousUserTrace.getLastIdSession() + 1);
			previousUserTrace.setLastConnectionTime(new Date());
			
			this.userDao.update(previousUserTrace);
		} else {
			user.setLastIdSession(1);
			user.setLastConnectionTime(new Date());
			
			this.userDao.save(user);
		}
	}
}