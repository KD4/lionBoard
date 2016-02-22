package com.github.lionboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Lion.k on 16. 2. 12..
 */

@Controller
@RequestMapping("/errors")
public class ErrorController {

    @RequestMapping(
            value = "/failure",
            method = RequestMethod.GET)
    public String showErrorPage(){
        return "/errors/failure";
    }

    @RequestMapping(
            method = RequestMethod.GET,
    value = "/denied")
    public String showDenied(){
        return "/errors/denied";
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/404")
    public String show404(){
        return "/errors/404";
    }

}
