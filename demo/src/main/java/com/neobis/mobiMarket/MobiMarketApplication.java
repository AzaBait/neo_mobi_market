package com.neobis.mobiMarket;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "com.neobis.mobiMarket")
@ComponentScan(basePackages = "com.neobis.mobiMarket")
@OpenAPIDefinition(servers = {
		@Server(url = "/", description = "Default Server URL")})
public class MobiMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobiMarketApplication.class, args);
	}

}
