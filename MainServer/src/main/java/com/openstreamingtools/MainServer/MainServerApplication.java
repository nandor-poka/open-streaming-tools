package com.openstreamingtools.MainServer;

import com.openstreamingtools.MainServer.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;


@SpringBootApplication
public class MainServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainServerApplication.class, args);
	}

	@Autowired
	Environment env;

	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("driverClassName"));
		dataSource.setUrl("jdbc:sqlite:./enginedj.db");
		dataSource.setUsername(env.getProperty("user"));
		dataSource.setPassword(env.getProperty("password"));
		return dataSource;
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins(Configuration.FRONTEND_ORIGN);
				//registry.addMapping("/getSettings").allowedOrigins(Configuration.FRONTEND_ORIGN);
				//registry.addMapping("/saveSettings").allowedOrigins(Configuration.FRONTEND_ORIGN);
			}
		};
	}

}
