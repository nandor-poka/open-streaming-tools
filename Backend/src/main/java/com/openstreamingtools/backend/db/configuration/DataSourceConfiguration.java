package com.openstreamingtools.backend.db.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Spring configuration for database datasource setup.
 * Creates a DriverManagerDataSource for SQLite database connections using
 * properties from the Spring environment configuration.
 */
@Configuration
public class DataSourceConfiguration {

    /** Spring Environment for accessing application properties */
    @Autowired
    Environment env;
    
    /** Default database URL pointing to local SQLite database file */
    private static String dbUrl ="jdbc:sqlite:enginedj.db";



    /**
     * Creates and configures a DataSource bean for database connections.
     * Uses properties from the environment to set driver class, username, and password.
     *
     * @return configured DataSource for database operations
     */
    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("driverClassName"));
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(env.getProperty("user"));
        dataSource.setPassword(env.getProperty("password"));
        return dataSource;
    }



    /**
     * Updates the database URL dynamically at runtime.
     * Wraps the provided path with SQLite JDBC prefix.
     *
     * @param newDBUrl the new database file path (relative or absolute)
     */
    public static void setDbUrl(String newDBUrl){
        dbUrl = "jdbc:sqlite:"+newDBUrl;
    }
}
