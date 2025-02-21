package com.openstreamingtools.MainServer;

import com.openstreamingtools.MainServer.config.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
public class MainServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainServerApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/*").allowedOrigins(Configuration.FRONTEND_ORIGN);
				//registry.addMapping("/getSettings").allowedOrigins(Configuration.FRONTEND_ORIGN);
				//registry.addMapping("/saveSettings").allowedOrigins(Configuration.FRONTEND_ORIGN);
			}
		};
	}

}
