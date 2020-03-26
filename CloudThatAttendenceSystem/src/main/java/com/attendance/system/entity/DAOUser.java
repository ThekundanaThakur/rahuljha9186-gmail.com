package com.attendance.system.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "attendenceuser")
public class DAOUser implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	private String name;
	@Column
	private String username;
	@Column
	private String password;
	@Column
	private String rfid;
	@Column
	private String ip;
	@Column
	private double lat1;
	@Column
	private double lon1;
	
	@Column
	private double rangeinm;
	@Column
	private int managerid;
	@Column
	private boolean ismanager;
	@Column
	private boolean ishr;
	
	public int getManagerid() {
		return managerid;
	}
	public void setManagerid(int managerid) {
		this.managerid = managerid;
	}

	public boolean isIsmanager() {
		return ismanager;
	}
	public void setIsmanager(boolean ismanager) {
		this.ismanager = ismanager;
	}
	public boolean isIshr() {
		return ishr;
	}
	public void setIshr(boolean ishr) {
		this.ishr = ishr;
	}
	public double getRangeinm() {
		return rangeinm;
	}
	public void setRangeinm(double rangeinm) {
		this.rangeinm = rangeinm;
	}
	public double getLat1() {
		return lat1;
	}
	public void setLat1(double lat1) {
		this.lat1 = lat1;
	}
	public double getLon1() {
		return lon1;
	}
	public void setLon1(double lon1) {
		this.lon1 = lon1;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getRfid() {
		return rfid;
	}
	public void setRfid(String rfid) {
		this.rfid = rfid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


}
