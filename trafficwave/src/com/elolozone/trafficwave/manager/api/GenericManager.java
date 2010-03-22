package com.elolozone.trafficwave.manager.api;

import java.io.Serializable;
import java.util.List;

public interface GenericManager<T, PK extends Serializable> {

	/**
	 * Generic method used to get all objects of a particular type. This is the
	 * same as lookup up all rows in a table.
	 *
	 * @return List of populated objects
	 */
	List<T> findAll();

	/**
	 * Generic method to get an object based on class and identifier. An
	 * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
	 * found.
	 *
	 * @param id
	 *            the identifier (primary key) of the object to get
	 * @return a populated object
	 * @see org.springframework.orm.ObjectRetrievalFailureException
	 */
	T findById(PK id);

	/**
	 * Checks for existence of an object of type T using the id arg.
	 *
	 * @param id
	 *            the identifier (primary key) of the object to get
	 * @return - true if it exists, false if it doesn't
	 */
	boolean exists(PK id);

	/**
	 * Generic method to save an object - handles both update and insert.
	 *
	 * @param object
	 *            the object to save
	 */
	void save(T object);

	/**
	 * Generic method to update an object - handles only update method.
	 *
	 * @param object
	 *            the object to update
	 */
	void update(T object);

	/**
	 * Generic method to delete an object based on class and id.
	 *
	 * @param id
	 *            the identifier (primary key) of the object to remove
	 */
	void remove(PK id);
}