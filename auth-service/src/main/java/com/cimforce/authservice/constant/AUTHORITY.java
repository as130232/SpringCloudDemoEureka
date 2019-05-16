package com.cimforce.authservice.constant;

public enum AUTHORITY {
	USER("USER"),
	ADMIN("ADMIN");
	
	public final String role;
	private AUTHORITY(String role) {
		this.role = role;
	}
}
