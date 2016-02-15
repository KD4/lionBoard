package com.github.lionboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Lion.k on 16. 2. 12..
 */

@Controller
@RequestMapping("/errors")
public class ErrorController {

    @RequestMapping(
            method = RequestMethod.GET)
    public String showErrorPage(){
        return "error";
    }


    @RequestMapping(
            method = RequestMethod.GET,
    value = "/denied")
    public String showDenied(){
        return "denied";
    }
}
