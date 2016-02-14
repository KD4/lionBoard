package com.github.lionboard.controller;

import com.github.lionboard.error.IncorrectAccessException;
import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.model.*;
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
 * Created by Lion.k on 16. 1. 25..
 */




@Controller
@RequestMapping("/posts")
public class PostController {

    private static final Logger logger =
            LoggerFactory.getLogger(PostController.class);

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

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            logger.debug(auth.getName()+" write the post. post's id is "+insertedPost.getPostId());

            //올라간 게시글의 아이디를 클라이언트로 보냄.
            return String.valueOf(insertedPost.getPostId());

        }catch (RuntimeException re){
            //에러 메시지를 클라이언트로 보냄.
            return re.getMessage();
        }

    }


    @RequestMapping(method= RequestMethod.GET)
    public ModelAndView getPosts(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset, @RequestParam(value = "limit", required = false, defaultValue = "15") int limit,@RequestParam(value = "sort", required = false, defaultValue = "posts.postNum") String sort){

        ModelAndView mav = new ModelAndView("index");
        List<Post> posts = lionBoardService.getStickyPosts(5);
        posts.addAll(lionBoardService.getPosts(offset, limit, sort));
        List<Pagination> paginations = lionBoardService.getPagination(offset,sort,"posts");
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
    public ModelAndView getPost(@PathVariable("postId") int postId,@RequestParam(value = "sort", required = false, defaultValue = "cmtNum") String sort){
        lionBoardService.addPostView(postId);
        ModelAndView mav = new ModelAndView("posts");
        Post post = lionBoardService.getPostByPostId(postId);
        List<Comment> comments = lionBoardService.getCommentsByPostId(postId, sort);
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.debug(auth.getName() + " modify the post. post's id is "+postId);
        return String.valueOf(postId);
    }


    @ResponseBody
    @RequestMapping(method= RequestMethod.DELETE,value = "/{postId}")
    public String removePost(@PathVariable("postId") int postId){
        try {
            lionBoardService.changePostStatusToDelete(postId);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            logger.debug(auth.getName() + " remove the post. post's id is " + postId);

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

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            logger.debug(auth.getName() + " give the like to the post. post's id is " + postId);

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

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            logger.debug(auth.getName() + " give the hate to the post. post's id is " + postId);
//      if update logic fail, throw the exception.
            return "success";
        } catch (RuntimeException e){
                return e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.PUT,value = "/{postId}/status")
    public String updateStatus(@PathVariable("postId") int postId, @RequestBody Post post){
        try {
            lionBoardService.changePostStatusByPostId(postId, post.getPostStatus());
//      if update logic fail, throw the exception.
            return "success";
        }catch (RuntimeException re){
            return re.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.POST,value = "/{postId}/comments")
    public String insertComment(@PathVariable("postId") int postId, Comment comment){

        lionBoardService.addComment(comment);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.debug(auth.getName() + " add the comment to the post. post's id is " + postId);

        return "success";
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.GET,
            produces="application/json;charset=utf8",
            value = "/{postId}/comments")
    public List<Comment> getCommentsByPost(@PathVariable("postId") int postId,@RequestParam(value = "sort", required = false, defaultValue = "cmtNum") String sort){
        return lionBoardService.getCommentsByPostId(postId, sort);
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.POST,value = "/{postId}/reports")
    public String reportPost(@PathVariable("postId") int postId, PostReport postReport){

        postReport.setPostId(postId);
        lionBoardService.reportPost(postReport);

        return "success";
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
    public String updateReportStatus(@PathVariable("postId") int postId, @RequestBody PostReport postReport){
        if(postReport.getProcessStatus().equals("C")){
            lionBoardService.changePostStatusByPostId(postReport.getPostId(),"T");
            lionBoardService.changeProcessStatusFromPost(postReport);
        }else{
            lionBoardService.changePostStatusByPostId(postReport.getPostId(),"S");
            lionBoardService.changeProcessStatusFromPost(postReport);
        }
        return "success";
    }

    @RequestMapping(method= RequestMethod.GET,value = "/{postId}/parent")
    public String getParentPost(@PathVariable("postId") int postId){

        Post parentPost = lionBoardService.getParentPost(postId);
        return "redirect:/posts/"+parentPost.getPostId();
    }


    @ResponseBody
    @RequestMapping(
            method= RequestMethod.GET,
            value="/search")
    public List<Post> searchPostList(@RequestParam(value = "query" , required = false) String query){
        if(query == null){
            throw new IncorrectAccessException();
        }
        return lionBoardService.searchPostWithQuery(query);
    }

    @ResponseBody
    @RequestMapping(
            method= RequestMethod.GET,
            value="/reports/search")
    public List<PostReport> searchPostReports(@RequestParam(value = "query" , required = false) String query){
        if(query == null){
            throw new IncorrectAccessException();
        }
        return lionBoardService.searchPostReportsWithQuery(query);
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
