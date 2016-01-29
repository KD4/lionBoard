package com.github.lionboard.controller;

import com.github.lionboard.error.IncorrectAccessException;
import com.github.lionboard.error.InvalidUserException;
import com.github.lionboard.model.Post;
//import com.github.lionboard.model.User;
import com.github.lionboard.service.IndexService;
import com.github.lionboard.model.TempModel;
import com.github.lionboard.service.LionBoardService;
import com.sun.javafx.sg.prism.NGShape;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Lion.k on 16. 1. 12..
 */


@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    LionBoardService lionBoardService;

    @RequestMapping(method = RequestMethod.GET)
    public String root() {
        return "redirect:/index";
    }

    @RequestMapping(
            value = "index",
            method = RequestMethod.GET)
    public String index(ModelMap modelMap,Model model) {
        if (modelMap.containsAttribute("posts")) {
            return "index";
        }
        else {
            return "redirect:/posts";
        }
    }

    @RequestMapping(
            value = "view/addPost",
            method = RequestMethod.GET)
    public ModelAndView viewAddPost(HttpSession session,HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String identity = auth.getName(); //get logged in username
        com.github.lionboard.model.User loginUser = lionBoardService.getUserByIdentity(identity);
        ModelAndView mav = new ModelAndView("addPost");
        mav.addObject("loginUserId", loginUser.getId());
        return mav;

    }

    @RequestMapping(
            value = "view/editPost/{postId}",
            method = RequestMethod.GET)
    public ModelAndView editPost(@PathVariable("postId") int postId, HttpSession session,HttpServletResponse response) {
        Post post = lionBoardService.getPostByPostId(postId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String identity = auth.getName(); //get logged in username
        com.github.lionboard.model.User loginUser = lionBoardService.getUserByIdentity(identity);

        if(post.getUserId() == loginUser.getId()) {
            ModelAndView mav = new ModelAndView("editPost");
            mav.addObject("post", post);
            mav.addObject("loginUserId", loginUser.getId());
            return mav;
        }else{
            throw new IncorrectAccessException();
        }

    }

    @RequestMapping(
            value = "signup",
            method = RequestMethod.GET)
    public String viewSignUp() {
        //todo:세션 로그인 확인
        return "signUp";
    }


    @RequestMapping(
            value = "login",
            method = RequestMethod.GET)
    public String viewLogIn() {
        //todo:세션 로그인 확인
        return "login";
    }


    @RequestMapping(value="logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }


}
