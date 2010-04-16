package com.elolozone.trafficwave.manager.api;

import java.util.Collection;

import com.elolozone.trafficwave.model.UserTarget;


public interface UserTargetManager  extends GenericManager<UserTarget, String> {

	
	Collection<UserTarget> findUserDestinations(int destinationDepuisSec, String idUser, double latitude, double longitude);
}
