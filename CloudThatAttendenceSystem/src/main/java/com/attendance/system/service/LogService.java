package com.attendance.system.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance.system.dto.AttendanceEmpLog;
import com.attendance.system.dto.Employee;
import com.attendance.system.entity.AttendenceLog;
import com.attendance.system.entity.DAOUser;
import com.attendance.system.repository.AttendenceRepo;
import com.attendance.system.repository.UserRepo;
import com.attendance.system.security.JwtTokenUtil;

@Service
public class LogService {

	@Autowired
	private AttendenceRepo attendenceRepo;
	
	@Autowired
	private UserRepo DAOUserRepo;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	TimeZone timeZone=TimeZone.getTimeZone("IST");
	
	SimpleDateFormat format = new SimpleDateFormat("hh-mm-ss aa");
	
	SimpleDateFormat fort = new SimpleDateFormat("hh-mm-ss");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		
	public List<Employee> getlistofemployee(DAOUser daouser)
	{
		List<Employee> list=new ArrayList<Employee>();
		try
		{
		   if(daouser.isIshr())
			   list=getemployeelist((List<DAOUser>) DAOUserRepo.findAll());
		   if(daouser.isIsmanager())
			   list=getemployeelist((List<DAOUser>) DAOUserRepo.findByManagerid(daouser.getId()));
			   
		}catch (Exception e) {
			
		}
		return list;
			
	}
	
	
	
