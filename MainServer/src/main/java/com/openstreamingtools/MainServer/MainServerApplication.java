package com.openstreamingtools.MainServer;

import com.openstreamingtools.MainServer.udp.StageLinQDiscoveryHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.ip.udp.MulticastReceivingChannelAdapter;



@SpringBootApplication
public class MainServerApplication {

	@Bean
	public MulticastReceivingChannelAdapter udpIn() {
		MulticastReceivingChannelAdapter  adapter = new MulticastReceivingChannelAdapter ("239.255.255.250",51337);
		adapter.setOutputChannelName(StageLinQDiscoveryHandler.StageLinQChannelID);
		return adapter;
	}

	public static void main(String[] args) {
		SpringApplication.run(MainServerApplication.class, args);
	}

}
