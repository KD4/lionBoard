package com.github.lionboard.controller;

import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.model.Post;
import com.github.lionboard.service.LionBoardService;
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
 * Created by daum on 16. 2. 15..
 */
@Controller
@RequestMapping("/restful")
public class RestfulController {

    private static final Logger logger =
            LoggerFactory.getLogger(RestfulController.class);

    @Autowired
    LionBoardService lionBoardService;

    @ResponseBody
    @RequestMapping(method= RequestMethod.GET,value = "/posts/{postId}")
    public Post getPost(@PathVariable("postId") int postId){
        try {
            return lionBoardService.getPostByPostIdForAdmin(postId);
        }catch (Exception e){
            logger.debug("returning Post Object fail.  : " + e.getMessage());
            throw new InvalidPostException(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.GET,value = "/stickyPosts/{postId}")
    public String isStickyPost(@PathVariable("postId") int postId){
        try {
            Post post = lionBoardService.getStickyPost(postId);
            if(post != null){
                return "true";
            }else{
                return "false";
            }
        }catch (Exception e){
            logger.debug("returning Post Object fail.  : " + e.getMessage());
            throw new InvalidPostException(e.getMessage());
        }
    }




}
