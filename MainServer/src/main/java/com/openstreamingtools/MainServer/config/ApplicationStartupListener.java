package com.openstreamingtools.MainServer.config;


import com.openstreamingtools.MainServer.api.Settings;
import com.openstreamingtools.MainServer.twitch.TwitchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static com.openstreamingtools.MainServer.utils.Utils.objectMapper;

@Slf4j
@Component
/**
 * Thic class listens for the Spring Boot Application startup event and
 * initializes the settings file resource object so that the settings
 * can be read and saved.
 */
//@Component
public class ApplicationStartupListener implements
        ApplicationListener<ContextRefreshedEvent> {



    // event listener to for the startup event
    @Override public void onApplicationEvent(ContextRefreshedEvent event) {
        File settingsFileDir = new File(OSTConfiguration.SETTINGS_DIR_PATH);
        if (!settingsFileDir.exists()) {
           if (!settingsFileDir.mkdirs()){
               log.error("Could not create directory: " + settingsFileDir.getAbsolutePath());
               return;
           }
        }
        File settingsFile = new File(OSTConfiguration.SETTINGS_FILE_PATH);
        if (!settingsFile.exists()) {
            try {
                if (settingsFile.createNewFile()){
                    objectMapper.writeValue(settingsFile, new Settings());
                }else{
                    log.error("Could not create file: " + settingsFile.getAbsolutePath());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        OSTConfiguration.setSettingsFile(settingsFile);
        OSTConfiguration.init();
        if (OSTConfiguration.settings.getTwitchToken() != null) {
            try {
                TwitchUtils.refreshAuthTokenFromTwitch(OSTConfiguration.settings.getTwitchToken().getRefresh_token());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
