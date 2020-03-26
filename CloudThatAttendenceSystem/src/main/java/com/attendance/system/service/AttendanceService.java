package com.attendance.system.service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.attendance.system.dto.AttendanceEmpLog;
import com.attendance.system.entity.AttendenceLog;
import com.attendance.system.entity.DAOUser;
import com.attendance.system.helper.Result;
import com.attendance.system.repository.AttendenceRepo;
import com.attendance.system.repository.UserRepo;
import com.attendance.system.security.JwtTokenUtil;

@Service
public class AttendanceService {

	@Autowired
	private AttendenceRepo attendenceRepo;
	
	@Autowired
	private UserRepo DAOUserRepo;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd");
	SimpleDateFormat simpletimeFormat = new SimpleDateFormat( "hh-mm-ss aa");
	
	TimeZone timeZone=TimeZone.getTimeZone("IST");
	
	public List<AttendenceLog> attendancelog(DAOUser user)
	{
		List<AttendenceLog> attenlog=null;
		try
		{
			attenlog=	attendenceRepo.findByEmpId(user.getId(), PageRequest.of(0, 7,Sort.by("date").descending()));
		}catch (Exception e) {
			
		}
		return attenlog;
	}
	


	public Result markIn(DAOUser user,String Ip,double lat1, double lon1)
	{
		simpletimeFormat.setTimeZone(timeZone);
		simpleDateFormat.setTimeZone(timeZone);
		Result result=new Result();
		result.setUsername(user.getName());
		
		try
		{
			if(Ip.equals(user.getIp()))
			{
				 if(distance(lat1, lon1, user.getLat1(), user.getLon1(), "M")<user.getRangeinm())
			  {
				  AttendenceLog log=new AttendenceLog();
				  log.setEmpId(user.getId());
				  log.setEntryTime(simpletimeFormat.format(new Date()));
				  log.setLogdate(simpleDateFormat.format(new Date()));
				  log.setDate(new Date());
				  attendenceRepo.save(log);
				  result.setStatus(200);
				  result.setMessage("you logged-in successfully.");
					
			  }
			  else
			  {
				    result.setStatus(300);
					result.setMessage("Please mark your Attendance from client location.");
			  }
			}
			else
			{
				result.setStatus(300);
				result.setMessage("Use your own system to mark Attendance.");
			}
		}catch (Exception e) {
			
			result.setStatus(300);
			result.setMessage("Failed to mark Attendance ,Please contact to Admin");
		   
		}
		return result;
	}
	
	public Result checklog(DAOUser user)
	{
		simpleDateFormat.setTimeZone(timeZone);
		Result result=new Result();
		result.setUsername(user.getName());
		result.setStatus(200);
		
		try
		{
			 List< AttendenceLog> attenloglist=attendenceRepo.findByLogdateAndEmpId(simpleDateFormat.format(new Date()),user.getId());
			 if(attenloglist!=null)
			 {
				 for(AttendenceLog attenlog:attenloglist)
				 {
						 result.setStatus(300);
				 }
			 }	
		}catch (Exception e) {
			
		}
		return result;
	}
	public Result markout(DAOUser user,String Ip,double lat1, double lon1)
	{
		Result result=new Result();
		result.setUsername(user.getName());
		simpleDateFormat.setTimeZone(timeZone);
		
		try
		{
			if(Ip.equals(user.getIp()))
			{
				 if(distance(lat1, lon1, user.getLat1(), user.getLon1(), "M")<user.getRangeinm())
			  {
				  List< AttendenceLog> attenloglist=attendenceRepo.findByLogdateAndEmpId(simpleDateFormat.format(new Date()),user.getId());
				 if(attenloglist!=null)
				 {
					 for(AttendenceLog attenlog:attenloglist)
					 {
						 attenlog.setExitTime(simpletimeFormat.format(new Date()));
						 attendenceRepo.save(attenlog);
						 result.setStatus(200);
						 result.setMessage("you logged-out successfully.");
					 }
				 }
				 else
				 {
					    result.setStatus(300);
						result.setMessage("Today you did't login.");
				 }
				}
			  else
			  {
				    result.setStatus(300);
					result.setMessage("Please mark your Attendance from client location.");
			  }
			}
			else
			{
				result.setStatus(300);
				result.setMessage("Use your own system to mark Attendance.");
			}
		}catch (Exception e) {
			
			result.setStatus(300);
			result.setMessage("Failed to mark Attendance ,Please contact to Admin");
		   
		}
		return result;
	}
	
	private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		if ((lat1 == lat2) && (lon1 == lon2)) {
			return 0;
		}
		else {
			double theta = lon1 - lon2;
			double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;
			if (unit.equals("K")) {
				dist = dist * 1.609344;
			} else if (unit.equals("N")) {
				dist = dist * 0.8684;
			}
			return (dist);
		}
	}
	
}
