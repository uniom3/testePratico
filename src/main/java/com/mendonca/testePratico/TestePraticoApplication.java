package com.mendonca.testePratico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TestePraticoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestePraticoApplication.class, args);
	}

}
