package com.elolozone.trafficwave.manager.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.elolozone.trafficwave.dao.api.GenericDao;
import com.elolozone.trafficwave.manager.api.GenericManager;

@Transactional
public abstract class GenericManagerImpl<T, PK extends Serializable> implements GenericManager<T, PK> {

	protected GenericDao<T, PK> dao;
	
	public GenericManagerImpl(final GenericDao<T, PK> genericDao) {
		this.dao = genericDao;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public boolean exists(PK id) {
		try {
			return this.getDao().exists(id);
		} catch (DataAccessException daoe) {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public T findById(PK id) {
		try {
			return this.getDao().findById(id);
		} catch (DataAccessException daoe) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public List<T> findAll() {
		try {
			List<T> allObjects = this.getDao().findAll();
			//Collections.sort(allObjects);

			return allObjects;
		} catch (DataAccessException daoe) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove(PK id) {
		try {
			this.getDao().removeById(id);
		} catch (DataAccessException daoe) {

		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void save(T object) {
		try {
			this.getDao().save(object);
		} catch (DataAccessException daoe) {
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void update(T object) {
		try {
			this.getDao().update(object);
		} catch (DataAccessException daoe) {
		}
	}

	private GenericDao<T, PK> getDao() {
		return this.dao;
	}
}