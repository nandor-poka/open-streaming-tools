package com.openstreamingtools.backend.controllers;


import com.openstreamingtools.backend.db.configuration.DataSourceConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataSourceController {

    @PostMapping(value = "/addDataSource")
    public void addDataSource(@RequestParam String dbPath){
        DataSourceConfiguration.setDbUrl(dbPath);
    }
}
