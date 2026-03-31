package com.sqld_board.sqld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Spring Boot 애플리케이션의 메인 진입점 클래스입니다.
 * 이 클래스는 애플리케이션을 초기화하고 실행하는 역할을 합니다.
 */
@EnableScheduling
@SpringBootApplication
public class SqldApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqldApplication.class, args);
	}

}
