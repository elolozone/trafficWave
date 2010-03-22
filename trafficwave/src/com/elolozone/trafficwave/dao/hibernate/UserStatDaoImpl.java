package com.elolozone.trafficwave.dao.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.elolozone.trafficwave.dao.api.UserStatDao;
import com.elolozone.trafficwave.model.UserStat;

@Repository(value = "userStatDao")
public class UserStatDaoImpl extends GenericDaoImpl<UserStat, String> implements UserStatDao {

	/**
	 * Logger Log4j.
	 */
	private static final Log LOG = LogFactory.getLog(UserStatDaoImpl.class);

	@Autowired
	public UserStatDaoImpl(@Qualifier(value = "sessionFactory") SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/* (non-Javadoc)
	 * @see com.elolozone.trafficwave.dao.api.UserStatDao#deleteAll()
	 */
	public int deleteAll() {
		String deleteQuery = "delete from UserStat";
		
		Query query = this.getSession().createQuery(deleteQuery);
		return query.executeUpdate();
	}
}
