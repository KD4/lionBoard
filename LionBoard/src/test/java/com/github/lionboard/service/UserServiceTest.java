package com.github.lionboard.service;

import com.github.lionboard.model.User;
import com.github.lionboard.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by lion.k on 16. 1. 17..
 */


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:/config/test/mvc-dispatcher-servlet-test.xml")
public class UserServiceTest {

    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DataSource dataSource;

    @Autowired
    PlatformTransactionManager transactionManager;

    UserServiceTx userServiceTx;

    private User user1;
    private User user2;

    @Before
    public void setUp(){
        this.mockMvc = webAppContextSetup(this.wac).build();
        user1 = new User("kangddanddan@gmail.com","kangddanddan@gmail.com","강딴딴","1234","no",3,3);
        user2 = new User("rkdrhksdn2@gmail.com","rkdrhksdn2@gmail.com","강관우","4321","no",3,3);
        userServiceTx = new UserServiceTx();
        userServiceTx.setTransactionManager(transactionManager);
        userServiceTx.setuserServiceImpl(userService);
    }


    @Test
    public void deleteAllUserTest() throws Exception {
        userServiceTx.deleteAllUser();
        Assert.assertThat(userServiceTx.countUsers(),is(0));
    }


    @Test
    public void addAndGet() throws ClassNotFoundException, RuntimeException {
        userServiceTx.deleteAllUser();
        Assert.assertThat(userServiceTx.countUsers(), is(0));

        int insertedUser1Id = userServiceTx.addNormalUser(user1);
        int insertedUser2Id = userServiceTx.addNormalUser(user2);
        Assert.assertThat(userServiceTx.countUsers(), is(2));

        User insertedUser1 = userServiceTx.getUserById(insertedUser1Id);
        User insertedUser2 = userServiceTx.getUserById(insertedUser2Id);

        Assert.assertThat(insertedUser1.getName(), is(user1.getName()));
        Assert.assertThat(insertedUser2.getName(),is(user2.getName()));
    }

    @Test
    public void count() throws ClassNotFoundException, SQLException {
        userServiceTx.deleteAllUser();
        Assert.assertThat(userServiceTx.countUsers(), is(0));
        userServiceTx.addNormalUser(user1);
        Assert.assertThat(userServiceTx.countUsers(), is(1));
        userServiceTx.addNormalUser(user2);
        Assert.assertThat(userServiceTx.countUsers(), is(2));
    }


    @Test
    public void countUsersWithState() throws ClassNotFoundException, SQLException {
        userServiceTx.deleteAllUser();
        Assert.assertThat(userServiceTx.countUsers(), is(0));
        userServiceTx.addNormalUser(user1);
        Assert.assertThat(userServiceTx.countUsersWithState(3), is(1));
        userServiceTx.addNormalUser(user2);
        Assert.assertThat(userServiceTx.countUsersWithState(1), is(0));
        Assert.assertThat(userServiceTx.countUsersWithState(3), is(2));
    }

    @Test
    public void changeStateOfAllUsers() throws ClassNotFoundException, SQLException {
        userServiceTx.deleteAllUser();
        Assert.assertThat(userServiceTx.countUsers(), is(0));

        int insertedUser1Id = userServiceTx.addNormalUser(user1);
        int insertedUser2Id = userServiceTx.addNormalUser(user2);
        Assert.assertThat(userServiceTx.countUsers(), is(2));
        Assert.assertThat(userServiceTx.countUsersWithState(3), is(2));

        userServiceTx.changeStateOfAllUsers(2);
        Assert.assertThat(userServiceTx.countUsersWithState(2), is(2));

        userServiceTx.changeStateOfAllUsers(1);
        Assert.assertThat(userServiceTx.countUsersWithState(1), is(2));

        User insertedUser1 = userServiceTx.getUserById(insertedUser1Id);
        User insertedUser2 = userServiceTx.getUserById(insertedUser2Id);

        Assert.assertThat(insertedUser1.getUserStateCode(), is(1));
        Assert.assertThat(insertedUser2.getUserStateCode(), is(1));
    }

    @Test
    public void updateUser() throws ClassNotFoundException, SQLException {
        userServiceTx.deleteAllUser();
        Assert.assertThat(userServiceTx.countUsers(), is(0));

        int insertedUserId = userServiceTx.addNormalUser(user1);
        user1.setEmail("goodmorning0726@gmail.com");
        user1.setPassword("newPw");
        user1.setId(insertedUserId);

        userServiceTx.updateUserInfo(user1);

        User updatedUser = userServiceTx.getUserById(insertedUserId);

        Assert.assertThat(updatedUser.getName(), is(user1.getName()));
        Assert.assertThat(updatedUser.getEmail(), is(user1.getEmail()));
        Assert.assertThat(updatedUser.getPassword(), is(user1.getPassword()));

    }




}
