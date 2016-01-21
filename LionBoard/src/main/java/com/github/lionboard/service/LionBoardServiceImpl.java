package com.github.lionboard.service;

import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.model.Comment;
import com.github.lionboard.model.Post;
import com.github.lionboard.model.PostFile;
import com.github.lionboard.repository.CommentRepository;
import com.github.lionboard.repository.PostFileRepository;
import com.github.lionboard.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lion.k on 16. 1. 20..
 */

@Service
public class LionBoardServiceImpl implements LionBoardService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostFileRepository postFileRepository;

    @Autowired
    CommentRepository commentRepository;

    @Override
    public List<Post> getPosts(int offset, int limit) {
        Map<String,Integer> pageArgs = new HashMap<>();
        pageArgs.put("offset",offset);
        pageArgs.put("limit",limit);
        List<Post> posts = postRepository.findByPage(pageArgs);

        //ToDo: Post 객체에 이름을 어떻게 넣을 것인가 ? 일단은 SQL 문으로 !

        return posts;
    }

    @Override
    public void addPost(Post post) {
        if(post.getDepth() < 1){
            postRepository.insertPost(post);
            //ToDo: Thinking - Status table 작업은 트리거로 넣는게 좋을까 ?
            postRepository.insertPostStatus(post);
        }else{
            //Todo: update post depth and Insert reply post with logic.
        }
    }

    @Override
    public void deleteAllPosts() {
        postRepository.deleteAll();
    }

    @Override
    public Post getPostByPostId(int postId) {
        Post selectedPost = postRepository.findPostByPostId(postId);
        if(selectedPost == null){
            throw new InvalidPostException();
        }
        return selectedPost;
    }

    @Override
    public void changePostStatusByPostId(int postId, String postStatus) {
        Map<String,Object> postStatusArgs = new HashMap<>();
        postStatusArgs.put("postId",postId);
        postStatusArgs.put("postStatus", postStatus);
        postRepository.updatePostStatus(postStatusArgs);
    }

    @Override
    public void addPostFile(PostFile postFile) {
        postFileRepository.insertPostFile(postFile);
    }

    @Override
    public List<PostFile> getPostFilesByPostId(int postId) {
        List<PostFile> postFiles = postFileRepository.findFilesByPostId(postId);
        if(postFiles == null){
            throw new InvalidPostException();
        }
        return postFiles;
    }

    @Override
    public List<Comment> getComments() {
        return commentRepository.findComments();
    }

    @Override
    public List<Comment> getCommentsByPostId(int postId) {
        return commentRepository.findCommentsByPostId(postId);
    }

    @Override
    public void addComment(Comment comment) {
        if(comment.getDepth() < 1){
            commentRepository.insertComment(comment);
            //ToDo: Thinking - Status table 작업은 트리거로 넣는게 좋을까 ?
            commentRepository.insertCommentStatus(comment);
        }else{
            //Todo: update post depth and Insert reply post with logic.
        }

        postRepository.addCmtCount(comment.getPostId());
    }

    @Override
    public void deleteAllComments() {
        commentRepository.deleteAll();
    }

    @Override
    public void changeCmtStatusByCmtId(int cmtId, String cmtStatus) {
        Map<String,Object> cmtStatusArgs = new HashMap<>();
        cmtStatusArgs.put("cmtId",cmtId);
        cmtStatusArgs.put("cmtStatus", cmtStatus);
        commentRepository.updateCmtStatusByCmtId(cmtStatusArgs);
    }

    @Override
    public Comment getCommentByCmtId(int cmtId) {
        Comment selectedCmt = commentRepository.findCommentByCmtId(cmtId);
        if(selectedCmt == null){
            throw new InvalidPostException();
        }
        return selectedCmt;
    }
}
