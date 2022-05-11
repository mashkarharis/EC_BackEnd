package com.eldercare.rest.Elder.CareWeb.Models;

import java.util.ArrayList;

public class CareTaker {
	
	private String nic;
	private String name;
	private String address;
	private String phone;
	private String email;
	private String password;
	private ArrayList<Elder> elders=new ArrayList<>();
	@Override
	public String toString() {
		return "CareTaker [nic=" + nic + ", name=" + name + ", address=" + address + ", phone=" + phone + ", email="
				+ email + ", password=" + password + ", elders=" + elders + "]";
	}
	public String getNic() {
		return nic;
	}
	public void setNic(String nic) {
		this.nic = nic;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ArrayList<Elder> getElders() {
		return elders;
	}
	public void setElders(ArrayList<Elder> elders) {
		this.elders = elders;
	}
	

}
