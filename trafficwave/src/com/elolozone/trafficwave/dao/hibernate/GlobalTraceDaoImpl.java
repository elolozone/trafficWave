package com.elolozone.trafficwave.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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

	/**
	 * {@inheritDoc}
	 */
	public List<GlobalTrace> findBy(Double latitude, Double longitude,
			Integer direction) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int deleteAll() {
		String deleteQuery = "delete from GlobalTrace";
		
		Query query = this.getSession().createQuery(deleteQuery);
		return query.executeUpdate();
	}

	/**
	 * {@inheritDoc}
	 */
	public GlobalTrace findAverageSpot(Double latitude, Double longitude,
			Integer direction) {
		Criteria critQuery = this.getSession().createCriteria(GlobalTrace.class);
		
		critQuery.
			add(Restrictions.eq("latitude", latitude)).
			add(Restrictions.eq("longitude", longitude)).
			add(Restrictions.gt("direction", direction));
		
		// We need only the first GlobalTrace so max result equals to 1
		critQuery.setMaxResults(1);

		List<GlobalTrace> lstGlobalTraces = (List<GlobalTrace>) critQuery.list();
		
		if (! lstGlobalTraces.isEmpty())
			return lstGlobalTraces.get(0);
		
		return null;
	}
}
