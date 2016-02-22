package com.github.lionboard.service;

import com.github.lionboard.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by lion.k on 16. 1. 20..
 */
public interface LionBoardService {
    List<Post> getPosts(int offset, int limit, String sort);

    void addPost(Post post);

    void deleteAllPosts();

    Post getPostByPostId(int postId);

    void changePostStatusByPostId(int postId, String postStatus);

    List<PostFile> getPostFilesByPostId(int postId);

    List<Comment> getComments();

    List<Comment> getCommentsByPostId(int postId, String sort);

    void addComment(Comment comment);

    void deleteAllComments();

    void changeCmtStatusByCmtId(int cmtId, String cmtStatus);

    Comment getCommentByCmtId(int cmtId);

    void deleteAllUsers();

    void addUser(User user);

    User getUserByUserId(int userId);

    User getUserByIdentity(String identity);

    int getPostLike(int postId);

    void addPostLike(int postId);

    void subtractPostLike(int postId);

    int getPostHate(int postId);

    void addPostHate(int postId);

    void subtractPostHate(int postId);

    int getPostView(int postId);

    void addPostView(int postId);

    void subtractPostView(int postId);

    int getCmtLike(int cmtId);

    void addCmtLike(int cmtId);

    void subtractCmtLike(int cmtId);

    int getCmtHate(int cmtId);

    void addCmtHate(int cmtId);

    void subtractCmtHate(int cmtId);

    void modifyUser(User user);

    void removeUserById(int userId);

    void changeUserStatusToLeave(int userId);

    void modifyPost(Post post);

    void changePostStatusToDelete(int postId);

    int getPostReportCount(int postId);

    void reportPost(PostReport postReport);


    List<PostReport> getPostReports(int postId);

    void changeProcessStatusFromPost(PostReport postReport);

    void modifyComment(Comment comment);

    int getCmtReportCount(int cmtId);

    void reportComment(CommentReport commentReport);

    List<CommentReport> getCommentReports(int cmtId);

    void changeProcessStatusFromCmt(CommentReport commentReport);

    List<Pagination> getPagination(int offset, String sort, String source);

    List<Post> getPostsByUserId(int userId);

    List<Comment> getCommentsByUserId(int userId);

    User getUserByName(String name);

    String uploadProfile(int userId, MultipartFile mpf) throws Exception;

    void updateProfileInfoOnUser(int userId, String uploadedUrl);

    String addFileToServer(int postId, MultipartFile uploadFiles) throws IOException, Exception;

    void addPostWithFile(Post post) throws Exception;

    void changeCmtStatusByPostId(int postId, String status);

    Post createBasePostForReply(int postId);

    void changeFileStatusToDelete(int fileId);

    void addFileOnPost(Post post) throws Exception;

    void addPostFile(PostFile postFile);

    User existUserByIdentity(String identity);

    void securityLogin(User user);

    Post getParentPost(int postId);

    List<Post> getStickyPosts(int unit);

    List<User> getAllUsers(int offset, int limit, String sort);

    void modifyUserStatus(User user);

    void modifyUserRole(User user);

    List<User> searchUserWithQuery(String query);

    List<Post> getAllPosts(int offset, int limit, String sort);

    List<Post> searchPostWithQuery(String query);

    List<Comment> getAllComments(int offset, int limit, String sort);

    List<Comment> searchCmtWithQuery(String query);

    List<PostReport> getAllPostReports(int offset, int limit, String sort);

    List<PostReport> searchPostReportsWithQuery(String query);

    List<CommentReport> getAllCmtReports(int offset, int limit, String sort);

    List<CommentReport> searchCmtReportsWithQuery(String query);

    Post getPostByPostIdForAdmin(int postId);

    Post getStickyPost(int postId);

    void setStickyPost(int postId);

    void setOffStickyPost(int postId);
}
