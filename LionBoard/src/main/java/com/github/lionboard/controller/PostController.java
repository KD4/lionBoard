package com.github.lionboard.controller;

import com.github.lionboard.error.IncorrectAccessException;
import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.error.InvalidUserException;
import com.github.lionboard.model.Comment;
import com.github.lionboard.model.Post;
import com.github.lionboard.model.PostReport;
import com.github.lionboard.model.User;
import com.github.lionboard.service.LionBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by daum on 16. 1. 25..
 */




@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    LionBoardService lionBoardService;

    @RequestMapping(method= RequestMethod.POST)
    public ModelAndView writePost(@RequestBody Post post){

        ModelAndView mav = new ModelAndView("posts");

        lionBoardService.addPost(post);

        Post insertedPost = lionBoardService.getPostByPostId(post.getPostId());

        mav.addObject("post",insertedPost);

        return mav;
    }


    @RequestMapping(method= RequestMethod.GET)
    public ModelAndView getPosts(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset, @RequestParam(value = "limit", required = false, defaultValue = "20") int limit){
        ModelAndView mav = new ModelAndView("index");
        List<Post> posts = lionBoardService.getPosts(offset, limit);
        mav.addObject("posts",posts);
        return mav;
    }

    @RequestMapping(method= RequestMethod.GET,value = "/{postId}")
    public ModelAndView getPosts(@PathVariable("postId") int postId){
        ModelAndView mav = new ModelAndView("posts");
        Post post = lionBoardService.getPostByPostId(postId);
        mav.addObject("post",post);
        return mav;
    }

    @RequestMapping(method= RequestMethod.PUT,
            consumes="application/json",
            produces="application/json;charset=utf8",
            value = "/{postId}")
    public ModelAndView editPost(@PathVariable("postId") int postId,@RequestBody Post post){
        ModelAndView mav = new ModelAndView("posts");
        post.setPostId(postId);
        lionBoardService.modifyPost(post);
        Post editedPost = lionBoardService.getPostByPostId(postId);
        mav.addObject("post",editedPost);
        return mav;
    }


    @RequestMapping(method= RequestMethod.DELETE,value = "/{postId}")
    public String removePost(@PathVariable("postId") int postId){
        lionBoardService.changePostStatusToDelete(postId);
        return "redirect:index";
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.PUT,value = "/{postId}/view")
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
    @RequestMapping(method= RequestMethod.PUT,value = "/{postId}/like")
    public boolean updateLikeCount(@PathVariable("postId") int postId, @RequestParam(value = "action",required = true) String action){

        if(action.equals("add")){
            lionBoardService.addPostLike(postId);
        }else if(action.equals("sub")){
            lionBoardService.subtractPostLike(postId);
        }else{
            throw new InvalidPostException("Invalid action code, please check action.");
        }

//      if update logic fail, throw the exception.
        return true;
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.PUT,value = "/{postId}/hate")
    public boolean updateHateCount(@PathVariable("postId") int postId, @RequestParam(value = "action",required = true) String action){
        if(action.equals("add")){
            lionBoardService.addPostHate(postId);
        }else if(action.equals("sub")){
            lionBoardService.subtractPostHate(postId);
        }else{
            throw new InvalidPostException("Invalid action code, please check action.");
        }

//      if update logic fail, throw the exception.
        return true;
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.PUT,value = "/{postId}/status")
    public boolean updateStatus(@PathVariable("postId") int postId, @RequestParam(value = "statusCode",required = true) String statusCode){

        lionBoardService.changePostStatusByPostId(postId,statusCode);

//      if update logic fail, throw the exception.
        return true;
    }

    @RequestMapping(method= RequestMethod.POST,value = "/{postId}/comments")
    public String insertComment(@PathVariable("postId") int postId, @RequestBody Comment comment){

        lionBoardService.addComment(comment);

        return "redirect:/posts/"+postId;
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
    public List<PostReport> getReportByPost(@PathVariable("postId") int postId){
        return lionBoardService.getReportByPost(postId);
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.PUT,
            produces="application/json;charset=utf8",
            value = "/{postId}/reports")
    public boolean updateReportStatus(@RequestParam(value = "processStatus",required = true) String processStatus,@RequestParam(value = "reportId",required = true) int reportId){
        PostReport postReport = new PostReport();
        postReport.setId(reportId);
        postReport.setProcessStatus(processStatus);
        lionBoardService.changeProcessStatusWithPostId(postReport);
        return true;
    }



    @ExceptionHandler(InvalidPostException.class)
    public ModelAndView InvalidException(Exception e) {
        return new ModelAndView("/errors").addObject("errorlog", e.getMessage());
    }

    @ExceptionHandler(IncorrectAccessException.class)
    public ModelAndView IncorrectAccessException(Exception e) {
        return new ModelAndView("/errors").addObject("errorlog", e.getMessage());
    }


}
