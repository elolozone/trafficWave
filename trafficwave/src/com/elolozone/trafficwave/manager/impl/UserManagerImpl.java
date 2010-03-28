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
	public User connect(String idUser) {
		User previousUserTrace = this.userDao.findById(idUser);

		if (previousUserTrace != null) {
			previousUserTrace.setLastIdSession(previousUserTrace.getLastIdSession() + 1);
			previousUserTrace.setLastConnectionTime(new Date());
			
			this.userDao.update(previousUserTrace);
			
			return previousUserTrace;
		} else {
			User newUser = new User();
			newUser.setId(idUser);
			newUser.setLastIdSession(1);
			newUser.setLastConnectionTime(new Date());
			
			this.userDao.save(newUser);
			
			return newUser;
		}
	}
}