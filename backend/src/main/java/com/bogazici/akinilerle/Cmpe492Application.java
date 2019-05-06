package com.bogazici.akinilerle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Cmpe492Application {

	public static void main(String[] args) {
		SpringApplication.run(Cmpe492Application.class, args);
	}

}
