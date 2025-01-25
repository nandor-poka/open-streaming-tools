package com.openstreamingtools.MainServer.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import java.net.*;
import java.io.*;
import java.util.Arrays;


public class DirectorySocketServer {

    private static final Logger logger = LoggerFactory.getLogger(DirectorySocketServer.class);
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private InputStream in;



    @Bean
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(60000);
        clientSocket = serverSocket.accept();
        in = clientSocket.getInputStream();
        byte[] data = new byte[44];
        while (in.read(data) !=-1){
            logger.debug(Arrays.toString(data));
        }
    }

    public void stop() throws IOException {
        in.close();
        clientSocket.close();
        serverSocket.close();
    }


}
