package com.openstreamingtools.MainServer.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {


 //   @RequestMapping("/")
    String home() {
        return "Hello World from controller!";
    }
}
