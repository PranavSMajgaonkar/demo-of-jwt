package com.sample.token;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan({"com.sample.token"})
public class JwtTokenDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtTokenDemoApplication.class, args);
	}

}
