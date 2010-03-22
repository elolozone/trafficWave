package com.elolozone.trafficwave.manager.impl;

import java.util.Date;
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
@Service(value = "userStatService")
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
}