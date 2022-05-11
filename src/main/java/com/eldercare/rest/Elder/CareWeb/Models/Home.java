package com.eldercare.rest.Elder.CareWeb.Models;

import java.util.ArrayList;
import java.util.HashMap;

public class Home {
	
	private String homeId;
	private String homeName;
	private String homeAddress;
	private String phone;
	private double lat;
	private double lon;
	private ArrayList<CareTaker> careTakers=new ArrayList<CareTaker>();
	
	@Override
	public String toString() {
		return "Home [homeId=" + homeId + ", homeName=" + homeName + ", homeAddress=" + homeAddress + ", phone=" + phone
				+ ", lat=" + lat + ", lon=" + lon + ", careTakers=" + careTakers + "]";
	}
	public String getHomeId() {
		return homeId;
	}
	public void setHomeId(String homeId) {
		this.homeId = homeId;
	}
	public String getHomeName() {
		return homeName;
	}
	public void setHomeName(String homeName) {
		this.homeName = homeName;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public ArrayList<CareTaker> getCareTakers() {
		return careTakers;
	}
	public void setCareTakers(ArrayList<CareTaker> careTakers) {
		this.careTakers = careTakers;
	}
	public void addCareTaker(CareTaker ct) {
		this.careTakers.add(ct);
	}
	
	public HashMap<String, Object> toSetMap(){
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("homeId", homeId);
		map.put("homeName", homeName);
		map.put("homeAddress", homeAddress);
		map.put("phone", phone);
		map.put("lat", lat);
		map.put("lon", lon);
		return map;
	}
}
