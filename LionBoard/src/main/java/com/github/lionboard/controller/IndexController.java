package com.github.lionboard.controller;

import com.github.lionboard.error.IncorrectAccessException;
import com.github.lionboard.model.Post;
//import com.github.lionboard.model.User;
import com.github.lionboard.model.PostFile;
import com.github.lionboard.model.RegistrationForm;
import com.github.lionboard.service.LionBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Lion.k on 16. 1. 12..
 *
 * Index, Login, SignUp, Logout page
 * view pages 
 */


@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    LionBoardService lionBoardService;

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @RequestMapping(method = RequestMethod.GET)
    public String root() {
        return "redirect:/index";
    }

    @RequestMapping(
            value = "index",
            method = RequestMethod.GET)
    public String index(ModelMap modelMap) {

        //postsController에서 넘어온 로직인지 확인함.
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
    public ModelAndView viewAddPost() {
        //Spring Security session에 저장된 login된 유저 정보 가져오기.
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
    public ModelAndView editPost(@PathVariable("postId") int postId) {
        Post post = lionBoardService.getPostByPostId(postId);

        //Spring Security session에 저장된 login된 유저 정보 가져오기.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String identity = auth.getName(); //get logged in username
        com.github.lionboard.model.User loginUser = lionBoardService.getUserByIdentity(identity);

        //첨부파일 목록 반환.
        List<PostFile> postFiles = lionBoardService.getPostFilesByPostId(postId);
        if(post.getUserId() == loginUser.getId()) {
            ModelAndView mav = new ModelAndView("editPost");
            mav.addObject("post", post);
            mav.addObject("loginUserId", loginUser.getId());
            mav.addObject("postFiles",postFiles);
            return mav;
        }else{
            throw new IncorrectAccessException();
        }

    }

    @RequestMapping(
            value = "view/replyPost/{postId}",
            method = RequestMethod.GET)
    public ModelAndView replyPost(@PathVariable("postId") int postId) {
        Post basePost = lionBoardService.createBasePostForReply(postId);

        //Spring Security session에 저장된 login된 유저 정보 가져오기.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String identity = auth.getName(); //get logged in username
        com.github.lionboard.model.User loginUser = lionBoardService.getUserByIdentity(identity);


        ModelAndView mav = new ModelAndView("replyPost");
        mav.addObject("basePost", basePost);
        mav.addObject("loginUserId", loginUser.getId());
        return mav;

    }

    @RequestMapping(
            value = "signup",
            method = RequestMethod.GET)
    public String showRegistrationForm(WebRequest request, Model model) {
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
        com.github.lionboard.model.User registration = createRegistrationDTO(connection);
        model.addAttribute("user", registration);
        return "signUp";
    }


    private com.github.lionboard.model.User createRegistrationDTO(Connection<?> connection) {
        com.github.lionboard.model.User dto = new com.github.lionboard.model.User();

        if (connection != null) {
            UserProfile socialMediaProfile = connection.fetchUserProfile();
            dto.setEmail(socialMediaProfile.getEmail());
            dto.setName(socialMediaProfile.getName());
            dto.setIsOAuth("T");
        }
        else{
            dto.setIsOAuth("F");
        }

        return dto;
    }


    @RequestMapping(
            value = "login",
            method = RequestMethod.GET)
    public String viewLogIn(Model model, @RequestParam(value = "error", required = false, defaultValue = "") String error) {
        model.addAttribute("error",error);
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
