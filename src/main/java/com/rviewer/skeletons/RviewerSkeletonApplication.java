package com.rviewer.skeletons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class RviewerSkeletonApplication {
	public static void main(String[] args) {
		SpringApplication.run(RviewerSkeletonApplication.class, args);
	}

}
