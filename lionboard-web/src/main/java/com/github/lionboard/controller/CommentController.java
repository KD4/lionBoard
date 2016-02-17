package com.github.lionboard.controller;

import com.github.lionboard.error.IncorrectAccessException;
import com.github.lionboard.error.InvalidCmtException;
import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.model.Comment;
import com.github.lionboard.model.CommentReport;
import com.github.lionboard.model.Post;
import com.github.lionboard.model.PostReport;
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
 * Created by Lion.k on 16. 1. 26..
 */


@Controller
@RequestMapping("/comments")
public class CommentController {

    private static final Logger logger =
            LoggerFactory.getLogger(CommentController.class);

    @Autowired
    LionBoardService lionBoardService;


    @ResponseBody
    @RequestMapping(method= RequestMethod.GET,
            produces="application/json;charset=utf8",
            value = "/{cmtId}")
    public Comment getComment(@PathVariable("cmtId") int cmtId){
        return lionBoardService.getCommentByCmtId(cmtId);
    }

    @RequestMapping(method= RequestMethod.PUT,
            consumes="application/json",
            value = "/{cmtId}")
    public String editComment(@PathVariable("cmtId") int cmtId,@RequestBody Comment comment){
        comment.setCmtId(cmtId);
        lionBoardService.modifyComment(comment);


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.debug(auth.getName() + " edit the comment. comment's id is " + cmtId);

        return "redirect:posts/"+comment.getPostId();
    }


    @ResponseBody
    @RequestMapping(method= RequestMethod.PUT,value = "/{cmtId}/likes")
    public String updateLikeCount(@PathVariable("cmtId") int cmtId, @RequestParam(value = "action",required = true) String action){
        try{
        if(action.equals("add")){
            lionBoardService.addCmtLike(cmtId);
        }else if(action.equals("sub")){
            lionBoardService.subtractCmtLike(cmtId);
        }else{
            throw new InvalidCmtException("Invalid action code, please check action.");
        }


            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            logger.debug(auth.getName() + " give the like to hhe comment. comment's id is " + cmtId);
        return "success";
//      if update logic fail, throw the exception.
        }catch (RuntimeException e){
            return e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.PUT,value = "/{cmtId}/hates")
    public String updateHateCount(@PathVariable("cmtId") int cmtId, @RequestParam(value = "action",required = true) String action){
        try{
        if(action.equals("add")){
            lionBoardService.addCmtHate(cmtId);
        }else if(action.equals("sub")){
            lionBoardService.subtractCmtHate(cmtId);
        }else{
            throw new InvalidCmtException("Invalid action code, please check action.");
        }


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.debug(auth.getName() + " give the hate to hhe comment. comment's id is " + cmtId);
//      if update logic fail, throw the exception.
            return "success";
//      if update logic fail, throw the exception.
        }catch (RuntimeException e){
            return e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.PUT,value = "/{cmtId}/status")
    public String updateStatus(@PathVariable("cmtId") int cmtId, @RequestBody Comment comment){
        try {
            lionBoardService.changeCmtStatusByCmtId(cmtId, comment.getCmtStatus());
            return "success";

//      if update logic fail, throw the exception.
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.POST,value = "/{cmtId}/reports")
    public String reportComment(@PathVariable("cmtId") int cmtId,CommentReport commentReport){

        commentReport.setCmtId(cmtId);
        lionBoardService.reportComment(commentReport);

        return "success";
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.GET,
            produces="application/json;charset=utf8",
            value = "/{cmtId}/reports")
    public List<CommentReport> getCommentReports(@PathVariable("cmtId") int cmtId){
        return lionBoardService.getCommentReports(cmtId);
    }


    @ResponseBody
    @RequestMapping(method= RequestMethod.PUT,
            produces="application/json;charset=utf8",
            value = "/{cmtId}/reports")
    public String updateReportStatus(@PathVariable("cmtId") int cmtId, @RequestBody CommentReport commentReport){
        if(commentReport.getProcessStatus().equals("C")){
            lionBoardService.changeCmtStatusByCmtId(commentReport.getCmtId(), "T");
            lionBoardService.changeProcessStatusFromCmt(commentReport);
        }else{
            lionBoardService.changeCmtStatusByCmtId(commentReport.getCmtId(), "S");
            lionBoardService.changeProcessStatusFromCmt(commentReport);
        }
        return "success";
    }



    @ResponseBody
    @RequestMapping(
            method= RequestMethod.GET,
            value="/search")
    public List<Comment> searchCommentList(@RequestParam(value = "query" , required = false) String query){
        if(query == null){
            throw new IncorrectAccessException();
        }
        return lionBoardService.searchCmtWithQuery(query);
    }

    @ResponseBody
    @RequestMapping(
            method= RequestMethod.GET,
            value="/reports/search")
    public List<CommentReport> searchPostReports(@RequestParam(value = "query" , required = false) String query){
        if(query == null){
            throw new IncorrectAccessException();
        }
        return lionBoardService.searchCmtReportsWithQuery(query);
    }

    @ExceptionHandler(IncorrectAccessException.class)
    public ModelAndView IncorrectAccessException(IncorrectAccessException e) {
        return new ModelAndView("/errors").addObject("errorlog", e.getMessage());
    }


    @ExceptionHandler(InvalidCmtException.class)
    public ModelAndView InvalidException(InvalidCmtException e) {
        return new ModelAndView("/errors").addObject("errorlog", e.getMessage());
    }


}
