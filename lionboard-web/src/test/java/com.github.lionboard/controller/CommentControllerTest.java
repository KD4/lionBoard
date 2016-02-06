package com.github.lionboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lionboard.error.InvalidCmtException;
import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.error.InvalidUserException;
import com.github.lionboard.model.*;
import com.github.lionboard.service.LionBoardService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Lion.k on 16. 1. 12..
 */


//ToDo: change configuration path with classpath.
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:/config/test-mvc-dispatcher-servlet.xml")
public class CommentControllerTest {

    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    LionBoardService lionBoardService;

    Post firstPost;

    Comment firstCmt;
    Comment secondCmt;


    @Before
    public void setup() {
        setFixturePosts();
        setFixtureComments();
        this.mockMvc = webAppContextSetup(this.wac).build();
        lionBoardService.deleteAllPosts();
        lionBoardService.deleteAllComments();

    }

    @Test
    public void getComment() throws Exception {
        lionBoardService.addPost(firstPost);
        firstCmt.setPostId(firstPost.getPostId());
        lionBoardService.addComment(firstCmt);

        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(get("/comments/"+firstCmt.getCmtId()))
                .andExpect(status().isOk())
                .andReturn();
        String content2String = result.getResponse().getContentAsString();
        Comment comment = mapper.readValue(content2String, Comment.class);

        Assert.assertThat(comment.getContents(), is(firstCmt.getContents()));
    }

    @Test
    public void editComment() throws Exception {

        lionBoardService.addPost(firstPost);
        firstCmt.setPostId(firstPost.getPostId());
        lionBoardService.addComment(firstCmt);

        Comment tempComment = lionBoardService.getCommentByCmtId(firstCmt.getCmtId());
        tempComment.setContents("edit");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(tempComment);

        mockMvc.perform(put("/comments/" + firstCmt.getCmtId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is3xxRedirection())
                .andDo(print());

        Comment editedComment = lionBoardService.getCommentByCmtId(firstCmt.getCmtId());

        Assert.assertThat(tempComment.getContents(), is(editedComment.getContents()));
    }


    @Test
    public void countFigure() throws Exception {

        lionBoardService.addPost(firstPost);
        firstCmt.setPostId(firstPost.getPostId());
        lionBoardService.addComment(firstCmt);

        mockMvc.perform(put("/comments/" + firstCmt.getCmtId()+ "/likes")
                .param("action","add"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/comments/" + firstCmt.getCmtId() + "/likes")
                .param("action", "add"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/comments/" + firstCmt.getCmtId() + "/hates")
                .param("action", "add"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/comments/" + firstCmt.getCmtId() + "/hates")
                .param("action", "add"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/comments/" + firstCmt.getCmtId() + "/hates")
                .param("action", "add"))
                .andExpect(status().isOk());

        Comment increasedComment = lionBoardService.getCommentByCmtId(firstCmt.getCmtId());
        Assert.assertThat(increasedComment.getLikeCount(), is(2));
        Assert.assertThat(increasedComment.getHateCount(), is(3));


        mockMvc.perform(put("/comments/" + firstCmt.getCmtId() + "/likes")
                .param("action","sub"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/comments/" + firstCmt.getCmtId() + "/hates")
                .param("action", "sub"))
                .andExpect(status().isOk());

        Comment decreasedComment = lionBoardService.getCommentByCmtId(firstCmt.getCmtId());
        Assert.assertThat(decreasedComment.getLikeCount(), is(1));
        Assert.assertThat(decreasedComment.getHateCount(), is(2));
    }


    @Test
    public void changeStatus() throws Exception {

        lionBoardService.addPost(firstPost);
        firstCmt.setPostId(firstPost.getPostId());
        lionBoardService.addComment(firstCmt);

        mockMvc.perform(put("/comments/" + firstCmt.getCmtId() + "/status")
                .param("statusCode", "T"))
                .andExpect(status().isOk());

        //if Exception not thrown, that's mean is that statusCode was not changed.
        try {
            Comment changeCmt = lionBoardService.getCommentByCmtId(firstCmt.getCmtId());
            fail("Exception not thrown, that's mean is that statusCode was not changed.");
        }catch (InvalidCmtException e){
            mockMvc.perform(put("/comments/" + firstCmt.getCmtId() + "/status")
                    .param("statusCode", "S"))
                    .andExpect(status().isOk());
            Comment changeCmt = lionBoardService.getCommentByCmtId(firstCmt.getCmtId());
            Assert.assertThat(changeCmt.getCmtStatus(),is("S"));

        }
    }


    @Test
    public void reportComment() throws Exception {
        lionBoardService.addPost(firstPost);
        firstCmt.setPostId(firstPost.getPostId());
        lionBoardService.addComment(firstCmt);

        ObjectMapper mapper = new ObjectMapper();

        CommentReport commentReport = new CommentReport();
        commentReport.setCmtId(firstCmt.getCmtId());
        commentReport.setReporterId(firstPost.getUserId());
        commentReport.setReason("just");
        String json = mapper.writeValueAsString(commentReport);

        mockMvc.perform(post("/comments/" + firstCmt.getCmtId() + "/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        int count = lionBoardService.getCmtReportCount(firstCmt.getCmtId());
        Assert.assertThat(count, is(1));
    }

    @Test
    public void getCommentReport() throws Exception {
        lionBoardService.addPost(firstPost);
        firstCmt.setPostId(firstPost.getPostId());
        lionBoardService.addComment(firstCmt);

        CommentReport commentReport = new CommentReport();
        commentReport.setCmtId(firstCmt.getCmtId());
        commentReport.setReporterId(firstPost.getUserId());
        commentReport.setReason("just");
        lionBoardService.reportComment(commentReport);


        MvcResult mvcResult =  mockMvc.perform(get("/comments/" + firstCmt.getCmtId() + "/reports"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String content2String = mvcResult.getResponse().getContentAsString();

        System.out.println(content2String);
    }

    @Test
    public void updateReportStatus() throws Exception {
        lionBoardService.addPost(firstPost);
        firstCmt.setPostId(firstPost.getPostId());
        lionBoardService.addComment(firstCmt);

        CommentReport commentReport = new CommentReport();
        commentReport.setCmtId(firstCmt.getCmtId());
        commentReport.setReporterId(firstPost.getUserId());
        commentReport.setReason("just");
        lionBoardService.reportComment(commentReport);

        MvcResult mvcResult =  mockMvc.perform(put("/comments/" + firstCmt.getCmtId() + "/reports")
                .param("reportId", String.valueOf(commentReport.getId()))
                .param("processStatus", "C"))
                .andExpect(status().isOk())
                .andReturn();

        List<CommentReport> commentReports = lionBoardService.getCommentReports(firstCmt.getCmtId());
        Assert.assertThat(commentReports.get(0).getProcessStatus(),is("C"));
    }




    private void setFixturePosts() {
        firstPost = new Post();
        firstPost.setUserId(101);
        firstPost.setUserName("KD4");
        firstPost.setTitle("first post");
        firstPost.setContents("hello ?");
        firstPost.setDepth(0);
        firstPost.setExistFiles("F");
        firstPost.setPostNum(0);
    }


    private void setFixtureComments() {
        firstCmt = new Comment();
        secondCmt = new Comment();
        firstCmt.setUserId(101);
        firstCmt.setContents("first comment");
        firstCmt.setDepth(0);
        firstCmt.setCmtNum(0);
        secondCmt.setUserId(102);
        secondCmt.setContents("second comment");
        secondCmt.setDepth(0);
        secondCmt.setCmtNum(0);
    }


}
