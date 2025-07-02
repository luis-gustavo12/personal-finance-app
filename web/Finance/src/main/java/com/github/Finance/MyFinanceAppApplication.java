package com.github.Finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class MyFinanceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyFinanceAppApplication.class, args);
	}

}
