package com.github.lionboard.controller;

import com.github.lionboard.model.Comment;
import com.github.lionboard.service.LionBoardService;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Lion.k on 16. 2. 13..
 */


@Controller
@RequestMapping("/admin")
public class AdminController {


    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class);

    @Autowired
    LionBoardService lionBoardService;
    
    @RequestMapping(method= RequestMethod.GET)
    public String showAdmin(ModelAndView modelAndView){

        return "redirect:/admin/users";
    }

    @RequestMapping(method= RequestMethod.GET,
            value = "/users")
    public ModelAndView showUsersOnAdmin(ModelAndView modelAndView){
        return modelAndView;
    }

    @RequestMapping(method= RequestMethod.GET,
            value = "/posts")
    public ModelAndView showPostsOnAdmin(ModelAndView modelAndView){
        return modelAndView;
    }

    @RequestMapping(method= RequestMethod.GET,
            value = "/comments")
    public ModelAndView showCommentsOnAdmin(ModelAndView modelAndView){
        return modelAndView;
    }

    @RequestMapping(method= RequestMethod.GET,
            value = "/reports")
    public ModelAndView showReportsOnAdmin(ModelAndView modelAndView){
        return modelAndView;
    }

}
