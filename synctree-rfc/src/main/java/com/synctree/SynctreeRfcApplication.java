package com.synctree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.synctree.util.logging.SynctreeLogger;

@SpringBootApplication
@ComponentScan("com.synctree")
public class SynctreeRfcApplication {
	private static final SynctreeLogger logger = new SynctreeLogger(SynctreeRfcApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(SynctreeRfcApplication.class, args);	
		logger.info("================== SynctreeRfcApplication Started >>>>>>>>>>>>>>>>>>");
	}

}
