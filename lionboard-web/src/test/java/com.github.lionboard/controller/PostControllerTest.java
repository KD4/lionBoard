package com.github.lionboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.error.InvalidUserException;
import com.github.lionboard.model.Comment;
import com.github.lionboard.model.Post;
import com.github.lionboard.model.PostReport;
import com.github.lionboard.model.User;
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
@ContextConfiguration("classpath:/config/mvc-dispatcher-servlet.xml")
public class PostControllerTest {

    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    LionBoardService lionBoardService;

    Post firstPost;
    Post secondPost;

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
    public void writeNewPost() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(firstPost);

        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(view().name("posts"))
            .andExpect(model().attributeExists("post"));

    }

    @Test
    public void getPosts() throws Exception {
        for(int i = 0 ;i<40;i++){
            if(i/10<2) {
                lionBoardService.addPost(firstPost);
            }else{
                lionBoardService.addPost(secondPost);
            }
        }
        mockMvc.perform(get("/posts")
                .param("offset","20")
                .param("limit", "40"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("posts"))
                .andDo(print());

    }

    @Test
    public void getPost() throws Exception {

        lionBoardService.addPost(firstPost);

        mockMvc.perform(get("/posts/" + firstPost.getPostId()))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("post"))
                .andDo(print());

    }

    @Test
    public void editPost() throws Exception {

        lionBoardService.addPost(firstPost);

        Post tempPost = lionBoardService.getPostByPostId(firstPost.getPostId());
        tempPost.setContents("edit");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(tempPost);

        mockMvc.perform(put("/posts/" + firstPost.getPostId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is3xxRedirection())
                .andDo(print());

        Post editedPost = lionBoardService.getPostByPostId(firstPost.getPostId());

        Assert.assertThat(tempPost.getContents(), is(editedPost.getContents()));

    }


    @Test
    public void countFigure() throws Exception {

        lionBoardService.addPost(firstPost);

        mockMvc.perform(put("/posts/" + firstPost.getPostId() + "/views")
                .param("action","add"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/posts/" + firstPost.getPostId() + "/likes")
                .param("action","add"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/posts/" + firstPost.getPostId() + "/likes")
                .param("action", "add"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/posts/" + firstPost.getPostId() + "/hates")
                .param("action", "add"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/posts/" + firstPost.getPostId() + "/hates")
                .param("action", "add"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/posts/" + firstPost.getPostId() + "/hates")
                .param("action", "add"))
                .andExpect(status().isOk());

        Post increasedPost = lionBoardService.getPostByPostId(firstPost.getPostId());
        Assert.assertThat(increasedPost.getViewCount(),is(1));
        Assert.assertThat(increasedPost.getLikeCount(),is(2));
        Assert.assertThat(increasedPost.getHateCount(), is(3));

        mockMvc.perform(put("/posts/" + firstPost.getPostId() + "/views")
                .param("action","sub"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/posts/" + firstPost.getPostId() + "/likes")
                .param("action","sub"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/posts/" + firstPost.getPostId() + "/hates")
                .param("action", "sub"))
                .andExpect(status().isOk());

        Post decreasedPost= lionBoardService.getPostByPostId(firstPost.getPostId());
        Assert.assertThat(decreasedPost.getViewCount(), is(0));
        Assert.assertThat(decreasedPost.getLikeCount(), is(1));
        Assert.assertThat(decreasedPost.getHateCount(), is(2));
    }


    @Test
    public void changeStatus() throws Exception {

        lionBoardService.addPost(firstPost);

        mockMvc.perform(put("/posts/" + firstPost.getPostId() + "/status")
                .param("statusCode","T"))
                .andExpect(status().isOk());


        //if Exception not thrown, that's mean is that statusCode was not changed.
        try {
            Post changedPost = lionBoardService.getPostByPostId(firstPost.getPostId());
            fail("Exception not thrown, that's mean is that statusCode was not changed.");
        }catch (InvalidPostException e){
            mockMvc.perform(put("/posts/" + firstPost.getPostId() + "/status")
                    .param("statusCode","S"))
                    .andExpect(status().isOk());
            Post changedPost = lionBoardService.getPostByPostId(firstPost.getPostId());
            Assert.assertThat(changedPost.getPostStatus(),is("S"));

        }
    }

    @Test
    public void insertComment() throws Exception {

        lionBoardService.addPost(firstPost);
        firstCmt.setPostId(firstPost.getPostId());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(firstCmt);

        mockMvc.perform(post("/posts/" + firstPost.getPostId() + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is3xxRedirection());

        List<Comment> comments = lionBoardService.getCommentsByPostId(firstPost.getPostId());

        Assert.assertThat(comments.size(),is(1));

        Assert.assertThat(comments.get(0).getContents(),is(firstCmt.getContents()));
    }

    @Test
    public void getCommentsByPost() throws Exception {

        lionBoardService.addPost(firstPost);
        firstCmt.setPostId(firstPost.getPostId());
        lionBoardService.addComment(firstCmt);

        MvcResult mvcResult = mockMvc.perform(get("/posts/" + firstPost.getPostId() + "/comments"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String content2String = mvcResult.getResponse().getContentAsString();

        System.out.println(content2String);

    }

    @Test
    public void reportPost() throws Exception {
        lionBoardService.addPost(firstPost);
        ObjectMapper mapper = new ObjectMapper();

        PostReport postReport = new PostReport();
        postReport.setPostId(firstPost.getPostId());
        postReport.setReporterId(firstPost.getUserId());
        postReport.setReason("just");
        String json = mapper.writeValueAsString(postReport);

        mockMvc.perform(post("/posts/" + firstPost.getPostId() + "/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        int count = lionBoardService.getPostReportCount(firstPost.getPostId());
        Assert.assertThat(count, is(1));
    }

    @Test
    public void getPostReport() throws Exception {
        lionBoardService.addPost(firstPost);
        PostReport postReport = new PostReport();
        postReport.setPostId(firstPost.getPostId());
        postReport.setReporterId(firstPost.getUserId());
        postReport.setReason("just");
        lionBoardService.reportPost(postReport);


        MvcResult mvcResult =  mockMvc.perform(get("/posts/" + firstPost.getPostId() + "/reports"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String content2String = mvcResult.getResponse().getContentAsString();

        System.out.println(content2String);
    }

    @Test
    public void updateReportStatus() throws Exception {
        lionBoardService.addPost(firstPost);
        PostReport postReport = new PostReport();
        postReport.setPostId(firstPost.getPostId());
        postReport.setReporterId(firstPost.getUserId());
        postReport.setReason("just");
        lionBoardService.reportPost(postReport);

        MvcResult mvcResult =  mockMvc.perform(put("/posts/" + firstPost.getPostId() + "/reports")
                .param("reportId", String.valueOf(postReport.getId()))
                .param("processStatus", "C"))
                .andExpect(status().isOk())
                .andReturn();

        List<PostReport> postReports = lionBoardService.getPostReports(firstPost.getPostId());
        Assert.assertThat(postReports.get(0).getProcessStatus(),is("C"));
    }


    private void setFixturePosts() {
        firstPost = new Post();
        secondPost = new Post();
        firstPost.setUserId(101);
        firstPost.setUserName("KD4");
        firstPost.setTitle("first post");
        firstPost.setContents("hello ?");
        firstPost.setDepth(0);
        firstPost.setExistFiles("F");
        firstPost.setPostNum(0);
        secondPost.setUserId(102);
        secondPost.setUserName("KD5");
        secondPost.setTitle("second post");
        secondPost.setContents("world ?");
        secondPost.setDepth(0);
        secondPost.setExistFiles("F");
        secondPost.setPostNum(0);
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
