package com.elolozone.trafficwave.model;

public class Location {
	
	private String posLo;
	private String posLa;
	private String speed;
	private String idUser;
	private String course;
	private String postalCode;
	private String street;
	private boolean inTraffic;
	private String inTrafficUser;
	private int idSession;
	
	public String getPosLo() {
		return posLo;
	}
	
	public void setPosLo(String posLo) {
		this.posLo = posLo;
	}
	
	public String getPosLa() {
		return posLa;
	}
	
	public void setPosLa(String posLa) {
		this.posLa = posLa;
	}
	
	public String getSpeed() {
		return speed;
	}
	
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	
	public String getIdUser() {
		return idUser;
	}
	
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	
	public String getCourse() {
		return course;
	}
	
	public void setCourse(String course) {
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
