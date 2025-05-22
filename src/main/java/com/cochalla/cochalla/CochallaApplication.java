package com.cochalla.cochalla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CochallaApplication {
	public static void main(String[] args) {
		SpringApplication.run(CochallaApplication.class, args);
	}
}
