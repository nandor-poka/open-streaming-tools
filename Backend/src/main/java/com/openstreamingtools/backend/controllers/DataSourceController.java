package com.openstreamingtools.backend.controllers;


import com.openstreamingtools.backend.db.configuration.DataSourceConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing database data source configuration.
 * Provides endpoints to update the database connection URL at runtime.
 */
@RestController
public class DataSourceController {

    /**
     * Sets the database URL for the data source.
     * Accepts a path parameter and updates the application's database connection configuration.
     *
     * @param dbPath the database file path or JDBC connection URL
     */
    @PostMapping(value = "/addDataSource")
    public void addDataSource(@RequestParam String dbPath){
        DataSourceConfiguration.setDbUrl(dbPath);
    }
}
