package com.openstreamingtools.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for routing non-API requests to the frontend.
 * Forwards all requests that don't start with "/api" to the root path
 * to allow the frontend Single Page Application (SPA) to handle routing.
 */
@RestController
public class RouterController {

    /**
     * Forwards non-API requests to the root index page for SPA routing.
     * This enables the frontend to handle all non-API paths.
     *
     * @return a forward directive to the root path
     */
    @RequestMapping("/^(?!api).*$")
    public String forwardNotApi() {
        return "forward:/";
    }

}
