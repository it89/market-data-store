package com.github.it89.cfutils.marketdatastore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MarketDataStoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(MarketDataStoreApplication.class, args);
	}

}
