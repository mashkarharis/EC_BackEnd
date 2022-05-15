package com.eldercare.rest.Elder.CareWeb.Models;

public class Token {
	private String token;
	private String email;


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "Token [token=" + token + ", email=" + email + "]";
	}

	
}
