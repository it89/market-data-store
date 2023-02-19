package com.github.it89.cfutils.csv.uploader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class CsvUploaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsvUploaderApplication.class, args);
	}

}
