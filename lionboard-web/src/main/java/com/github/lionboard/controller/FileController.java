package com.github.lionboard.controller;

import com.github.lionboard.model.Post;
import com.github.lionboard.service.LionBoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Lion.k on 16. 2. 2..
 */

@Controller
@RequestMapping("/files")
public class FileController {
    private static final Logger logger =
            LoggerFactory.getLogger(FileController.class);
    @Autowired
    LionBoardService lionBoardService;

    @ResponseBody
    @RequestMapping(
            headers = "Accept=application/json",
            method = RequestMethod.POST)
    public String addFile(Post post) throws Exception {
        lionBoardService.addFileOnPost(post);
        return "success";
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE,
            value = "/{fileId}")
    public String removeFile(@PathVariable("fileId") int fileId) {
        try {
            lionBoardService.changeFileStatusToDelete(fileId);
            return "success";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }


}
