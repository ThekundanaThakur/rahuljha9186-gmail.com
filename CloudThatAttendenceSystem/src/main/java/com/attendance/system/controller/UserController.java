package com.attendance.system.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.system.helper.Result;
import com.attendance.system.security.JwtUserDetailsService;
import com.attendance.system.service.DAOUserService;



@RestController

public class UserController { 
	
	@Autowired
	DAOUserService dAOUserService;
	
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	
	@PostMapping(value="/validiateuser/{email}/{password}")
	public  ResponseEntity<?> validiateuser(@PathVariable String email,@PathVariable String password)
	{
		Result result=dAOUserService.checklogin(email, password);
		
		return new ResponseEntity<>(result,HttpStatus.ACCEPTED);
	}
	
	@GetMapping(value="/checkAuth")
	public ResponseEntity<?>checkAuth(HttpServletRequest request)
	{
		 final String requestTokenHeader = request.getHeader("Authorization");
		 com.attendance.system.entity.DAOUser daouser=jwtUserDetailsService.getdaouserfromtoken(requestTokenHeader);
		 
		 if(daouser!=null)
		 {
			 Result result=new Result();
			 if(daouser.isIsmanager()==true ||  daouser.isIshr()==true )
				 result.setStatus(150);
			 else
				 result.setStatus(200);
				result.setUsername(daouser.getName());
				return new ResponseEntity<>(result,HttpStatus.ACCEPTED);
		 }
				
		 else
			    return new ResponseEntity<>("Please Login Again",HttpStatus.UNAUTHORIZED);
	}
}
