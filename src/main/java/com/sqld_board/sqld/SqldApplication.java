package com.sqld_board.sqld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SqldApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqldApplication.class, args);
	}

}
