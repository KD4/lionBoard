package com.github.lionboard.controller;

import com.github.lionboard.error.IncorrectAccessException;
import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.error.InvalidUserException;
import com.github.lionboard.model.*;
import com.github.lionboard.service.LionBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Lion.k on 16. 1. 25..
 */




@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    LionBoardService lionBoardService;

    @ResponseBody @RequestMapping(
            headers = "Accept=application/json",
            method= RequestMethod.POST)
    public String writePost(Post post){
        try {
            if (post.getUploadFile() != null) {
                post.setExistFiles("T");
               //ToDo 여러 개의 파일 업로드
                lionBoardService.addPostWithFile(post);
            } else {
                post.setExistFiles("F");
                lionBoardService.addPost(post);
            }
            Post insertedPost = lionBoardService.getPostByPostId(post.getPostId());

            //올라간 게시글의 아이디를 클라이언트로 보냄.
            return String.valueOf(insertedPost.getPostId());

        }catch (RuntimeException re){
            //에러 메시지를 클라이언트로 보냄.
            return re.getMessage();
        }

    }


    @RequestMapping(method= RequestMethod.GET)
    public ModelAndView getPosts(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset, @RequestParam(value = "limit", required = false, defaultValue = "20") int limit){

        ModelAndView mav = new ModelAndView("index");

        List<Post> posts = lionBoardService.getPosts(offset, limit);
        List<Pagination> paginations = lionBoardService.getPagination(offset);
        mav.addObject("posts",posts);
        mav.addObject("paginations",paginations);

        //Spring Security session에 저장된 login된 유저 정보 가져오기.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String identity = auth.getName(); //get logged in username
        if(!identity.equals("anonymousUser")) {
            com.github.lionboard.model.User loginUser = lionBoardService.getUserByIdentity(identity);
            mav.addObject("loginUserId", loginUser.getId());
        }
        return mav;


    }

    @RequestMapping(method= RequestMethod.GET,value = "/{postId}")
    public ModelAndView getPost(@PathVariable("postId") int postId,HttpSession session){
        lionBoardService.addPostView(postId);
        ModelAndView mav = new ModelAndView("posts");
        Post post = lionBoardService.getPostByPostId(postId);
        List<Comment> comments = lionBoardService.getCommentsByPostId(postId);
        List<PostFile> postFiles = lionBoardService.getPostFilesByPostId(postId);
        mav.addObject("post", post);
        mav.addObject("comments", comments);
        mav.addObject("postFiles",postFiles);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String identity = auth.getName(); //get logged in username
        if(!identity.equals("anonymousUser")) {
            com.github.lionboard.model.User loginUser = lionBoardService.getUserByIdentity(identity);
            mav.addObject("loginUserId", loginUser.getId());
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.PUT,
            consumes="application/json",
            value = "/{postId}")
    public String editPost(@PathVariable("postId") int postId,@RequestBody Post post){
        lionBoardService.modifyPost(post);
        return String.valueOf(postId);
    }


    @ResponseBody
    @RequestMapping(method= RequestMethod.DELETE,value = "/{postId}")
    public String removePost(@PathVariable("postId") int postId){
        try {
            lionBoardService.changePostStatusToDelete(postId);
            return "success";
        }catch (RuntimeException e){
            return e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.PUT,value = "/{postId}/views")
    public boolean updateViewCount(@PathVariable("postId") int postId,@RequestParam(value = "action",required = true) String action){

        if(action.equals("add")){
            lionBoardService.addPostView(postId);
        }else if(action.equals("sub")){
            lionBoardService.subtractPostView(postId);
        }else{
            throw new InvalidPostException("Invalid action code, please check action.");
        }

//      if update logic fail, throw the exception.
        return true;
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.PUT,value = "/{postId}/likes")
    public String updateLikeCount(@PathVariable("postId") int postId, @RequestParam(value = "action",required = true) String action){
        try{
            if(action.equals("add")){
                lionBoardService.addPostLike(postId);
            }else if(action.equals("sub")){
                lionBoardService.subtractPostLike(postId);
            }else{
                throw new InvalidPostException("Invalid action code, please check action.");
            }

    //      if update logic fail, throw the exception.
            return "success";
        }catch (RuntimeException e){
            return e.getMessage();
        }

    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.PUT,value = "/{postId}/hates")
    public String updateHateCount(@PathVariable("postId") int postId, @RequestParam(value = "action",required = true) String action){
        try {
            if (action.equals("add")) {
                lionBoardService.addPostHate(postId);
            } else if (action.equals("sub")) {
                lionBoardService.subtractPostHate(postId);
            } else {
                throw new InvalidPostException("Invalid action code, please check action.");
            }

//      if update logic fail, throw the exception.
            return "success";
        } catch (RuntimeException e){
                return e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.PUT,value = "/{postId}/status")
    public boolean updateStatus(@PathVariable("postId") int postId, @RequestParam(value = "statusCode",required = true) String statusCode){

        lionBoardService.changePostStatusByPostId(postId,statusCode);

//      if update logic fail, throw the exception.
        return true;
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.POST,value = "/{postId}/comments")
    public String insertComment(@PathVariable("postId") int postId, Comment comment){

        lionBoardService.addComment(comment);

        return "success";
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.GET,
            produces="application/json;charset=utf8",
            value = "/{postId}/comments")
    public List<Comment> getCommentsByPost(@PathVariable("postId") int postId){
        return lionBoardService.getCommentsByPostId(postId);
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.POST,value = "/{postId}/reports")
    public boolean reportPost(@PathVariable("postId") int postId, @RequestBody PostReport postReport){

        postReport.setPostId(postId);
        lionBoardService.reportPost(postReport);

        return true;
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.GET,
            produces="application/json;charset=utf8",
            value = "/{postId}/reports")
    public List<PostReport> getPostReports(@PathVariable("postId") int postId){
        return lionBoardService.getPostReports(postId);
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.PUT,
            produces="application/json;charset=utf8",
            value = "/{postId}/reports")
    public boolean updateReportStatus(@RequestParam(value = "processStatus",required = true) String processStatus,@RequestParam(value = "reportId",required = true) int reportId){
        PostReport postReport = new PostReport();
        postReport.setId(reportId);
        postReport.setProcessStatus(processStatus);
        lionBoardService.changeProcessStatusFromPost(postReport);
        return true;
    }



    @ExceptionHandler(InvalidPostException.class)
    public ModelAndView InvalidException(Exception e) {
        return new ModelAndView("errors").addObject("errorlog", e.getMessage());
    }

    @ExceptionHandler(IncorrectAccessException.class)
    public ModelAndView IncorrectAccessException(Exception e) {
        return new ModelAndView("errors").addObject("errorlog", e.getMessage());
    }


}
