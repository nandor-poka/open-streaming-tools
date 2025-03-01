package com.openstreamingtools.MainServer.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Thic class listens for the Spring Boot Application startup event and
 * initializes the settings file resource object so that the settings
 * can be read and saved.
 */
//@Component
public class ApplicationStartupListener implements
        ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LogManager.getLogger(ApplicationStartupListener.class);


    // MAnaged by Spring and loaded by the time the application finishes the startup sequence
    @Value("classpath:settings.json")
    Resource settingsFileResource;


    // event listener to for the startup event
    @Override public void onApplicationEvent(ContextRefreshedEvent event) {
        Configuration.setSettingsFileResource(settingsFileResource);
        Configuration.init();
    }
}
