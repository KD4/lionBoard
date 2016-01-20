package com.github.lionboard.service;

import com.github.lionboard.model.Post;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by daum on 16. 1. 20..
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/config/mem-context.xml")
public class LionBoardServiceTest {

    @Autowired
    LionBoardService lionBoardService;

    @Test
    public void getPosts() throws ClassNotFoundException, SQLException {
        List<Post> posts = lionBoardService.getPosts(0,20);
        Assert.assertNotNull(posts);
    }

}
