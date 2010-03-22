package com.elolozone.trafficwave.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.elolozone.trafficwave.dao.api.GlobalTraceDao;
import com.elolozone.trafficwave.model.GlobalTrace;

@Repository(value = "globalTraceDao")
public class GlobalTraceDaoImpl extends GenericDaoImpl<GlobalTrace, String> implements GlobalTraceDao {

	/**
	 * Logger Log4j.
	 */
	private static final Log LOG = LogFactory.getLog(GlobalTraceDaoImpl.class);

	@Autowired
	public GlobalTraceDaoImpl(@Qualifier(value = "sessionFactory") SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<GlobalTrace> findBy(Double latitude, Double longitude,
			String poleDirection) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int deleteAll() {
		String deleteQuery = "delete from GlobalTrace";
		
		Query query = this.getSession().createQuery(deleteQuery);
		return query.executeUpdate();
	}
}
