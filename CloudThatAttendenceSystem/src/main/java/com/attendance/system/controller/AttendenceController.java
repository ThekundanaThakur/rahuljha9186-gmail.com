package com.attendance.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.system.entity.AttendenceLog;
import com.attendance.system.entity.DAOUser;
import com.attendance.system.helper.Result;
import com.attendance.system.security.JwtTokenUtil;
import com.attendance.system.security.JwtUserDetailsService;
import com.attendance.system.service.AttendanceService;
@RestController
public class AttendenceController {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	
	@Autowired
	private AttendanceService attendanceService;
	
	
	@GetMapping(value="/getattendacelog")
	public ResponseEntity<?>getattendacelog(HttpServletRequest request)
	{
		List<AttendenceLog> attelog=null;
		try
		{
			 final String requestTokenHeader = request.getHeader("Authorization");
			 DAOUser daouser=jwtUserDetailsService.getdaouserfromtoken(requestTokenHeader);
			 
			 if(daouser!=null)
			 {
				 attelog=attendanceService.attendancelog(daouser);
			 }
		}catch (Exception e) {
			// TODO: handle exception
		}
		return new ResponseEntity<>(attelog,HttpStatus.ACCEPTED);
	}
	
	@PostMapping(value="/markin/{ip}/{latit}/{longi}")
	public  ResponseEntity<?> markin(HttpServletRequest request,@PathVariable String ip,@PathVariable Double latit,@PathVariable Double longi)
	{
		Result result=new Result();
		System.out.println("ip"+ ip+ "longi "+longi+" latit"+latit);
		try
		{
			 final String requestTokenHeader = request.getHeader("Authorization");
			 DAOUser daouser=jwtUserDetailsService.getdaouserfromtoken(requestTokenHeader);
			 
			 if(daouser!=null)
				 result=attendanceService.markIn(daouser, ip,latit,longi);
			 else
			 {
				    result.setStatus(300);
					result.setMessage("user not found."); 
			 }
			 
			
		}
		catch (Exception e) {
			    result.setStatus(300);
				result.setMessage("user not found."); 
		}
		
		
		return new ResponseEntity<>(result,HttpStatus.ACCEPTED);
	}
	
	@PostMapping(value="/markout/{ip}/{latit}/{longi}")
	public  ResponseEntity<?> markout(HttpServletRequest request,@PathVariable String ip,@PathVariable Double latit,@PathVariable Double longi)
	{
		System.out.println("ip"+ ip+ "longi "+longi+" latit"+latit);
		Result result=new Result();
		try
		{
			 final String requestTokenHeader = request.getHeader("Authorization");
			 DAOUser daouser=jwtUserDetailsService.getdaouserfromtoken(requestTokenHeader);
			 if(daouser!=null)
				 result=attendanceService.markout(daouser, ip, latit, longi);
				 
			 else
			 {
				    result.setStatus(300);
					result.setMessage("user not found."); 
			 }
			 
			
		}
		catch (Exception e) {
			    result.setStatus(300);
				result.setMessage("user not found."); 
		}
		
		
		return new ResponseEntity<>(result,HttpStatus.ACCEPTED);
	}
	
	@GetMapping(value="/checklog")
	public ResponseEntity<?>checkAuth(HttpServletRequest request)
	{
		
		
		Result result=new Result();
		 final String requestTokenHeader = request.getHeader("Authorization");
			 DAOUser daouser=jwtUserDetailsService.getdaouserfromtoken(requestTokenHeader);
			 if(daouser!=null)
				 result=attendanceService.checklog(daouser);
				 
		
		
		
		
		return new ResponseEntity<>(result,HttpStatus.ACCEPTED);
		
		
	}

}
