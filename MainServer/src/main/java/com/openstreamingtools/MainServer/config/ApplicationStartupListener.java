package com.openstreamingtools.MainServer.config;


import com.openstreamingtools.MainServer.api.Settings;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import static com.openstreamingtools.MainServer.utils.Utils.objectMapper;

@Component
public class ApplicationStartupListener implements
        ApplicationListener<ContextRefreshedEvent> {




    @Override public void onApplicationEvent(ContextRefreshedEvent event) {
        File settingsFileDir = new File(Configuration.SETTINGS_DIR_PATH);
        if (!settingsFileDir.exists()) {
            settingsFileDir.mkdirs();
        }
        File settingsFile = new File(Configuration.SETTINGS_FILE_PATH);
        if (!settingsFile.exists()) {
            try {
                settingsFile.createNewFile();
                objectMapper.writeValue(settingsFile, new Settings());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Configuration.setSettingsFile(settingsFile);
        Configuration.init();
    }
}
