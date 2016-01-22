package com.github.lionboard.repository;
import com.github.lionboard.model.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Lion.k on 16. 1. 20..
 */

@Repository
public interface PostRepository {
    List<Post> findByPage(Map<String, Integer> pageArgs);

    void insertPost(Post post);

    void deleteAll();

    Post findPostByPostId(int postId);

    void updatePostStatus(Map<String, Object> postStatusArgs);

    void insertPostStatus(Post post);

    void addCmtCount(int postId);

    void updatePostNumForInsertRow(Map<String, Integer> range);
}
