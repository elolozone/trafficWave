package com.elolozone.trafficwave.dao.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.elolozone.trafficwave.dao.api.UserDao;
import com.elolozone.trafficwave.model.User;

@Repository(value = "userDao")
public class UserDaoImpl extends GenericDaoImpl<User, String> implements UserDao {

	/**
	 * Logger Log4j.
	 */
	private static final Log LOG = LogFactory.getLog(UserDaoImpl.class);

	@Autowired
	public UserDaoImpl(@Qualifier(value = "sessionFactory") SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	public int deleteAll() {
		String deleteQuery = "delete from User";
		
		Query query = this.getSession().createQuery(deleteQuery);
		return query.executeUpdate();
	}
}
