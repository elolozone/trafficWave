package com.elolozone.trafficwave.model;

public class Location {
	
	private Double longitude;
	private Double latitude;
	private Double speed;
	private String idUser;
	private Double course;
	private String postalCode;
	private String street;
	private boolean inTraffic;
	private String inTrafficUser;
	private int idSession;

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getSpeed() {
		return speed;
	}
	
	public void setSpeed(Double speed) {
		this.speed = speed;
	}
	
	public String getIdUser() {
		return idUser;
	}
	
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	
	public Double getCourse() {
		return course;
	}
	
	public void setCourse(Double course) {
		this.course = course;
	}
	
	public String getPostalCode() {
		return postalCode;
	}
	
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public boolean isInTraffic() {
		return inTraffic;
	}
	
	public void setInTraffic(boolean inTraffic) {
		this.inTraffic = inTraffic;
	}
	
	public String getInTrafficUser() {
		return inTrafficUser;
	}
	
	public void setInTrafficUser(String inTrafficUser) {
		this.inTrafficUser = inTrafficUser;
	}
	
	public int getIdSession() {
		return idSession;
	}
	
	public void setIdSession(int idSession) {
		this.idSession = idSession;
	}
}
