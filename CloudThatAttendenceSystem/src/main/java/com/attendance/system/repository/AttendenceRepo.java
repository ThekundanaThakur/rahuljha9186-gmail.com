package com.attendance.system.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.attendance.system.entity.AttendenceLog;


@Repository
public interface AttendenceRepo extends CrudRepository<AttendenceLog, Integer>
{

	List<AttendenceLog> findByLogdateAndEmpId(String logdate,int empId);
	List<AttendenceLog> findByEmpId(int empId,Pageable pageable);
	List<AttendenceLog> findByEmpId(int empId);
	
	List<AttendenceLog> findByLogdate(String logdate);
	List<AttendenceLog> findByDateGreaterThanAndDateLessThanAndEmpId(Date date,Date date1,int empId);
}