	public List<Employee> getemployeelist(List<DAOUser> daouser)
	{
		List<Employee> list=new ArrayList<Employee>();
		try
		{
			for(DAOUser dao:daouser)
			{
				Employee employee=new Employee();
				employee.setEmail(dao.getUsername());
				employee.setName(dao.getName());
				employee.setRfid(dao.getRfid());
				employee.setId(dao.getId());
				list.add(employee);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	public List<AttendanceEmpLog> getAlldayLog(int userid)
	{
		List<AttendanceEmpLog> attenlog=null;
		try
		{
			DAOUser user=DAOUserRepo.findById(userid);
			attenlog=getempLog(user);
			
		}catch (Exception e) {
			
		}
		return attenlog;
	}
	
	
	public List<AttendanceEmpLog> getempLog(DAOUser userd)
	{
		List<AttendanceEmpLog> attenlog=new ArrayList<AttendanceEmpLog>();
		try
		{
			List<AttendenceLog> atten=attendenceRepo.findByEmpId(userd.getId());
			if(atten!=null)
			{
				for(AttendenceLog attlog:atten)
				{
					
					AttendanceEmpLog attendanceEmpLog=new AttendanceEmpLog();
					attendanceEmpLog.setEntryTime(attlog.getEntryTime());
					attendanceEmpLog.setExitTime(attlog.getExitTime());
					attendanceEmpLog.setLogdate(attlog.getLogdate());
					attendanceEmpLog.setName(userd.getName());
					attenlog.add(attendanceEmpLog);
				}
			}
			
			
		}catch (Exception e) {
			System.out.println(e);
			
		}
		return attenlog;
	}
	
	public List<AttendanceEmpLog> getempLog(DAOUser userd,Date startdate,Date enddate)
	{
		List<AttendanceEmpLog> attenlog=new ArrayList<AttendanceEmpLog>();
		try
		{
			List<AttendenceLog> atten=attendenceRepo.findByDateGreaterThanAndDateLessThanAndEmpId(startdate,enddate, userd.getId());
			if(atten!=null)
			{
				for(AttendenceLog attlog:atten)
				{
					
					AttendanceEmpLog attendanceEmpLog=new AttendanceEmpLog();
					attendanceEmpLog.setEntryTime(attlog.getEntryTime());
					attendanceEmpLog.setExitTime(attlog.getExitTime());
					attendanceEmpLog.setLogdate(attlog.getLogdate());
					attendanceEmpLog.setName(userd.getName());
					attenlog.add(attendanceEmpLog);
				}
			}
			
			
		}catch (Exception e) {
			System.out.println(e);
			
		}
		return attenlog;
	}
	public String status(String entry,String exit)
	{
		format.setTimeZone(timeZone);	
		String status="";
		try
		{
		Date date1 = format.parse(entry);
		Date date2 = format.parse(exit);
		long durationInMillis = date2.getTime() - date1.getTime(); 
		if(durationInMillis == 0)
		{
			status="A";
		}
		else
		{
			status="P";
		}
		}
		catch (Exception e) {
			System.out.println(e);
			}
		return status;	
	}
	
	public String spenttime(String entry,String exit)
	{
		format.setTimeZone(timeZone);
		String spent="";
		try
		{
			Date date1 = format.parse(entry);
			Date date2 = format.parse(exit);
			long durationInMillis = date2.getTime() - date1.getTime(); 
			long millis = durationInMillis % 1000;
			long second = (durationInMillis / 1000) % 60;
			long minute = (durationInMillis / (1000 * 60)) % 60;
			long hour = (durationInMillis / (1000 * 60 * 60)) % 24;
			spent = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis);
			
		}catch (Exception e) {
		System.out.println(e);
		}
		return spent;
		
	}
	
	
	public Object[]  getExceldata(List<AttendanceEmpLog> attlog,Date date)
	{
		format.setTimeZone(timeZone);
		dateFormat.setTimeZone(timeZone);
		simpleDateFormat.setTimeZone(timeZone);
		Object[] data=null;
	     for(AttendanceEmpLog emplog:attlog)
	     {
	    	 if(simpleDateFormat.format(date).equals(emplog.getLogdate()))
	    		   data= new Object[] {dateFormat.format(date), emplog.getEntryTime(),emplog.getExitTime(),spenttime(emplog.getEntryTime(),emplog.getExitTime()),"P"};
		 }
	        
	     if(data==null)
	     {
	    	 if(date.getDay()==5 || date.getDay()==6)
	    	 {
	    		 data= new Object[] {dateFormat.format(date), "","","00:00:00","W/O"}; 
	    	 }
	     }
	     if(data==null)
	     {
	    	 data= new Object[] {dateFormat.format(date), "","","00:00:00","AB"}; 
	     }
	     return data;
	}

	 public String getExcelemplogs(DAOUser userd,String daterange)
	 {
		 Date startdate=getDateRange(daterange, 0);
		 Date enddate=getDateRange(daterange, 1);
		 if(startdate.after(new Date()))
			 startdate=new Date();
		 if(enddate.after(new Date()))
			 enddate=new Date();
		 
		 String filename="";
		 Workbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = (XSSFSheet) workbook.createSheet(userd.getName()+" Attendance Logs");
	        Map<String, Object[]> data = new TreeMap<String, Object[]>();
	        data.put("1", new Object[] {"Attendance Date","Entry Time", "Exit Time","Spent Time","Status"});
	        int rowno=2;
	        
	        
	        
	        
	        
	        List<AttendanceEmpLog> attlog=getempLog(userd,startdate,enddate);
	        
	        for ( ; startdate.before(enddate);) {
	        	data.put(String.valueOf(rowno),getExceldata(attlog, startdate));
	        	
	        	startdate=new Date(startdate.getTime() + (1000 * 60 * 60 * 24));
	        	rowno++;
	        	
	        }
	        
	    
	        Set<String> keyset = data.keySet();
	        int rownum = 0;
	        for (String key : keyset)
	        {
	            Row row = sheet.createRow(rownum++);
	            Object [] objArr = data.get(key);
	            int cellnum = 0;
	            for (Object obj : objArr)
	            {
	               Cell cell = row.createCell(cellnum++);
	               if(obj instanceof String)
	                    cell.setCellValue((String)obj);
	                else if(obj instanceof Integer)
	                    cell.setCellValue((Integer)obj);
	            }
	        }
	        try
	        {
	        	String path="../";
	        	 filename=userd.getName()+new Date().getHours()+new Date().getMinutes()+new Date().getSeconds()+".xlsx";
	            FileOutputStream out = new FileOutputStream(new File(path+filename));
	            workbook.write(out);
	            out.close();
	            System.out.println(" successfully on disk.");
	        } 
	        catch (Exception e) 
	        {
	            e.printStackTrace();
	        }
	        
	        return  filename;
	 }
	 
	 public  Date getDateRange(String daterange,int isenddate)
	 {
		 simpleDateFormat.setTimeZone(timeZone);
		// February 19, 2020 - March 19, 2020
		 String arr[]=daterange.split("-");
		 try
		 {
			 String[] dateform=arr[isenddate].trim().split(" ");
			 //yyyy-MM-dd
			 String date=dateform[2]+"-"+String.valueOf(getMonth(dateform[0]))+"-"+dateform[1].replace(",", "");
			 return simpleDateFormat.parse(date);
			 
		 }catch (Exception e) {
			
		}
		 return null;
	 }
	 public int getMonth(String month)
	 {
		 int mon=1;
		 if(month.equalsIgnoreCase("January"))
			 mon=1;
		 if(month.equalsIgnoreCase("February"))
			 mon=2;
		 if(month.equalsIgnoreCase("March"))
			 mon=3;		 
		 if(month.equalsIgnoreCase("April"))
			 mon=4;
		 if(month.equalsIgnoreCase("May"))
			 mon=5;
		 if(month.equalsIgnoreCase("June"))
			 mon=6;
		 if(month.equalsIgnoreCase("July"))
			 mon=7;
		 if(month.equalsIgnoreCase("August"))
			 mon=8;
		 if(month.equalsIgnoreCase("September"))
			 mon=9;
		 if(month.equalsIgnoreCase("October"))
			 mon=10;
		 if(month.equalsIgnoreCase("November"))
			 mon=11;
		 if(month.equalsIgnoreCase("December"))
			 mon=12;
		 
		 
		 return mon;
	 }

}
