package com.attendance.system.helper;

import java.io.Serializable;

public class Result implements Serializable{
	
	public int status;
	public String message;
	public String username;
	public String jwstoken;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getJwstoken() {
		return jwstoken;
	}
	public void setJwstoken(String jwstoken) {
		this.jwstoken = jwstoken;
	}
	
	
	
	

}
