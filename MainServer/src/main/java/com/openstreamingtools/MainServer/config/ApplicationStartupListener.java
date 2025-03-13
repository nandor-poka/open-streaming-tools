package com.openstreamingtools.MainServer.config;


import com.openstreamingtools.MainServer.api.Settings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;

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
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id","n6breeyo2zy1nzlpfx43x91lgaobgo");
        params.add("client_secret", "y6gfc36ycbvt8z89rhxsaqk6hb649f");
        params.add("grant_type", "client_credentials");

   /*     String response = OSTConfiguration.restClient.post()
                .uri("https://id.twitch.tv/oauth2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
                .body(String.class);
        log.debug(response);*/

    }
}
