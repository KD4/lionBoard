package com.github.lionboard.service;

import com.github.lionboard.model.Post;
import com.github.lionboard.model.PostFile;
import com.github.lionboard.model.PostReport;

import java.util.List;

/**
 * Created by Lion.k on 16. 2. 3..
 */
public interface PostService {
    List<Post> getPosts(int offset, int limit, String sort);

    void insertRootPost(Post post);

    void insertReplyPost(Post post);

    void hardDeleteAllPosts();

    Post getPostByPostId(int postId);

    void updatePostStatusByPostId(int postId, String postStatus);

    void addPostFile(PostFile postFile);

    List<PostFile> getPostFilesByPostId(int postId);

    void modifyPost(Post post);

    int getReportCount(int postId);

    void reportPost(PostReport postReport);

    List<PostReport> getReportsByPostId(int postId);

    void changeProcessStatusAboutReport(PostReport postReport);

    List<Post> getPostsByUserId(int userId);

    int countPosts();

    void addCmtCount(int postId);

    void updateFileStatusByFileId(int fileId,String fileStatus);

    Integer getLikeCount(int postId);

    int addLikeCount(int postId);

    int subtractLikeCount(int postId);

    Integer getHateCount(int postId);

    int addHateCount(int postId);

    int subtractHateCount(int postId);

    Integer getViewCount(int postId);

    int addViewCount(int postId);

    int subtractViewCount(int postId);

    Post getParentPost(int postId);

    List<Post> getStickyPosts(int unit);

    List<Post> getAllPosts(int offset, int limit, String sort);

    int countAllPosts();

    List<Post> searchPostWithQuery(String query);
}
