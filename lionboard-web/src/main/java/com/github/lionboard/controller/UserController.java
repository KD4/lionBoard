package com.github.lionboard.controller;

import com.github.lionboard.error.IncorrectAccessException;
import com.github.lionboard.error.InvalidUserException;
import com.github.lionboard.model.Comment;
import com.github.lionboard.model.Post;
import com.github.lionboard.model.User;
import com.github.lionboard.service.LionBoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Lion.k on 16. 1. 25..
 */


@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class);

    @Autowired
    LionBoardService lionBoardService;

    @ResponseBody @RequestMapping(
            headers = "Accept=application/json",
            produces="application/json;charset=utf8",
            method= RequestMethod.POST)
    public List<String> signUp(User user){

        List<String> results = new ArrayList<String>();
        //검색된 결과가 없어서 예외가 발생하는 것이 정상.
        try {
            if (lionBoardService.getUserByIdentity(user.getIdentity()) != null) {
                results.add("이미 등록된 이메일입니다. 다른 이메일을 사용해주세요.");
            }
                return results;
        }catch (InvalidUserException e){}

        try{
            if(lionBoardService.getUserByName(user.getName()) != null){
                results.add("이미 등록된 이름입니다. 다른 이름을 사용해주세요.");
            }
                return results;
        }catch (InvalidUserException e){}

        lionBoardService.addUser(user);

        //results 첫번째 인덱스는 작업결과 (success or error msg) 두번째 인덱스는 등록에 성공한 유저 아이디.
        //results 값으로 프론트에서 Tenth2 업로드 요청을 함. - img 업로드 API 와 회원등록 API 분리.
        results.add("success");
        results.add(String.valueOf(user.getId()));


        logger.debug(user.getId() + " sign up ! hello "+ user.getName());

        return results;
    }

    @RequestMapping(
            method= RequestMethod.GET,
            value="/{userId}")
    public ModelAndView findUser(@PathVariable("userId") int userId){
        ModelAndView mav = new ModelAndView("users");
        User selectedUser = lionBoardService.getUserByUserId(userId);
        if(selectedUser == null){
            throw new InvalidUserException();
        }
        List<Post> posts = lionBoardService.getPostsByUserId(userId);
        List<Comment> comments = lionBoardService.getCommentsByUserId(userId);
        mav.addObject("user", selectedUser);
        mav.addObject("posts", posts);
        mav.addObject("comments", comments);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String identity = auth.getName(); //get logged in username
        if(!identity.equals("anonymousUser")) {
            com.github.lionboard.model.User loginUser = lionBoardService.getUserByIdentity(identity);
            mav.addObject("loginUserId", loginUser.getId());
        }
        return mav;
    }


    @ResponseBody
    @RequestMapping(
            method= RequestMethod.PUT,
            value="/{userId}")
    public String updateUser(@PathVariable("userId") int userId,@RequestBody User user){
        user.setId(userId);
        lionBoardService.modifyUser(user);
        logger.debug(userId + " update the information..:");
        return String.valueOf(user.getId());
    }

    @ResponseBody
    @RequestMapping(
            method= RequestMethod.PUT,
            value="/{userId}/status")
    public String updateUserStatus(@PathVariable("userId") int userId, @RequestBody User user){
        user.setId(userId);
        lionBoardService.modifyUserStatus(user);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String identity = auth.getName(); //get logged in username
        logger.debug(userId + " update status to "+user.getUserStatus()+" by "+ identity);
        return "success";
    }

    @ResponseBody
    @RequestMapping(
            method= RequestMethod.PUT,
            value="/{userId}/roles")
    public String updateUserRole(@PathVariable("userId") int userId, @RequestBody User user){
        user.setId(userId);
        lionBoardService.modifyUserRole(user);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String identity = auth.getName(); //get logged in username
        logger.debug(userId + " update role to " + user.getRoles() + " by " + identity);
        return "success";
    }

    @RequestMapping(
            method= RequestMethod.DELETE,
            value="/{userId}")
    public ModelAndView leaveUser(@PathVariable("userId") int userId){
        ModelAndView mav = new ModelAndView("/users");
        lionBoardService.changeUserStatusToLeave(userId);

        logger.debug(userId + " leave the service..! goodbye " + userId);
        return mav;
    }




    @ResponseBody @RequestMapping(
            value="/{userId}/profile",
            produces="application/json;charset=utf8",
            method= RequestMethod.POST)
    public String uploadProfile(@PathVariable("userId") int userId, MultipartHttpServletRequest request) throws Exception {
        Iterator<String> itr =  request.getFileNames();
        MultipartFile mpf = request.getFile(itr.next());
        try {
            //tenth2 서버로 이미지 업로드를 요청함.
            String uploadedUrl = lionBoardService.uploadProfile(userId, mpf.getInputStream());
            lionBoardService.updateProfileInfoOnUser(userId,uploadedUrl);

            logger.debug(userId + " upload the profile..:" + uploadedUrl);
            return "success";
        } catch (InvalidUserException e) {
            return e.getMessage();
        }


    }

    @ResponseBody
    @RequestMapping(
            method= RequestMethod.GET,
            value="/search")
    public List<User> searchUserList(@RequestParam(value = "query" , required = false) String query){
        if(query == null){
            throw new IncorrectAccessException();
        }
        return lionBoardService.searchUserWithQuery(query);
    }

    @RequestMapping(
            method= RequestMethod.GET)
    public User getUserList(){
        throw new IncorrectAccessException();
    }




}
