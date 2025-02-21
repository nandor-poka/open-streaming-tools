package com.openstreamingtools.MainServer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener implements
        ApplicationListener<ContextRefreshedEvent> {

    @Value("classpath:settings.json")
    Resource settingsFileResource;

    @Override public void onApplicationEvent(ContextRefreshedEvent event) {
        Configuration.setSettingsFileResource(settingsFileResource);
        Configuration.init();
    }
}
