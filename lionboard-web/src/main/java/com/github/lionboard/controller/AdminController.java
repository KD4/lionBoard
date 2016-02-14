package com.github.lionboard.controller;

import com.github.lionboard.error.IncorrectAccessException;
import com.github.lionboard.error.InvalidUserException;
import com.github.lionboard.model.Comment;
import com.github.lionboard.model.Pagination;
import com.github.lionboard.model.Post;
import com.github.lionboard.model.User;
import com.github.lionboard.service.LionBoardService;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
    public ModelAndView showUsersOnAdmin(ModelAndView modelAndView,@RequestParam(value = "offset", required = false, defaultValue = "0") int offset, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit,@RequestParam(value = "sort", required = false, defaultValue = "id") String sort){
        List<User> users = lionBoardService.getAllUsers(offset,limit,sort);
        List<Pagination> paginations = lionBoardService.getPagination(offset, sort, "adminUsers");
        modelAndView.addObject("paginations",paginations);
        modelAndView.addObject("users",users);
        return modelAndView;
    }

    @RequestMapping(method= RequestMethod.GET,
            value = "/posts")
    public ModelAndView showPostsOnAdmin(ModelAndView modelAndView,@RequestParam(value = "offset", required = false, defaultValue = "0") int offset, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit,@RequestParam(value = "sort", required = false, defaultValue = "posts.postId") String sort){

        List<Post> posts = lionBoardService.getAllPosts(offset, limit, sort);
        List<Pagination> paginations = lionBoardService.getPagination(offset, sort, "adminPosts");
        modelAndView.addObject("paginations",paginations);
        modelAndView.addObject("posts",posts);

        return modelAndView;
    }

    @RequestMapping(method= RequestMethod.GET,
            value = "/comments")
    public ModelAndView showCommentsOnAdmin(ModelAndView modelAndView,@RequestParam(value = "offset", required = false, defaultValue = "0") int offset, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit,@RequestParam(value = "sort", required = false, defaultValue = "cmts.cmtId") String sort){

        List<Comment> comments = lionBoardService.getAllComments(offset, limit, sort);
        List<Pagination> paginations = lionBoardService.getPagination(offset, sort,"adminComments");
        modelAndView.addObject("paginations",paginations);
        modelAndView.addObject("comments",comments);

        return modelAndView;
    }

    @RequestMapping(method= RequestMethod.GET,
            value = "/reports")
    public ModelAndView showReportsOnAdmin(ModelAndView modelAndView){
        return modelAndView;
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView InvalidException(RuntimeException e) {
        return new ModelAndView("errors").addObject("errorlog", e.getMessage());
    }

}
