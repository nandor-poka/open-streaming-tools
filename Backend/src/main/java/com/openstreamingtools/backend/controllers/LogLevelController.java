package com.openstreamingtools.backend.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing application logging levels at runtime.
 * Provides endpoints to query and update the root logger level dynamically
 * without requiring server restart.
 */
@RestController
@Slf4j
public class LogLevelController {

    /**
     * Retrieves the current root logging level.
     *
     * @return the current root log level as a string (e.g., "INFO", "DEBUG", "WARN")
     */
    @GetMapping(value = "/api/getLogLevel", produces = "application/json")
    public String getLogLevel() {
        return System.getProperty("logging.level.root", "INFO");
    }

    /**
     * Sets the root logging level to the specified level.
     * Valid levels are: TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF.
     *
     * @param logLevel the desired log level (case-insensitive)
     */
    @PostMapping(value = "/api/setLogLevel/{logLevel}")
    public void setLogLevel(@PathVariable String logLevel) {
        LoggingSystem system = LoggingSystem.get(LogLevelController.class.getClassLoader());
        try{
            LogLevel level = LogLevel.valueOf(logLevel.toUpperCase());
            system.setLogLevel("root", level);
            log.warn("Log level set to: {}", logLevel);
        } catch (IllegalArgumentException e) {
            log.error("Invalid log level: {}", logLevel);
        }
    }
}
