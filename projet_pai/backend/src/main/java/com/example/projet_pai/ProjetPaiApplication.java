package com.example.projet_pai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.projet_pai.security")
public class ProjetPaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetPaiApplication.class, args);
	}

}
