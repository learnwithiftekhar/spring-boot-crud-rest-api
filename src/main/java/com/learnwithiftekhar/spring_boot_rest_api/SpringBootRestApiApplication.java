package com.learnwithiftekhar.spring_boot_rest_api;

import com.learnwithiftekhar.spring_boot_rest_api.model.Product;
import com.learnwithiftekhar.spring_boot_rest_api.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestApiApplication.class, args);
	}
}
