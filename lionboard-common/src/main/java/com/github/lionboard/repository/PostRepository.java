package com.github.lionboard.repository;
import com.github.lionboard.model.Comment;
import com.github.lionboard.model.Post;
import com.github.lionboard.model.PostReport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Lion.k on 16. 1. 20..
 */

@Repository
public interface PostRepository {
    List<Post> findByPage(Map<String, Object> pageArgs);

    void insertPost(Post post);

    void deleteAll();

    Post findPostByPostId(int postId);

    void updatePostStatus(Post post);

    void insertPostStatus(Post post);

    void addCmtCount(int postId);

    void updatePostNumForInsertRow(Map<String, Integer> range);

    Integer getLikeCount(int postId);

    int addLikeCount(int postId);

    int subtractLikeCount(int postId);


    Integer getHateCount(int postId);

    int addHateCount(int postId);

    int subtractHateCount(int postId);


    Integer getViewCount(int postId);

    int addViewCount(int postId);

    int subtractViewCount(int postId);

    Post findPostByPostNum(int lowerNum);

    void updatePost(Post post);

    void updatePostStatusToDelete(int postId);

    int getReportCount(int postId);

    void insertReport(PostReport postReport);

    List<PostReport> findReportByPostId(int postId);

    void updateProcessStatus(PostReport postReport);

    int countPost();

    List<Post> findPostsByUserId(int userId);

    Post findParentPost(Post tempParent);

    List<Post> findStickyPosts(int unit);
}
