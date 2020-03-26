package com.attendance.system.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.attendance.system.entity.DAOUser;

@Repository
public interface UserRepo extends CrudRepository<DAOUser, Integer>{

	DAOUser findById(int id);
	DAOUser findByUsername(String username);
	List<DAOUser> findByManagerid(int managerid);

}
