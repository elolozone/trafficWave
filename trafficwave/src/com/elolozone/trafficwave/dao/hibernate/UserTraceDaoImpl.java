package com.elolozone.trafficwave.dao.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.elolozone.trafficwave.dao.api.UserTraceDao;
import com.elolozone.trafficwave.model.UserTrace;

@Repository(value = "userTraceDao")
public class UserTraceDaoImpl extends GenericDaoImpl<UserTrace, String> implements UserTraceDao {

	/**
	 * Logger Log4j.
	 */
	private static final Log LOG = LogFactory.getLog(UserTraceDaoImpl.class);

	@Autowired
	public UserTraceDaoImpl(@Qualifier(value = "sessionFactory") SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	public int deleteAll() {
		String deleteQuery = "delete from UserTrace";
		
		Query query = this.getSession().createQuery(deleteQuery);
		return query.executeUpdate();
	}

	@Override
	public UserTrace getLastTraceByUser(String userId) {
		// TODO Auto-generated method stub
		return null;
	}
}
