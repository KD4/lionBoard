package com.github.lionboard.service;

import com.github.lionboard.model.Comment;
import com.github.lionboard.model.Post;
import com.github.lionboard.model.PostFile;
import com.github.lionboard.model.User;

import java.util.List;

/**
 * Created by lion.k on 16. 1. 20..
 */
public interface LionBoardService {
    List<Post> getPosts(int offset, int limit);

    void addPost(Post post);

    void deleteAllPosts();

    Post getPostByPostId(int postId);

    void changePostStatusByPostId(int postId, String postStatus);

    void addPostFile(PostFile postFile);

    List<PostFile> getPostFilesByPostId(int postId);

    List<Comment> getComments();

    List<Comment> getCommentsByPostId(int postId);

    void addComment(Comment comment);

    void deleteAllComments();

    void changeCmtStatusByCmtId(int cmtId, String cmtStatus);

    Comment getCommentByCmtId(int cmtId);

    void deleteAllUsers();

    void addUser(User user);

    User getUser(int userId);
}
