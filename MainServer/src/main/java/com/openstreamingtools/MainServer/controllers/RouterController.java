package com.openstreamingtools.MainServer.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouterController {

    @RequestMapping("/^(?!api).*$")
    public String forwardNotApi() {
        return "forward:/";
    }

}
