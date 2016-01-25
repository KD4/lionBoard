package com.github.lionboard.controller;

import com.github.lionboard.error.IncorrectAccessException;
import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.error.InvalidUserException;
import com.github.lionboard.model.User;
import com.github.lionboard.service.LionBoardService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by daum on 16. 1. 25..
 */


@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    LionBoardService lionBoardService;

    @ResponseBody @RequestMapping(
            consumes="application/json",
            produces="application/json;charset=utf8",
            method= RequestMethod.POST)
    public User signUp(@RequestBody User user){

        lionBoardService.addUser(user);

        User insertedUser = lionBoardService.getUserByUserId(user.getId());

        if(insertedUser != null){
            return insertedUser;
        }else{
            throw new InvalidUserException();
        }
    }

    @RequestMapping(
            method= RequestMethod.GET,
            value="/{userId}")
    public ModelAndView findUser(@PathVariable("userId") int userId){
        ModelAndView mav = new ModelAndView("/users");
        User selectedUser = lionBoardService.getUserByUserId(userId);
        if(selectedUser == null){
            throw new InvalidUserException();
        }
        mav.addObject("user", selectedUser);
        return mav;
    }


    @RequestMapping(
            method= RequestMethod.PUT,
            value="/{userId}")
    public ModelAndView updateUser(@PathVariable("userId") int userId,@RequestBody User user){
        ModelAndView mav = new ModelAndView("/users");
        user.setId(userId);
        lionBoardService.modifyUser(user);
        User updatedUser = lionBoardService.getUserByUserId(userId);
        if(updatedUser == null){
            throw new InvalidUserException();
        }
        mav.addObject("user",updatedUser);
        return mav;
    }

    @RequestMapping(
            method= RequestMethod.DELETE,
            value="/{userId}")
    public ModelAndView leaveUser(@PathVariable("userId") int userId){
        ModelAndView mav = new ModelAndView("/users");
        lionBoardService.changeUserStatusToLeave(userId);
        return mav;
    }






    @RequestMapping(
            method= RequestMethod.GET)
    public User getUserList(){
        throw new IncorrectAccessException();
    }

    @ExceptionHandler(InvalidUserException.class)
    public ModelAndView InvalidException(Exception e) {
        return new ModelAndView("/errors").addObject("errorlog", e.getMessage());
    }

    @ExceptionHandler(IncorrectAccessException.class)
    public ModelAndView IncorrectAccessException(Exception e) {
        return new ModelAndView("/errors").addObject("errorlog", e.getMessage());
    }



}
