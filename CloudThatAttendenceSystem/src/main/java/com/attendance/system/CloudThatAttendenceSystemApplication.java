package com.attendance.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.attendance.system.service.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class CloudThatAttendenceSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudThatAttendenceSystemApplication.class, args);
	}

}
