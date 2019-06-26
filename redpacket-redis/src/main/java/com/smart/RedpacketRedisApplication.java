package com.smart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class RedpacketRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedpacketRedisApplication.class, args);
	}

}
