package com.openstreamingtools.MainServer.config;


import com.openstreamingtools.MainServer.api.Settings;
import com.openstreamingtools.MainServer.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static com.openstreamingtools.MainServer.utils.Utils.objectMapper;

@Slf4j
@Component
public class ApplicationStartupListener implements
        ApplicationListener<ContextRefreshedEvent> {




    @Override public void onApplicationEvent(ContextRefreshedEvent event) {
        File settingsFileDir = new File(OSTConfiguration.SETTINGS_DIR_PATH);
        if (!settingsFileDir.exists()) {
            settingsFileDir.mkdirs();
        }
        File settingsFile = new File(OSTConfiguration.SETTINGS_FILE_PATH);
        if (!settingsFile.exists()) {
            try {
                settingsFile.createNewFile();
                objectMapper.writeValue(settingsFile, new Settings());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        OSTConfiguration.setSettingsFile(settingsFile);
        OSTConfiguration.init();
        if (OSTConfiguration.settings.getTwitchToken() != null) {
            try {
                Utils.refreshAuthTokenFromTwitch(OSTConfiguration.settings.getTwitchToken().getRefresh_token());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
