package com.attendance.system.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.attendance.system.entity.DAOUser;
import com.attendance.system.repository.UserRepo;
import com.attendance.system.service.DAOUserService;








@Service
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepo userDao;

	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private DAOUserService dAOUserService;
	 
	
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		DAOUser user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}
	
	
	public DAOUser getdaouserfromtoken(String token)
	{
		 DAOUser daouser=null;
		  String username=null;
		     try
		     {
		    	 username=jwtTokenUtil.getUsernameFromToken(token);
		     }
		     catch (Exception e) {
			}
		  
		  if(username!=null && !username.isEmpty())
		  daouser=dAOUserService.checkDAOUser(username);
		   return daouser;
	}
	
	
}