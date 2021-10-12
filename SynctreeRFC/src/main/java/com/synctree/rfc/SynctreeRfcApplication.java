package com.synctree.rfc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.synctree")
public class SynctreeRfcApplication {

	public static void main(String[] args) {
		SpringApplication.run(SynctreeRfcApplication.class, args);	
	}

}
