package com.github.Finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MyFinanceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyFinanceAppApplication.class, args);
	}

}
