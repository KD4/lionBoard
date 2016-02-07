package com.github.lionboard.controller;

import com.github.lionboard.error.IncorrectAccessException;
import com.github.lionboard.error.InvalidCmtException;
import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.model.Comment;
import com.github.lionboard.model.CommentReport;
import com.github.lionboard.model.Post;
import com.github.lionboard.model.PostReport;
import com.github.lionboard.service.LionBoardService;
import org.springframework.beans.factory.annotation.Autowired;
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

//      if update logic fail, throw the exception.
            return "success";
//      if update logic fail, throw the exception.
        }catch (RuntimeException e){
            return e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.PUT,value = "/{cmtId}/status")
    public String updateStatus(@PathVariable("cmtId") int cmtId, @RequestParam(value = "statusCode",required = true) String statusCode){

        try {
            lionBoardService.changeCmtStatusByCmtId(cmtId, statusCode);
            return "success";

//      if update logic fail, throw the exception.
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping(method= RequestMethod.POST,value = "/{cmtId}/reports")
    public boolean reportComment(@PathVariable("cmtId") int cmtId, @RequestBody CommentReport commentReport){

        commentReport.setCmtId(cmtId);
        lionBoardService.reportComment(commentReport);

        return true;
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
    public boolean updateReportStatus(@RequestParam(value = "processStatus",required = true) String processStatus,@RequestParam(value = "reportId",required = true) int reportId){
        CommentReport commentReport = new CommentReport();
        commentReport.setId(reportId);
        commentReport.setProcessStatus(processStatus);
        lionBoardService.changeProcessStatusFromCmt(commentReport);
        return true;
    }


//
//    @ExceptionHandler(RuntimeException.class)
//    public ModelAndView InvalidException(RuntimeException e) {
//        return new ModelAndView("/errors").addObject("errorlog", e.getMessage());
//    }


    @ExceptionHandler(IncorrectAccessException.class)
    public ModelAndView IncorrectAccessException(Exception e) {
        return new ModelAndView("/errors").addObject("errorlog", e.getMessage());
    }


}