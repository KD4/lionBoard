package com.github.lionboard.service;

import com.github.lionboard.error.InvalidCmtException;
import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.error.InvalidUserException;
import com.github.lionboard.model.Comment;
import com.github.lionboard.model.Post;
import com.github.lionboard.model.PostFile;
import com.github.lionboard.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by lion.k on 16. 1. 20..
 */


//ToDo: change configuration path with classpath.
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/config/testApplicationContext.xml")
public class LionBoardServiceTest {

    @Autowired
    LionBoardService lionBoardService;

    Post firstPost;
    Post secondPost;
    Post replyOfFirstPost;
    Post replyOfSecondPost;
    Post replyOfSecondReply;

    Comment firstCmt;
    Comment secondCmt;
    Comment replyOfFirstCmt;
    Comment replyOfSecondCmt;


    User firstUser;
    User secondUser;

    @Before
    public void setup(){
        setFixturePosts();
        setFixtureComments();
        setFixtureUsers();
    }


    //ToDo: Post service Test

    @Test
    public void getPosts(){
        List<Post> posts = lionBoardService.getPosts(0, 20);
        Assert.assertNotNull(posts);
    }

    @Test(expected = InvalidPostException.class)
    public void getPostById() {
        lionBoardService.addPost(firstPost);
        Post insertedPost = lionBoardService.getPostByPostId(firstPost.getPostId());
        Assert.assertThat(insertedPost.getTitle(), is(firstPost.getTitle()));
        Assert.assertNotEquals(insertedPost.getTitle(), secondPost.getTitle());
        Post invalidPost = lionBoardService.getPostByPostId(-10);
        Assert.assertNull(invalidPost);
    }

    @Test
    public void addPost() {
        List<Post> beforePosts = lionBoardService.getPosts(0, 20);
        Assert.assertEquals(beforePosts.size(), 0);
        lionBoardService.addPost(firstPost);
        lionBoardService.addPost(secondPost);

        List<Post> afterPosts = lionBoardService.getPosts(0, 20);
        Assert.assertEquals(afterPosts.size(), 2);

//      get list order by DESC.
        Assert.assertEquals(afterPosts.get(0).getPostNum(), 2000);
        Assert.assertEquals(afterPosts.get(1).getPostNum(), 1000);
    }

    @Test
    public void addReplyPost() {
        List<Post> beforePosts = lionBoardService.getPosts(0, 20);
        Assert.assertEquals(beforePosts.size(), 0);
        lionBoardService.addPost(firstPost);
        lionBoardService.addPost(secondPost);
        lionBoardService.addPost(replyOfFirstPost);
        lionBoardService.addPost(replyOfSecondPost);
        lionBoardService.addPost(replyOfSecondReply);

        List<Post> afterPosts = lionBoardService.getPosts(0, 20);
        Assert.assertEquals(afterPosts.size(), 5);


//      post 2
//       -reply2
//         reply2-1
//       -reply1
//      post1
        Assert.assertEquals(afterPosts.get(0).getPostNum(), 2000);
        Assert.assertThat(afterPosts.get(0).getTitle(), is("second post"));
        Assert.assertEquals(afterPosts.get(1).getPostNum(), 1999);
        Assert.assertThat(afterPosts.get(1).getTitle(), is("reply2 of second post"));
        Assert.assertEquals(afterPosts.get(2).getPostNum(), 1998);
        Assert.assertThat(afterPosts.get(2).getTitle(), is("reply1 of reply2"));
        Assert.assertEquals(afterPosts.get(3).getPostNum(), 1997);
        Assert.assertThat(afterPosts.get(3).getTitle(), is("reply1 of second post"));
        Assert.assertEquals(afterPosts.get(4).getPostNum(), 1000);
        Assert.assertThat(afterPosts.get(4).getTitle(),is("first post"));
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
        secondCmt.setPostId(firstPost.getPostId());

        lionBoardService.addComment(firstCmt);
        lionBoardService.addComment(secondCmt);


        List<Comment> comments = lionBoardService.getCommentsByPostId(firstPost.getPostId());
        //      get list order by DESC.
        Assert.assertEquals(comments.get(0).getCmtNum(), 2000);
        Assert.assertThat(comments.get(0).getContents(), is("second comment"));

        Post insertedPost = lionBoardService.getPostByPostId(firstPost.getPostId());
        Assert.assertThat(insertedPost.getCmtCount(), is(2));
    }


