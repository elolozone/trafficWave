package com.elolozone.trafficwave.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.elolozone.trafficwave.constants.IConstants;
import com.elolozone.trafficwave.dao.api.UserTargetDao;
import com.elolozone.trafficwave.model.UserTarget;

@Repository(value = "userTargetDao")
public class UserTargetDaoImpl extends GenericDaoImpl<UserTarget, String> implements UserTargetDao {

	/**
	 * Logger Log4j.
	 */
	private static final Log LOG = LogFactory.getLog(UserTargetDaoImpl.class);

	@Autowired
	public UserTargetDaoImpl(@Qualifier(value = "sessionFactory") SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<UserTarget> findBy(String idUser) {
		Criteria critQuery = this.getSession().createCriteria(UserTarget.class);
		
		critQuery.
			add(Restrictions.eq("idUser", idUser));
		
		return (List<UserTarget>) critQuery.list();
	}

	
}
