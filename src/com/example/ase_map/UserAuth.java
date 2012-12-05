package com.example.ase_map;

import java.io.Serializable;

public class UserAuth implements Serializable {

	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	
	public UserAuth() {				
	}
	
	public UserAuth(String username, String password, String email, String first, String last) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = first; 
		this.lastName = last;
	}
	
	public UserAuth(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public UserAuth(String username, String first, String last) {
		this.username = username;
		this.firstName = first;
		this.lastName = last;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

}
