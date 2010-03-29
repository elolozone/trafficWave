package com.elolozone.trafficwave.dao.hibernate.naming;

import org.hibernate.cfg.DefaultNamingStrategy;

public class CustomNamingStrategy extends DefaultNamingStrategy {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	public String tableName(String tableName) {
		return "tw_" + tableName;
	}
}
