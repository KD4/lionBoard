package com.github.lionboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lionboard.error.InvalidUserException;
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

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Lion.k on 16. 1. 12..
 */


//ToDo: change configuration path with classpath.
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:/config/mvc-dispatcher-servlet.xml")
public class UserControllerTest {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;
    @Autowired
    LionBoardService lionBoardService;
    User firstUser;
    User secondUser;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        setFixtureUsers();
        this.mockMvc = webAppContextSetup(this.wac).build();
        lionBoardService.deleteAllUsers();

    }

    @Test
    public void signUp() throws Exception {


        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(firstUser);
        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();
        String content2String = result.getResponse().getContentAsString();
        User user = mapper.readValue(content2String, User.class);

        Assert.assertThat(user.getName(), is(firstUser.getName()));

    }

    @Test
    public void findUser() throws Exception {

        lionBoardService.addUser(firstUser);

        mockMvc.perform(get("/users/" + firstUser.getId())
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("/users"))
                .andExpect(model().attributeExists("user"))
                .andReturn();
    }

    @Test
    public void updateUser() throws Exception {
        //setup test.
        lionBoardService.addUser(firstUser);


        User tempUser = lionBoardService.getUserByUserId(firstUser.getId());
        tempUser.setName("변경된이름");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(tempUser);

        mockMvc.perform(put("/users/" + tempUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(view().name("/users"))
                .andExpect(model().attributeExists("user"));

    }

    @Test(expected = InvalidUserException.class)
    public void deleteUser() throws Exception {
        //setup test.
        lionBoardService.deleteAllUsers();
        lionBoardService.addUser(firstUser);

        User tempUser = lionBoardService.getUserByUserId(firstUser.getId());
        Assert.assertThat(tempUser.getUserStatus(), is("S"));

        mockMvc.perform(delete("/users/" + firstUser.getId())
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());

        //because status is 'L', throw the exception.
        User deletedUser = lionBoardService.getUserByUserId(firstUser.getId());

    }


    private void setFixtureUsers() {
        firstUser = new User();
        secondUser = new User();
        firstUser.setIdentity("token");
        firstUser.setEmail("kangddanddan@gmail.com");
        firstUser.setName("강관우");
        firstUser.setProfileUrl("prororororofile");
        firstUser.setPassword("pwpw");
        firstUser.setIsOAuth("F");
        firstUser.setRoles("U");
        secondUser.setIdentity("token2");
        secondUser.setEmail("kangddanddan2@gmail.com");
        secondUser.setName("강관우2");
        secondUser.setProfileUrl("prororororofile2");
        secondUser.setPassword("pwpw2");
        secondUser.setIsOAuth("F");
        secondUser.setRoles("U");
    }


}
