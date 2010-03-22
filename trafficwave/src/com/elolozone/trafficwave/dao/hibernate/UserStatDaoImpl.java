package com.elolozone.trafficwave.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;

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

import com.elolozone.trafficstore.PMF;
import com.elolozone.trafficwave.constants.IConstants;
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

	@SuppressWarnings("unchecked")
	public List<UserStat> findBySessionAndUser(int sessionId, String userId) {
		// TODO : Récupération du graphique (ici 24 heures)
		Date selectDate = new Date(new Date().getTime() - 1000 * 60 * 60*24*365);
		
		Criteria critQuery = this.getSession().createCriteria(UserStat.class);
		critQuery.
			add(Restrictions.eq("idUser", userId)).
			add(Restrictions.gt("lastLocationDate", selectDate));
		
		if (sessionId != -1) {
			critQuery.add(Restrictions.like("idSession", sessionId));
		}
		
		critQuery.addOrder(Order.desc("lastLocationDate"));
		
		// On veut voir que les 150 derniers points
		critQuery.setMaxResults(IConstants.NBRE_POINT_TOGRAPH);
		
		return (List<UserStat>) critQuery.list();
	}

	@SuppressWarnings("unchecked")
	public List<UserStat> findUserInTraffic(int activeUserSec, int mockTime) {
		Date nowDate = new Date();
		
		Date selectDateBouchon = new Date(nowDate.getTime() - 1000 * mockTime);
		Date selectDateActifUser = new Date(nowDate.getTime() - 1000 * activeUserSec);
		
		Criteria critQuery = this.getSession().createCriteria(UserStat.class);
		
		critQuery.
			add(Restrictions.le("inTrafficDeclaredTime", selectDateBouchon)).
			add(Restrictions.eq("inTraffic", Boolean.TRUE)).
			addOrder(Order.desc("inTrafficDeclaredTime"));
		
		List<UserStat> userStats = (List<UserStat>) critQuery.list();
		
		Map<String, UserStat> mapUserStats = new HashMap<String, UserStat>();
		for (UserStat userStat : userStats) {
			if (!mapUserStats.containsKey(userStat.getIdUser()) && userStat.getLastLocationDate().after(selectDateActifUser)) {
				mapUserStats.put(userStat.getIdUser(), userStat);
			}

			// TODO : virer les userStat non actifs, dans un batch , moins de deux minutes ou les lastlocationtrue ancien
		}
		
		return new ArrayList<UserStat>(mapUserStats.values());
	}

	@SuppressWarnings("unchecked")
	public List<UserStat> findAll(String orderedProperty, boolean asc) {
		Criteria critQuery = this.getSession().createCriteria(UserStat.class);
		
		if (asc)
			critQuery.addOrder(Order.asc(orderedProperty));
		else
			critQuery.addOrder(Order.desc(orderedProperty));
		
		return (List<UserStat>) critQuery.list();
	}
}
