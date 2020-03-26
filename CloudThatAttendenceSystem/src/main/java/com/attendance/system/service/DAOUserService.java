package com.attendance.system.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.attendance.system.entity.DAOUser;
import com.attendance.system.helper.PasswordHelper;
import com.attendance.system.helper.Result;
import com.attendance.system.repository.UserRepo;
import com.attendance.system.security.JwtTokenUtil;
import com.attendance.system.security.JwtUserDetailsService;

@Service
public class DAOUserService {
	
	@Autowired
	private UserRepo DAOUserRepo;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public DAOUser checkDAOUser(String email)
	{
		
		try
		{
			DAOUser dAOUser=DAOUserRepo.findByUsername(email);
			return dAOUser;
		}
		catch (Exception e) {
		
			return null;
		}
		
	}
	
	
	
	public Result checklogin(String email,String password)
	{

		Result result=new Result();
		try
		{
			DAOUser user=DAOUserRepo.findByUsername(email);
			if(user.getUsername().equals(email) && password.equals(PasswordHelper.passwordDecode(user.getPassword())))
			{
				
					final UserDetails userDetails =new org.springframework.security.core.userdetails.User(email, password,new ArrayList<>());
                    String token=jwtTokenUtil.generateToken(userDetails);
                    
					result.setStatus(200);
					result.setUsername(user.getName());
					result.setMessage("you logged-in successfully.");
					result.setJwstoken(token);
             	
			}
			else
			{
				result.setStatus(400);
				result.setMessage("check your password.");

			}
		}
		catch (Exception e) {
			result.setStatus(400);
			result.setMessage("Please check your username.");
		}

		return result;
		
	
	}
	

	

	
	
}
