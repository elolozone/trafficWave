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

	/**
	 * {@inheritDoc}
	 */
	public int deleteAll() {
		String deleteQuery = "delete from UserTrace";
		
		Query query = this.getSession().createQuery(deleteQuery);
		return query.executeUpdate();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<UserTrace> findBySessionAndUser(int sessionId, String userId) {
		// TODO : Récupération du graphique (ici 24 heures)
		Date selectDate = new Date(new Date().getTime() - 1000 * 60 * 60*24*365);
		
		Criteria critQuery = this.getSession().createCriteria(UserTrace.class);
		critQuery.
			add(Restrictions.eq("idUser", userId)).
			add(Restrictions.gt("lastLocationDate", selectDate));
		
		if (sessionId != -1) {
			critQuery.add(Restrictions.like("idSession", sessionId));
		}
		
		critQuery.addOrder(Order.desc("lastLocationDate"));
		
		// On veut voir que les 150 derniers points
		critQuery.setMaxResults(IConstants.NBRE_POINT_TOGRAPH);
		
		return (List<UserTrace>) critQuery.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<UserTrace> findUserInTraffic(int activeUserSec, int mockTime) {
		Date nowDate = new Date();
		
		Date selectDateBouchon = new Date(nowDate.getTime() - 1000 * mockTime);
		Date selectDateActifUser = new Date(nowDate.getTime() - 1000 * activeUserSec);
		
		Criteria critQuery = this.getSession().createCriteria(UserTrace.class);
		
		critQuery.
			add(Restrictions.le("inTrafficDeclaredTime", selectDateBouchon)).
			add(Restrictions.eq("inTraffic", Boolean.TRUE)).
			addOrder(Order.desc("inTrafficDeclaredTime"));
		
		List<UserTrace> userStats = (List<UserTrace>) critQuery.list();
		
		Map<String, UserTrace> mapUserStats = new HashMap<String, UserTrace>();
		for (UserTrace userStat : userStats) {
			if (!mapUserStats.containsKey(userStat.getIdUser()) && userStat.getLastLocationDate().after(selectDateActifUser)) {
				mapUserStats.put(userStat.getIdUser(), userStat);
			}

			// TODO : virer les userStat non actifs, dans un batch , moins de deux minutes ou les lastlocationtrue ancien
		}
		
		return new ArrayList<UserTrace>(mapUserStats.values());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<UserTrace> findAll(String orderedProperty, boolean asc) {
		Criteria critQuery = this.getSession().createCriteria(UserTrace.class);
		
		if (asc)
			critQuery.addOrder(Order.asc(orderedProperty));
		else
			critQuery.addOrder(Order.desc(orderedProperty));
		
		return (List<UserTrace>) critQuery.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<UserTrace> findBy(String idUser, Integer idSession,
			Date selectDate) {
		Criteria critQuery = this.getSession().createCriteria(UserTrace.class);
		
		critQuery.
			add(Restrictions.eq("idUser", idUser)).
			add(Restrictions.eq("idSession", idSession)).
			add(Restrictions.gt("lastLocationDate", selectDate));
		critQuery.addOrder( Order.desc("lastLocationDate") );
		return (List<UserTrace>) critQuery.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<UserTrace> findByUserAndLowerThanLastLocationDate(
			String idUser, Date date) {
		Criteria critQuery = this.getSession().createCriteria(UserTrace.class);
		
		critQuery.
			add(Restrictions.eq("idUser", idUser)).
			add(Restrictions.gt("lastLocationDate", date));
		
		return (List<UserTrace>) critQuery.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<UserTrace> findByUserAndLastLocationAndLowerThanLastLocationDate(
			String idUser, boolean lastLocation, Date date) {
		Criteria critQuery = this.getSession().createCriteria(UserTrace.class);
		
		critQuery.
			add(Restrictions.eq("idUser", idUser)).
			add(Restrictions.eq("lastLocation", lastLocation)).
			add(Restrictions.gt("lastLocationDate", date));
		
		return (List<UserTrace>) critQuery.list();
	}
}
