package com.DeepMindset.Server_Regestery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServerRegesteryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerRegesteryApplication.class, args);
	}

}
