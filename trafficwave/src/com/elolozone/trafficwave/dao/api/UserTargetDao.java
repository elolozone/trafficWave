package com.elolozone.trafficwave.dao.api;

import java.util.Date;
import java.util.List;

import com.elolozone.trafficwave.model.UserTarget;


public interface UserTargetDao extends GenericDao<UserTarget, String> {

	
	List<UserTarget> findBy(String idUser);
	

}
