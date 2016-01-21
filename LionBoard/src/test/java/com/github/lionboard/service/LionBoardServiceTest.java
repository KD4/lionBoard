package com.github.lionboard.service;

import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.model.Comment;
import com.github.lionboard.model.Post;
import com.github.lionboard.model.PostFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by lion.k on 16. 1. 20..
 */


//ToDo: change configuration path with classpath.
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/config/mem-context.xml")
public class LionBoardServiceTest {

    @Autowired
    LionBoardService lionBoardService;

    Post firstPost;
    Post secondPost;

    Comment firstCmt;
    Comment secondCmt;

    @Before
    public void setup(){
        firstPost = new Post();
        secondPost = new Post();
        firstPost.setUserId(101);
        firstPost.setUserName("KD4");
        firstPost.setTitle("first post");
        firstPost.setContents("hello ?");
        firstPost.setDepth(0);
        firstPost.setExistFiles("F");
        secondPost.setUserId(102);
        secondPost.setUserName("KD5");
        secondPost.setTitle("second post");
        secondPost.setContents("world ?");
        secondPost.setDepth(0);
        secondPost.setExistFiles("F");
        firstCmt = new Comment();
        firstCmt.setUserId(101);
        firstCmt.setContents("first Comment");
        firstCmt.setDepth(0);
        secondCmt = new Comment();
        secondCmt.setUserId(102);
        secondCmt.setContents("second Comment");
        secondCmt.setDepth(0);
        lionBoardService.deleteAllPosts();
        lionBoardService.deleteAllComments();
    }

    //ToDo: Post service Test

    @Test
    public void getPosts(){
        List<Post> posts = lionBoardService.getPosts(0,20);
        Assert.assertNotNull(posts);
    }

    @Test(expected = InvalidPostException.class)
    public void getPostById() {
        lionBoardService.addPost(firstPost);
        Post insertedPost = lionBoardService.getPostByPostId(firstPost.getPostId());
        Assert.assertThat(insertedPost.getTitle(),is(firstPost.getTitle()));
        Assert.assertNotEquals(insertedPost.getTitle(), secondPost.getTitle());
        Post invalidPost = lionBoardService.getPostByPostId(-10);
        Assert.assertNull(invalidPost);
    }

    @Test
    public void addPost() {
        List<Post> beforePosts = lionBoardService.getPosts(0, 20);
        Assert.assertEquals(beforePosts.size(),0);
        lionBoardService.addPost(firstPost);
        lionBoardService.addPost(secondPost);
        List<Post> afterPosts = lionBoardService.getPosts(0, 20);
        Assert.assertEquals(afterPosts.size(), 2);
    }


    @Test
    public void addPostWithFile() {
        PostFile postFile = new PostFile();
        firstPost.setExistFiles("T");
        lionBoardService.addPost(firstPost);
        postFile.setPostId(firstPost.getPostId());
        postFile.setFileName("Temp file");
        postFile.setFileUrl("tenth2.url.com");
        lionBoardService.addPostFile(postFile);
        List<PostFile> postFiles = lionBoardService.getPostFilesByPostId(postFile.getPostId());
        Assert.assertThat(postFiles.get(0).getPostId(), is(firstPost.getPostId()));
    }

    @Test
    public void deleteAllPosts() {
        lionBoardService.deleteAllPosts();
        List<Post> beforePosts = lionBoardService.getPosts(0,20);
        Assert.assertEquals(beforePosts.size(), 0);
        lionBoardService.addPost(firstPost);
        lionBoardService.addPost(secondPost);
        lionBoardService.deleteAllPosts();
        List<Post> afterPosts = lionBoardService.getPosts(0, 20);
        Assert.assertEquals(afterPosts.size(), 0);
    }

    @Test(expected = InvalidPostException.class)
    public void changePostStatus(){
//      Before change
        lionBoardService.addPost(firstPost);
        lionBoardService.addPost(secondPost);
        Post insertedPost1 = lionBoardService.getPostByPostId(firstPost.getPostId());
        Post insertedPost2 = lionBoardService.getPostByPostId(secondPost.getPostId());
        Assert.assertThat(insertedPost1.getPostStatus(), is("S"));
        Assert.assertThat(insertedPost2.getPostStatus(), is("S"));

//      Change post status.
        lionBoardService.changePostStatusByPostId(insertedPost1.getPostId(), "T");
        lionBoardService.changePostStatusByPostId(insertedPost2.getPostId(), "A");

//      after change
        Post afterPost1 = lionBoardService.getPostByPostId(insertedPost1.getPostId());
        Post afterPost2 = lionBoardService.getPostByPostId(insertedPost2.getPostId());

    }

    //ToDo: comment service Test

    @Test
    public void getCommentsByPostId(){
        lionBoardService.addPost(firstPost);
        List<Comment> comments = lionBoardService.getCommentsByPostId(firstPost.getPostId());
        Assert.assertNotNull(comments);
    }

    @Test
    public void addComment(){
        lionBoardService.addPost(firstPost);
        firstCmt.setPostId(firstPost.getPostId());
        lionBoardService.addComment(firstCmt);
        List<Comment> comments = lionBoardService.getCommentsByPostId(firstPost.getPostId());
        Assert.assertThat(comments.get(0).getContents(), is(firstCmt.getContents()));
        Post insertedPost = lionBoardService.getPostByPostId(firstPost.getPostId());
        Assert.assertThat(insertedPost.getCmtCount(), is(1));
    }

    @Test
    public void deleteAllComments(){
        lionBoardService.deleteAllComments();
        List<Comment> beforeComments = lionBoardService.getComments();
        Assert.assertEquals(beforeComments.size(), 0);
        lionBoardService.addPost(firstPost);
        firstCmt.setPostId(firstPost.getPostId());
        lionBoardService.addComment(firstCmt);
        lionBoardService.deleteAllComments();
        List<Comment> afterComments = lionBoardService.getComments();
        Assert.assertEquals(afterComments.size(), 0);
    }

    @Test(expected = InvalidPostException.class)
    public void changeCmtStatus(){
//      Before change
        lionBoardService.addPost(firstPost);
        firstCmt.setPostId(firstPost.getPostId());
        lionBoardService.addComment(firstCmt);
        List<Comment> comments = lionBoardService.getCommentsByPostId(firstPost.getPostId());
        Assert.assertThat(comments.get(0).getCmtStatus(), is("S"));

//      Change post status.
        lionBoardService.changeCmtStatusByCmtId(firstCmt.getCmtId(), "D");

//      after change
        Comment afterCmt = lionBoardService.getCommentByCmtId(firstCmt.getCmtId());

    }

    //ToDo: User service Test





}
