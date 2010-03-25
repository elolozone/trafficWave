package com.elolozone.trafficwave.util;

import static java.lang.Math.min;
import static java.lang.Math.max;

import java.math.BigDecimal;

import com.elolozone.trafficwave.model.UserTrace;

public class Math {

	public static Double calcSurfDiff(UserTrace current, UserTrace previous){
		long l1 = current.getLastLocationDate().getTime();
		long l2 = previous.getLastLocationDate().getTime();
		Double a = new Double(l1-l2); 
		
		Double currentCalcDif = current.getAvgSpeed() - min(current.getAvgSpeed(), current.getSpeed());
		Double previousCalcDif = previous.getAvgSpeed() - min(previous.getAvgSpeed(), previous.getSpeed());
		
		Double b = min(currentCalcDif, previousCalcDif);
		Double c = max(previousCalcDif, previousCalcDif);
		
		return a*b+((c-b)*a)/2;
	}
	
	public static Double calcSurfVmoy(UserTrace current, UserTrace previous){
		long l1 = current.getLastLocationDate().getTime();
		long l2 = previous.getLastLocationDate().getTime();
		Double a = new Double(l1-l2); 

		Double b = min(current.getAvgSpeed(), previous.getAvgSpeed());
		Double c = max(current.getAvgSpeed(), previous.getAvgSpeed());
		
		return a*b+((c-b)*a)/2;
	}
	
	static public Double gridConvertion(Double pos) {
		return roundDown(pos, 4);
	}

	static public Double roundDown(Double pos, int n) {
		BigDecimal bdPos;
		bdPos = BigDecimal.valueOf(pos);
		bdPos = bdPos.setScale(n, BigDecimal.ROUND_DOWN);
		return Double.valueOf(bdPos.doubleValue());
	}
}
