package com.example.ase_map;

import java.io.Serializable;

public class LocShare implements Serializable {

	private String username;
	private Double longitude;
	private Double latitude;
	
	public LocShare() {				
	}
	
	public LocShare(String username, Double longitude, Double latitude) {
		this.username = username;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public String getUsername() {
		return username;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public Double getLatitude() {
		return latitude;
	}
}
