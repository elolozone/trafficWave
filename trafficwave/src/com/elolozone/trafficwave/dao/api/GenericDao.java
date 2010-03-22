package com.elolozone.trafficwave.dao.api;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for a Data Access Object that can be used for a single specified
 * type domain object. A single instance implementing this interface can be used
 * only for the type of domain object specified in the type parameters.
 *
 * @author brasseld@gmail.com
 *
 * @param <T> The type of the domain object for which this instance is to be used.
 * @param <ID> The type of the ID of the domain object for which this instance is to be used.
 */
public interface GenericDao<T, ID extends Serializable> {

	void save(T object);

	void update(T object);

	void remove(T object);
	
	void removeById(ID id);

	/**
	 * Get the entity with the specified type and ID from the datastore.
	 *
	 * @param id The serializable ID of the domain object.
	 * @return The entity with the specified type and ID.
	 * @throws DaoException DaoException.
	 */
	T findById(ID id);

	/**
	 * Get a list of all the objects of the specified type.
	 *
	 * @return A {@link List} of domain objects of the specified type.
	 * @throws DaoException DaoException.
	 */
	List<T> findAll();

	boolean exists(ID id);
}
