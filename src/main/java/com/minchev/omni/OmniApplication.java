package com.minchev.omni;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class OmniApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmniApplication.class, args);
	}

}
