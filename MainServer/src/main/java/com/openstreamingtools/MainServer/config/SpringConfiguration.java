package com.openstreamingtools.MainServer.config;

import com.openstreamingtools.MainServer.udp.StageLinQDiscoveryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.ip.udp.MulticastReceivingChannelAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringConfiguration {
    @Bean
    public MulticastReceivingChannelAdapter udpIn() {
        MulticastReceivingChannelAdapter  adapter = new MulticastReceivingChannelAdapter ("239.255.255.250",51337);
        adapter.setOutputChannelName(StageLinQDiscoveryHandler.StageLinQChannelID);
        return adapter;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(OSTConfiguration.FRONTEND_ORIGN, OSTConfiguration.FRONTEND_JAR_ORIGN);
            }
        };
    }
}
