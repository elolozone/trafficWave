package com.elolozone.trafficwave.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.Criteria;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.criterion.Criterion;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.elolozone.trafficwave.dao.api.GenericDao;

/**
 * @author brasseld@gmail.com
 *
 * @param <T>
 * @param <ID>
 */
public class GenericDaoImpl<T, ID extends Serializable> extends HibernateDaoSupport implements GenericDao<T, ID>  {

	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(GenericDaoImpl.class);

	protected Class<T> persistentClass;

	@SuppressWarnings (value = "unchecked")
	public GenericDaoImpl() {
		ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
		this.persistentClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

	public Class<T> getPersistentClass() {
        return persistentClass;
    }

    /* (non-Javadoc)
     * @see com.elolozone.trafficwave.dao.api.GenericDao#findById(java.io.Serializable)
     */
    public T findById(ID id) {
    	if (LOG.isDebugEnabled()) {
			LOG.debug("-> Method: findById(ID id)");
    	}

    	try {
	        return (T) getHibernateTemplate().load(getPersistentClass(), id);
    	} catch (DataAccessException dataAccessException) {
    		LOG.error("FindByID exception : ", dataAccessException);
    		throw dataAccessException;
    	} catch (ObjectNotFoundException onfe) {
    		LOG.error("FindByID - ObjectNotFoundException : ", onfe);
    		return null;
    	}
    }

	/* (non-Javadoc)
	 * @see com.elolozone.trafficwave.dao.api.GenericDao#save(java.lang.Object)
	 */
	public void save(T obj) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("-> Method: save(T obj)");
		}

		try {
			getHibernateTemplate().save(obj);
		} catch (DataAccessException dataAccessException) {
			LOG.error("Save exception : " + obj + "\n" + dataAccessException.getMessage(), dataAccessException);
		}
	}

	/* (non-Javadoc)
	 * @see com.elolozone.trafficwave.dao.api.GenericDao#update(java.lang.Object)
	 */
	public void update(T obj) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("-> Method: update(T obj)");
		}

		try {
			getHibernateTemplate().update(obj);
		} catch (DataAccessException dataAccessException) {
			LOG.error("Update exception : " + obj, dataAccessException);
		}
	}

	/* (non-Javadoc)
	 * @see com.elolozone.trafficwave.dao.api.GenericDao#remove(java.lang.Object)
	 */
	public void remove(T obj) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("-> Method: remove(T obj)");
		}

		try {
			getHibernateTemplate().delete(obj);
		} catch (DataAccessException dataAccessException) {
			LOG.error("Delete exception : " + obj, dataAccessException);
		}
	}

	/* (non-Javadoc)
	 * @see com.elolozone.trafficwave.dao.api.GenericDao#findAll()
	 */
	public List<T> findAll() {
		return findByCriteria();
	}

	/**
	 * @param criterion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("-> Method: findByCriteria(Criterion... criterion)");
		}

		try {
			Criteria crit = getHibernateTemplate().getSessionFactory().openSession().createCriteria(getPersistentClass());
			for (Criterion c : criterion) {
				crit.add(c);
			}
			return crit.list();
		} catch (DataAccessException dataAccessException) {
			LOG.error("FindByCriteria exception ", dataAccessException);
			throw dataAccessException;
		}
	}

	/* (non-Javadoc)
	 * @see com.elolozone.trafficwave.dao.api.GenericDao#exists(java.io.Serializable)
	 */
	public boolean exists(ID id) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("-> Method: exists(ID id)");
		}

		try {
			T entity = this.getHibernateTemplate().get(this.persistentClass, id);

			return entity != null;
		} catch (DataAccessException dae) {
			LOG.error("exists exception ", dae);
			throw dae;
		}
	}

	/* (non-Javadoc)
	 * @see com.elolozone.trafficwave.dao.api.GenericDao#removeById(java.io.Serializable)
	 */
	public void removeById(ID id) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("-> Method: removeById(ID id)");
		}

		this.remove(this.findById(id));
	}
}
