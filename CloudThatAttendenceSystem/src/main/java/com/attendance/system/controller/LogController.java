package com.attendance.system.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.attendance.system.dto.AttendanceEmpLog;
import com.attendance.system.dto.Employee;
import com.attendance.system.entity.DAOUser;
import com.attendance.system.repository.UserRepo;
import com.attendance.system.security.JwtUserDetailsService;
import com.attendance.system.service.AttendanceService;
import com.attendance.system.service.DAOUserService;
import com.attendance.system.service.FileStorageService;
import com.attendance.system.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class LogController {
	
	@Autowired
	DAOUserService dAOUserService;
	
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	
	@Autowired
	private AttendanceService attendanceService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserRepo DAOUserRepo;
	
	@Autowired
	FileStorageService fileStorageService;
	
	@GetMapping(value="/listofEmployee")
	public ResponseEntity<?>listofEmployee(HttpServletRequest request)
	{
		
		List<Employee> emplist=null;
		try
		{
			 final String requestTokenHeader = request.getHeader("Authorization");
			 DAOUser daouser=jwtUserDetailsService.getdaouserfromtoken(requestTokenHeader);
			 
			 if(daouser!=null)
			 {
				 emplist=logService.getlistofemployee(daouser);
			 }
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(emplist,HttpStatus.ACCEPTED);
		
	}
	
	@GetMapping(value="/getattendaceemplog/{id}")
	public ResponseEntity<?>getattendacelog(HttpServletRequest request,@PathVariable int id)
	{
		List<AttendanceEmpLog> attelog=null;
		try
		{
			 final String requestTokenHeader = request.getHeader("Authorization");
			 DAOUser daouser=jwtUserDetailsService.getdaouserfromtoken(requestTokenHeader);
			 
			 if(daouser.isIshr()==true || daouser.isIsmanager()==true)
			 {
				 attelog=logService.getAlldayLog(id);			 }
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(attelog,HttpStatus.ACCEPTED);
	}
	@GetMapping(value="/getfilename/{id}/{daterange}")
	public ResponseEntity<?>getfilename(HttpServletRequest request,@PathVariable int id,@PathVariable String daterange)
	{
		String filename=null;
		try
		{
			 final String requestTokenHeader = request.getHeader("Authorization");
			 DAOUser daouser=jwtUserDetailsService.getdaouserfromtoken(requestTokenHeader);
			 if(daouser.isIshr()==true || daouser.isIsmanager()==true)
			 {
				 DAOUser user=DAOUserRepo.findById(id);
				 filename=logService.getExcelemplogs(user,daterange)	;
			 }
		}catch (Exception e) {
		}
		return new ResponseEntity<>(filename,HttpStatus.ACCEPTED);
	}

	@GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
         
        }
         if(contentType == null) {
            contentType = "application/octet-stream";
        }
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
   