package com.github.lionboard.controller;

import com.github.lionboard.error.InvalidUserException;
import com.github.lionboard.model.User;
import com.github.lionboard.service.IndexService;
import com.github.lionboard.model.TempModel;
import com.github.lionboard.service.LionBoardService;
import com.sun.javafx.sg.prism.NGShape;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
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
        return "redirect:/posts";
    }

    @RequestMapping(
            value = "index",
            method = RequestMethod.GET)
    public String index(ModelMap model) {
        if (model.containsAttribute("posts")) {
            return "index";
        }
        else {
            return "redirect:/posts";
        }
    }

    @RequestMapping(
            value = "addPost",
            method = RequestMethod.GET)
    public ModelAndView viewAddPost(HttpSession session,HttpServletResponse response) {
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null){
            ModelAndView mav = new ModelAndView("login");
            return mav;
        }else{
            ModelAndView mav = new ModelAndView("addPost");
            mav.addObject("userId", loginUser.getId());
            return mav;
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


    @RequestMapping(
            value = "login",
            headers = "Accept=*/*",
            produces="application/json;charset=utf8",
            method= RequestMethod.POST)
    public @ResponseBody String processLogIn(User user,HttpSession session,HttpServletResponse response) {

        User loginUser = null;
        try {
            loginUser = lionBoardService.getUserByIdentity(user.getEmail());
            if (loginUser != null && loginUser.getPassword().equals(user.getPassword())) {
                session.setAttribute("loginUser", loginUser);
                response.addCookie(new Cookie("isLogin","true"));
                return "ok";
            }else{
                return "incorrect password.";
            }
        }catch (InvalidUserException ue){
            return ue.getMessage();
        }


    }

}
