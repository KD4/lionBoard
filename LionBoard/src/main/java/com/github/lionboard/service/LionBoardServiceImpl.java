package com.github.lionboard.service;

import com.github.lionboard.error.InvalidCmtException;
import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.error.InvalidUserException;
import com.github.lionboard.model.Comment;
import com.github.lionboard.model.Post;
import com.github.lionboard.model.PostFile;
import com.github.lionboard.model.User;
import com.github.lionboard.repository.CommentRepository;
import com.github.lionboard.repository.PostFileRepository;
import com.github.lionboard.repository.PostRepository;
import com.github.lionboard.repository.UserRepository;
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

    @Autowired
    UserRepository userRepository;

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
            Map<String,Integer> range = new HashMap<>();
            range.put("upperNum",post.getPostNum()-1);
            int lowerNum = (post.getPostNum()-1) / 1000 * 1000 + 1;
            range.put("lowerNum", lowerNum);
            postRepository.updatePostNumForInsertRow(range);
            postRepository.insertPost(post);
            postRepository.insertPostStatus(post);
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
            Map<String,Integer> range = new HashMap<>();
            range.put("upperNum",comment.getCmtNum()-1);
            int lowerNum = (comment.getCmtNum()-1) / 1000 * 1000 +1;
            range.put("lowerNum", lowerNum);
            commentRepository.updateCmtNumForInsertRow(range);
            commentRepository.insertComment(comment);
            commentRepository.insertCommentStatus(comment);
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
            throw new InvalidCmtException();
        }
        return selectedCmt;
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public void addUser(User user) {
        if(user.getIsOAuth().equals("F")){
            userRepository.insertUser(user);
        }else{
            //Todo: OAuth logic
        }
    }

    @Override
    public User getUserByUserId(int userId) {
        User selectedUser = userRepository.findUserByUserId(userId);
        if(selectedUser == null){
            throw new InvalidUserException();
        }
        return selectedUser;

    }

    @Override
    public User getUserByIdentity(String identity) {
        User selectedUser = userRepository.findUserByIdentity(identity);
        if(selectedUser == null){
            throw new InvalidUserException();
        }
        return selectedUser;
    }

    @Override
    public int getPostLike(int postId) {
        Integer likeCount = postRepository.getLikeCount(postId);
        if(likeCount == null){
            throw new InvalidPostException();
        }
        return likeCount;
    }

    @Override
    public void addPostLike(int postId) {
        int rows = postRepository.addLikeCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public void subtractPostLike(int postId) {
        int rows = postRepository.subtractLikeCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public int getPostHate(int postId) {
        Integer hateCount = postRepository.getHateCount(postId);
        if(hateCount == null){
            throw new InvalidPostException();
        }
        return hateCount;
    }

    @Override
    public void addPostHate(int postId) {
        int rows = postRepository.addHateCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public void subtractPostHate(int postId) {
        int rows = postRepository.subtractHateCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public int getPostView(int postId) {
        Integer hateCount = postRepository.getViewCount(postId);
        if(hateCount == null){
            throw new InvalidPostException();
        }
        return hateCount;
    }

    @Override
    public void addPostView(int postId) {
        int rows = postRepository.addViewCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public void subtractPostView(int postId) {
        int rows = postRepository.subtractViewCount(postId);
        if(rows == 0){
            throw new InvalidPostException();
        }
    }

    @Override
    public int getCmtLike(int cmtId) {
        Integer likeCount = commentRepository.getLikeCount(cmtId);
        if(likeCount == null){
            throw new InvalidCmtException();
        }
        return likeCount;
    }

    @Override
    public void addCmtLike(int cmtId) {
        int rows = commentRepository.addLikeCount(cmtId);
        if(rows == 0){
            throw new InvalidCmtException();
        }
    }

    @Override
    public void subtractCmtLike(int cmtId) {
        int rows = commentRepository.subtractLikeCount(cmtId);
        if(rows == 0){
            throw new InvalidCmtException();
        }
    }

    @Override
    public int getCmtHate(int cmtId) {
        Integer likeCount = commentRepository.getHateCount(cmtId);
        if(likeCount == null){
            throw new InvalidCmtException();
        }
        return likeCount;
    }

    @Override
    public void addCmtHate(int cmtId) {
        int rows = commentRepository.addHateCount(cmtId);
        if(rows == 0){
            throw new InvalidCmtException();
        }
    }

    @Override
    public void subtractCmtHate(int cmtId) {
        int rows = commentRepository.subtractHateCount(cmtId);
        if(rows == 0){
            throw new InvalidCmtException();
        }
    }
}
