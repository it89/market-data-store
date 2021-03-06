package com.github.it89.cfutils.tcs.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TcsUploaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TcsUploaderApplication.class, args);
	}

}