    @Test
    public void addReplyComment(){
        lionBoardService.addPost(firstPost);
        firstCmt.setPostId(firstPost.getPostId());
        secondCmt.setPostId(firstPost.getPostId());
        replyOfFirstCmt.setPostId(firstPost.getPostId());
        replyOfSecondCmt.setPostId(firstPost.getPostId());

        lionBoardService.addComment(firstCmt);
        lionBoardService.addComment(secondCmt);
        lionBoardService.addComment(replyOfFirstCmt);
        lionBoardService.addComment(replyOfSecondCmt);

        List<Comment> comments = lionBoardService.getCommentsByPostId(firstPost.getPostId());
        //      get list order by DESC.
        Assert.assertEquals(comments.get(0).getCmtNum(), 2000);
        Assert.assertThat(comments.get(0).getContents(), is("second comment"));
        Assert.assertEquals(comments.get(1).getCmtNum(), 1999);
        Assert.assertThat(comments.get(1).getContents(), is("reply of second cmt"));
        Assert.assertEquals(comments.get(2).getCmtNum(), 1000);
        Assert.assertThat(comments.get(2).getContents(), is("first comment"));
        Assert.assertEquals(comments.get(3).getCmtNum(), 999);
        Assert.assertThat(comments.get(3).getContents(), is("reply of first cmt"));

        Post insertedPost = lionBoardService.getPostByPostId(firstPost.getPostId());
        Assert.assertThat(insertedPost.getCmtCount(), is(4));
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

    @Test(expected = InvalidCmtException.class)
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

    @Test
    public void addUserWithoutOAuth(){
        lionBoardService.deleteAllUsers();
        lionBoardService.addUser(firstUser);
        User insertedUser = lionBoardService.getUserByUserId(firstUser.getId());
        Assert.assertThat(insertedUser.getPassword(),is(firstUser.getPassword()));
    }

    @Test(expected = InvalidUserException.class)
    public void getUserByUserId(){
        lionBoardService.deleteAllUsers();
        lionBoardService.addUser(firstUser);
        User insertedUser = lionBoardService.getUserByUserId(firstUser.getId());
        Assert.assertThat(insertedUser.getPassword(), is(firstUser.getPassword()));
        User invalidUser = lionBoardService.getUserByUserId(0);
    }

    @Test(expected = InvalidUserException.class)
    public void getUserByIdentity(){
        lionBoardService.deleteAllUsers();
        lionBoardService.addUser(firstUser);
        User insertedUser = lionBoardService.getUserByIdentity(firstUser.getIdentity());
        Assert.assertThat(insertedUser.getPassword(), is(firstUser.getPassword()));
        User invalidUser = lionBoardService.getUserByIdentity("none");
    }




//=========================Setup Fixture================================

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
        lionBoardService.deleteAllPosts();
        lionBoardService.deleteAllComments();
    }

    private void setFixtureComments() {
        firstCmt = new Comment();
        secondCmt = new Comment();
        replyOfFirstCmt = new Comment();
        replyOfSecondCmt = new Comment();
        firstCmt.setUserId(101);
        firstCmt.setContents("first comment");
        firstCmt.setDepth(0);
        firstCmt.setCmtNum(0);
        secondCmt.setUserId(102);
        secondCmt.setContents("second comment");
        secondCmt.setDepth(0);
        secondCmt.setCmtNum(0);
        replyOfFirstCmt.setUserId(101);
        replyOfFirstCmt.setContents("reply of first cmt");
        replyOfFirstCmt.setDepth(1);
        replyOfFirstCmt.setCmtNum(1000);
        replyOfSecondCmt.setUserId(101);
        replyOfSecondCmt.setContents("reply of second cmt");
        replyOfSecondCmt.setDepth(1);
        replyOfSecondCmt.setCmtNum(2000);
    }

    private void setFixturePosts() {
        firstPost = new Post();
        secondPost = new Post();
        replyOfFirstPost = new Post();
        replyOfSecondPost = new Post();
        replyOfSecondReply = new Post();
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
        replyOfFirstPost.setUserId(103);
        replyOfFirstPost.setUserName("new?");
        replyOfFirstPost.setTitle("reply1 of second post");
        replyOfFirstPost.setContents("reply ?");
        replyOfFirstPost.setDepth(1);
        replyOfFirstPost.setExistFiles("F");
        replyOfFirstPost.setPostNum(2000);
        replyOfSecondPost.setUserId(104);
        replyOfSecondPost.setUserName("new!");
        replyOfSecondPost.setTitle("reply2 of second post");
        replyOfSecondPost.setContents("reply ?");
        replyOfSecondPost.setDepth(1);
        replyOfSecondPost.setExistFiles("F");
        replyOfSecondPost.setPostNum(2000);
        replyOfSecondReply.setUserId(104);
        replyOfSecondReply.setUserName("new!");
        replyOfSecondReply.setTitle("reply1 of reply2");
        replyOfSecondReply.setContents("reply ?");
        replyOfSecondReply.setDepth(2);
        replyOfSecondReply.setExistFiles("F");
        replyOfSecondReply.setPostNum(1999);
    }




}
