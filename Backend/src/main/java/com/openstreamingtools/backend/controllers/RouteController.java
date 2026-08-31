package com.openstreamingtools.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Spring MVC controller for forwarding SPA routes to the index page.
 * Handles all non-dotted paths (excluding files with extensions) to support
 * client-side routing in the frontend application.
 */
@Controller
public class RouteController {

    /**
     * Forwards requests for non-file paths to the root index for SPA routing.
     * Pattern excludes paths with dots (file extensions).
     *
     * @return a forward directive to the root index page
     */
    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/";
    }
}