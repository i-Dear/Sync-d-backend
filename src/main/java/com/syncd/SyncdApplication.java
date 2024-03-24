package com.syncd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class SyncdApplication {

	public static void main(String[] args) {
		SpringApplication.run(SyncdApplication.class, args);
	}

}
