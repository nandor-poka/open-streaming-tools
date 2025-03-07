package com.openstreamingtools.MainServer.db.configuration;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
//@EnableJpaRepositories(bootstrapMode = BootstrapMode.LAZY)
public class DataSourceConfiguration {

    @Autowired
    Environment env;
    
    private static String dbUrl ="jdbc:sqlite:enginedj.db";



    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("driverClassName"));
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(env.getProperty("user"));
        dataSource.setPassword(env.getProperty("password"));
        return dataSource;
    }



    public static void setDbUrl(String newDBUrl){
        dbUrl = "jdbc:sqlite:"+newDBUrl;
    }
}
